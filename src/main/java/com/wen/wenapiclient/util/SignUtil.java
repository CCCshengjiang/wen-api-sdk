package com.wen.wenapiclient.util;

import cn.hutool.crypto.digest.DigestUtil;

/**
 * 签名认证类
 *
 * @author wen
 */
public class SignUtil {

    /**
     * 生成签名
     * @param body 请求参数
     * @param secretKey 密钥
     * @return 加密签名
     */
    public static String getSign(String body, String secretKey) {
        return DigestUtil.md5Hex(body + "wen" + secretKey);
    }

}
