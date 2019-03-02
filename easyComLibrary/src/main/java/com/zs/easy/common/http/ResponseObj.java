package com.zs.easy.common.http;

/**
 * Created by zhangshun on 2017/6/22.
 */

public class ResponseObj {
    /**
     * 返回体 一般为json串格式
     */
    private String jsonStr;
    /**
     * 返回码  例如：200代表成功 204无数据 400请求参数错误 500服务器异常
     */
    private int code;

    public String getJsonStr() {
        return jsonStr;
    }

    public void setJsonStr(String jsonStr) {
        this.jsonStr = jsonStr;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
