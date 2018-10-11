package com.develop.wallet.eos.utils;

import com.develop.wallet.eos.crypto.Point;
import com.develop.wallet.eos.crypto.Secp256k;
import com.develop.wallet.eos.crypto.digest.Ripemd160;
import com.develop.wallet.eos.crypto.digest.Sha;
import com.develop.wallet.eos.crypto.utils.Base58;
import com.develop.wallet.eos.crypto.utils.ByteBuffer;
import com.develop.wallet.eos.crypto.utils.ByteUtils;
import com.develop.wallet.eos.model.transaction.push.TxSign;
import com.develop.wallet.eos.utils.ese.Ese;

import java.math.BigInteger;
import java.util.List;


/**
 * Ecc
 *
 * @author espritblock http://eblock.io
 */
public class EccTool {

    public static final String address_prefix = "EOS";

    public static final Secp256k secp = new Secp256k();

    /**
     * 通过种子生成私钥
     *
     * @param seed 种子
     * @return
     */
    public static String privateKeyFromSeed(String seed) {
        return seedShaPrivate(Sha.SHA256(seed));
    }

    public static String privateKeyFromSeed(byte[] seed) {
        return seedShaPrivate(Sha.SHA256(seed));
    }

    /**
     * seedPrivate
     *
     * @return
     */
    private static String seedShaPrivate(byte[] seedSha) {
        byte[] a = {(byte) 0x80};
        byte[] b = new BigInteger(seedSha).toByteArray();
        byte[] private_key = ByteUtils.concat(a, b);
        byte[] checksum = Sha.SHA256(private_key);
        checksum = Sha.SHA256(checksum);
        byte[] check = ByteUtils.copy(checksum, 0, 4);
        byte[] pk = ByteUtils.concat(private_key, check);
        return Base58.encode(pk);
    }


    /**
     * privateKey
     *
     * @param pk
     * @return
     */
    private static BigInteger privateKey(String pk) {
        byte[] private_wif = Base58.decode(pk);
        byte version = (byte) 0x80;
        if (private_wif[0] != version) {
            throw new EException("version_error", "Expected version " + 0x80 + ", instead got " + version);
        }
        byte[] private_key = ByteUtils.copy(private_wif, 0, private_wif.length - 4);
        byte[] new_checksum = Sha.SHA256(private_key);
        new_checksum = Sha.SHA256(new_checksum);
        new_checksum = ByteUtils.copy(new_checksum, 0, 4);
        byte[] last_private_key = ByteUtils.copy(private_key, 1, private_key.length - 1);
        BigInteger d = new BigInteger(Hex.bytesToHexString(last_private_key), 16);
        return d;
    }

    /**
     * 通过私钥生成公钥
     *
     * @param pk 私钥
     * @return
     */
    public static String privateToPublic(String pk) {
        if (pk == null || pk.length() == 0) {
            throw new EException("args_empty", "args is empty");
        }
        // private key
        BigInteger d = privateKey(pk);
        // publick key
        Point ep = secp.G().multiply(d);
        byte[] pub_buf = ep.getEncoded();
        byte[] csum = Ripemd160.from(pub_buf).bytes();
        csum = ByteUtils.copy(csum, 0, 4);
        byte[] addy = ByteUtils.concat(pub_buf, csum);
        StringBuffer bf = new StringBuffer(address_prefix);
        bf.append(Base58.encode(addy));
        return bf.toString();
    }

    /**
     * signHash
     *
     * @param pk
     * @param b
     * @return
     */
    public static String signHash(String pk, byte[] b) {
        String dataSha256 = Hex.bytesToHexString(Sha.SHA256(b));
        BigInteger e = new BigInteger(dataSha256, 16);
        int nonce = 0;
        int i = 0;
        BigInteger d = privateKey(pk);
        Point Q = secp.G().multiply(d);
        nonce = 0;
        Ecdsa ecd = new Ecdsa(secp);
        Ecdsa.SignBigInt sign;
        while (true) {
            sign = ecd.sign(dataSha256, d, nonce++);
            byte der[] = sign.getDer();
            byte lenR = der[3];
            byte lenS = der[5 + lenR];
            if (lenR == 32 && lenS == 32) {
                i = ecd.calcPubKeyRecoveryParam(e, sign, Q);
                i += 4; // compressed
                i += 27; // compact // 24 or 27 :( forcing odd-y 2nd key candidate)
                break;
            }
        }
        byte[] pub_buf = new byte[65];
        pub_buf[0] = (byte) i;
        ByteUtils.copy(sign.getR().toByteArray(), 0, pub_buf, 1, sign.getR().toByteArray().length);
        ByteUtils.copy(sign.getS().toByteArray(), 0, pub_buf, sign.getR().toByteArray().length + 1,
                sign.getS().toByteArray().length);

        byte[] checksum = Ripemd160.from(ByteUtils.concat(pub_buf, "K1".getBytes())).bytes();

        byte[] signatureString = ByteUtils.concat(pub_buf, ByteUtils.copy(checksum, 0, 4));

        return "SIG_K1_" + Base58.encode(signatureString);
    }

    /**
     * 普通数据签名
     *
     * @param pk   私钥
     * @param data 需要签名的数据
     * @return
     */
    public static String sign(String pk, String data) {
        String dataSha256 = Hex.bytesToHexString(Sha.SHA256(data));
        BigInteger e = new BigInteger(dataSha256, 16);
        int nonce = 0;
        int i = 0;
        BigInteger d = privateKey(pk);
        Point Q = secp.G().multiply(d);
        nonce = 0;
        Ecdsa ecd = new Ecdsa(secp);
        Ecdsa.SignBigInt sign;
        while (true) {
            sign = ecd.sign(dataSha256, d, nonce++);
            byte der[] = sign.getDer();
            byte lenR = der[3];
            byte lenS = der[5 + lenR];
            if (lenR == 32 && lenS == 32) {
                i = ecd.calcPubKeyRecoveryParam(e, sign, Q);
                i += 4; // compressed
                i += 27; // compact // 24 or 27 :( forcing odd-y 2nd key candidate)
                break;
            }
        }
        byte[] pub_buf = new byte[65];
        pub_buf[0] = (byte) i;
        ByteUtils.copy(sign.getR().toByteArray(), 0, pub_buf, 1, sign.getR().toByteArray().length);
        ByteUtils.copy(sign.getS().toByteArray(), 0, pub_buf, sign.getR().toByteArray().length + 1,
                sign.getS().toByteArray().length);

        byte[] checksum = Ripemd160.from(ByteUtils.concat(pub_buf, "K1".getBytes())).bytes();

        byte[] signatureString = ByteUtils.concat(pub_buf, ByteUtils.copy(checksum, 0, 4));

        return "SIG_K1_" + Base58.encode(signatureString);
    }

    /**
     * 交易签名
     *
     * @param privateKey 私钥
     * @param push       需要签名的对象
     * @return
     */
    public static String signTransaction(String privateKey, TxSign push) {
        // tx
        ByteBuffer bf = new ByteBuffer();
        ObjectUtils.writeBytes(push, bf);
        byte[] real = bf.getBuffer();
        // append
        real = ByteUtils.concat(real, java.nio.ByteBuffer.allocate(33).array());

        // final byte [] b = real.clone();
        // int[] a = IntStream.range(0, b.length).map(i -> b[i] & 0xff).toArray();
        // for(int i=1;i<=a.length;i++) {
        // System.out.print(a[i-1]+","+((i%8==0)?"\n":""));
        // }
        return signHash(privateKey, real);
    }

    /**
     * 转账数据序列化
     *
     * @param from     从
     * @param to       到
     * @param quantity 转账金额和币种
     * @param memo     备注留言
     * @return
     */
    public static String parseTransferData(String from, String to, String quantity, String memo) {
        return Ese.parseTransferData(from, to, quantity, memo);
    }

    /**
     * up 序列化
     *
     * @param from
     * @param to
     * @param quantity
     * @return
     */
    public static String parseUpData(String from, String to, String quantity) {
        return Ese.parseUpData(from, to, quantity);
    }

    /**
     * @param voter
     * @param proxy
     * @param producers
     * @return
     */
    public static String parseVoteProducerData(String voter, String proxy, List<String> producers) {
        return Ese.parseVoteProducerData(voter, proxy, producers);
    }

    /**
     * 创建账户数据序列化
     *
     * @param creator 创建者
     * @param name    账户名
     * @param onwer   onwer公钥
     * @param active  active公钥
     * @return
     */
    public static String parseAccountData(String creator, String name, String onwer, String active) {
        return Ese.parseAccountData(creator, name, onwer, active);
    }

    /**
     * 购买ram数据序列化
     *
     * @param payer    付款账户
     * @param receiver 接收账户
     * @param bytes    购买字节数量
     * @return
     */
    public static String parseBuyRamData(String payer, String receiver, Long bytes) {
        return Ese.parseBuyRamData(payer, receiver, bytes);
    }

    /**
     * 抵押数据序列化
     *
     * @param from             抵押账户
     * @param receiver         接受账户
     * @param stakeNetQuantity 网络抵押数量和币种
     * @param stakeCpuQuantity CPU抵押数量和币种
     * @param transfer         是否讲抵押资产转送给对方，0自己所有，1对方所有
     * @return
     */
    public static String parseBuyRamData(String from, String receiver, String stakeNetQuantity, String stakeCpuQuantity,
                                         int transfer) {
        return Ese.parseDelegateData(from, receiver, stakeNetQuantity, stakeCpuQuantity, transfer);
    }

    /**
     * 关闭token
     *
     * @param owner
     * @param symbol
     * @return
     */
    public static String parseCloseData(String owner, String symbol) {
        return Ese.parseCloseData(owner, symbol);
    }

}
