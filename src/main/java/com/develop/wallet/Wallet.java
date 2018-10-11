package com.develop.wallet;

/**
 * @author Angus
 */
public class Wallet {
    private String mnemonic;
    private String address;
    private String privateKey;
    private String publicKey;
    private String keystore;

    public Wallet(String mnemonic, String address) {
        this.mnemonic = mnemonic;
        this.address = address;
    }

    public Wallet(String mnemonic, String address, String privateKey, String publicKey) {
        this.mnemonic = mnemonic;
        this.address = address;
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }

    public Wallet(String mnemonic, String address, String privateKey, String publicKey, String keystore) {
        this(mnemonic, address, privateKey, publicKey);
        this.keystore = keystore;
    }

    public String getMnemonic() {
        return mnemonic;
    }

    public void setMnemonic(String mnemonic) {
        this.mnemonic = mnemonic;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getKeystore() {
        return keystore;
    }

    public void setKeystore(String keystore) {
        this.keystore = keystore;
    }
}
