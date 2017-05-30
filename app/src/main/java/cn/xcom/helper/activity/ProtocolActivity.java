package cn.xcom.helper.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.xcom.helper.HelperApplication;
import cn.xcom.helper.R;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.utils.SingleVolleyRequest;
import cn.xcom.helper.utils.StringPostRequest;
import cn.xcom.helper.utils.ToastUtils;

import static com.baidu.mapapi.BMapManager.getContext;

/**
 * Created by Administrator on 2017/4/17 0017.
 */

public class ProtocolActivity extends Activity {
    @BindView(R.id.rl_i_protocol_back)
    RelativeLayout rlIProtocolBack;
    @BindView(R.id.wv_userr_protocol)
    WebView wvUserrProtocol;
    Context context;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_protocol);
        ButterKnife.bind(this);
        context = this;
        wvUserrProtocol.getSettings().setJavaScriptEnabled(true);
        wvUserrProtocol.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        wvUserrProtocol.loadUrl(NetConstant.USER_PROTOCOL);
        wvUserrProtocol.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }

    @OnClick(R.id.rl_i_protocol_back)
    public void onViewClicked() {
        finish();
    }

}
