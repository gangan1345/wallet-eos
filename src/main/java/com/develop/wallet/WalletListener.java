package com.develop.wallet;

/**
 * @author Angus
 */
public interface WalletListener {
    /**
     * 转账
     *
     * @param hash
     */
    void onSendTransaction(String hash);

    /**
     * 余额
     *
     * @param balance
     */
    void onQueryTokenBalance(String balance);
}
