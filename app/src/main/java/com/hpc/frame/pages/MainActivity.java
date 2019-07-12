package com.hpc.frame.pages;

import com.hpc.frame.R;
import com.hpc.frame.annotations.ViewInject;
import com.hpc.frame.common.BaseActivity;
import com.hpc.frame.network.NetWorkManager;
import com.hpc.frame.network.request.IFaceRequest;
import com.hpc.frame.network.response.ResponseTransformer;
import com.hpc.frame.network.schedulers.SchedulerProvider;


@ViewInject(R.layout.activity_main)
public class MainActivity extends BaseActivity {

    IFaceRequest iFaceRequest = null;

    @Override
    protected void init() {
        iFaceRequest = NetWorkManager.getRetrofit().create(IFaceRequest.class);
        this.iFaceRequest.faceSearch("")
                .compose(ResponseTransformer.handleResult())
                .compose(SchedulerProvider.getInstance().applySchedulers())
                .subscribe(carBeans -> {
                    // 处理数据 直接获取到List<JavaBean> carBeans
                }, throwable -> {
                    // 处理异常
                });
    }
}
