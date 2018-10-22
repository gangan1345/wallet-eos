package com.develop.wallet.eos.model.transaction.push;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;


/**
 * @author espritblock http://eblock.io
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Tx extends BaseTx {
    @FieldAnnotation(order = 1)
    private Object expiration;
    @FieldAnnotation(order = 2)
    private Long ref_block_num;
    @FieldAnnotation(order = 3)
    private Long ref_block_prefix;
    @FieldAnnotation(order = 4)
    private Long net_usage_words;
    @FieldAnnotation(order = 5)
    private Long max_cpu_usage_ms;
    @FieldAnnotation(order = 6)
    private Long delay_sec;
    @FieldAnnotation(order = 7)
    private List<String> context_free_actions = new ArrayList<>();
    @FieldAnnotation(order = 8)
    private List<TxAction> actions;
    @FieldAnnotation(order = 9)
    private List<TxExtenstions> transaction_extensions = new ArrayList<>();

    public Object getExpiration() {
        return expiration;
    }

    public void setExpiration(Object expiration) {
        this.expiration = expiration;
    }

    public Long getRef_block_num() {
        return ref_block_num;
    }

    public void setRef_block_num(Long ref_block_num) {
        this.ref_block_num = ref_block_num;
    }

    public Long getRef_block_prefix() {
        return ref_block_prefix;
    }

    public void setRef_block_prefix(Long ref_block_prefix) {
        this.ref_block_prefix = ref_block_prefix;
    }

    public Long getNet_usage_words() {
        return net_usage_words;
    }

    public void setNet_usage_words(Long net_usage_words) {
        this.net_usage_words = net_usage_words;
    }

    public Long getMax_cpu_usage_ms() {
        return max_cpu_usage_ms;
    }

    public void setMax_cpu_usage_ms(Long max_cpu_usage_ms) {
        this.max_cpu_usage_ms = max_cpu_usage_ms;
    }

    public Long getDelay_sec() {
        return delay_sec;
    }

    public void setDelay_sec(Long delay_sec) {
        this.delay_sec = delay_sec;
    }

    public List<String> getContext_free_actions() {
        return context_free_actions;
    }

    public void setContext_free_actions(List<String> context_free_actions) {
        this.context_free_actions = context_free_actions;
    }

    public List<TxAction> getActions() {
        return actions;
    }

    public void setActions(List<TxAction> actions) {
        this.actions = actions;
    }

    public List<TxExtenstions> getTransaction_extensions() {
        return transaction_extensions;
    }

    public void setTransaction_extensions(List<TxExtenstions> transaction_extensions) {
        this.transaction_extensions = transaction_extensions;
    }
}
