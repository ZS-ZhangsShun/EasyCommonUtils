package com.zs.easy.common.http.retrofit;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.zs.easy.common.utils.LogUtil;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.HttpException;

public class ExceptionHandle {

    public static final int HTTP_ERROR_400 = 400;
    public static final int UNAUTHORIZED = 401;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int REQUEST_TIMEOUT = 408;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int BAD_GATEWAY = 502;
    private static final int SERVICE_UNAVAILABLE = 503;
    private static final int GATEWAY_TIMEOUT = 504;

    public static ResponseThrowable handleException(Throwable e) {
        ResponseThrowable ex;
        LogUtil.i("e.toString = " + e.toString());
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            ex = new ResponseThrowable(e, httpException.code());
            switch (httpException.code()) {
                case HTTP_ERROR_400:
                    ex.message = "参数错误";
                    break;
                case UNAUTHORIZED:
                    ex.message = "401鉴权失败";
                    break;
                case FORBIDDEN:
                case NOT_FOUND:
                    ex.message = "404";
                    break;
                case REQUEST_TIMEOUT:
                    ex.message = "408";
                    break;
                case GATEWAY_TIMEOUT:
                    ex.message = "504";
                    break;
                case INTERNAL_SERVER_ERROR:
                    ex.message = "500服务器异常";
                    break;
                case BAD_GATEWAY:
                case SERVICE_UNAVAILABLE:
                default:
                    //ex.code = httpException.code();
                    ex.message = "服务器异常";
                    break;
            }
            try {
                String error = httpException.response().errorBody().string();
                ex.errorJson = error;
                if (!TextUtils.isEmpty(error)) {
                    BaseErrorDTO bean = new Gson().fromJson(error, BaseErrorDTO.class);
                    ex.errorDTO = bean;
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            return ex;
        } else if (e instanceof ServerException) {
            ServerException resultException = (ServerException) e;
            ex = new ResponseThrowable(resultException, resultException.code);
            ex.message = resultException.message;
            return ex;
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
            /*|| e instanceof ParseException*/) {
            ex = new ResponseThrowable(e, ERROR.PARSE_ERROR);
            ex.message = "解析错误";
            return ex;
        } else if (e instanceof ConnectException) {
            ex = new ResponseThrowable(e, ERROR.NETWORD_ERROR);
            ex.message = "连接失败";
            return ex;
        } else if (e instanceof javax.net.ssl.SSLHandshakeException) {
            ex = new ResponseThrowable(e, ERROR.SSL_ERROR);
            ex.message = "证书验证失败";
            return ex;
        } else if (e instanceof SocketTimeoutException) {
            ex = new ResponseThrowable(e, ERROR.SOCKAT_TIME_OUT_ERROR);
            ex.message = "服务器响应超时";
            return ex;
        } else if (e instanceof SocketTimeoutException) {
            ex = new ResponseThrowable(e, ERROR.CONNECT_TIME_OUT_ERROR);
            ex.message = "服务器请求超时";
            return ex;
        } else if (e instanceof UnknownHostException) {
            ex = new ResponseThrowable(e, ERROR.DNS_TIME_OUT_ERROR);
            ex.message = "Dns 解析超时";
            return ex;
        } else {
            ex = new ResponseThrowable(e, ERROR.UNKNOWN);
            ex.message = "未知错误";
            return ex;
        }
    }


    /**
     * 约定异常
     */
    public class ERROR {
        /**
         * 未知错误
         */
        public static final int UNKNOWN = 1000;
        /**
         * 解析错误
         */
        public static final int PARSE_ERROR = 1001;
        /**
         * 网络错误
         */
        public static final int NETWORD_ERROR = 1002;
        /**
         * 协议出错
         */
        public static final int HTTP_ERROR = 1003;

        /**
         * 证书出错
         */
        public static final int SSL_ERROR = 1005;

        /**
         * 服务器响应超时
         */
        public static final int SOCKAT_TIME_OUT_ERROR = 1006;

        /**
         * 服务器连接超时
         */
        public static final int CONNECT_TIME_OUT_ERROR = 1007;
        /**
         * DNS 解析超时
         */
        public static final int DNS_TIME_OUT_ERROR = 1008;
    }

    public static class ResponseThrowable extends Exception {
        public int code;
        public String message;
        public String errorJson;
        public BaseErrorDTO errorDTO;

        public ResponseThrowable(Throwable throwable, int code) {
            super(throwable);
            this.code = code;
            this.message = throwable.getMessage();
        }

        public ResponseThrowable(Throwable throwable, int code, BaseErrorDTO errorDTO) {
            super(throwable);
            this.code = code;
            this.message = throwable.getMessage();
            this.errorDTO = errorDTO;
        }

        public ResponseThrowable(Throwable throwable, int code, String errorJson, BaseErrorDTO errorDTO) {
            super(throwable);
            this.code = code;
            this.message = throwable.getMessage();
            this.errorJson = errorJson;
            this.errorDTO = errorDTO;
        }
    }

    /**
     * ServerException发生后，将自动转换为ResponeThrowable返回
     */
    class ServerException extends RuntimeException {
        int code;
        String message;
    }

}
