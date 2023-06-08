package org.example.enums;

import java.util.Arrays;
import java.util.Optional;
import java.util.TimeZone;

public enum Region {
    JERUSALEM("ירושלים", 31.767, 35.233, 686.0, TimeZone.getTimeZone("Asia/Jerusalem"), true, 40.0);

    private String name;
    private double latitude;
    private double longitude;
    private double elevation;
    private TimeZone timeZone;
    private boolean inIsrael;
    private double candleLightingOffset;

    Region(String name, double latitude, double longitude, double elevation, TimeZone timeZone, boolean inIsrael, double candleLightingOffset) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.elevation = elevation;
        this.timeZone = timeZone;
        this.inIsrael = inIsrael;
        this.candleLightingOffset = candleLightingOffset;
    }

    public static Region fromName(String name) {
        return Arrays.stream(values())
                .filter(r -> r.name.equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getElevation() {
        return elevation;
    }

    public void setElevation(double elevation) {
        this.elevation = elevation;
    }

    public TimeZone getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
    }

    public boolean isInIsrael() {
        return inIsrael;
    }

    public void setInIsrael(boolean inIsrael) {
        this.inIsrael = inIsrael;
    }

    public double getCandleLightingOffset() {
        return candleLightingOffset;
    }

    public void setCandleLightingOffset(double candleLightingOffset) {
        this.candleLightingOffset = candleLightingOffset;
    }
}
