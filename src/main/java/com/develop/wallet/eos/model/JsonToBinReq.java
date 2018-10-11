package com.develop.wallet.eos.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonToBinReq {
    private String code;

    private String action;

    private UpTransfer args;

    public JsonToBinReq(String code, String action, UpTransfer args) {
        this.code = code;
        this.action = action;
        this.args = args;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public UpTransfer getArgs() {
        return args;
    }

    public void setArgs(UpTransfer args) {
        this.args = args;
    }
}
