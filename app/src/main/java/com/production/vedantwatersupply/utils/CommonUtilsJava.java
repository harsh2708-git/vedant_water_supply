package com.production.vedantwatersupply.utils;

public class CommonUtilsJava {
    /**
     * Get Numeric value from a formatted number string
     *
     * @param doubleString number string
     * @return respective no. 0.0 if string is not a valid number.
     */
    public static double getNumeric(String doubleString) {
        try {
            if (doubleString.contains(",")) {
                doubleString = doubleString.replace(",", "");
            }
            return Double.parseDouble(doubleString);
        } catch (Exception e) {
            return 0.0;
        }
    }
}
