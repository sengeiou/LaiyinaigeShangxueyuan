package com.baselib.util;


import com.baselib.util.gson.DoubleDefault0Adapter;
import com.baselib.util.gson.IntegerDefault0Adapter;
import com.baselib.util.gson.LongDefault0Adapter;
import com.baselib.util.gson.NullStringToEmptyAdapterFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * @author
 * @Description:谷歌的Gson解析工具类，官方推荐
 */
public class GsonUtils {

    public GsonUtils() {
    }

    /**
     * 对象转JSON
     *
     * @param object
     * @return
     */
    public static String toJson(Object object) {
        String gsonString = null;
        try {
            Gson gson = new GsonBuilder().registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory()).create();
            gsonString = gson.toJson(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return gsonString;
    }

    /**
     * 对象转JSON
     *
     * @param object
     * @param type
     * @return
     */
    public static String toJson(Object object, Type type) {
        String gsonString = null;
        try {
            Gson gson = new GsonBuilder().registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory()).create();
            gsonString = gson.toJson(object, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return gsonString;
    }

    /**
     * JSON转对象
     *
     * @param gsonString
     * @param cls
     * @return
     */
    public static <T> T jsonToBean(String gsonString, Class<T> cls) {
        T t = null;
        try {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(int.class, new IntegerDefault0Adapter())
                    .registerTypeAdapter(Integer.class, new IntegerDefault0Adapter())
                    .registerTypeAdapter(long.class, new LongDefault0Adapter())
                    .registerTypeAdapter(Long.class, new LongDefault0Adapter())
                    .registerTypeAdapter(double.class, new DoubleDefault0Adapter())
                    .registerTypeAdapter(Double.class, new DoubleDefault0Adapter())
                    .registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory())
//                    .registerTypeHierarchyAdapter(List.class, new ListJsonDeserializer())
//                    .registerTypeHierarchyAdapter(cls, new ObjectJsonDeserializer())
                    .create();
            t = gson.fromJson(gsonString, cls);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }

    /**
     * JSON转List
     *
     * @param gsonString
     * @param cls
     * @return
     */
    public static <T> List<T> jsonToList(String gsonString, Class cls) {
        List<T> list = null;
        try {
            Gson gson = new GsonBuilder()
//                    .registerTypeAdapter(int.class, new IntegerDefault0Adapter())
//                    .registerTypeAdapter(Integer.class, new IntegerDefault0Adapter())
//                    .registerTypeAdapter(long.class, new LongDefault0Adapter())
//                    .registerTypeAdapter(Long.class, new LongDefault0Adapter())
//                    .registerTypeAdapter(double.class, new DoubleDefault0Adapter())
//                    .registerTypeAdapter(Double.class, new DoubleDefault0Adapter())
                    .registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory())
//                    .registerTypeHierarchyAdapter(List.class, new ListJsonDeserializer())
//                    .registerTypeHierarchyAdapter(cls, new ObjectJsonDeserializer())
                    .create();
            list = gson.fromJson(gsonString, type(List.class, cls));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * JSON转List
     *
     * @param gsonString
     * @param type
     * @return
     */
    public static <T> List<T> jsonToList(String gsonString, Type type) {
        List<T> list = null;
        try {
            Gson gson = new GsonBuilder().registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory()).create();
            list = gson.fromJson(gsonString, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * JSON转ListMap
     *
     * @param gsonString
     * @return
     */
    public static <T> List<Map<String, T>> jsonToListMap(String gsonString) {
        List<Map<String, T>> list = null;
        try {
            Gson gson = new GsonBuilder().registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory()).create();
            list = gson.fromJson(gsonString, new TypeToken<List<Map<String, T>>>() {
            }.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * JSON转Map
     *
     * @param gsonString
     * @return
     */
    public static <T> Map<String, T> jsonToMap(String gsonString) {
        Map<String, T> map = null;
        try {
            Gson gson = new GsonBuilder().registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory()).create();
            map = gson.fromJson(gsonString, new TypeToken<Map<String, T>>() {
            }.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * JSON转对象
     *
     * @param gsonString
     * @param type
     * @return
     */
    public static <T> T jsonToBean(String gsonString, Type type, Class clazz) {
        T t = null;
        try {
            Gson gson = new GsonBuilder()
//                    .registerTypeAdapter(int.class, new IntegerDefault0Adapter())
//                    .registerTypeAdapter(Integer.class, new IntegerDefault0Adapter())
//                    .registerTypeAdapter(long.class, new LongDefault0Adapter())
//                    .registerTypeAdapter(Long.class, new LongDefault0Adapter())
//                    .registerTypeAdapter(double.class, new DoubleDefault0Adapter())
//                    .registerTypeAdapter(Double.class, new DoubleDefault0Adapter())
                    .registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory())
//                    .registerTypeHierarchyAdapter(List.class, new ListJsonDeserializer())
//                    .registerTypeHierarchyAdapter(clazz, new ObjectJsonDeserializer())
                    .create();
            t = gson.fromJson(gsonString, type);
        } catch (Exception e) {
            AppExceptionUtils.getAppExceptionHandler().saveErrorLog(e);
            e.printStackTrace();
        }
        return t;
    }

    /**
     * JSON转对象
     *
     * @param gsonString
     * @param type
     * @return
     */
    public static <T> T jsonToBean(String gsonString, Type type) {
        T t = null;
        try {
            Gson gson = new GsonBuilder()
//                    .registerTypeAdapter(int.class, new IntegerDefault0Adapter())
//                    .registerTypeAdapter(Integer.class, new IntegerDefault0Adapter())
//                    .registerTypeAdapter(long.class, new LongDefault0Adapter())
//                    .registerTypeAdapter(Long.class, new LongDefault0Adapter())
//                    .registerTypeAdapter(double.class, new DoubleDefault0Adapter())
//                    .registerTypeAdapter(Double.class, new DoubleDefault0Adapter())
                    .registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory())
//                    .registerTypeHierarchyAdapter(List.class, new ListJsonDeserializer())
                    .create();
            t = gson.fromJson(gsonString, type);
        } catch (Exception e) {
            AppExceptionUtils.getAppExceptionHandler().saveErrorLog(e);
            e.printStackTrace();
        }
        return t;
    }

    /**
     * JSON转对象，data支持泛型对象
     *
     * @param json  json数据
     * @param clazz Result基类中data对像类型
     * @return
     */
    public static Result json2Bean(String json, Class clazz) {
        Type objectType = type(Result.class, clazz);
        return GsonUtils.jsonToBean(json, objectType, clazz);
    }

    /**
     * JSON转对象，data支持泛型列表对象
     *
     * @param json  json数据
     * @param clazz Result基类中data对像类型
     * @return
     */
    public static ResultList json2List(String json, Class clazz) {
        Type objectType = type(ResultList.class, clazz);
        return GsonUtils.jsonToBean(json, objectType, clazz);
    }

    /**
     * 处理泛型的类型
     *
     * @param raw
     * @param args
     * @return
     */
    static ParameterizedType type(final Class raw, final Type... args) {
        return new ParameterizedType() {
            public Type getRawType() {
                return raw;
            }

            public Type[] getActualTypeArguments() {
                return args;
            }

            public Type getOwnerType() {
                return null;
            }
        };
    }


    /****************************
     * TEST
     ****************************/
    public static void main(String[] args) {

        User user = GsonUtils.jsonToBean("{\"name\":\"chenzhikai\"}", User.class);
        List<User> userList = GsonUtils.jsonToList("[{\"name\":\"chenzhikai\"},{\"name\":\"123\"}]", User.class);
        List<User> userList2 = GsonUtils.jsonToList("[{\"name\":\"chenzhikai\"}]", new TypeToken<List<User>>(){}.getType());

        System.out.println("user======" + user.toString());
        System.out.println("userList======" + userList.toString() + userList.get(0).getName());
        System.out.println("userList2======" + userList2.toString());


        // data支持泛型对象
//        Result<User> result = GsonUtils.json2Bean("{\"code\":null,\"message\":\"message\",\"data\":{\"name\":{\"name\":\"chenzhikai\"}}}", User.class);
        // data支持泛型数组
//        ResultList<User> resultList = GsonUtils.json2List("{\"code\":\"12\",\"message\":\"message\",\"data\":[{\"name\":\"chenzhikai\"}]}", User.class);

//        System.out.println(result.toString());
//        System.out.println(resultList.toString());

        // 对象转JSON
//        System.out.println("转换后Result======" + GsonUtils.toJson(result));
//        System.out.println("转换后ResultList======" + GsonUtils.toJson(resultList));

    }

    public class User {

        private int age;

        private String name;

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "User{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }


}
