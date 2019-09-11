package com.tcup.transformer.transnav.bean;

public class MessageEvent {
    private Object msg;

    public MessageEvent(Object msg) {
        this.msg = msg;
    }

    public Object getMsg() {
        return msg;
    }

    public void setMsg(Object msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "MessageEvent{" + "msg=" + msg + '}';
    }
}

