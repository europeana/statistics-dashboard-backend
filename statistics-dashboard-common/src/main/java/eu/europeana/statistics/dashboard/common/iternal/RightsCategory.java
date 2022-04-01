package eu.europeana.statistics.dashboard.common.iternal;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum RightsCategory {

    CC0("CC0", "^https?[:]//creativecommons[.]org/licen[c,s]es/zero/[0-9][.][0-9](/[a-z]{2})?/?$"),
    PUBLIC_DOMAIN_MARK("Public Domain Mark", "^https?[:]//creativecommons[.]org/licen[c,s]es/mark/[0-9][.][0-9](/[a-z]{2})?/?$"),
    CC_BY("CC BY", "^https?[:]//creativecommons[.]org/licen[c,s]es//by/[0-9][.][0-9](/[a-z]{2})?/?$"),
    CC_BY_SA("CC BY-SA", "^https?[:]//creativecommons[.]org/licen[c,s]es/by-sa/[0-9][.][0-9](/[a-z]{2})?/?$"),
    CC_BY_ND("CC BY-ND", "^https?[:]//creativecommons[.]org/licen[c,s]es/by-nd/[0-9][.][0-9](/[a-z]{2})?/?$"),
    CC_BY_NC("CC BY-NC", "^https?[:]//creativecommons[.]org/licen[c,s]es/by-nc/[0-9][.][0-9](/[a-z]{2})?/?$"),
    CC_BY_NC_SA("CC BY-NC-SA", "^https?[:]//creativecommons[.]org/licen[c,s]es/by-nc-sa/[0-9][.][0-9](/[a-z]{2})?/?$"),
    CC_BY_NC_ND("CC BY-NC-ND", "^https?[:]//creativecommons[.]org/licen[c,s]es/by-nc-nd/[0-9][.][0-9](/[a-z]{2})?/?$"),
    NO_COPYRIGHT_NON_COMMERCIAL("No Copyright - Non Commercial Re-Use Only", "^https?[:]//rightsstatements[.]org/vocab/NoC-NC/[0-9][.][0-9](/[a-z]{2})?/?$"),
    NO_COPYRIGHT_OTHER_KNOWN_LEGAL("No Copyright - Other Known Legal Restriction", "^https?[:]//rightsstatements[.]org/vocab/NoC-NC/[0-9][.][0-9](/[a-z]{2})?/?$"),
    IN_COPYRIGHT_EDUCATIONAL("In Copyright - Educational Use Permitted", "^https?[:]//rightsstatements[.]org/vocab/InC-EDU/[0-9][.][0-9](/[a-z]{2})?/?$"),
    IN_COPYRIGHT_EU("In Copyright - EU Orphan Work", "^https?[:]//rightsstatements[.]org/vocab/InC-OW-EU/[0-9][.][0-9](/[a-z]{2})?/?$"),
    IN_COPYRIGHT("In Copyright", "^https?[:]//rightsstatements[.]org/vocab/InC/[0-9][.][0-9](/[a-z]{2})?/?$"),
    COPYRIGHT_NOT_EVALUATED("Copyright Not Evaluated", "^https?[:]//rightsstatements[.]org/vocab/CNE/[0-9][.][0-9](/[a-z]{2})?/?$"),
    UNKNOWN("Unknown", "");

    private final String name;
    private final String regularExpression;

    RightsCategory(String name, String regularExpression){
        this.name = name;
        this.regularExpression = regularExpression;
    }

    public String getName() {
        return name;
    }

    public String getRegularExpression() {
        return regularExpression;
    }

    public static RightsCategory matchCategoryFromUrl(String url){
        Set<RightsCategory> setRightsCategory = Arrays.stream(RightsCategory.values()).filter(x -> x != UNKNOWN).collect(Collectors.toSet());
        RightsCategory result = UNKNOWN;
        for(RightsCategory category: setRightsCategory){
            if(url.matches(category.getRegularExpression())){
                result = category;
                break;
            }
        }
        return result;
    }
}
