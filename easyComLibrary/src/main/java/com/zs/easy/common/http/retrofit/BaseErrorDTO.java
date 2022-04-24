package com.zs.easy.common.http.retrofit;

/**
 * 通用http异常返回值解析 此处是个性化业务 需要根据自己的项目定制
 */
public class BaseErrorDTO {

    /**
     * error_message : merchant has related to commoditys!
     * code : 25456
     */

    private String error_message;
    private String code;

    public String getError_message() {
        return error_message;
    }

    public void setError_message(String error_message) {
        this.error_message = error_message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "BaseErrorDTO{" +
                "error_message='" + error_message + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
