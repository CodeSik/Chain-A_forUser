package com.example.chaina.map;

import com.google.gson.annotations.SerializedName;

public class RecordResponse {
    @SerializedName("lat")
    private String lat;
    @SerializedName("lng")
    private String lng;
    @SerializedName("companyName")
    private String companyName;
    @SerializedName("addressName")
    private String addressName;
    @SerializedName("timestamp")
    private String time;

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
}
