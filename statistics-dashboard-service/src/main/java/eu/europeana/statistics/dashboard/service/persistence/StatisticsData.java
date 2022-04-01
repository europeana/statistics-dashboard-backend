package eu.europeana.statistics.dashboard.service.persistence;

import eu.europeana.statistics.dashboard.common.internal.MongoStatisticsField;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.util.CollectionUtils;

/**
 * This is the result of a statistics query. Various instances of this object form trees that
 * represent breakdowns of the data.
 */
public class StatisticsData {

  private final MongoStatisticsField field;
  private final String fieldValue;
  private final int recordCount;
  private final List<StatisticsData> breakdown;

  /**
   * Constructor for a node with children (i.e. a nested breakdown). The record count is computed
   * from the total of the children's record counts.
   *
   * @param field      The field on which this object and its siblings are broken down. Is null for
   *                   the top level node.
   * @param fieldValue The value of the field that applies to this data (and its children) and that
   *                   distinguishes it from its siblings. Is null for the top level node.
   * @param breakdown  The breakdown of the data represented by this node (by another field). Is
   *                   null in case of leaf nodes.
   */
  StatisticsData(MongoStatisticsField field, String fieldValue, List<StatisticsData> breakdown) {
    this.field = field;
    this.fieldValue = fieldValue;
    this.recordCount = Optional.ofNullable(breakdown)
        .map(list -> list.stream().mapToInt(StatisticsData::getRecordCount).sum()).orElse(0);
    this.breakdown = Optional.ofNullable(breakdown).filter(list -> !list.isEmpty())
        .map(ArrayList::new).orElse(null);
  }

  /**
   * Constructor for a leaf node (without children).
   *
   * @param field       The field on which this object and its siblings are broken down. Is null for
   *                    the top level node.
   * @param fieldValue  The value of the field that applies to this data (and its children) and that
   *                    distinguishes it from its siblings. Is null for the top level node.
   * @param recordCount The number of records represented by this node. Should be greater than 1.
   */
  StatisticsData(MongoStatisticsField field, String fieldValue, int recordCount) {
    this.field = field;
    this.fieldValue = fieldValue;
    this.recordCount = recordCount;
    this.breakdown = null;
  }

  /**
   * @return The field on which this object and its siblings are broken down. Is null for the top
   * level node.
   */
  public MongoStatisticsField getField() {
    return field;
  }

  /**
   * @return The value of the field that applies to this data (and its children) and that
   * distinguishes it from its siblings. Is null for the top level node.
   */
  public String getFieldValue() {
    return fieldValue;
  }

  /**
   * @return The number of records represented by this node. Should be greater than 1.
   */
  public int getRecordCount() {
    return recordCount;
  }

  /**
   * @return The breakdown of the data represented by this node (by another field). Is null in case
   * of leaf nodes.
   */
  public List<StatisticsData> getBreakdown() {
    return Collections.unmodifiableList(breakdown);
  }

  /**
   * @return False if the breakdown list is not empty; True otherwise
   */
  public boolean isBreakdownListEmpty(){
    return CollectionUtils.isEmpty(breakdown);
  }
}
