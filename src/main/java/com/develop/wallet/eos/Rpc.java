package com.develop.wallet.eos;

import com.develop.wallet.BuildConfig;
import com.develop.wallet.eos.api.RpcService;
import com.develop.wallet.eos.api.http.Generator;
import com.develop.wallet.eos.model.*;
import com.develop.wallet.eos.model.account.Account;
import com.develop.wallet.eos.model.transaction.Transaction;
import com.develop.wallet.eos.model.transaction.push.Tx;
import com.develop.wallet.eos.model.transaction.push.TxAction;
import com.develop.wallet.eos.model.transaction.push.TxRequest;
import com.develop.wallet.eos.model.transaction.push.TxSign;
import com.develop.wallet.eos.utils.EccTool;
import com.develop.wallet.eos.utils.ese.Action;
import com.develop.wallet.eos.utils.ese.DataParam;
import com.develop.wallet.eos.utils.ese.DataType;
import com.develop.wallet.eos.utils.ese.Ese;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.SimpleDateFormat;
import java.util.*;


public class Rpc {

    private final RpcService rpcService;
    private boolean DEBUG = BuildConfig.DEBUG;

    SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    public Rpc(String baseUrl) {
        dateFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        rpcService = Generator.createService(RpcService.class, baseUrl, DEBUG);
    }

    /**
     * 获得链信息
     *
     * @return
     */
    public ChainInfo getChainInfo() {
        return Generator.executeSync(rpcService.getChainInfo());
    }

    /**
     * 获得区块信息
     *
     * @param blockNumberOrId 区块ID或者高度
     * @return
     */
    public Block getBlock(String blockNumberOrId) {
        return Generator.executeSync(rpcService.getBlock(Collections.singletonMap("block_num_or_id", blockNumberOrId)));
    }

    /**
     * 获得账户信息
     *
     * @param account 账户名称
     * @return
     */
    public Account getAccount(String account) {
        return Generator.executeSync(rpcService.getAccount(Collections.singletonMap("account_name", account)));
    }

    /**
     * 获得table数据
     *
     * @param req
     * @return
     */
    public TableRows getTableRows(TableRowsReq req) {
        return Generator.executeSync(rpcService.getTableRows(req));
    }

    public JsonToBin abiJsonToBin(JsonToBinReq req) {
        return Generator.executeSync(rpcService.abiJsonToBin(req));
    }

    /**
     * 发送请求
     *
     * @param compression     压缩
     * @param pushTransaction 交易
     * @param signatures      签名
     * @return
     * @throws Exception
     */
    public Transaction pushTransaction(String compression, Tx pushTransaction, String[] signatures) throws Exception {
//		ObjectMapper mapper = new ObjectMapper();
//		String mapJakcson = mapper.writeValueAsString(new TxRequest(compression, pushTransaction, signatures));
//		System.out.println(mapJakcson);
        return Generator
                .executeSync(rpcService.pushTransaction(new TxRequest(compression, pushTransaction, signatures)));
    }

    /**
     * 发送交易
     *
     * @param tx
     * @return
     * @throws Exception
     */
    public Transaction pushTransaction(String tx) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TxRequest txObj = mapper.readValue(tx, TxRequest.class);
        return Generator.executeSync(rpcService.pushTransaction(txObj));
    }

    /**
     * 获取离线签名参数
     *
     * @param exp 过期时间秒
     * @return
     */
    public SignParam getOfflineSignParams(Long exp) {
        SignParam params = new SignParam();
        ChainInfo info = getChainInfo();
        Block block = getBlock(info.getLastIrreversibleBlockNum().toString());
        params.setChainId(info.getChainId());
        params.setHeadBlockTime(info.getHeadBlockTime());
        params.setLastIrreversibleBlockNum(info.getLastIrreversibleBlockNum());
        params.setRefBlockPrefix(block.getRefBlockPrefix());
        params.setExp(exp);
        return params;
    }

    /**
     * 转账
     *
     * @param pk              私钥
     * @param contractAccount 合约账户
     * @param from            从
     * @param to              到
     * @param quantity        币种金额
     * @param memo            留言
     * @return
     * @throws Exception
     */
    public Transaction transfer(String pk, String contractAccount, String from, String to, String quantity, String memo)
            throws Exception {
        // get chain info
        ChainInfo info = getChainInfo();
        // get block info
        Block block = getBlock(info.getLastIrreversibleBlockNum().toString());
        // tx
        Tx tx = new Tx();
        tx.setExpiration(info.getHeadBlockTime().getTime() / 1000 + 60);
        tx.setRef_block_num(info.getLastIrreversibleBlockNum());
        tx.setRef_block_prefix(block.getRefBlockPrefix());
        tx.setNet_usage_words(0l);
        tx.setMax_cpu_usage_ms(0l);
        tx.setDelay_sec(0l);
        // actions
        List<TxAction> actions = new ArrayList<>();
        // data
        Map<String, Object> dataMap = new LinkedHashMap<>();
        dataMap.put("from", from);
        dataMap.put("to", to);
        dataMap.put("quantity", new DataParam(quantity, DataType.asset, Action.transfer).getValue());
        dataMap.put("memo", memo);
        // action
        TxAction action = new TxAction(from, contractAccount, "transfer", dataMap);
        actions.add(action);
        tx.setActions(actions);
        // sgin
        String sign = EccTool.signTransaction(pk, new TxSign(info.getChainId(), tx));
        // data parse
        String data = EccTool.parseTransferData(from, to, quantity, memo);
        // reset data
        action.setData(data);
        // reset expiration
        tx.setExpiration(dateFormatter.format(new Date(1000 * Long.parseLong(tx.getExpiration().toString()))));
        return pushTransaction("none", tx, new String[]{sign});
    }


    public Transaction up(String pk, String contractAccount, String from, String to, String value)
            throws Exception {
        // get chain info
        ChainInfo info = getChainInfo();
        // get block info
        Block block = getBlock(info.getLastIrreversibleBlockNum().toString());
        // tx
        Tx tx = new Tx();
        tx.setExpiration(info.getHeadBlockTime().getTime() / 1000 + 60);
        tx.setRef_block_num(info.getLastIrreversibleBlockNum());
        tx.setRef_block_prefix(block.getRefBlockPrefix());
        tx.setNet_usage_words(0l);
        tx.setMax_cpu_usage_ms(0l);
        tx.setDelay_sec(0l);
        // actions
        List<TxAction> actions = new ArrayList<>();
        // data
        Map<String, Object> dataMap = new LinkedHashMap<>();
        dataMap.put("upfrom", from);
        dataMap.put("upto", to);
        dataMap.put("value", new DataParam(value, DataType.asset, Action.up).getValue());
        // action
        TxAction action = new TxAction(from, contractAccount, "up", dataMap);
        actions.add(action);
        tx.setActions(actions);
        // sgin
        String sign = EccTool.signTransaction(pk, new TxSign(info.getChainId(), tx));
        // data parse
        String data = EccTool.parseUpData(from, to, value);
        // reset data
        action.setData(data);
        // reset expiration
        tx.setExpiration(dateFormatter.format(new Date(1000 * Long.parseLong(tx.getExpiration().toString()))));
        return pushTransaction("none", tx, new String[]{sign});
    }

    /**
     * 创建账户
     *
     * @param pk         私钥
     * @param creator    创建者
     * @param newAccount 新账户
     * @param owner      公钥
     * @param active     公钥
     * @param buyRam     ram
     * @return
     * @throws Exception
     */
    public Transaction createAccount(String pk, String creator, String newAccount, String owner, String active,
                                     Long buyRam) throws Exception {
        // get chain info
        ChainInfo info = getChainInfo();
        // get block info
        Block block = getBlock(info.getLastIrreversibleBlockNum().toString());
        // tx
        Tx tx = new Tx();
        tx.setExpiration(info.getHeadBlockTime().getTime() / 1000 + 60);
        tx.setRef_block_num(info.getLastIrreversibleBlockNum());
        tx.setRef_block_prefix(block.getRefBlockPrefix());
        tx.setNet_usage_words(0l);
        tx.setMax_cpu_usage_ms(0l);
        tx.setDelay_sec(0l);
        // actions
        List<TxAction> actions = new ArrayList<>();
        tx.setActions(actions);
        // create
        Map<String, Object> createMap = new LinkedHashMap<>();
        createMap.put("creator", creator);
        createMap.put("name", newAccount);
        createMap.put("owner", owner);
        createMap.put("active", active);
        TxAction createAction = new TxAction(creator, "eosio", "newaccount", createMap);
        actions.add(createAction);

        TxAction buyAction = null;
        if (buyRam != null && buyRam > 0) {
            // buyrap
            Map<String, Object> buyMap = new LinkedHashMap<>();
            buyMap.put("payer", creator);
            buyMap.put("receiver", newAccount);
            buyMap.put("bytes", buyRam);
            buyAction = new TxAction(creator, "eosio", "buyrambytes", buyMap);
            actions.add(buyAction);
        }
        // sgin
        String sign = EccTool.signTransaction(pk, new TxSign(info.getChainId(), tx));
        // data parse
        String accountData = Ese.parseAccountData(creator, newAccount, owner, active);
        createAction.setData(accountData);
        // data parse
        if (buyAction != null) {
            String ramData = Ese.parseBuyRamData(creator, newAccount, buyRam);
            buyAction.setData(ramData);
        }
        // reset expiration
        tx.setExpiration(dateFormatter.format(new Date(1000 * Long.parseLong(tx.getExpiration().toString()))));
        return pushTransaction("none", tx, new String[]{sign});
    }

    /**
     * 创建账户
     *
     * @param pk               私钥
     * @param creator          创建者
     * @param newAccount       新账户
     * @param owner            公钥
     * @param active           公钥
     * @param buyRam           购买空间数量
     * @param stakeNetQuantity 网络抵押
     * @param stakeCpuQuantity cpu抵押
     * @param transfer         抵押资产是否转送给对方，0自己所有，1对方所有
     * @return
     * @throws Exception
     */
    public Transaction createAccount(String pk, String creator, String newAccount, String owner, String active,
                                     Long buyRam, String stakeNetQuantity, String stakeCpuQuantity, Long transfer) throws Exception {
        // get chain info
        ChainInfo info = getChainInfo();
        // get block info
        Block block = getBlock(info.getLastIrreversibleBlockNum().toString());
        // tx
        Tx tx = new Tx();
        tx.setExpiration(info.getHeadBlockTime().getTime() / 1000 + 60);
        tx.setRef_block_num(info.getLastIrreversibleBlockNum());
        tx.setRef_block_prefix(block.getRefBlockPrefix());
        tx.setNet_usage_words(0l);
        tx.setMax_cpu_usage_ms(0l);
        tx.setDelay_sec(0l);
        // actions
        List<TxAction> actions = new ArrayList<>();
        tx.setActions(actions);
        // create
        Map<String, Object> createMap = new LinkedHashMap<>();
        createMap.put("creator", creator);
        createMap.put("name", newAccount);
        createMap.put("owner", owner);
        createMap.put("active", active);
        TxAction createAction = new TxAction(creator, "eosio", "newaccount", createMap);
        actions.add(createAction);
        // buyrap
        Map<String, Object> buyMap = new LinkedHashMap<>();
        buyMap.put("payer", creator);
        buyMap.put("receiver", newAccount);
        buyMap.put("bytes", buyRam);
        TxAction buyAction = new TxAction(creator, "eosio", "buyrambytes", buyMap);
        actions.add(buyAction);
        // buyrap
        Map<String, Object> delMap = new LinkedHashMap<>();
        delMap.put("from", creator);
        delMap.put("receiver", newAccount);
        delMap.put("stake_net_quantity", new DataParam(stakeNetQuantity, DataType.asset, Action.delegate).getValue());
        delMap.put("stake_cpu_quantity", new DataParam(stakeCpuQuantity, DataType.asset, Action.delegate).getValue());
        delMap.put("transfer", transfer);
        TxAction delAction = new TxAction(creator, "eosio", "delegatebw", delMap);
        actions.add(delAction);
        // // sgin
        String sign = EccTool.signTransaction(pk, new TxSign(info.getChainId(), tx));
        // data parse
        String accountData = Ese.parseAccountData(creator, newAccount, owner, active);
        createAction.setData(accountData);
        // data parse
        String ramData = Ese.parseBuyRamData(creator, newAccount, buyRam);
        buyAction.setData(ramData);
        // data parse
        String delData = Ese.parseDelegateData(creator, newAccount, stakeNetQuantity, stakeCpuQuantity,
                transfer.intValue());
        delAction.setData(delData);
        // reset expiration
        tx.setExpiration(dateFormatter.format(new Date(1000 * Long.parseLong(tx.getExpiration().toString()))));
        return pushTransaction("none", tx, new String[]{sign});
    }

    /**
     * @param pk
     * @param voter
     * @param proxy
     * @param producers
     * @return
     * @throws Exception
     */
    public Transaction voteproducer(String pk, String voter, String proxy, List<String> producers) throws Exception {
        Comparator<String> comparator = (h1, h2) -> h2.compareTo(h1);
        producers.sort(comparator.reversed());
        // get chain info
        ChainInfo info = getChainInfo();
        // get block info
        Block block = getBlock(info.getLastIrreversibleBlockNum().toString());
        // tx
        Tx tx = new Tx();
        tx.setExpiration(info.getHeadBlockTime().getTime() / 1000 + 60);
        tx.setRef_block_num(info.getLastIrreversibleBlockNum());
        tx.setRef_block_prefix(block.getRefBlockPrefix());
        tx.setNet_usage_words(0l);
        tx.setMax_cpu_usage_ms(0l);
        tx.setDelay_sec(0l);
        // actions
        List<TxAction> actions = new ArrayList<>();
        // data
        Map<String, Object> dataMap = new LinkedHashMap<>();
        dataMap.put("voter", voter);
        dataMap.put("proxy", proxy);
        dataMap.put("producers", producers);
        // action
        TxAction action = new TxAction(voter, "eosio", "voteproducer", dataMap);
        actions.add(action);
        tx.setActions(actions);
        // sgin
        String sign = EccTool.signTransaction(pk, new TxSign(info.getChainId(), tx));
        // data parse
        String data = EccTool.parseVoteProducerData(voter, proxy, producers);
        // reset data
        action.setData(data);
        // reset expiration
        tx.setExpiration(dateFormatter.format(new Date(1000 * Long.parseLong(tx.getExpiration().toString()))));
        return pushTransaction("none", tx, new String[]{sign});
    }

    /**
     * token close
     *
     * @param owner
     * @param symbol
     * @return
     * @throws Exception
     */
    public Transaction close(String pk, String contract, String owner, String symbol) throws Exception {
        ChainInfo info = getChainInfo();
        Block block = getBlock(info.getLastIrreversibleBlockNum().toString());
        Tx tx = new Tx();
        tx.setExpiration(info.getHeadBlockTime().getTime() / 1000 + 60);
        tx.setRef_block_num(info.getLastIrreversibleBlockNum());
        tx.setRef_block_prefix(block.getRefBlockPrefix());
        tx.setNet_usage_words(0l);
        tx.setMax_cpu_usage_ms(0l);
        tx.setDelay_sec(0l);
        // actions
        List<TxAction> actions = new ArrayList<>();
        // data
        Map<String, Object> dataMap = new LinkedHashMap<>();
        dataMap.put("close-owner", owner);
        dataMap.put("close-symbol", new DataParam(symbol, DataType.symbol, Action.close).getValue());
        // action
        TxAction action = new TxAction(owner, contract, "close", dataMap);
        actions.add(action);
        tx.setActions(actions);
        // sgin
        String sign = EccTool.signTransaction(pk, new TxSign(info.getChainId(), tx));
        // data parse
        String data = EccTool.parseCloseData(owner, symbol);
        // reset data
        action.setData(data);
        // reset expiration
        tx.setExpiration(dateFormatter.format(new Date(1000 * Long.parseLong(tx.getExpiration().toString()))));
        return pushTransaction("none", tx, new String[]{sign});
    }
}
