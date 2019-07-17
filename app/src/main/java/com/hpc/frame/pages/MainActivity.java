package com.hpc.frame.pages;

import com.alibaba.fastjson.JSONObject;
import com.hpc.frame.R;
import com.hpc.frame.annotations.ViewInject;
import com.hpc.frame.common.BaseActivity;
import com.hpc.frame.network.request.FaceRequestWrapper;


@ViewInject(R.layout.activity_main)
public class MainActivity extends BaseActivity<FaceRequestWrapper> {

    @Override
    protected void init() {
        JSONObject obj = new JSONObject();
        obj.put("image", "11111111");
        wrapper.faceSearch(obj).subscribe(res -> {
            System.out.println(res);
        },err->{
            System.out.println(err);
        });
    }
}
