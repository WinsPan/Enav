package com.tcup.transformer.transnav.mvp.model.entity;

import java.io.Serializable;
import java.util.List;

public class RangeSearchBean  implements Serializable {
    private int status;
    private String message;
    private List<SiteListBean> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<SiteListBean> getData() {
        return data;
    }

    public void setData(List<SiteListBean> data) {
        this.data = data;
    }
}
