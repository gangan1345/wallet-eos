package com.develop.wallet.eos.model.transaction.push;

/**
 * @author espritblock http://eblock.io
 */
public class TxActionAuth extends BaseTx {
    @FieldAnnotation(order = 1)
    private String actor;
    @FieldAnnotation(order = 2)
    private String permission;

    public TxActionAuth() {

    }

    public TxActionAuth(String actor, String permission) {
        this.actor = actor;
        this.permission = permission;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

}
