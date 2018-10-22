package com.develop.wallet.eos.model.transaction.push;


import com.develop.wallet.eos.model.BaseVo;

/**
 * @author espritblock http://eblock.io
 */
public class TxRequest extends BaseVo {
    private String compression;

    private Tx transaction;

    private String[] signatures;

    public TxRequest() {

    }

    public TxRequest(String compression, Tx transaction, String[] signatures) {
        this.compression = compression;
        this.transaction = transaction;
        this.signatures = signatures;
    }

    public String getCompression() {
        return compression;
    }

    public void setCompression(String compression) {
        this.compression = compression;
    }

    public Tx getTransaction() {
        return transaction;
    }

    public void setTransaction(Tx transaction) {
        this.transaction = transaction;
    }

    public String[] getSignatures() {
        return signatures;
    }

    public void setSignatures(String[] signatures) {
        this.signatures = signatures;
    }

}
