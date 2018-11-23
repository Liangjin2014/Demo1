package com.example.yun.demo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends BaseActivity {

    @BindView(R.id.video_view)
    VideoView videoView;
    @BindView(R.id.ma_bt_login)
    Button maBtLogin;
    @BindView(R.id.ma_bt_regiater)
    Button maBtRegiater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        playfunction();
    }

    private void playfunction() {
        String path = "";
        path = "http://gslb.miaopai.com/stream/3D~8BM-7CZqjZscVBEYr5g__.mp4";

        if (path == "") {
            Toast.makeText(MainActivity.this,
                    "Url path Shouldn't be empty!",
                    Toast.LENGTH_LONG).show();
        } else {
            videoView.setVideoPath(path);
            videoView.setMediaController(new MediaController(this));
            videoView.requestFocus();

            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.setPlaybackSpeed(1.0f);
                }
            });
        }

    }

    @OnClick({R.id.ma_bt_login, R.id.ma_bt_regiater})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.ma_bt_login:
                intent = new Intent(MainActivity.this, LoginActivity.class);
                break;
            case R.id.ma_bt_regiater:
                intent = new Intent(MainActivity.this, RegisterActivity.class);
                break;
        }
        startActivity(intent);
    }

}
