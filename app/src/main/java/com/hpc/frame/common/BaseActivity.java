package com.hpc.frame.common;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;


import com.hpc.frame.annotations.ViewInject;

import java.lang.reflect.Field;
import java.util.Map;

public abstract class BaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*初始化注解*/
        autoInjectAllField();
        /*调用初始方法*/
        init();
    }


    protected void init() {
    }

    public void onClick(View view) {
    }

    /**
     * 解析注解
     */
    public void autoInjectAllField() {
        try {
            Class<?> clazz = this.getClass();
            if (clazz.isAnnotationPresent(ViewInject.class)) {
                ViewInject annotation = clazz.getAnnotation(ViewInject.class);
                int value = annotation.value();
                setContentView(value);
            }
            Field[] fields = clazz.getDeclaredFields();//获得Activity中声明的字段
            for (Field field : fields) {
                // 查看这个字段是否有我们自定义的注解类标志的
                if (field.isAnnotationPresent(ViewInject.class)) {
                    ViewInject inject = field.getAnnotation(ViewInject.class);
                    int id = inject.value();
                    if (id > 0) {
                        field.setAccessible(true);
                        View view = this.findViewById(id);
                        if (view instanceof Button) {
                            view.setOnClickListener(this::onClick);
                        }
                        field.set(this, view);//给我们要找的字段设置值
                    }

                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param to     : 调转到哪一个页面
     * @param finish : 是否开启一个新的栈
     * @param maps   :  页面调转携带的数据
     * @return void
     * @description : 页面调转
     * @author : HPC
     * @date : 2019/7/12 11:21
     */
    public void jumpPage(Class<?> to, Boolean finish, Map<String, Object>... maps) {
        Intent intent = new Intent(this, to);
        if (finish) {
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        if (maps != null && maps.length > 0) {
            Map<String, Object> map = maps[0];
            for (String key : map.keySet()) {
                Object o = map.get(key);
                if (o instanceof Byte) {
                    intent.putExtra(key, (Byte) o);
                } else if (o instanceof Short) {
                    intent.putExtra(key, (Short) o);
                } else if (o instanceof Integer) {
                    intent.putExtra(key, (Integer) o);
                } else if (o instanceof Float) {
                    intent.putExtra(key, (Float) o);
                } else if (o instanceof Double) {
                    intent.putExtra(key, (Double) o);
                } else if (o instanceof Long) {
                    intent.putExtra(key, (Long) o);
                } else if (o instanceof Character) {
                    intent.putExtra(key, (Character) o);
                } else if (o instanceof String) {
                    intent.putExtra(key, (String) o);
                } else if (o instanceof Boolean) {
                    intent.putExtra(key, (Boolean) o);
                }

                if (o instanceof Byte[]) {
                    intent.putExtra(key, (Byte[]) o);
                } else if (o instanceof Short[]) {
                    intent.putExtra(key, (Short[]) o);
                } else if (o instanceof Integer[]) {
                    intent.putExtra(key, (Integer[]) o);
                } else if (o instanceof Float[]) {
                    intent.putExtra(key, (Float[]) o);
                } else if (o instanceof Double[]) {
                    intent.putExtra(key, (Double[]) o);
                } else if (o instanceof Long[]) {
                    intent.putExtra(key, (Long[]) o);
                } else if (o instanceof Character[]) {
                    intent.putExtra(key, (Character[]) o);
                } else if (o instanceof String[]) {
                    intent.putExtra(key, (String[]) o);
                } else if (o instanceof Boolean[]) {
                    intent.putExtra(key, (Boolean[]) o);
                }
            }
        }
        startActivity(intent);
    }

    /**
     * 更新数据集合
     *
     * @param view
     */
    public void updateListView(ListView view) {
        ((BaseAdapter) view.getAdapter()).notifyDataSetChanged();
    }
}