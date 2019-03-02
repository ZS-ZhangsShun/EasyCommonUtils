package com.zs.easy.common.http;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 解决get模式请求多次时，出现“java.io.IOException: unexpected end of stream on okhttp3.Address@178de5cc！”的bug
 */
 public class NetInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request().newBuilder()
                .addHeader("Connection","close").build();
        return chain.proceed(request);
    }
 }