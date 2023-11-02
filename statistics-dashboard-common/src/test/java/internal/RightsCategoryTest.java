package internal;

import eu.europeana.statistics.dashboard.common.internal.RightsCategory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


class RightsCategoryTest {

    @Test
    void testMatchCategoryFromUrlCC0_expectSuccess(){
        String cc0Example = "https://creativecommons.org/publicdomain/zero/3.0/ab/";
        String cc0OtherExample = "http://creativecommons.org/publicdomain/zero/2.5/";
        String caseSensitiveExample = "http://creativecommons.org/publicdomain/ZERO/6.8/";
        RightsCategory firstResult = RightsCategory.matchCategoryFromUrl(cc0Example);
        RightsCategory secondResult = RightsCategory.matchCategoryFromUrl(cc0OtherExample);
        RightsCategory thirdResult = RightsCategory.matchCategoryFromUrl(caseSensitiveExample);
        assertEquals(RightsCategory.CC0, firstResult);
        assertEquals(RightsCategory.CC0, secondResult);
        assertEquals(RightsCategory.UNKNOWN, thirdResult);
    }

    @Test
    void testMatchCategoryFromUrlPublicDomainMark_expectSuccess(){
        String PublicDomainMarkExample = "https://creativecommons.org/publicdomain/mark/3.0/ab/";
        String PublicDomainMarkOtherExample = "http://creativecommons.org/publicdomain/mark/2.5/";
        String caseSensitiveExample = "http://creativecommons.org/publicdomain/MARK/6.8/";
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
        String ccByNcNdAnotherPossibleExample = "http://creativecommons.org/licenses/by-nc-nd/3.0/es/deed.ca";
        RightsCategory firstResult = RightsCategory.matchCategoryFromUrl(ccByNcNdExample);
        RightsCategory secondResult = RightsCategory.matchCategoryFromUrl(ccByNcNdOtherExample);
        RightsCategory thirdResult = RightsCategory.matchCategoryFromUrl(caseSensitiveExample);
        RightsCategory fourthResult = RightsCategory.matchCategoryFromUrl(ccByNcNdAnotherPossibleExample);
        assertEquals(RightsCategory.CC_BY_NC_ND, firstResult);
        assertEquals(RightsCategory.CC_BY_NC_ND, secondResult);
        assertEquals(RightsCategory.UNKNOWN, thirdResult);
        assertEquals(RightsCategory.CC_BY_NC_ND, fourthResult);
    }

    @Test
    void testMatchCategoryFromUrlNoCopyrightNonCommercial_expectSuccess(){
        String noCopyrightNonCommercialExample = "https://rightsstatements.org/vocab/NoC-NC/3.0/ab/";
        String noCopyrightNonCommercialOtherExample = "http://rightsstatements.org/vocab/NoC-NC/2.5/";
        String caseSensitiveExample = "http://rightsstatements.org/vocab/noc-nc/6.8/";
        String noCopyrightNonCommercialOtherPossibleValue = "http://www.europeana.eu/rights/out-of-copyright-non-commercial/";
        String noCopyrightNonCommercialPageExample = "https://rightsstatements.org/page/NoC-NC/3.0/";
        RightsCategory firstResult = RightsCategory.matchCategoryFromUrl(noCopyrightNonCommercialExample);
        RightsCategory secondResult = RightsCategory.matchCategoryFromUrl(noCopyrightNonCommercialOtherExample);
        RightsCategory thirdResult = RightsCategory.matchCategoryFromUrl(caseSensitiveExample);
        RightsCategory fourthResult = RightsCategory.matchCategoryFromUrl(noCopyrightNonCommercialOtherPossibleValue);
        RightsCategory fifthResult = RightsCategory.matchCategoryFromUrl(noCopyrightNonCommercialPageExample);
        assertEquals(RightsCategory.NO_COPYRIGHT_NON_COMMERCIAL, firstResult);
        assertEquals(RightsCategory.NO_COPYRIGHT_NON_COMMERCIAL, secondResult);
        assertEquals(RightsCategory.UNKNOWN, thirdResult);
        assertEquals(RightsCategory.NO_COPYRIGHT_NON_COMMERCIAL, fourthResult);
        assertEquals(RightsCategory.NO_COPYRIGHT_NON_COMMERCIAL, fifthResult);
    }

    @Test
    void testMatchCategoryFromUrlNoCopyrightOtherKnownLegal_expectSuccess(){
        String noCopyrightNonCommercialExample = "https://rightsstatements.org/vocab/NoC-OKLR/3.0/ab/";
        String noCopyrightNonCommercialOtherExample = "http://rightsstatements.org/vocab/NoC-OKLR/2.5/";
        String caseSensitiveExample = "http://rightsstatements.org/vocab/noc-oklr/6.8/";
        String noCopyrightNonCommercialPageExample = "https://rightsstatements.org/page/NoC-OKLR/3.0/";
        RightsCategory firstResult = RightsCategory.matchCategoryFromUrl(noCopyrightNonCommercialExample);
        RightsCategory secondResult = RightsCategory.matchCategoryFromUrl(noCopyrightNonCommercialOtherExample);
        RightsCategory thirdResult = RightsCategory.matchCategoryFromUrl(caseSensitiveExample);
        RightsCategory fourthResult = RightsCategory.matchCategoryFromUrl(noCopyrightNonCommercialPageExample);
        assertEquals(RightsCategory.NO_COPYRIGHT_OTHER_KNOWN_LEGAL, firstResult);
        assertEquals(RightsCategory.NO_COPYRIGHT_OTHER_KNOWN_LEGAL, secondResult);
        assertEquals(RightsCategory.UNKNOWN, thirdResult);
        assertEquals(RightsCategory.NO_COPYRIGHT_OTHER_KNOWN_LEGAL, fourthResult);
    }

    @Test
    void testMatchCategoryFromUrlInCopyrightEducational_expectSuccess(){
        String inCopyrightEducationalExample = "https://rightsstatements.org/vocab/InC-EDU/3.0/ab/";
        String inCopyrightEducationalOtherExample = "http://rightsstatements.org/vocab/InC-EDU/2.5/";
        String caseSensitiveExample = "http://rightsstatements.org/vocab/inc-edu/6.8/";
        String inCopyrightEducationalPageExample = "https://rightsstatements.org/page/InC-EDU/3.0";
        RightsCategory firstResult = RightsCategory.matchCategoryFromUrl(inCopyrightEducationalExample);
        RightsCategory secondResult = RightsCategory.matchCategoryFromUrl(inCopyrightEducationalOtherExample);
        RightsCategory thirdResult = RightsCategory.matchCategoryFromUrl(caseSensitiveExample);
        RightsCategory fifthResult = RightsCategory.matchCategoryFromUrl(inCopyrightEducationalPageExample);
        assertEquals(RightsCategory.IN_COPYRIGHT_EDUCATIONAL, firstResult);
        assertEquals(RightsCategory.IN_COPYRIGHT_EDUCATIONAL, secondResult);
        assertEquals(RightsCategory.UNKNOWN, thirdResult);
        assertEquals(RightsCategory.IN_COPYRIGHT_EDUCATIONAL, fifthResult);
    }

    @Test
    void testMatchCategoryFromUrlInCopyrightEU_expectSuccess(){
        String inCopyrightEUExample = "https://rightsstatements.org/vocab/InC-OW-EU/3.0/ab/";
        String inCopyrightEUOtherExample = "http://rightsstatements.org/vocab/InC-OW-EU/2.5/";
        String caseSensitiveExample = "http://rightsstatements.org/vocab/inc-ow-edu/6.8/";
        String inCopyrightEUPageExample = "https://rightsstatements.org/page/InC-OW-EU/3.0";
        String inCopyrightEUTestExample = "http://rightsstatements.org/vocab/InC-OW-EU/1.0/";
        RightsCategory firstResult = RightsCategory.matchCategoryFromUrl(inCopyrightEUExample);
        RightsCategory secondResult = RightsCategory.matchCategoryFromUrl(inCopyrightEUOtherExample);
        RightsCategory thirdResult = RightsCategory.matchCategoryFromUrl(caseSensitiveExample);
        RightsCategory fourthResult = RightsCategory.matchCategoryFromUrl(inCopyrightEUPageExample);
        RightsCategory fifthResult = RightsCategory.matchCategoryFromUrl(inCopyrightEUTestExample);
        assertEquals(RightsCategory.IN_COPYRIGHT_EU, firstResult);
        assertEquals(RightsCategory.IN_COPYRIGHT_EU, secondResult);
        assertEquals(RightsCategory.UNKNOWN, thirdResult);
        assertEquals(RightsCategory.IN_COPYRIGHT_EU, fourthResult);
        assertEquals(RightsCategory.IN_COPYRIGHT_EU, fifthResult);
    }

    @Test
    void testMatchCategoryFromUrlInCopyright_expectSuccess(){
        String inCopyrightExample = "https://rightsstatements.org/vocab/InC/3.0/ab/";
        String inCopyrightOtherExample = "http://rightsstatements.org/vocab/InC/2.5/";
        String caseSensitiveExample = "http://rightsstatements.org/vocab/inc/6.8/";
        String inCopyrightPageExample = "https://rightsstatements.org/page/InC/3.0";
        String inCopyrightAnotherPossibleValue = "http://www.europeana.eu/rights/rr-f/";
        String inCopyrightDifferentExample = "http://rightsstatements.org/page/InC/1.0/?language=en";
        RightsCategory firstResult = RightsCategory.matchCategoryFromUrl(inCopyrightExample);
        RightsCategory secondResult = RightsCategory.matchCategoryFromUrl(inCopyrightOtherExample);
        RightsCategory thirdResult = RightsCategory.matchCategoryFromUrl(caseSensitiveExample);
        RightsCategory fourthResult = RightsCategory.matchCategoryFromUrl(inCopyrightPageExample);
        RightsCategory fifthResult = RightsCategory.matchCategoryFromUrl(inCopyrightAnotherPossibleValue);
        RightsCategory sixthResult = RightsCategory.matchCategoryFromUrl(inCopyrightDifferentExample);
        assertEquals(RightsCategory.IN_COPYRIGHT, firstResult);
        assertEquals(RightsCategory.IN_COPYRIGHT, secondResult);
        assertEquals(RightsCategory.UNKNOWN, thirdResult);
        assertEquals(RightsCategory.IN_COPYRIGHT, fourthResult);
        assertEquals(RightsCategory.IN_COPYRIGHT, fifthResult);
        assertEquals(RightsCategory.IN_COPYRIGHT, sixthResult);
    }

    @Test
    void testMatchCategoryFromUrlCopyrightNotEvaluated_expectSuccess(){
        String CopyrightNotEvaluatedExample = "https://rightsstatements.org/vocab/CNE/3.0/ab/";
        String CopyrightNotEvaluatedOtherExample = "http://rightsstatements.org/vocab/CNE/2.5/";
        String caseSensitiveExample = "http://rightsstatements.org/vocab/cne/6.8/";
        String copyrightNotEvaluatedOtherPossibleValue = "http://www.europeana.eu/rights/unknown/";
        String CopyrightNotEvaluatedPageExample = "https://rightsstatements.org/page/CNE/3.0";
        RightsCategory firstResult = RightsCategory.matchCategoryFromUrl(CopyrightNotEvaluatedExample);
        RightsCategory secondResult = RightsCategory.matchCategoryFromUrl(CopyrightNotEvaluatedOtherExample);
        RightsCategory thirdResult = RightsCategory.matchCategoryFromUrl(caseSensitiveExample);
        RightsCategory fourthResult = RightsCategory.matchCategoryFromUrl(copyrightNotEvaluatedOtherPossibleValue);
        RightsCategory fifthResult = RightsCategory.matchCategoryFromUrl(CopyrightNotEvaluatedPageExample);
        assertEquals(RightsCategory.COPYRIGHT_NOT_EVALUATED, firstResult);
        assertEquals(RightsCategory.COPYRIGHT_NOT_EVALUATED, secondResult);
        assertEquals(RightsCategory.UNKNOWN, thirdResult);
        assertEquals(RightsCategory.COPYRIGHT_NOT_EVALUATED, fourthResult);
        assertEquals(RightsCategory.COPYRIGHT_NOT_EVALUATED, fifthResult);
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

    @Test
    void testToCategoryFromName_expectSuccess(){
        String nameCc0 = "CC0";
        String nameCcBy = "CC BY";
        String nameCcBySa = "CC BY-SA";
        String nameNoCopyright = "No Copyright - Non Commercial Re-Use Only";
        RightsCategory resultCc0 = RightsCategory.toCategoryFromName(nameCc0);
        RightsCategory resultCcBy = RightsCategory.toCategoryFromName(nameCcBy);
        RightsCategory resultCcBySa = RightsCategory.toCategoryFromName(nameCcBySa);
        RightsCategory resultNoCopyright = RightsCategory.toCategoryFromName(nameNoCopyright);
        assertEquals(RightsCategory.CC0, resultCc0);
        assertEquals(RightsCategory.CC_BY, resultCcBy);
        assertEquals(RightsCategory.CC_BY_SA, resultCcBySa);
        assertEquals(RightsCategory.NO_COPYRIGHT_NON_COMMERCIAL, resultNoCopyright);
    }

    @Test
    void testToCategoryFromName_expectFail(){
        String name = "Wrong value";
        String nameCcBy = "CC_BY";
        String nameCcBySa = "CC_by_SA";
        String nameNoCopyright = "NO_COPYRIGHT_NON_COMMERCIAL";
        assertThrows(IllegalArgumentException.class, () -> RightsCategory.toCategoryFromName(name));
        assertThrows(IllegalArgumentException.class, () -> RightsCategory.toCategoryFromName(nameCcBy));
        assertThrows(IllegalArgumentException.class, () -> RightsCategory.toCategoryFromName(nameCcBySa));
        assertThrows(IllegalArgumentException.class, () -> RightsCategory.toCategoryFromName(nameNoCopyright));
    }
}
