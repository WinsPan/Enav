package com.tcup.transformer.transnav.mvp.model.entity;

import java.util.List;

public class RangeSearchBean {
    private String range;
    private List<SiteListBean> rangeSiteList;

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public List<SiteListBean> getRangeSiteList() {
        return rangeSiteList;
    }

    public void setRangeSiteList(List<SiteListBean> rangeSiteList) {
        this.rangeSiteList = rangeSiteList;
    }
}
