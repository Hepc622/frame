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
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

public abstract class BaseActivity<T> extends Activity {

    /**
     * 请求api包装类
     */
    public T wrapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*初始化注解*/
        autoInjectAllField();
        /*初始化请求*/
        initRequestApi();
        /*调用初始方法*/
        init();
    }

    /*初始化请求*/
    protected void initRequestApi() {
        try {
            //使用反射创建model对象
            //1.获取子类类型： StandardAction.class
            //this:当前运行时的实例
            //System.out.println(this.getClass()+"====");
            Class clz = this.getClass();//this指的是当前运行的实例（子类实例）

			/*
			private int a;   				TypeVariable
			private int[] a;				GenericArrayType
			private Student a;				WildcardType
			private List<Student> a;		ParameterizedType

			*/
            //2.获取类的泛型父类 : BaseAction<Standard>
            //Type: 是Java里面所有类型的父接口
            Type type = clz.getGenericSuperclass();//获取泛型父类，必须用该方法，此处的泛型父类不是指当前的类，而是具体继承的BaseAction<Standard>，当前类为BaseAction<T>泛型尚未确定

            //3.把Type转换为具体的类型: BaseAction<Standard>
            ParameterizedType pt = (ParameterizedType) type;//将泛型父类转换为具体的那种类型

            //4.从具体类型中获取泛型  : Standard.class
            //System.out.println(pt.getActualTypeArguments()[0]);

            Class modelClass = (Class) pt.getActualTypeArguments()[0];//获取具体泛型类Action中的泛型

            //5.创建泛型类的的对象
            wrapper = (T) modelClass.getConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
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