package cn.xcom.helper.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.xcom.helper.R;
import cn.xcom.helper.utils.MyImageLoader;

/**
 * Created by Administrator on 2017/4/14 0014.
 */

public class AdvImgDetialActivity extends Activity {
    @BindView(R.id.adv_img_detial)
    ImageView advImgDetial;
    Context context;
    String path;
    String url;
    @BindView(R.id.advertising_back)
    RelativeLayout advertisingBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adv_img);
        ButterKnife.bind(this);
        context = this;
        path = getIntent().getStringExtra("path");
        url = getIntent().getStringExtra("url");
        MyImageLoader.display(path, advImgDetial);
        if (!url.equals("")) {
            dialog();
        }
    }

    private void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AdvImgDetialActivity.this);
        builder.setMessage("是否跳转到目标网址");
        builder.setTitle("系统提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                Uri uri = Uri.parse("http://"+url);
//                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                startActivity(intent);
                Intent intent = new Intent(context, AdvertisingActivity.class);
                intent.putExtra("ad_url", url);
                startActivity(intent);
            }

        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        builder.show();
    }

    @OnClick(R.id.advertising_back)
    public void onViewClicked() {
        finish();
    }
}
