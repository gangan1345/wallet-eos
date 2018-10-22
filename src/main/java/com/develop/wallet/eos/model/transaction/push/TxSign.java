package com.develop.wallet.eos.model.transaction.push;


/**
 * @author espritblock http://eblock.io
 */
public class TxSign extends BaseTx {
    @FieldAnnotation(order = 1)
    private String chain_id;
    @FieldAnnotation(order = 2)
    private Tx transaction;

    public TxSign() {

    }

    public TxSign(String chain_id, Tx transaction) {
        this.chain_id = chain_id;
        this.transaction = transaction;
    }

    public String getChain_id() {
        return chain_id;
    }

    public void setChain_id(String chain_id) {
        this.chain_id = chain_id;
    }

    public Tx getTransaction() {
        return transaction;
    }

    public void setTransaction(Tx transaction) {
        this.transaction = transaction;
    }

}
