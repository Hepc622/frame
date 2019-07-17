package com.hpc.frame.network.request;


import com.alibaba.fastjson.JSONObject;
import com.hpc.frame.network.response.Response;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

/**
 * @author : HPC
 * @description :  人脸识别请求接口
 * @date : 2019/7/12 14:50
 */
public interface IFaceRequest {

    @POST("/face/faceSearch")
    Observable<Response<JSONObject>> faceSearch(@Body JSONObject json);

}
