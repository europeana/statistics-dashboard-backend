package eu.europeana.statistics.dashboard.common.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Class that contains all possible categories values and their associated url regexes that represent them
 */
public enum RightsCategory {

    CC0("CC0", List.of("^https?://creativecommons\\.org/publicdomain/zero/.*$")),
    PUBLIC_DOMAIN_MARK("Public Domain Mark", List.of("^https?://creativecommons\\.org/publicdomain/mark/.*$")),
    CC_BY("CC BY", List.of("^https?://creativecommons\\.org/licen[cs]es/by/.*$")),
    CC_BY_SA("CC BY-SA", List.of("^https?://creativecommons\\.org/licen[cs]es/by-sa/.*$")),
    CC_BY_ND("CC BY-ND", List.of("^https?://creativecommons\\.org/licen[cs]es/by-nd/.*$")),
    CC_BY_NC("CC BY-NC", List.of("^https?://creativecommons\\.org/licen[cs]es/by-nc/.*$")),
    CC_BY_NC_SA("CC BY-NC-SA", List.of("^https?://creativecommons\\.org/licen[cs]es/by-nc-sa/.*$$")),
    CC_BY_NC_ND("CC BY-NC-ND", List.of("^https?://creativecommons\\.org/licen[cs]es/by-nc-nd/.*$")),
    NO_COPYRIGHT_NON_COMMERCIAL("No Copyright - Non Commercial Re-Use Only", List.of("^https?://rightsstatements\\.org/(vocab|page)/NoC-NC/.*$",
            "^https?://www.europeana\\.eu/rights/out-of-copyright-non-commercial/.*$")),
    NO_COPYRIGHT_OTHER_KNOWN_LEGAL("No Copyright - Other Known Legal Restriction", List.of("^https?://rightsstatements\\.org/(vocab|page)/NoC-OKLR/.*$")),
    IN_COPYRIGHT_EDUCATIONAL("In Copyright - Educational Use Permitted", List.of("^https?://rightsstatements\\.org/(vocab|page)/InC-EDU/.*$")),
    IN_COPYRIGHT_EU("In Copyright - EU Orphan Work", List.of("^https?://rightsstatements\\.org/(vocab|page)/InC-OW-EU/.*$",
            "^https?://www.europeana\\.eu/rights/orphan-work-eu.*$")),
    IN_COPYRIGHT("In Copyright", List.of("^https?://rightsstatements\\.org/(vocab|page)/InC/.*$",
            "^https?://www.europeana\\.eu/rights/rr-f/.*$")),
    COPYRIGHT_NOT_EVALUATED("Copyright Not Evaluated", List.of("^https?://rightsstatements\\.org/(vocab|page)/CNE/.*$",
            "^https?://www.europeana\\.eu/rights/unknown/.*$")),
    UNKNOWN("Unknown", List.of("http://creativecommons.org/licenses/by-nd/4.0/"));

    private final String name;
    private final List<Pattern> regularExpressions;

    RightsCategory(String name, List<String> regularExpressions) {
        this.name = name;
        this.regularExpressions = regularExpressions.isEmpty() ? new ArrayList<>() :
                regularExpressions.stream().map(Pattern::compile).collect(Collectors.toList());
    }

    public String getName() {
        return name;
    }

    public List<Pattern> getRegularExpressions() {
        return regularExpressions;
    }

    /**
     * Method that return a category that its regexes match the given url.
     * Returns Unknown if the url matches none.
     *
     * @param url - The url to match to a category
     * @return The category that its regexes match the given url. Unknown if matches none.
     */
    public static RightsCategory matchCategoryFromUrl(String url) {
        return Arrays.stream(RightsCategory.values()).filter(category -> category != UNKNOWN)
                .filter(category -> category.getRegularExpressions().stream()
                        .anyMatch(regex -> regex.matcher(url).matches()))
                .findFirst().orElse(RightsCategory.UNKNOWN);
    }

    /**
     * Method that returns the category that its name matches the given name.
     *
     * @param name The name to match to a category
     * @return The category that its name match the given name
     * @throws IllegalArgumentException if no such category with 'name' exists
     */
    public static RightsCategory toCategoryFromName(String name) throws IllegalArgumentException {
        for (RightsCategory category : RightsCategory.values()) {
            if (name.equalsIgnoreCase(category.getName())) {
                return category;
            }
        }
        throw new IllegalArgumentException(String.format("No such rights category with name '%s' exists", name));
    }
}
