package com.zs.easy.common.http.retrofit;

import com.zs.easy.common.constants.EasyConstants;
import com.zs.easy.common.utils.LogUtil;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class BaseRetrofitServiceFactory {

    protected Retrofit mRetrofit = null;
    protected OkHttpClient okHttpClient;
    protected static List<Interceptor> interceptors = new ArrayList<>();

    protected BaseRetrofitServiceFactory() {
        initOKAndRetrofit(EasyConstants.READ_TIME_OUT);
    }

    protected BaseRetrofitServiceFactory(int timeOut) {
        initOKAndRetrofit(timeOut);
    }

    private void initOKAndRetrofit(int timeOut) {
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        builder.dns(new XDns(EasyConstants.CONNECT_TIME_OUT));
        builder.connectTimeout(EasyConstants.CONNECT_TIME_OUT, TimeUnit.SECONDS);
        builder.readTimeout(timeOut, TimeUnit.SECONDS);
        builder.writeTimeout(timeOut, TimeUnit.SECONDS);
        builder.retryOnConnectionFailure(true);

        //添加拦截器 添加header
//        builder.addNetworkInterceptor(new Interceptor() {
//            @Override
//            public Response intercept(Chain chain) throws IOException {
//                Request original = chain.request();
//                Request request = original.newBuilder()
//                        //这里设置不生效 猜测是Retrofit会默认覆盖 每个请求用@Header 添加就会生效
//                        .header("Content-Type", TenantConstants.CONTENT_TYPE)
//                        .addHeader("Connection", "keep-alive")
//                        .addHeader("Accept", "*/*")
//                        .addHeader("Access-Control-Allow-Origin", "*")
//                        .addHeader("Access-Control-Allow-Headers", "X-Requested-With")
//                        .addHeader("Vary", "Accept-Encoding")
//                        .build();
//
//                return chain.proceed(request);
//            }
//        });

        // log用拦截器
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                try {
                    final String text = URLDecoder.decode(message, "utf-8");
                    LogUtil.e(text);
                    if (EasyConstants.IS_WRITE_LOG_TO_FILE) {
                        LogUtil.writeLogtoDefaultPath(EasyConstants.HTTP_TAG, text);
                    }
                } catch (Exception e) {

                }
            }
        });
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(loggingInterceptor);

        for (int i = 0; i < interceptors.size(); i++) {
            builder.addInterceptor(interceptors.get(i));
        }
//       builder.addInterceptor(new ZSLoggingInterceptor());
//        builder.addInterceptor(new LoggingInterceptor.Builder()
//                .loggable(BuildConfig.DEBUG)
//                .setLevel(Level.BASIC)
//                .log(Platform.INFO)
//                .request("Request")
//                .response("Response")
//                .build());

        //信任所有证书
        builder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String s, SSLSession sslSession) {
                //设置为true
                return true;
            }
        });
        //创建管理器
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            @Override
            public void checkClientTrusted(
                    java.security.cert.X509Certificate[] x509Certificates,
                    String s) throws java.security.cert.CertificateException {
            }

            @Override
            public void checkServerTrusted(
                    java.security.cert.X509Certificate[] x509Certificates,
                    String s) throws java.security.cert.CertificateException {
            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[]{};
            }
        }};
        try {
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            //为OkHttpClient设置sslSocketFactory
            builder.sslSocketFactory(sslContext.getSocketFactory());

        } catch (Exception e) {
            e.printStackTrace();
        }

        HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
        builder.hostnameVerifier(DO_NOT_VERIFY);
//        builder.addInterceptor(new Interceptor() {
//            @Override
//            public Response intercept(Chain chain) throws IOException {
//                Request request = chain.request();
//                MediaType mediaType = request.body().contentType();
//                try {
//                    Field field = mediaType.getClass().getDeclaredField("mediaType");
//                    field.setAccessible(true);
//                    field.set(mediaType, TenantConstants.CONTENT_TYPE);
//                } catch (NoSuchFieldException e) {
//                    e.printStackTrace();
//                } catch (IllegalAccessException e) {
//                    e.printStackTrace();
//                }
//                return chain.proceed(request);
//            }
//        });
        okHttpClient = builder.build();
        mRetrofit = new Retrofit.Builder()
                .baseUrl(EasyConstants.BASE_URL)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
