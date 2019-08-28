package com.tcup.transformer.transnav.mvp.model.entity;

import java.util.List;

public class AreaResponse {
    private int status;
    private String message;
    private List<AreaBean> data;

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

    public List<AreaBean> getData() {
        return data;
    }

    public void setData(List<AreaBean> data) {
        this.data = data;
    }
}
