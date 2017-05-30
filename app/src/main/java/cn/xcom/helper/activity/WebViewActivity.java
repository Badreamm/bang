package cn.xcom.helper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import cn.xcom.helper.R;

/**
 * Created by hzh on 16/11/3.
 */

public class WebViewActivity extends BaseActivity {
    private TextView titleTv;
    private WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        titleTv = (TextView) findViewById(R.id.title);
        webView = (WebView) findViewById(R.id.web_view);
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String url = intent.getStringExtra("url");

        titleTv.setText(title);
        webView.loadUrl(url);
        findViewById(R.id.rl_help_me_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.destroy();
    }
}
