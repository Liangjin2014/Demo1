package com.example.yun.demo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/*
 * 解析Json数据，本程序集成了Gson，okHttp,eventbus框架
 *
 *
 *
 * */

public class MainActivity extends BaseActivity {

    private final String url = "http://192.168.0.103:9000/jsontest/";
    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.button)
    Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
    }

    //解析数据
    public String parseData(String response) {
        Gson gson = new Gson();
        DataBean dataBean = gson.fromJson(response, DataBean.class);
        return dataBean.getName();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    //EventBus接收消息的方法
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvent(String name) {
        Toast.makeText(this, name, Toast.LENGTH_SHORT).show();
    }

    @OnClick({R.id.button})
    public void onViewOnClicked(View view) {
       getDataAsync();
    }

    //异步向服务器发送GET请求来获取数据
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

}
