package com.company.model;

public class TimeRecord {

    private static final char SEPARATOR = ':';

    private static final String HH_MIN = "00";
    private static final String MM_MIN = "00";
    private static final String SS_MIN = "00";

    private static final String HH_MAX = "24";
    private static final String MM_MAX = "59";
    private static final String SS_MAX = "59";

    private static final String HHMMSS_MIN = HH_MIN + SEPARATOR + MM_MIN + SEPARATOR + SS_MIN;
    private static final String HHMMSS_MAX = HH_MAX + SEPARATOR + MM_MIN + SEPARATOR + SS_MIN;

    private String hhMmSsStr;   // e.g. "183730" for 18 h. 37 min. 30 sec.

    private boolean minValue;
    private boolean maxValue;

    public TimeRecord() {
        this.hhMmSsStr = HHMMSS_MIN;
    }

    public TimeRecord(String hhMmSsStr) {
        this.hhMmSsStr = hhMmSsStr;
    }

    public String getHhMmSsStr() {
        return hhMmSsStr;
    }

    public void setHhMmSsStr(String hhMmSsStr) {
        this.hhMmSsStr = hhMmSsStr;
    }

    public String toHexString() {
        return hhMmSsStr.substring(0, 2) + ((Integer.parseInt(hhMmSsStr.substring(2,4)) / 60.0) * 60.0)/7.5;
    }

    @Override
    public String toString() {
        return hhMmSsStr.substring(0, 2) + SEPARATOR + hhMmSsStr.substring(2, 4) + SEPARATOR + hhMmSsStr.substring(4, 6);
    }

    public boolean isMin() {
        return hhMmSsStr.equals(HHMMSS_MIN);
    }

    public void setToMin() {
        hhMmSsStr = HHMMSS_MIN;
    }

    public boolean isMax() {
        return hhMmSsStr.equals(HHMMSS_MAX);
    }

    public void setToMax() {
        hhMmSsStr = HHMMSS_MAX;
    }

}
