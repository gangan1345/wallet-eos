package com.develop.wallet;

import com.develop.mnemonic.KeyPairUtils;
import com.develop.mnemonic.MnemonicUtils;
import com.develop.mnemonic.utils.Numeric;
import com.develop.wallet.eos.utils.EccTool;
import com.develop.wallet.eos.utils.EosUtils;

/**
 * @author Angus
 */
public class Test {

    public static final void main(String[] args) {

        // 生成助记词
        String mnemonic = MnemonicUtils.generateMnemonic();
        System.out.println("mnemonic:" + mnemonic);

        // 生成种子
        byte[] seed = MnemonicUtils.generateSeed(mnemonic, "");
        System.out.println("seed:" + Numeric.toHexString(seed));

        // bip44 bip32 私钥
        byte[] privateKeyBytes = KeyPairUtils.generatePrivateKey(seed, KeyPairUtils.CoinTypes.EOS);
        System.out.println("privateKeyBytes:" + Numeric.toHexString(privateKeyBytes));

        // 生成EOS私钥
        String pk = EccTool.privateKeyFromSeed(privateKeyBytes);
        System.out.println("private key :" + pk);

        // 生成EOS公钥
        String pu = EccTool.privateToPublic(pk);
        System.out.println("public key :" + pu);

/////////////////////////////////////////////////////////////////////
        String accountA = "a1111111111a", accountB = "a1111111111b";
        String privateKeyA = "5JVubbh5s6RP5zunU8TVEdhQKBTY4BNkbsxP2abbjCQi4HqpUvG", privateKeyB = "5JbZ4RyKGqNNrhRzyDVjRdERbjZMJfe1XTnsLqqB1hXZCLH5Mgd";

        // 随机生成EOS账号
        String account = EosUtils.generateAccount();
        System.out.println("account :" + account + ", eos = " + EosUtils.isEosAccount(account));

        // 查询系统代币总额
        WalletManager.querySupplys();

        // 生成EOS账号（随机）
        WalletManager.generateWalletAddress();

        // 生成EOS账号
        WalletManager.generateWalletAddress("a1111111111b");

        // 查询账号
        WalletManager.queryAccount(accountA);

        WalletManager.queryAccount(accountB);

        // 转账
        WalletManager.transfer(accountA, privateKeyA, accountB, "1.0000 SYS", "xxx");

        WalletManager.up(accountA, privateKeyA, accountB, "1.0000 SYS");

    }
}
