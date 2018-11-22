package com.example.yun.demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.vov.vitamio.Vitamio;

public class BaseActivity extends Activity {

    @Override
    public void setContentView(View view) {
        setContentView(R.layout.base_activity);
        //初始化butterknife
        ButterKnife.bind(this);
        //初始化Vitamio
        Vitamio.isInitialized(getApplicationContext());
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        ButterKnife.bind(this);
    }

}


