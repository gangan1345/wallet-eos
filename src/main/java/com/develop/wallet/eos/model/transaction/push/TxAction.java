package com.develop.wallet.eos.model.transaction.push;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;


/**
 * @author espritblock http://eblock.io
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TxAction extends BaseTx {
    @FieldAnnotation(order = 1)
    private String account;
    @FieldAnnotation(order = 2)
    private String name;
    @FieldAnnotation(order = 3)
    private List<TxActionAuth> authorization;
    @FieldAnnotation(order = 4)
    private Object data;

    public TxAction() {

    }

    public TxAction(String actor, String account, String name, Object data) {
        this.account = account;
        this.name = name;
        this.data = data;
        this.authorization = new ArrayList<>();
        this.authorization.add(new TxActionAuth(actor, "active"));
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TxActionAuth> getAuthorization() {
        return authorization;
    }

    public void setAuthorization(List<TxActionAuth> authorization) {
        this.authorization = authorization;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}
