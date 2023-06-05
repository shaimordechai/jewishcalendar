package org.example.enums;

import java.util.Arrays;
import java.util.Optional;
import java.util.TimeZone;

public enum Region {
    JERUSALEM("ירושלים", 31.76832, 35.21371, 769.0, TimeZone.getTimeZone("Asia/Jerusalem"), true);

    private String name;
    private double latitude;
    private double longitude;
    private double elevation;
    private TimeZone timeZone;
    private boolean inIsrael;

    Region(String name, double latitude, double longitude, double elevation, TimeZone timeZone, boolean inIsrael) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.elevation = elevation;
        this.timeZone = timeZone;
        this.inIsrael = inIsrael;
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
}
