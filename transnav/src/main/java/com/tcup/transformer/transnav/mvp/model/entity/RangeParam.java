package com.tcup.transformer.transnav.mvp.model.entity;

public class RangeParam {
    private final String userAccount = "appAccount";
    private final String token = "df6bc488b3c6f3a2140f316c923462e5";
    private String markLng;
    private String markLat;
    private String range;

    public String getUserAccount() {
        return userAccount;
    }

    public String getToken() {
        return token;
    }

    public String getMarkLng() {
        return markLng;
    }

    public void setMarkLng(String markLng) {
        this.markLng = markLng;
    }

    public String getMarkLat() {
        return markLat;
    }

    public void setMarkLat(String markLat) {
        this.markLat = markLat;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }
}
