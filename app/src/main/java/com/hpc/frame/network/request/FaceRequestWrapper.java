package com.hpc.frame.network.request;

import com.alibaba.fastjson.JSONObject;
import com.hpc.frame.network.NetWorkManager;
import com.hpc.frame.network.response.Response;

import io.reactivex.Observable;

/**
 * @author : HPC
 * @description :  人脸识别请求包装
 * @date : 2019/7/15 8:58
 */
public class FaceRequestWrapper extends AbstractRequestWrapper implements IFaceRequest {

    private IFaceRequest iFaceRequest = NetWorkManager.getRetrofit().create(IFaceRequest.class);

    @Override
    public Observable<Response<JSONObject>> faceSearch(JSONObject json) {
        return dealResult(iFaceRequest.faceSearch(json));
    }
}
