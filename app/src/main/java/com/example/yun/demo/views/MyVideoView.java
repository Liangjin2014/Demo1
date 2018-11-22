package com.example.yun.demo.views;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;

import io.vov.vitamio.utils.Log;
import io.vov.vitamio.widget.VideoView;

public class MyVideoView extends VideoView {

    private Paint mPaint;

    //在代码中动态创建view时调用这个构造函数
    public MyVideoView(Context context) {
        super(context);
    }

    //在xml中添加view的时候调用这个构造函数
    public MyVideoView(Context context,AttributeSet attrs){
        super(context, attrs);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.e("tag", "onMeasure:widthMea="+MeasureSpec.getSize(widthMeasureSpec));
        setMeasuredDimension(measureWH(widthMeasureSpec), measureWH(heightMeasureSpec));

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    private int measureWH(int widthMeasureSpec) {

        int result;
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);
        if(specMode == MeasureSpec.EXACTLY){
            result = specSize;
        }else{
            result = 200;
            if(specMode == MeasureSpec.AT_MOST){
                result = Math.min(specSize, result);
            }
        }
        return result;
    }

}
