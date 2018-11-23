package com.example.yun.demo.dialogs;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;

import com.example.yun.demo.R;

/**
 * 加载提醒对话框
 */
public class CustomProgressDialog extends ProgressDialog
{
    public CustomProgressDialog(Context context)
    {
        super(context);
    }

    public CustomProgressDialog(Context context, int theme)
    {
        super(context, theme);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        init(getContext());
    }
    private void init(Context context)
    {
        //设置不可取消，点击其他区域不能取消，实际中可以抽出去封装供外包设置
 //       setCancelable(false);
 //       setCanceledOnTouchOutside(false);
        setContentView(R.layout.load_dialog);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(params);
    }
    @Override
    public void show()
    {
        super.show();
    }

    @Override
    public void setCancelable(boolean flag) {
        super.setCancelable(flag);
    }

    @Override
    public void setCanceledOnTouchOutside(boolean cancel) {
        super.setCanceledOnTouchOutside(cancel);
    }
}