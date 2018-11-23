package com.example.yun.demo;

import android.app.AppComponentFactory;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.yun.demo.beans.DataBean;
import com.example.yun.demo.beans.Result;
import com.example.yun.demo.interfaces.LoginService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/*
 * 解析Json数据，本程序集成了Gson，okHttp,eventbus框架
 *
 *
 *
 * */

public class LoginActivity extends AppCompatActivity {

    private final String url = "http://192.168.0.103:9000/LoginANDRegister/login";
    private final String BASE_URL = "http://192.168.0.103:9000/";
    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.button)
    Button button;


    private final int PASSWORDNOTFOUND = 404;
    private final int USERDOESNOTEXIST = 400;
    private final int REQUESTSUCCESS = 200;
    @BindView(R.id.bt_register)
    Button btRegister;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
    }


    public <T> Object parseData(String response, Type cls) {
        Gson gson = new Gson();
        Object obj = gson.fromJson(response, cls);
        return obj;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    //EventBus接收消息的方法
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvent(String name) {
        Toast.makeText(this, "Hi" + name, Toast.LENGTH_SHORT).show();
    }

    @OnClick({R.id.button})
    public void onViewOnClicked(View view) {
//       getDataAsync();
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();
        Map map = new HashMap();
        map.put("username", username);
        map.put("password", password);
        if (!TextUtils.isEmpty(username.trim()) && !TextUtils.isEmpty(password.trim())) {
//            postDataSync(username, password);
              doRequestByRxRetrofit(map);
        } else {
            Toast.makeText(this, "姓名或密码不能为空哦！", Toast.LENGTH_LONG).show();
        }
    }


    public void postDataSync(String key, String value) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();

        //post方式提交的数据
        FormBody formBody = new FormBody.Builder()
                .add("username", key)
                .add("password", value)
                .build();

        final Request request = new Request.Builder()
                .url(url)//请求的url
                .post(formBody)
                .build();


        //创建/Call
        Call call = okHttpClient.newCall(request);
        //加入队列 异步操作
        call.enqueue(new Callback() {
            //请求错误回调方法
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("ERROR", "链接失败！");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {


                switch (response.code()) {
                    case USERDOESNOTEXIST:
                        Log.e("RESPONSE", response.body().string());
                        break;
                    case REQUESTSUCCESS:
                        Type dataType = new TypeToken<Result<DataBean>>() {
                        }.getType();
                        Result<DataBean> dataResult = (Result<DataBean>) parseData(response.body().string(), dataType);
                        DataBean data = dataResult.data;
                        EventBus.getDefault().post(data.getName());
                        break;
                    case PASSWORDNOTFOUND:
                        Log.e("RESPONSE", response.body().string());
                        break;
                }

            }
        });

    }

    @OnClick(R.id.bt_register)
    public void onViewClicked() {

        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }



    private void doRequestByRxRetrofit(Map<String, String> params) {
        final ProgressDialog pd = new ProgressDialog(this);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)//基础URL 建议以 / 结尾
                .addConverterFactory(GsonConverterFactory.create())//设置 Json 转换器
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())//RxJava 适配器
                .build();
        LoginService rxjavaService = retrofit.create(LoginService .class);
        rxjavaService .getMessage(params)
                .subscribeOn(Schedulers.io())//IO线程加载数据
                .observeOn(AndroidSchedulers.mainThread())//主线程显示数据
                .subscribe(new Subscriber<Result>() {
                    @Override
                    public void onCompleted() {

                        if (pd != null && pd.isShowing()) {
                            pd.dismiss();
                        }
                        pd.dismiss();
                    }

                    @Override//登陆失败的回调
                    public void onError(Throwable e) {
                        if (pd != null && pd.isShowing()) {
                            pd.dismiss();
                        }
                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNext(Result result) {
                        if (pd != null && pd.isShowing()) {
                            pd.dismiss();
                        }
                        Toast.makeText(LoginActivity.this, result.getMessage(), Toast.LENGTH_LONG).show();
                    }


                    @Override
                    public void onStart() {
                        pd.onStart();

                    }
                });
    }

}
