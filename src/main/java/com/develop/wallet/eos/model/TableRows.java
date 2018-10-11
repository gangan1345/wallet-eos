package com.develop.wallet.eos.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TableRows {

    private Boolean more;

    private List<Map> rows;

    public Boolean getMore() {
        return more;
    }

    public void setMore(Boolean more) {
        this.more = more;
    }

    public List<Map> getRows() {
        return rows;
    }

    public void setRows(List<Map> rows) {
        this.rows = rows;
    }

    public String getRowValue(String key) {
        if (rows != null && !rows.isEmpty()) {
            Object obj = rows.get(0).get(key);
            if (obj != null) {
                return obj.toString();
            }
        }
        return null;
    }
}
