package com.tcup.transformer.transnav.mvp.model.entity;

public class SiteListBean {
    private int id;
    private String siteNo;
    private String siteName;
    private String siteAddr;
    private String siteDate;
    private String siteLng;
    private String siteLat;
    private EqpTypeDomainBean eqpTypeDomain;
    private EqpAreaDomainBean eqpAreaDomain;
    private String siteStatus;
    private String siteRemark;
    private BaseUserBean createUser;
    private String createTime;
    private BaseUserBean operateUser;
    private String operateTime;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setSiteNo(String siteNo) {
        this.siteNo = siteNo;
    }

    public String getSiteNo() {
        return siteNo;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteAddr(String siteAddr) {
        this.siteAddr = siteAddr;
    }

    public String getSiteAddr() {
        return siteAddr;
    }

    public void setSiteDate(String siteDate) {
        this.siteDate = siteDate;
    }

    public String getSiteDate() {
        return siteDate;
    }

    public void setSiteLng(String siteLng) {
        this.siteLng = siteLng;
    }

    public String getSiteLng() {
        return siteLng;
    }

    public void setSiteLat(String siteLat) {
        this.siteLat = siteLat;
    }

    public String getSiteLat() {
        return siteLat;
    }

    public void setEqpTypeDomain(EqpTypeDomainBean eqpTypeDomain) {
        this.eqpTypeDomain = eqpTypeDomain;
    }

    public EqpTypeDomainBean getEqpTypeDomain() {
        return eqpTypeDomain;
    }

    public void setEqpAreaDomain(EqpAreaDomainBean eqpAreaDomain) {
        this.eqpAreaDomain = eqpAreaDomain;
    }

    public EqpAreaDomainBean getEqpAreaDomain() {
        return eqpAreaDomain;
    }

    public void setSiteStatus(String siteStatus) {
        this.siteStatus = siteStatus;
    }

    public String getSiteStatus() {
        return siteStatus;
    }

    public void setSiteRemark(String siteRemark) {
        this.siteRemark = siteRemark;
    }

    public String getSiteRemark() {
        return siteRemark;
    }

    public void setCreateUser(BaseUserBean createUser) {
        this.createUser = createUser;
    }

    public BaseUserBean getCreateUser() {
        return createUser;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setOperateUser(BaseUserBean operateUser) {
        this.operateUser = operateUser;
    }

    public BaseUserBean getOperateUser() {
        return operateUser;
    }

    public void setOperateTime(String operateTime) {
        this.operateTime = operateTime;
    }

    public String getOperateTime() {
        return operateTime;
    }
}
