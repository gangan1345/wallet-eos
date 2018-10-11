package com.develop.wallet.eos.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Angus
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Transfer {

    /**
     * quantity : 6.0000 EOS
     * memo : 恭喜发财,大吉大利
     * from : lucan222
     * to : eosio.token
     */

    private String quantity;
    private String memo;
    private String from;
    private String to;

    public Transfer(String quantity, String memo, String from, String to) {
        this.quantity = quantity;
        this.memo = memo;
        this.from = from;
        this.to = to;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

}
