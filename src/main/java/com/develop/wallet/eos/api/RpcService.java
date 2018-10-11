package com.develop.wallet.eos.api;

import com.develop.wallet.eos.model.*;
import com.develop.wallet.eos.model.account.Account;
import com.develop.wallet.eos.model.transaction.Transaction;
import com.develop.wallet.eos.model.transaction.push.TxRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

import java.util.Map;

/**
 * @author espritblock http://eblock.io
 */
public interface RpcService {

    @GET("/v1/chain/get_info")
    Call<ChainInfo> getChainInfo();

    @POST("/v1/chain/get_block")
    Call<Block> getBlock(@Body Map<String, String> requestFields);

    @POST("/v1/chain/get_account")
    Call<Account> getAccount(@Body Map<String, String> requestFields);

    @POST("/v1/chain/push_transaction")
    Call<Transaction> pushTransaction(@Body TxRequest request);

    @POST("/v1/chain/get_table_rows")
    Call<TableRows> getTableRows(@Body TableRowsReq request);

    @POST("/v1/chain/abi_json_to_bin")
    Call<JsonToBin> abiJsonToBin(@Body JsonToBinReq request);

}
