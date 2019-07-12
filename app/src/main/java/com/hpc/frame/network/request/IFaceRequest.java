package com.hpc.frame.network.request;


import com.alibaba.fastjson.JSONObject;
import com.hpc.frame.network.response.Response;

import io.reactivex.Observable;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @description :  人脸识别请求接口
 * @author : HPC
 * @date : 2019/7/12 14:50
 */
public interface IFaceRequest extends IBaseRequest{

    @POST("/face/faceSearch")
    Observable<Response<JSONObject>> faceSearch(@Query("image") String userId);

}
