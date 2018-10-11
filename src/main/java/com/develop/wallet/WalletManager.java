package com.develop.wallet;

import com.develop.mnemonic.KeyPairUtils;
import com.develop.mnemonic.MnemonicUtils;
import com.develop.wallet.eos.Rpc;
import com.develop.wallet.eos.api.exception.ApiException;
import com.develop.wallet.eos.model.TableRows;
import com.develop.wallet.eos.model.TableRowsReq;
import com.develop.wallet.eos.model.transaction.Transaction;
import com.develop.wallet.eos.utils.EccTool;
import com.develop.wallet.eos.utils.EosUtils;


/**
 * @author Angus
 */
public class WalletManager {
    public static boolean DEBUG = BuildConfig.DEBUG;

    /**
     * 区块链服务器地址
     */
    public static String URL = BuildConfig.EOS_URL;

    private static Rpc rpc;

    public static Rpc getRpc() {
        if (rpc == null) {
            rpc = new Rpc(URL);
        }
        return rpc;
    }

    /**
     * 生成账号，随机生成
     */
    public static Wallet generateWalletAddress() {
        String account = EosUtils.generateAccount();
        log("random generate account : " + account);
        return generateWalletAddress(account);
    }

    /**
     * 生成账号
     *
     * @param account
     * @return
     */
    public static Wallet generateWalletAddress(String account) {
        try {
            String mnemonic = MnemonicUtils.generateMnemonic();
            byte[] seed = KeyPairUtils.generatePrivateKey(mnemonic, KeyPairUtils.CoinTypes.EOS);
            String privateKey = EccTool.privateKeyFromSeed(seed);
            String publicKey = EccTool.privateToPublic(privateKey);

            Transaction results = getRpc().createAccount(BuildConfig.EOS_CREATOR_PRIVATE_KEY, BuildConfig.EOS_CREATOR_ACCOUNT, account, publicKey, publicKey, null/*81921L*/);
            log("generate success transactionId = " + results.getTransactionId());

            log(String.format("generateWalletAddress: mnemonic = %s, account = %s, privateKey = %s, publicKey = %s", mnemonic, account, privateKey, publicKey));
            return new Wallet(mnemonic, account, publicKey, publicKey);
        } catch (ApiException ae) {
            // {"code":500,"message":"Internal Service Error","error":{"code":3050000,"name":"action_validate_exception","what":"action exception","details":[{"message":"Cannot create account named abc12345222a, as that name is already taken","file":"eosio_contract.cpp","line_number":91,"method":"apply_eosio_newaccount"},{"message":"","file":"eosio_contract.cpp","line_number":120,"method":"apply_eosio_newaccount"},{"message":"","file":"apply_context.cpp","line_number":60,"method":"exec_one"}]}}
            log(String.format("code = %s, message = %s", ae.getError().getError().getCode(), ae.getError().getError().getDetails()[0].getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过助记词获取私钥
     *
     * @param mnemonic
     */
    public static String generatePrivateKey(String mnemonic) {
        byte[] seed = KeyPairUtils.generatePrivateKey(mnemonic, KeyPairUtils.CoinTypes.EOS);
        String privateKey = EccTool.privateKeyFromSeed(seed);
        log(String.format("generatePrivateKey: mnemonic = %s, privateKey = %s", mnemonic, privateKey));
        return privateKey;
    }

    /**
     * 查询账号的余额
     */
    public static void queryAccount(String account) {
        // {"rows":[{"balance":"800.0970 SYS","uppower":"800.0970 SYS","upvote":"800.0970 SYS","upvalue":"0.0000 SYS"}],"more":false}
        try {
            TableRows mTableRows = getRpc().getTableRows(new TableRowsReq(BuildConfig.EOS_CREATOR_ACCOUNT, account, "accounts"));
        } catch (ApiException ae) {
            log(String.format("code = %s, message = %s", ae.getError().getError().getCode(), ae.getError().getError().getDetails()[0].getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }

    /**
     * 查询系统代币总额
     */
    public static void querySupplys() {
        // {"rows":[{"totalrealsupply":"8865.4694 SYS"}],"more":false}
        try {
            TableRows mTableRows = getRpc().getTableRows(new TableRowsReq(BuildConfig.EOS_CREATOR_ACCOUNT, BuildConfig.EOS_CREATOR_ACCOUNT, "realsupplys"));
            String totalrealsupply = EosUtils.getBalance(mTableRows.getRowValue("totalrealsupply"));
            log("totalrealsupply = " + totalrealsupply);
        } catch (ApiException ae) {
            log(String.format("code = %s, message = %s", ae.getError().getError().getCode(), ae.getError().getError().getDetails()[0].getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 转账
     *
     * @param fromAccount
     * @param fromPrivateKey
     * @param toAccount
     * @param quantity
     * @param memo
     */
    public static void transfer(String fromAccount, String fromPrivateKey, String toAccount, String quantity, String memo) {
        try {
            /**
             * {"transaction_id":"f9c09e7584432228394323199909b93ea11820d47f011ee1bf33a1210d4b74ba",
             * "processed":{"id":"f9c09e7584432228394323199909b93ea11820d47f011ee1bf33a1210d4b74ba",
             * "receipt":{"status":"executed","cpu_usage_us":10675,"net_usage_words":16},"elapsed":10675,"net_usage":128,"scheduled":false,
             * "action_traces":[{"receipt":{"receiver":"eosio.token","act_digest":"c7bb8180f1bf92cac749c92a1f0184b47be6dbdc3f89790eb4ff037872d65de2","global_sequence":1166405,
             * "recv_sequence":704,"auth_sequence":[["a1111111111a",5]],"code_sequence":2,"abi_sequence":2},
             * "act":{"account":"eosio.token","name":"transfer","authorization":[{"actor":"a1111111111a","permission":"active"}],
             * "data":{"from":"a1111111111a","to":"a1111111111b","quantity":"1.0000 SYS","memo":"xxx"},"hex_data":"604208218410423070420821841042301027000000000000045359530000000003787878"},
             * "elapsed":7607,"cpu_usage":0,"console":"","total_cpu_usage":0,"trx_id":"f9c09e7584432228394323199909b93ea11820d47f011ee1bf33a1210d4b74ba",
             * "inline_traces":[{"receipt":{"receiver":"a1111111111a","act_digest":"c7bb8180f1bf92cac749c92a1f0184b47be6dbdc3f89790eb4ff037872d65de2","global_sequence":1166406,"recv_sequence":3,
             * "auth_sequence":[["a1111111111a",6]],"code_sequence":2,"abi_sequence":2},"act":{"account":"eosio.token","name":"transfer",
             * "authorization":[{"actor":"a1111111111a","permission":"active"}],
             * "data":{"from":"a1111111111a","to":"a1111111111b","quantity":"1.0000 SYS","memo":"xxx"},
             * "hex_data":"604208218410423070420821841042301027000000000000045359530000000003787878"},"elapsed":94,"cpu_usage":0,"console":"","total_cpu_usage":0,
             * "trx_id":"f9c09e7584432228394323199909b93ea11820d47f011ee1bf33a1210d4b74ba","inline_traces":[]},{"receipt":{"receiver":"a1111111111b",
             * "act_digest":"c7bb8180f1bf92cac749c92a1f0184b47be6dbdc3f89790eb4ff037872d65de2","global_sequence":1166407,"recv_sequence":2,
             * "auth_sequence":[["a1111111111a",7]],"code_sequence":2,"abi_sequence":2},"act":{"account":"eosio.token","name":"transfer",
             * "authorization":[{"actor":"a1111111111a","permission":"active"}],"data":{"from":"a1111111111a","to":"a1111111111b","quantity":"1.0000 SYS","memo":"xxx"},
             * "hex_data":"604208218410423070420821841042301027000000000000045359530000000003787878"},"elapsed":118,"cpu_usage":0,"console":"",
             * "total_cpu_usage":0,"trx_id":"f9c09e7584432228394323199909b93ea11820d47f011ee1bf33a1210d4b74ba","inline_traces":[]}]}],"except":null}}
             */
            Transaction results = getRpc().transfer(fromPrivateKey, BuildConfig.EOS_CREATOR_ACCOUNT, fromAccount, toAccount, quantity, memo);
            log("transfer success transactionId = " + results.getTransactionId());
        } catch (ApiException ae) {
            log(String.format("code = %s, message = %s", ae.getError().getError().getCode(), ae.getError().getError().getDetails()[0].getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * up
     *
     * @param fromAccount
     * @param fromPrivateKey
     * @param toAccount
     * @param value
     */
    public static void up(String fromAccount, String fromPrivateKey, String toAccount, String value) {
        try {
            /**
             * {"transaction_id":"7f5164d82d5d1444d55f5cc804e2cfee4c6e8be00276045f1c1a5abb12908bb7",
             * "processed":{"id":"7f5164d82d5d1444d55f5cc804e2cfee4c6e8be00276045f1c1a5abb12908bb7",
             * "receipt":{"status":"executed","cpu_usage_us":9513,"net_usage_words":16},"elapsed":9513,"net_usage":128,"scheduled":false,
             * "action_traces":[{"receipt":{"receiver":"eosio.token","act_digest":"4bc3c9cf2669dfdeabb61ded8637a07e5dc4ccc0124e0f5485c6f5df00a1573e","global_sequence":1166290,
             * "recv_sequence":703,"auth_sequence":[["a1111111111a",4]],"code_sequence":2,"abi_sequence":2},
             * "act":{"account":"eosio.token","name":"up","authorization":[{"actor":"a1111111111a","permission":"active"}],
             * "data":{"upfrom":"a1111111111a","upto":"a1111111111b","value":"1.0000 SYS"},"hex_data":"6042082184104230704208218410423010270000000000000453595300000000"},
             * "elapsed":7434,"cpu_usage":0,"console":"","total_cpu_usage":0,"trx_id":"7f5164d82d5d1444d55f5cc804e2cfee4c6e8be00276045f1c1a5abb12908bb7","inline_traces":[]}],"except":null}}
             */
            //
            Transaction results = getRpc().up(fromPrivateKey, BuildConfig.EOS_CREATOR_ACCOUNT, fromAccount, toAccount, value);
            log("up success transactionId = " + results.getTransactionId());
        } catch (ApiException ae) {
            log(String.format("code = %s, message = %s", ae.getError().getError().getCode(), ae.getError().getError().getDetails()[0].getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void log(String message) {
        if (DEBUG) {
            System.out.println("WalletManager->" + message);
//            Log.i("wallet", "WalletManager->" + message);
        }
    }
}
