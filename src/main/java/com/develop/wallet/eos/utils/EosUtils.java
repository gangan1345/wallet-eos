package com.develop.wallet.eos.utils;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * eos
 *
 * @author Angus
 */
public class EosUtils {
    public static final String NUMBERS_AND_LETTERS = "12345abcdefghijklmnopqrstuvwxyz";
    public static final String LOWER_CASE_LETTERS = "abcdefghijklmnopqrstuvwxyz";

    /**
     * 随机生成一个eos账号
     *
     * @return
     */
    public static String generateAccount() {
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append(getRandom(LOWER_CASE_LETTERS, 1));
        sBuilder.append(getRandom(NUMBERS_AND_LETTERS, 11));
        return sBuilder.toString();
    }

    /**
     * eos账号名
     *
     * @param eosAccount the eos name
     * @return boolean
     */
    public static boolean isEosAccount(String eosAccount) {
        String strPattern = "^[a-z]{1}[1-5a-z]{11}$";
        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(eosAccount);
        return m.matches();
    }

    public static String getRandom(String source, int length) {
        return (source == null || source.equals("")) ? null : getRandom(source.toCharArray(), length);
    }

    public static String getRandom(char[] sourceChar, int length) {
        if (sourceChar == null || sourceChar.length == 0 || length < 0) {
            return null;
        }

        StringBuilder str = new StringBuilder(length);
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            str.append(sourceChar[random.nextInt(sourceChar.length)]);
        }
        return str.toString();
    }

    /**
     * 获取余额数值
     *
     * @param value "8865.4694 SYS"
     * @return
     */
    public static String getBalance(String value) {
        if (value == null || value.equals("")) {
            return "";
        }
        String vs[] = value.split(" ");
        if (vs.length == 2) {
            return vs[0];
        }
        return "";
    }
}
