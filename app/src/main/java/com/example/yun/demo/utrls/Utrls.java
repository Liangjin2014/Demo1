package com.example.yun.demo.utrls;

import com.example.yun.demo.beans.Result;
import com.google.gson.Gson;

import java.io.Reader;
import java.lang.reflect.Type;

public class Utrls {

     /*/异步向服务器发送GET请求来获取数据
    private void getDataAsync() {
        OkHttpClient okHttpClient = new OkHttpClient();
        //2.创建Request对象，设置一个url地址,设置请求方式。
        Request request = new Request.Builder().url(url).method("GET", null).build();
        //3.创建一个call对象,参数就是Request请求对象
        Call call = okHttpClient.newCall(request);
        //4.请求加入调度，重写回调方法
        call.enqueue(new Callback() {
            //请求失败执行的方法
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("ERROR", e.toString());
            }

            //请求成功执行的方法
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String name = parseData(response.body().string());
                EventBus.getDefault().post(name);

            }
        });
    }
*/

      //解析数据
      public static <T> Object parseData(String response, Type cls) {
          Gson gson = new Gson();
          Object obj = gson.fromJson(response, cls);
          return obj;
      }
    //}

    public interface ParameterizedType extends Type {
        // 返回Map<String,User>里的String和User，所以这里返回[String.class,User.clas]
        Type[] getActualTypeArguments();
        // Map<String,User>里的Map,所以返回值是Map.class
        Type getRawType();
        // 用于这个泛型上中包含了内部类的情况,一般返回null
        Type getOwnerType();
    }

    public class ParameterizedTypeImpl implements ParameterizedType {
        private final Class raw;
        private final Type[] args;
        public ParameterizedTypeImpl(Class raw, Type[] args) {
            this.raw = raw;
            this.args = args != null ? args : new Type[0];
        }
        @Override
        public Type[] getActualTypeArguments() {
            return args;
        }
        @Override
        public Type getRawType() {
            return raw;
        }
        @Override
        public Type getOwnerType() {return null;}
    }

 /*   public static <T> Result<T> fromJsonObject(Reader reader, Class<T> clazz) {
        Type type = new ParameterizedTypeImpl(Result.class, new Class[]{clazz});
        return GSON.fromJson(reader, type);
    }*/


}
