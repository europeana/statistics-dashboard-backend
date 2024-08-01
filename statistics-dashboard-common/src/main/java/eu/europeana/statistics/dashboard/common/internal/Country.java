package eu.europeana.statistics.dashboard.common.internal;

/**
 * Enum to represent countries available to add to a dataset process
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * This enum class is used as a database key and should NOT be changed *
 * without taking the existing database into account!                  *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */
public enum Country {

    ALBANIA("AL", "Albania"), AUSTRIA("AT", "Austria"), AZERBAIJAN("AZ", "Azerbaijan"),
    BELARUS("BY","Belarus"), BELGIUM("BE","Belgium"), BOSNIAAND_HERZEGOVINA("BA",
            "Bosnia and Herzegovina"), BULGARIA("BG","Bulgaria"), CROATIA("HR","Croatia"),
    CYPRUS("CY","Cyprus"), CZECH_REPUBLIC("CZ","Czech Republic"), DENMARK("DK","Denmark"),
    ESTONIA("EE","Estonia"), EUROPE("EU","Europe"), FINLAND("FI","Finland"),
    FRANCE("FR","France"), GEORGIA("GE","Georgia"), GERMANY("DE","Germany"),
    GREECE("GR","Greece"), HUNGARY("HU","Hungary"), ICELAND("IS","Iceland"),
    IRELAND("IE","Ireland"), ITALY("IT","Italy"), ISRAEL("IL","Israel"),
    LATVIA("LV","Latvia"), LITHUANIA("LT","Lithuania"), LUXEMBOURG("LU","Luxembourg"),
    NORTH_MACEDONIA("MK","North Macedonia"), MALTA("MT","Malta"), MOLDOVA("MD","Moldova"),
    MONTENEGRO("ME","Montenegro"), NETHERLANDS("NL","Netherlands"), NORWAY("BV","Norway"),
    POLAND("PL","Poland"), PORTUGAL("PT","Portugal"), ROMANIA("RO","Romania"),
    RUSSIA("RU","Russia"), SERBIA("RS","Serbia"), SLOVAKIA("SK","Slovakia"),
    SLOVENIA("SI","Slovenia"), SPAIN("ES","Spain"), SWEDEN("SE","Sweden"),
    SWITZERLAND("CH","Switzerland"), TURKEY("TR","Turkey"), UKRAINE("UA","Ukraine"),
    UNITED_KINGDOM("GB","United Kingdom"), UNITED_STATES_OF_AMERICA("UM","United States of America");

    private final String isoCodeCountry;
    private final String countryName;

    /**
     * Constructor of the enum
     * @param isoCodeCountry - The ISO code to represent the country
     * @param countryName -  The name of the country
     */
    Country(String isoCodeCountry, String countryName) {
        this.isoCodeCountry = isoCodeCountry;
        this.countryName = countryName;
    }

    /**
     * Returns the ISO code of the country
     * @return The ISO code of the country as a String
     */
    public String getIsoCodeCountry() {
        return isoCodeCountry;
    }

    /**
     * Returns the country name as a String value
     * @return The country name as a Sting value
     */
    public String getCountryName() {
        return countryName;
    }

    /**
     * Method that returns the enum with the corresponding given string value
     * @param countryName The string value to match the enum values with
     * @return The enum country values that matches the given string value
     */
    public static Country fromCountryName(String countryName){
        Country result = null;
        for(Country country : values()){
            if(country.getCountryName().equals(countryName)){
                result = country;
                break;
            }
        }

        if(result == null){
            throw new IllegalArgumentException("Country name "+countryName+" not found");
        }

        return result;

    }
}
