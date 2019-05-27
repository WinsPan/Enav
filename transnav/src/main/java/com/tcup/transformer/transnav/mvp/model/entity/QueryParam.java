package com.tcup.transformer.transnav.mvp.model.entity;

public class QueryParam {
    private final String userAccount = "appAccount";
    private final String token = "df6bc488b3c6f3a2140f316c923462e5";
    private String siteNo;
    private String siteName;
    private String siteAddr;
    private String siteFromDate;
    private String siteEndDate;
    private int typeId;
    private int areaId;
    private String siteStatus;
    private int pageIndex;
    private int pageSize;

    public String getUserAccount() {
        return userAccount;
    }

    public String getToken() {
        return token;
    }

    public String getSiteNo() {
        return siteNo;
    }

    public void setSiteNo(String siteNo) {
        this.siteNo = siteNo;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getSiteAddr() {
        return siteAddr;
    }

    public void setSiteAddr(String siteAddr) {
        this.siteAddr = siteAddr;
    }

    public String getSiteFromDate() {
        return siteFromDate;
    }

    public void setSiteFromDate(String siteFromDate) {
        this.siteFromDate = siteFromDate;
    }

    public String getSiteEndDate() {
        return siteEndDate;
    }

    public void setSiteEndDate(String siteEndDate) {
        this.siteEndDate = siteEndDate;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public int getAreaId() {
        return areaId;
    }

    public void setAreaId(int areaId) {
        this.areaId = areaId;
    }

    public String getSiteStatus() {
        return siteStatus;
    }

    public void setSiteStatus(String siteStatus) {
        this.siteStatus = siteStatus;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
