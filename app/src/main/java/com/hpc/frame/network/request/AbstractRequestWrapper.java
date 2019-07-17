package com.hpc.frame.network.request;

import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.hpc.frame.BaseApplication;
import com.hpc.frame.network.response.Response;
import com.hpc.frame.network.schedulers.SchedulerProvider;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;

/**
 * @author : HPC
 * @description : api基类
 * @date : 2019/7/15 8:57
 */
abstract class AbstractRequestWrapper {
    private SchedulerProvider instance = SchedulerProvider.getInstance();

    Observable<Response<JSONObject>> dealResult(Observable<Response<JSONObject>> observable) {
        return observable.observeOn(instance.ui()).subscribeOn(instance.io()).flatMap(this::flatResponse);
    }

    private ObservableSource<? extends Response<JSONObject>> flatResponse(Response<JSONObject> response) {
        return Observable.create(subscriber -> {
            if ("0000".equals(response.getCode())) {
                Toast.makeText(BaseApplication.context, response.getMsg(), Toast.LENGTH_SHORT).show();
                subscriber.onNext(response);
            } else {
                /*返回结果错误*/
                Toast.makeText(BaseApplication.context, response.getMsg(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
