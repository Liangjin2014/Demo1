package com.example.yun.demo.interfaces;

import com.example.yun.demo.beans.DataBean;
import com.example.yun.demo.beans.Result;

import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

public interface LoginService {
    @Headers("apikey:81bf9da930c7f9825a3c3383f1d8d766")
    @FormUrlEncoded
    @POST("LoginANDRegister/login")
    Observable<Result> getMessage(@FieldMap Map<String, String> params);
}
