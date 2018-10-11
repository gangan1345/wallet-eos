package com.develop.wallet.eos.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Angus
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpTransfer {

    private String upfrom;
    private String upto;
    private String value;

    public UpTransfer(String upfrom, String upto, String value) {
        this.upfrom = upfrom;
        this.upto = upto;
        this.value = value;
    }

    public String getUpfrom() {
        return upfrom;
    }

    public void setUpfrom(String upfrom) {
        this.upfrom = upfrom;
    }

    public String getUpto() {
        return upto;
    }

    public void setUpto(String upto) {
        this.upto = upto;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
