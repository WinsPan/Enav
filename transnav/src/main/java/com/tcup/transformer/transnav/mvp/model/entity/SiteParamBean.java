package com.tcup.transformer.transnav.mvp.model.entity;

public class SiteParamBean {
    private final String userAccount = "appAccount";
    private final String token = "df6bc488b3c6f3a2140f316c923462e5";
    //id
    private int id;
    //站点编号
    private String siteNo;
    //站点名称
    private String siteName;
    //站点地址
    private String siteAddr;
    //站点投运日期
    private String siteDate;
    //站点经度
    private String siteLng;
    //站点纬度
    private String siteLat;
    //站点类型id
    private int typeId;
    //站点区域id
    private int areaId;
    //站点状态
    private String siteStatus;
    //站点备注
    private String siteRemark;
    //创建用户
    private String createUserId;
    //操作用户
    private String operateUserId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getSiteDate() {
        return siteDate;
    }

    public void setSiteDate(String siteDate) {
        this.siteDate = siteDate;
    }

    public String getSiteLng() {
        return siteLng;
    }

    public void setSiteLng(String siteLng) {
        this.siteLng = siteLng;
    }

    public String getSiteLat() {
        return siteLat;
    }

    public void setSiteLat(String siteLat) {
        this.siteLat = siteLat;
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

    public String getSiteRemark() {
        return siteRemark;
    }

    public void setSiteRemark(String siteRemark) {
        this.siteRemark = siteRemark;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public String getOperateUserId() {
        return operateUserId;
    }

    public void setOperateUserId(String operateUserId) {
        this.operateUserId = operateUserId;
    }
}
