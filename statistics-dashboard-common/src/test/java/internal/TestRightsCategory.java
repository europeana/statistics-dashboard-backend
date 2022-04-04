package internal;

import eu.europeana.statistics.dashboard.common.internal.RightsCategory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class TestRightsCategory {

    @Test
    void testMatchCategoryFromUrlCC0_expectSuccess(){
        String cc0Example = "https://creativecommons.org/licences/zero/3.0/ab/";
        String cc0OtherExample = "http://creativecommons.org/licenses/zero/2.5/";
        String caseSensitiveExample = "http://creativecommons.org/licenses/ZERO/6.8/";
        RightsCategory firstResult = RightsCategory.matchCategoryFromUrl(cc0Example);
        RightsCategory secondResult = RightsCategory.matchCategoryFromUrl(cc0OtherExample);
        RightsCategory thirdResult = RightsCategory.matchCategoryFromUrl(caseSensitiveExample);
        assertEquals(RightsCategory.CC0, firstResult);
        assertEquals(RightsCategory.CC0, secondResult);
        assertEquals(RightsCategory.UNKNOWN, thirdResult);
    }

    @Test
    void testMatchCategoryFromUrlPublicDomainMark_expectSuccess(){
        String PublicDomainMarkExample = "https://creativecommons.org/licences/mark/3.0/ab/";
        String PublicDomainMarkOtherExample = "http://creativecommons.org/licenses/mark/2.5/";
        String caseSensitiveExample = "http://creativecommons.org/licenses/MARK/6.8/";
        RightsCategory firstResult = RightsCategory.matchCategoryFromUrl(PublicDomainMarkExample);
        RightsCategory secondResult = RightsCategory.matchCategoryFromUrl(PublicDomainMarkOtherExample);
        RightsCategory thirdResult = RightsCategory.matchCategoryFromUrl(caseSensitiveExample);
        assertEquals(RightsCategory.PUBLIC_DOMAIN_MARK, firstResult);
        assertEquals(RightsCategory.PUBLIC_DOMAIN_MARK, secondResult);
        assertEquals(RightsCategory.UNKNOWN, thirdResult);
    }

    @Test
    void testMatchCategoryFromUrlCCBY_expectSuccess(){
        String ccByExample = "https://creativecommons.org/licences/by/3.0/ab/";
        String ccByOtherExample = "http://creativecommons.org/licenses/by/2.5/";
        String caseSensitiveExample = "http://creativecommons.org/licenses/BY/6.8/";
        RightsCategory firstResult = RightsCategory.matchCategoryFromUrl(ccByExample);
        RightsCategory secondResult = RightsCategory.matchCategoryFromUrl(ccByOtherExample);
        RightsCategory thirdResult = RightsCategory.matchCategoryFromUrl(caseSensitiveExample);
        assertEquals(RightsCategory.CC_BY, firstResult);
        assertEquals(RightsCategory.CC_BY, secondResult);
        assertEquals(RightsCategory.UNKNOWN, thirdResult);
    }

    @Test
    void testMatchCategoryFromUrlCCBYSA_expectSuccess(){
        String ccBySaExample = "https://creativecommons.org/licences/by-sa/3.0/ab/";
        String ccBySaOtherExample = "http://creativecommons.org/licenses/by-sa/2.5/";
        String caseSensitiveExample = "http://creativecommons.org/licenses/BY-SA/6.8/";
        RightsCategory firstResult = RightsCategory.matchCategoryFromUrl(ccBySaExample);
        RightsCategory secondResult = RightsCategory.matchCategoryFromUrl(ccBySaOtherExample);
        RightsCategory thirdResult = RightsCategory.matchCategoryFromUrl(caseSensitiveExample);
        assertEquals(RightsCategory.CC_BY_SA, firstResult);
        assertEquals(RightsCategory.CC_BY_SA, secondResult);
        assertEquals(RightsCategory.UNKNOWN, thirdResult);
    }

    @Test
    void testMatchCategoryFromUrlCCBYND_expectSuccess(){
        String ccByNdExample = "https://creativecommons.org/licences/by-nd/3.0/ab/";
        String ccByNdOtherExample = "http://creativecommons.org/licenses/by-nd/2.5/";
        String caseSensitiveExample = "http://creativecommons.org/licenses/BY-ND/6.8/";
        RightsCategory firstResult = RightsCategory.matchCategoryFromUrl(ccByNdExample);
        RightsCategory secondResult = RightsCategory.matchCategoryFromUrl(ccByNdOtherExample);
        RightsCategory thirdResult = RightsCategory.matchCategoryFromUrl(caseSensitiveExample);
        assertEquals(RightsCategory.CC_BY_ND, firstResult);
        assertEquals(RightsCategory.CC_BY_ND, secondResult);
        assertEquals(RightsCategory.UNKNOWN, thirdResult);
    }

    @Test
    void testMatchCategoryFromUrlCCBYNC_expectSuccess(){
        String ccByNcExample = "https://creativecommons.org/licences/by-nc/3.0/ab/";
        String ccByNcOtherExample = "http://creativecommons.org/licenses/by-nc/2.5/";
        String caseSensitiveExample = "http://creativecommons.org/licenses/BY-NC/6.8/";
        RightsCategory firstResult = RightsCategory.matchCategoryFromUrl(ccByNcExample);
        RightsCategory secondResult = RightsCategory.matchCategoryFromUrl(ccByNcOtherExample);
        RightsCategory thirdResult = RightsCategory.matchCategoryFromUrl(caseSensitiveExample);
        assertEquals(RightsCategory.CC_BY_NC, firstResult);
        assertEquals(RightsCategory.CC_BY_NC, secondResult);
        assertEquals(RightsCategory.UNKNOWN, thirdResult);
    }

    @Test
    void testMatchCategoryFromUrlCCBYNCSA_expectSuccess(){
        String ccByNcSaExample = "https://creativecommons.org/licences/by-nc-sa/3.0/ab/";
        String ccByNcSaOtherExample = "http://creativecommons.org/licenses/by-nc-sa/2.5/";
        String caseSensitiveExample = "http://creativecommons.org/licenses/BY-NC-SA/6.8/";
        RightsCategory firstResult = RightsCategory.matchCategoryFromUrl(ccByNcSaExample);
        RightsCategory secondResult = RightsCategory.matchCategoryFromUrl(ccByNcSaOtherExample);
        RightsCategory thirdResult = RightsCategory.matchCategoryFromUrl(caseSensitiveExample);
        assertEquals(RightsCategory.CC_BY_NC_SA, firstResult);
        assertEquals(RightsCategory.CC_BY_NC_SA, secondResult);
        assertEquals(RightsCategory.UNKNOWN, thirdResult);
    }

    @Test
    void testMatchCategoryFromUrlCCBYNCND_expectSuccess(){
        String ccByNcNdExample = "https://creativecommons.org/licences/by-nc-nd/3.0/ab/";
        String ccByNcNdOtherExample = "http://creativecommons.org/licenses/by-nc-nd/2.5/";
        String caseSensitiveExample = "http://creativecommons.org/licenses/BY-NC-ND/6.8/";
        RightsCategory firstResult = RightsCategory.matchCategoryFromUrl(ccByNcNdExample);
        RightsCategory secondResult = RightsCategory.matchCategoryFromUrl(ccByNcNdOtherExample);
        RightsCategory thirdResult = RightsCategory.matchCategoryFromUrl(caseSensitiveExample);
        assertEquals(RightsCategory.CC_BY_NC_ND, firstResult);
        assertEquals(RightsCategory.CC_BY_NC_ND, secondResult);
        assertEquals(RightsCategory.UNKNOWN, thirdResult);
    }

    @Test
    void testMatchCategoryFromUrlNoCopyrightNonCommercial_expectSuccess(){
        String noCopyrightNonCommercialExample = "https://rightsstatements.org/vocab/NoC-NC/3.0/ab/";
        String noCopyrightNonCommercialOtherExample = "http://rightsstatements.org/vocab/NoC-NC/2.5/";
        String caseSensitiveExample = "http://rightsstatements.org/vocab/noc-nc/6.8/";
        RightsCategory firstResult = RightsCategory.matchCategoryFromUrl(noCopyrightNonCommercialExample);
        RightsCategory secondResult = RightsCategory.matchCategoryFromUrl(noCopyrightNonCommercialOtherExample);
        RightsCategory thirdResult = RightsCategory.matchCategoryFromUrl(caseSensitiveExample);
        assertEquals(RightsCategory.NO_COPYRIGHT_NON_COMMERCIAL, firstResult);
        assertEquals(RightsCategory.NO_COPYRIGHT_NON_COMMERCIAL, secondResult);
        assertEquals(RightsCategory.UNKNOWN, thirdResult);
    }

    @Test
    void testMatchCategoryFromUrlNoCopyrightOtherKnownLegal_expectSuccess(){
        String noCopyrightNonCommercialExample = "https://rightsstatements.org/vocab/NoC-OKLR/3.0/ab/";
        String noCopyrightNonCommercialOtherExample = "http://rightsstatements.org/vocab/NoC-OKLR/2.5/";
        String caseSensitiveExample = "http://rightsstatements.org/vocab/noc-oklr/6.8/";
        RightsCategory firstResult = RightsCategory.matchCategoryFromUrl(noCopyrightNonCommercialExample);
        RightsCategory secondResult = RightsCategory.matchCategoryFromUrl(noCopyrightNonCommercialOtherExample);
        RightsCategory thirdResult = RightsCategory.matchCategoryFromUrl(caseSensitiveExample);
        assertEquals(RightsCategory.NO_COPYRIGHT_OTHER_KNOWN_LEGAL, firstResult);
        assertEquals(RightsCategory.NO_COPYRIGHT_OTHER_KNOWN_LEGAL, secondResult);
        assertEquals(RightsCategory.UNKNOWN, thirdResult);
    }

    @Test
    void testMatchCategoryFromUrlInCopyrightEducational_expectSuccess(){
        String inCopyrightEducationalExample = "https://rightsstatements.org/vocab/InC-EDU/3.0/ab/";
        String inCopyrightEducationalOtherExample = "http://rightsstatements.org/vocab/InC-EDU/2.5/";
        String caseSensitiveExample = "http://rightsstatements.org/vocab/inc-edu/6.8/";
        RightsCategory firstResult = RightsCategory.matchCategoryFromUrl(inCopyrightEducationalExample);
        RightsCategory secondResult = RightsCategory.matchCategoryFromUrl(inCopyrightEducationalOtherExample);
        RightsCategory thirdResult = RightsCategory.matchCategoryFromUrl(caseSensitiveExample);
        assertEquals(RightsCategory.IN_COPYRIGHT_EDUCATIONAL, firstResult);
        assertEquals(RightsCategory.IN_COPYRIGHT_EDUCATIONAL, secondResult);
        assertEquals(RightsCategory.UNKNOWN, thirdResult);
    }

    @Test
    void testMatchCategoryFromUrlInCopyrightEU_expectSuccess(){
        String inCopyrightEUExample = "https://rightsstatements.org/vocab/InC-OW-EU/3.0/ab/";
        String inCopyrightEUOtherExample = "http://rightsstatements.org/vocab/InC-OW-EU/2.5/";
        String caseSensitiveExample = "http://rightsstatements.org/vocab/inc-ow-edu/6.8/";
        RightsCategory firstResult = RightsCategory.matchCategoryFromUrl(inCopyrightEUExample);
        RightsCategory secondResult = RightsCategory.matchCategoryFromUrl(inCopyrightEUOtherExample);
        RightsCategory thirdResult = RightsCategory.matchCategoryFromUrl(caseSensitiveExample);
        assertEquals(RightsCategory.IN_COPYRIGHT_EU, firstResult);
        assertEquals(RightsCategory.IN_COPYRIGHT_EU, secondResult);
        assertEquals(RightsCategory.UNKNOWN, thirdResult);
    }

    @Test
    void testMatchCategoryFromUrlInCopyright_expectSuccess(){
        String inCopyrightExample = "https://rightsstatements.org/vocab/InC/3.0/ab/";
        String inCopyrightOtherExample = "http://rightsstatements.org/vocab/InC/2.5/";
        String caseSensitiveExample = "http://rightsstatements.org/vocab/inc/6.8/";
        RightsCategory firstResult = RightsCategory.matchCategoryFromUrl(inCopyrightExample);
        RightsCategory secondResult = RightsCategory.matchCategoryFromUrl(inCopyrightOtherExample);
        RightsCategory thirdResult = RightsCategory.matchCategoryFromUrl(caseSensitiveExample);
        assertEquals(RightsCategory.IN_COPYRIGHT, firstResult);
        assertEquals(RightsCategory.IN_COPYRIGHT, secondResult);
        assertEquals(RightsCategory.UNKNOWN, thirdResult);
    }

    @Test
    void testMatchCategoryFromUrlCopyrightNotEvaluated_expectSuccess(){
        String CopyrightNotEvaluatedExample = "https://rightsstatements.org/vocab/CNE/3.0/ab/";
        String CopyrightNotEvaluatedOtherExample = "http://rightsstatements.org/vocab/CNE/2.5/";
        String caseSensitiveExample = "http://rightsstatements.org/vocab/cne/6.8/";
        RightsCategory firstResult = RightsCategory.matchCategoryFromUrl(CopyrightNotEvaluatedExample);
        RightsCategory secondResult = RightsCategory.matchCategoryFromUrl(CopyrightNotEvaluatedOtherExample);
        RightsCategory thirdResult = RightsCategory.matchCategoryFromUrl(caseSensitiveExample);
        assertEquals(RightsCategory.COPYRIGHT_NOT_EVALUATED, firstResult);
        assertEquals(RightsCategory.COPYRIGHT_NOT_EVALUATED, secondResult);
        assertEquals(RightsCategory.UNKNOWN, thirdResult);
    }

    @Test
    void testMatchCategoryFromUrlUnknown_expectSuccess(){
        String firstExample = "https://somethingwrong.org/abcde/fgh/1.2/ab/";
        String secondExample = "https://rightsSTATEMENTS.org/VOCAB/CNE/2.5/";
        String thirdExample = "http://CREATUVEcommons.org/licenses/BY-sa/4.7/";
        RightsCategory firstResult = RightsCategory.matchCategoryFromUrl(firstExample);
        RightsCategory secondResult = RightsCategory.matchCategoryFromUrl(secondExample);
        RightsCategory thirdResult = RightsCategory.matchCategoryFromUrl(thirdExample);
        assertEquals(RightsCategory.UNKNOWN, firstResult);
        assertEquals(RightsCategory.UNKNOWN, secondResult);
        assertEquals(RightsCategory.UNKNOWN, thirdResult);
    }
}
