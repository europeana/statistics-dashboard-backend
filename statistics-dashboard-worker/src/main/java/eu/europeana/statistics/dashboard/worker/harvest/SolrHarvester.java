package eu.europeana.statistics.dashboard.worker.harvest;

import eu.europeana.statistics.dashboard.common.internal.Country;
import eu.europeana.statistics.dashboard.common.internal.model.StatisticsRecordModel;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrQuery.SortClause;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.PivotField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.jetbrains.annotations.NotNull;

/**
 * This class provides functionality to harvest from the Solr database. This object can be reused for multiple calls (but there is
 * no guarantee of thread-safety).
 */
public class SolrHarvester {

  // The various fields as they occur in the SOLR database.
  private static final String CONTENT_TIER_FIELD = "contentTier";
  private static final String COUNTRY_FIELD = "COUNTRY";
  private static final String DATA_PROVIDER_FIELD = "DATA_PROVIDER";
  private static final String EUROPEANA_ID_FIELD = "europeana_id";
  private static final String METADATA_TIER_FIELD = "metadataTier";
  private static final String PROVIDER_FIELD = "PROVIDER";
  private static final String RIGHTS_FIELD = "RIGHTS";
  private static final String TIMESTAMP_CREATED_FIELD = "timestamp_created";
  private static final String TIMESTAMP_UPDATE_FIELD = "timestamp_update";
  private static final String TYPE_FIELD = "TYPE";

  // Additional field for the dataset ID (does not occur in SOLR).
  private static final String DATASET_ID_FAKE_FIELD = "datasetId";

  // Try to have fewer branches at the top and more branches deeper. Exclude date fields.
  private static final List<String> PIVOT_ORDER = List.of(COUNTRY_FIELD, PROVIDER_FIELD,
      DATA_PROVIDER_FIELD, RIGHTS_FIELD, TYPE_FIELD, CONTENT_TIER_FIELD, METADATA_TIER_FIELD);

  // Use this timezone to split time into days.
  private static final ZoneId TIMEZONE_TO_USE = ZoneId.systemDefault();

  private final SolrClient solrClient;

  public SolrHarvester(SolrClient solrClient) {
    this.solrClient = solrClient;
  }

  /**
   * Harvest the statistics for a given dataset.
   *
   * @param datasetId The dataset ID to harvest.
   * @return The objects that together form all statistics for the given dataset. Is not null but can be empty (if the dataset is
   * not found in the Solr).
   * @throws DataHarvestingException In case there was an issue connecting to the Solr.
   */
  public List<StatisticsRecordModel> harvestDataset(String datasetId)
      throws DataHarvestingException {
    try {
      return harvestDatasetInternal(datasetId);
    } catch (SolrServerException | IOException e) {
      throw new DataHarvestingException("An exception occurred while harvesting data.", e);
    }
  }

  private List<StatisticsRecordModel> harvestDatasetInternal(String datasetId)
      throws IOException, SolrServerException, DataHarvestingException {

    // Compute the total number of records we are expecting.
    final long size = getDatasetSize(datasetId);
    if (size == 0) {
      return Collections.emptyList();
    }

    // Obtain the different values for the created and updated dates.
    final List<LocalDate> createdDates = getAllDatesForField(datasetId, TIMESTAMP_CREATED_FIELD);
    final List<LocalDate> updatedDates = getAllDatesForField(datasetId, TIMESTAMP_UPDATE_FIELD);

    // For each date combination, obtain the data separately (as we can't pivot on these).
    final List<StatisticsRecordModel> result = new ArrayList<>();
    for (LocalDate createdDate : createdDates) {
      for (LocalDate updatedDate : updatedDates) {
        getAllDataCombinations(datasetId, createdDate, updatedDate, result::add);
      }
    }

    // Check that the total count of all records is the expected count of the whole dataset.
    if (result.stream().mapToInt(StatisticsRecordModel::getRecordCount).sum() != size) {
      throw new DataHarvestingException("Computed total over the lists for separate dates does not "
          + "match the expected total for dataset " + datasetId + ".");
    }

    // Done.
    return result;
  }

  private void getAllDataCombinations(String datasetId, LocalDate createdDate,
      LocalDate updatedDate, Consumer<StatisticsRecordModel> resultConsumer)
      throws IOException, SolrServerException, DataHarvestingException {

    // Perform the counting.
    final String createdStartIncl = getSolrTimestampForStartOfDay(createdDate);
    final String createdEndExcl = getSolrTimestampForStartOfDay(createdDate.plusDays(1));
    final String updatedStartIncl = getSolrTimestampForStartOfDay(updatedDate);
    final String updatedEndExcl = getSolrTimestampForStartOfDay(updatedDate.plusDays(1));
    final SolrQuery solrQuery = new SolrQuery("*:*");
    solrQuery.setFilterQueries(createIdQuery(datasetId),
        createRangeQuery(TIMESTAMP_CREATED_FIELD, createdStartIncl, createdEndExcl),
        createRangeQuery(TIMESTAMP_UPDATE_FIELD, updatedStartIncl, updatedEndExcl));
    solrQuery.setStart(0);
    solrQuery.setRows(0);
    solrQuery.setFacet(true);
    solrQuery.setFacetMissing(true);
    solrQuery.addFacetPivotField(String.join(",", PIVOT_ORDER));

    solrQuery.setFacetLimit(-1); // Don't have default maximum of 100.
    final QueryResponse queryResult = solrClient.query(solrQuery);

    // Prepare the properties: add dataset and dates.
    final Map<String, String> foundProperties = new HashMap<>(PIVOT_ORDER.size() + 3);
    foundProperties.put(DATASET_ID_FAKE_FIELD, datasetId);
    foundProperties.put(TIMESTAMP_CREATED_FIELD, getAsIsoString(createdDate));
    foundProperties.put(TIMESTAMP_UPDATE_FIELD, getAsIsoString(updatedDate));

    // Verify the top level totals.
    final long totalFound = queryResult.getResults().getNumFound();
    final List<PivotField> topLevelResult = queryResult.getFacetPivot().getVal(0);
    verifyTotals(topLevelResult, (int) totalFound, foundProperties);

    // Recurse to the next level.
    for (PivotField pivotField : topLevelResult) {
      recurseThroughPivots(pivotField, foundProperties, resultConsumer);
    }
  }

  @NotNull
  private String createRangeQuery(String field, String start, String end) {
    return String.format("%s:[%s TO %s}", field, start, end);
  }

  @NotNull
  private String createIdQuery(String datasetId) {
    return String.format("%s:\\/%s\\/*", EUROPEANA_ID_FIELD, datasetId);
  }

  private static void recurseThroughPivots(PivotField pivotField,
      Map<String, String> foundProperties, Consumer<StatisticsRecordModel> resultConsumer)
      throws DataHarvestingException {

    // Set the value of this pivot - if already set, we have a problem.
    final String previousValue = foundProperties.put(pivotField.getField(),
        Optional.ofNullable(pivotField.getValue().toString()).orElse(""));
    if (previousValue != null) {
      throw new DataHarvestingException("Value '" + pivotField.getField() + "' already set.");
    }

    // Consider this field's pivot.
    if (pivotField.getPivot() != null && !pivotField.getPivot().isEmpty()) {

      // Verify the totals for this level.
      verifyTotals(pivotField.getPivot(), pivotField.getCount(), foundProperties);

      // Recurse.
      for (PivotField childField : pivotField.getPivot()) {
        recurseThroughPivots(childField, foundProperties, resultConsumer);
      }

    } else {

      // Report a result.
      resultConsumer.accept(convert(foundProperties, pivotField.getCount()));
    }

    // Unset the value of this pivot.
    foundProperties.remove(pivotField.getField());
  }

  private static StatisticsRecordModel convert(Map<String, String> properties, int recordCount) {
    final StatisticsRecordModel result = new StatisticsRecordModel();
    result.setContentTier(Objects.requireNonNull(properties.get(CONTENT_TIER_FIELD)));
    result.setCountry(Objects.requireNonNull(Country.fromCountryName(properties.get(COUNTRY_FIELD)).getIsoCodeCountry()));
    result.setDataProvider(Objects.requireNonNull(properties.get(DATA_PROVIDER_FIELD)));
    result.setMetadataTier(Objects.requireNonNull(properties.get(METADATA_TIER_FIELD)));
    result.setProvider(Objects.requireNonNull(properties.get(PROVIDER_FIELD)));
    result.setRights(Objects.requireNonNull(properties.get(RIGHTS_FIELD)));
    result.setCreatedDate(Objects.requireNonNull(properties.get(TIMESTAMP_CREATED_FIELD)));
    result.setUpdatedDate(Objects.requireNonNull(properties.get(TIMESTAMP_UPDATE_FIELD)));
    result.setType(Objects.requireNonNull(properties.get(TYPE_FIELD)));
    result.setDatasetId(Objects.requireNonNull(properties.get(DATASET_ID_FAKE_FIELD)));
    result.setRecordCount(recordCount);
    return result;
  }

  private static void verifyTotals(List<PivotField> pivot, int expectedTotal,
      Map<String, String> foundProperties) throws DataHarvestingException {
    final long computedTotal = pivot.stream().mapToInt(PivotField::getCount).sum();
    if (computedTotal != expectedTotal) {
      throw new DataHarvestingException(
          "Computed total for list of pivots does not match the expected total.\n"
              + "Properties: " + toString(foundProperties));
    }
  }

  private static String toString(Map<String, String> properties) {
    return properties.entrySet().stream().sorted(Entry.comparingByKey())
        .map(entry -> entry.getKey() + " = " + entry.getValue())
        .collect(Collectors.joining("\n  ", "[\n  ", "\n]"));
  }

  private long getDatasetSize(String datasetId) throws IOException, SolrServerException {
    final SolrQuery solrQuery = new SolrQuery("*:*");
    solrQuery.setFilterQueries(createIdQuery(datasetId));
    solrQuery.setStart(0);
    solrQuery.setRows(0);
    return solrClient.query(solrQuery).getResults().getNumFound();
  }

  private List<LocalDate> getAllDatesForField(String datasetId, String dateFieldName)
      throws IOException, SolrServerException {
    // Start searching well before EPOCH but on a day still representable by a UNIX timestamp.
    LocalDate foundDate = LocalDate.of(1950, 1, 1);
    final List<LocalDate> dates = new ArrayList<>();
    while (true) {
      // Continue search on the day after the last found date.
      foundDate = getNextDateForField(datasetId, dateFieldName, foundDate.plusDays(1));
      if (foundDate == null) {
        break;
      }
      dates.add(foundDate);
    }
    return dates;
  }

  private LocalDate getNextDateForField(String datasetId, String dateFieldName,
      LocalDate firstPermissibleDate) throws IOException, SolrServerException {

    // Create query
    final String startInclusive = getSolrTimestampForStartOfDay(firstPermissibleDate);
    final SolrQuery solrQuery = new SolrQuery("*:*");
    solrQuery.setFilterQueries(createIdQuery(datasetId),
        createRangeQuery(dateFieldName, startInclusive, "*"));
    solrQuery.setSort(new SortClause(dateFieldName, ORDER.asc));
    solrQuery.setStart(0);
    solrQuery.setRows(1);
    solrQuery.setFields(dateFieldName);

    // Get and return result
    return solrClient.query(solrQuery).getResults().stream().findFirst()
        .map(document -> document.getFieldValue(dateFieldName))
        .map(Date.class::cast).map(Date::toInstant)
        .map(instant -> LocalDate.ofInstant(instant, TIMEZONE_TO_USE))
        .orElse(null);
  }

  private static String getSolrTimestampForStartOfDay(LocalDate date) {
    return DateTimeFormatter.ISO_INSTANT.format(date.atStartOfDay(TIMEZONE_TO_USE).toInstant());
  }

  private static String getAsIsoString(LocalDate date) {
    return DateTimeFormatter.ISO_LOCAL_DATE.format(date);
  }
}
