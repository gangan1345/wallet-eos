package com.develop.wallet.eos.utils;

import com.develop.wallet.eos.crypto.utils.ByteBuffer;
import com.develop.wallet.eos.crypto.utils.ByteUtils;
import com.develop.wallet.eos.model.BaseVo;
import com.develop.wallet.eos.model.transaction.push.BaseTx;
import com.develop.wallet.eos.model.transaction.push.FieldAnnotation;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;


public class ObjectUtils {

    private static Object getFieldValueByName(String fieldName, Object o) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = o.getClass().getMethod(getter, new Class[]{});
            Object value = method.invoke(o, new Object[]{});
            return value;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Bean2Map
     *
     * @param obj
     * @return
     */
    public static Map<String, Object> Bean2Map(Object obj) {
        if (obj == null) {
            return null;
        }
        Map<String, Object> map = new LinkedHashMap<>();
        Field[] fields = obj.getClass().getDeclaredFields();
        // The Android client access to the order of the different lead to signature mistakes
        if (obj instanceof BaseTx) {
            List<Field> list = getOrderedField(fields);
            for (int i = 0; i < list.size(); i++) {
                map.put(list.get(i).getName(), getFieldValueByName(list.get(i).getName(), obj));
            }
        } else {
            for (int i = 0; i < fields.length; i++) {
                map.put(fields[i].getName(), getFieldValueByName(fields[i].getName(), obj));
            }
        }
        return map;
    }

    private static List<Field> getOrderedField(Field[] fields) {
        List<Field> fieldList = new ArrayList<>();
        // Filter Field with annotations
        for (Field f : fields) {
            if (f.getAnnotation(FieldAnnotation.class) != null) {
                fieldList.add(f);
            }
        }

//        // for java 1.8
//        fieldList.sort(Comparator.comparingInt(
//                m -> m.getAnnotation(FieldAnnotation.class).order()
//        ));

        Collections.sort(fieldList, new Comparator<Field>() {

            @Override
            public int compare(Field o1, Field o2) {
                int i = o1.getAnnotation(FieldAnnotation.class).order() - o2.getAnnotation(FieldAnnotation.class).order();
                return i;
            }
        });
        return fieldList;
    }

    public static void writeBytes(Object vo, ByteBuffer bf) {
        Map<String, Object> params = null;
        if (vo instanceof Map) {
            params = (Map) vo;
        } else {
            params = Bean2Map(vo);
        }
        Map<String, Object> objMap = new LinkedHashMap<>();
        for (String key : params.keySet()) {
            Object obj = params.get(key);
            if (obj instanceof BaseVo || obj instanceof List || obj instanceof Map) {
                if ("authorization".equals(key)) {
                    bf.concat(ByteUtils.writerVarint32(String.valueOf(((List) obj).size())));
                    for (Object ob : (List) obj) {
                        writeBytes(ob, bf);
                    }
                } else if ("data".equals(key)) {
                    ByteBuffer databf = new ByteBuffer();
                    writeBytes(obj, databf);
                    bf.concat(ByteUtils.writerVarint32(String.valueOf(databf.getBuffer().length)));
                    bf.concat(databf.getBuffer());
                } else if ("transaction_extensions".equals(key)) {

                } else {
                    objMap.put(key, obj);
                }
            } else {
                if ("chain_id".equals(key)) {
                    bf.concat(Hex.hexStringToBytes(obj.toString()));
                } else if ("expiration".equals(key)) {
                    bf.concat(ByteUtils.writerUnit32(obj.toString()));
                } else if ("ref_block_num".equals(key)) {
                    bf.concat(ByteUtils.writerUnit16(obj.toString()));
                } else if ("ref_block_prefix".equals(key)) {
                    bf.concat(ByteUtils.writerUnit32(obj.toString()));
                } else if ("net_usage_words".equals(key)) {
                    bf.concat(ByteUtils.writerVarint32(obj.toString()));
                } else if ("max_cpu_usage_ms".equals(key)) {
                    bf.concat(ByteUtils.writerUnit8(obj.toString()));
                } else if ("delay_sec".equals(key)) {
                    bf.concat(ByteUtils.writerVarint32(obj.toString()));
                } else if ("account".equals(key)) {
                    bf.concat(ByteUtils.writeName(obj.toString()));
                } else if ("name".equals(key)) {
                    bf.concat(ByteUtils.writeName(obj.toString()));
                } else if ("actor".equals(key)) {
                    bf.concat(ByteUtils.writeName(obj.toString()));
                } else if ("permission".equals(key)) {
                    bf.concat(ByteUtils.writeName(obj.toString()));
                } else if ("from".equals(key)) {
                    bf.concat(ByteUtils.writeName(obj.toString()));
                } else if ("to".equals(key)) {
                    bf.concat(ByteUtils.writeName(obj.toString()));
                } else if ("quantity".equals(key)) {
                    bf.concat(ByteUtils.writerAsset(obj.toString()));
                } else if ("memo".equals(key)) {
                    bf.concat(ByteUtils.writerString(obj.toString()));
                } else if ("creator".equals(key)) {
                    bf.concat(ByteUtils.writeName(obj.toString()));
                } else if ("owner".equals(key)) {
                    bf.concat(ByteUtils.writerKey(obj.toString()));
                } else if ("active".equals(key)) {
                    bf.concat(ByteUtils.writerKey(obj.toString()));
                } else if ("payer".equals(key)) {
                    bf.concat(ByteUtils.writeName(obj.toString()));
                } else if ("receiver".equals(key)) {
                    bf.concat(ByteUtils.writeName(obj.toString()));
                } else if ("bytes".equals(key)) {
                    bf.concat(ByteUtils.writerUnit32(obj.toString()));
                } else if ("stake_net_quantity".equals(key)) {
                    bf.concat(ByteUtils.writerAsset(obj.toString()));
                } else if ("stake_cpu_quantity".equals(key)) {
                    bf.concat(ByteUtils.writerAsset(obj.toString()));
                } else if ("transfer".equals(key)) {
                    bf.concat(ByteUtils.writerUnit8(obj.toString()));
                } else if ("voter".equals(key)) {
                    bf.concat(ByteUtils.writeName(obj.toString()));
                } else if ("proxy".equals(key)) {
                    bf.concat(ByteUtils.writeName(obj.toString()));
                } else if ("producer".equals(key)) {
                    bf.concat(ByteUtils.writeName(obj.toString()));
                } else if ("close-owner".equals(key)) {
                    bf.concat(ByteUtils.writeName(obj.toString()));
                } else if ("close-symbol".equals(key)) {
                    bf.concat(ByteUtils.writerSymbol(obj.toString()));
                } else if ("upfrom".equals(key)) {
                    bf.concat(ByteUtils.writeName(obj.toString()));
                } else if ("upto".equals(key)) {
                    bf.concat(ByteUtils.writeName(obj.toString()));
                } else if ("value".equals(key)) {
                    bf.concat(ByteUtils.writerAsset(obj.toString()));
                }
            }
        }
        for (String key : objMap.keySet()) {
            Object obj = params.get(key);
            if ("context_free_actions".equals(key)) {
                bf.concat(ByteUtils.writerVarint32(String.valueOf(((List) obj).size())));
                for (Object ob : (List) obj) {
                    writeBytes(ob, bf);
                }
            } else if ("actions".equals(key)) {
                bf.concat(ByteUtils.writerVarint32(String.valueOf(((List) obj).size())));
                for (Object ob : (List) obj) {
                    writeBytes(ob, bf);
                }
            } else if ("producers".equals(key)) {
                bf.concat(ByteUtils.writerVarint32(String.valueOf(((List) obj).size())));
                for (Object ob : (List) obj) {
                    Map<String, Object> mp = new HashMap<>();
                    mp.put("producer", ob);
                    writeBytes(mp, bf);
                }
            } else {
                writeBytes(obj, bf);
            }
        }
    }
}
