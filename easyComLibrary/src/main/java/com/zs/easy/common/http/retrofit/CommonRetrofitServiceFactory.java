package com.zs.easy.common.http.retrofit;

import com.zs.easy.common.constants.EasyConstants;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

public class CommonRetrofitServiceFactory extends BaseRetrofitServiceFactory {
    private static volatile CommonRetrofitServiceFactory instance;
    private static volatile CommonRetrofitServiceFactory instanceForFile;

    private CommonRetrofitServiceFactory() {
        super();
    }

    private CommonRetrofitServiceFactory(int timeOut) {
        super(timeOut);
    }

    public static CommonRetrofitServiceFactory getInstance() {
        if (instance == null) {
            synchronized (CommonRetrofitServiceFactory.class) {
                if (instance == null) {
                    instance = new CommonRetrofitServiceFactory();
                }
            }
        }
        return instance;
    }

    public static CommonRetrofitServiceFactory getFileInstance() {
        if (instanceForFile == null) {
            synchronized (CommonRetrofitServiceFactory.class) {
                if (instanceForFile == null) {
                    instanceForFile = new CommonRetrofitServiceFactory(EasyConstants.READ_TIME_OUT_FOR_FILE);
                }
            }
        }
        return instanceForFile;
    }

    public <T> T createService(Class<T> service) {
        return mRetrofit.create(service);
    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    public static Builder init(String baseUrl) {
        return new Builder(baseUrl);
    }

    public static void addInterceptor(Interceptor interceptor) {
        interceptors.add(interceptor);
    }

    /**
     * CommonRetrofitServiceFactory 构造类
     */
    public static class Builder {

        /**
         * 服务器地址
         */
        private String baseUrl = "";

        /**
         * 连接超时时间
         */
        private int connectTimeOut = 5;

        /**
         * 读取超时时间
         */
        private int readTimeOut = 5;

        /**
         * 文件读取超时时间
         */
        private int readTimeOutForFile = 300;

        /**
         * http日志Tag
         */
        private String httpLogTag = "HTTP_LOG";

        /**
         * 开启保存日志到文件
         */
        private boolean isWriteLogToFile = true;


        public Builder(String baseUrl) {
            this.baseUrl = baseUrl;
            EasyConstants.BASE_URL = baseUrl;
        }

        public Builder connectTimeOut(int connectTimeOut) {
            this.connectTimeOut = connectTimeOut;
            EasyConstants.CONNECT_TIME_OUT = connectTimeOut;
            return this;
        }

        public Builder readTimeOut(int readTimeOut) {
            this.readTimeOut = readTimeOut;
            EasyConstants.READ_TIME_OUT = readTimeOut;
            return this;
        }

        public Builder readTimeOutForFile(int readTimeOutForFile) {
            this.readTimeOutForFile = readTimeOutForFile;
            EasyConstants.READ_TIME_OUT_FOR_FILE = readTimeOutForFile;
            return this;
        }

        public Builder isWriteLogToFile(boolean isWriteLogToFile) {
            this.isWriteLogToFile = isWriteLogToFile;
            EasyConstants.IS_WRITE_LOG_TO_FILE = isWriteLogToFile;
            return this;
        }

        public Builder httpLogTag(String httpLogTag) {
            this.httpLogTag = httpLogTag;
            EasyConstants.HTTP_TAG = httpLogTag;
            return this;
        }

    }
}
