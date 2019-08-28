package com.tcup.transformer.transnav.mvp.model.entity;

import java.util.List;

public class TypeResponse {
    private int status;
    private String message;
    private List<TypeBean> data;

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

    public List<TypeBean> getData() {
        return data;
    }

    public void setData(List<TypeBean> data) {
        this.data = data;
    }
}
