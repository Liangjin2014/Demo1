package com.example.yun.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yun.demo.beans.Result;
import com.example.yun.demo.utrls.Utrls;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
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

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id._rst_tv_username)
    TextView RstTvUsername;
    @BindView(R.id.rst_et_username)
    EditText rstEtUsername;
    @BindView(R.id.rst_tv_password)
    TextView rstTvPassword;
    @BindView(R.id.rst_et_password)
    EditText rstEtPassword;
    @BindView(R.id.rst_tv_confirm)
    TextView rstTvConfirm;
    @BindView(R.id.rst_et_confirm)
    EditText rstEtConfirm;
    @BindView(R.id.rst_bt_confirm)
    Button rstBtConfirm;
    @BindView(R.id.rst_tv_email)
    TextView rstTvEmail;
    @BindView(R.id.rst_et_email)
    EditText rstEtEmail;

    private final String url = "http://192.168.0.103:9000/LoginANDRegister/register";
    private  final int USEREXISTCODE = 101;
    private final int EMAILEXISTCODE = 102;
    private final int REGISTERSUCCESSCODE = 201;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

    }

    @OnClick(R.id.rst_bt_confirm)
    public void onViewClicked() {
        String username = rstEtUsername.getText().toString();
        String password1 = rstEtPassword.getText().toString();
        String password2 = rstEtConfirm.getText().toString();
        String email = rstEtEmail.getText().toString();

        if(TextUtils.isEmpty(username) || TextUtils.isEmpty(password1) || TextUtils.isEmpty(password2) || TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "输入不能为空！", Toast.LENGTH_LONG).show();
        }
        else if(!password1.equals(password2))
        {
            Log.e("password", password1 + "," + password2);
            Toast.makeText(this, "两次输入的密码不一致，请重新输入！", Toast.LENGTH_LONG).show();
        }
        else {
            Map map = new HashMap();
            map.put("username", username);
            map.put("password", password1);
            map.put("email", email);

            postDataSync(map);

        }
    }


    public void postDataSync(Map map)
    {
        Gson gson = new Gson();
        String s = gson.toJson(map);
        Log.e("MAP", s);
        OkHttpClient okHttpClient  = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10,TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();

        //post方式提交的数据
        FormBody formBody = new FormBody.Builder()
                .add("data", s)
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

                Result result = (Result) Utrls.parseData(response.body().string(), Result.class);
                EventBus.getDefault().post(result);
            }
        });

    }

    //EventBus接收消息的方法
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvent(Result result) {

        if(result.getCode() == REGISTERSUCCESSCODE)
        {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);

        }
        else
        {
            String message = result.getMessage();
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }

    }


}
