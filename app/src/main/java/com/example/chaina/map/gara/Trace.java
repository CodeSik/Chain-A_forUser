package com.example.chaina.map.gara;

public class Trace {
    public Trace(String lat, String lng, String companyName, String addressName, String time) {
        this.lat = lat;
        this.lng = lng;
        this.companyName = companyName;
        this.addressName = addressName;
        this.time = time;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    private String lat;
    private String lng;
    private String companyName;
    private String addressName;
    private String time;
}
