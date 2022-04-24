package com.zs.easy.imgcompress.demo.api.service;

import com.zs.easy.imgcompress.demo.api.dto.CategoryDTO;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface TestService {

    /**
     * 请求目录的接口
     * @param header 头文件
     * @return 一级目录列表
     */
    @GET("/test-service/cats")
    Observable<List<CategoryDTO>> getCats(@Header("EasyHead") String header);
}