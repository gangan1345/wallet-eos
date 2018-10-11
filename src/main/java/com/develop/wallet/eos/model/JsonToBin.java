package com.develop.wallet.eos.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonToBin {

    private String binargs;

    private List<String> required_scope;

    private List<String> required_auth;

    public String getBinargs() {
        return binargs == null ? "" : binargs;
    }

    public void setBinargs(String binargs) {
        this.binargs = binargs;
    }

    public List<String> getRequired_scope() {
        if (required_scope == null) {
            return new ArrayList<>();
        }
        return required_scope;
    }

    public void setRequired_scope(List<String> required_scope) {
        this.required_scope = required_scope;
    }

    public List<String> getRequired_auth() {
        if (required_auth == null) {
            return new ArrayList<>();
        }
        return required_auth;
    }

    public void setRequired_auth(List<String> required_auth) {
        this.required_auth = required_auth;
    }
}
