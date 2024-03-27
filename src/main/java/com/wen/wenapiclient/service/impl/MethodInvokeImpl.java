package com.wen.wenapiclient.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.wen.wenapiclient.request.GetInvokeRequest;
import com.wen.wenapiclient.service.MethodInvoke;
import com.wen.wenapiclient.util.SignUtil;
import com.wen.wenapicommon.common.BaseCode;
import com.wen.wenapicommon.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 调用接口方法实现类
 *
 * @author wen
 */
@Slf4j
public class MethodInvokeImpl implements MethodInvoke {

    /*private static final String GATEWAY_HOST = "https://api-gateway.cwblue.cn";*/
    private static final String GATEWAY_HOST = "http://localhost:8090";

    /**
     * get 请求
     *
     * @param getInvokeRequest 请求参数
     * @return JSON 数据
     */
    @Override
    public JSON get(GetInvokeRequest getInvokeRequest) {
        String url = getInvokeRequest.getUrl();
        String params = getInvokeRequest.getParams();
        String accessKey = getInvokeRequest.getAccessKey();
        String secretKey = getInvokeRequest.getSecretKey();
        HttpResponse httpResponse;
        try {
            httpResponse = HttpRequest.get(GATEWAY_HOST + url)
                    .addHeaders(getHeaderMap(params, accessKey, secretKey))
                    .body(params)
                    .form("words", params)
                    .execute();
        }catch (Exception e) {
            throw new BusinessException(BaseCode.INTERNAL_ERROR);
        }
        log.info("客户端响应：" + httpResponse);
        JSON res = JSONUtil.parse(httpResponse.body());
        log.info("客户端返回：" + res);
        return res;
    }

    /**
     * post 请求
     *
     * @param getInvokeRequest 请求参数
     * @return JSON 数据
     */
    @Override
    public JSON post(GetInvokeRequest getInvokeRequest) {
        String url = getInvokeRequest.getUrl();
        String params = getInvokeRequest.getParams();
        String accessKey = getInvokeRequest.getAccessKey();
        String secretKey = getInvokeRequest.getSecretKey();
        HttpResponse httpResponse;
        try {
            httpResponse = HttpRequest.post(GATEWAY_HOST + url)
                    .addHeaders(getHeaderMap(params, accessKey, secretKey))
                    .body(params)
                    .execute();
        }catch (Exception e) {
            throw new BusinessException(BaseCode.INTERNAL_ERROR);
        }
        log.info("客户端响应：" + httpResponse);
        JSON res = JSONUtil.parse(httpResponse.body());
        log.info("客户端返回：" + res);
        return res;
    }


    /**
     * 封装请求头
     *
     * @param params 请求头
     * @return 封装后的 Map
     */
    private Map<String, String> getHeaderMap(String params, String accessKey, String secretKey) {
        String bytesParams = Arrays.toString(params.getBytes());
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("accessKey", accessKey);
        headerMap.put("body", bytesParams);
        headerMap.put("nonce", RandomUtil.randomNumbers(5));
        headerMap.put("timeStamp", String.valueOf(System.currentTimeMillis() / 1000));
        headerMap.put("sign", SignUtil.getSign(bytesParams, secretKey));
        return headerMap;
    }

}


