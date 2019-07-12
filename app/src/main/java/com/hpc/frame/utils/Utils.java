package com.hpc.frame.utils;

import java.util.ArrayList;


/**
 * @description :  工具类
 * @author : HPC
 * @date : 2019/7/12 16:38
 */
public class Utils {

    /**
     * 通过字符串表达式获取计算值
     *
     * @return
     */
    public static Integer getValueWithString(String str) {
        Calculate calculate = new Calculate();
        ArrayList result = calculate.getStringList(str);  //String转换为List
        result = calculate.getPostOrder(result);   //中缀变后缀
        int i = calculate.calculate(result);   //计算
        return i;
    }
}
