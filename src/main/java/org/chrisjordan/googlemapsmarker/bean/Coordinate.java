package org.chrisjordan.googlemapsmarker.bean;

public class Coordinate {
    private double latitude;
    private double longitude;

    public Coordinate(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "Coordinate [latitude=" + latitude + ", longitude=" + longitude + "]";
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
