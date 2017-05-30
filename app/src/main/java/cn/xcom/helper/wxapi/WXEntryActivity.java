package cn.xcom.helper.wxapi;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import cn.xcom.helper.WXpay.Constants;


/**
 * Created by zhuchongkun
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private static final String APP_ID="";
    private IWXAPI iwxapi;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=this;
        iwxapi= WXAPIFactory.createWXAPI(this, Constants.APP_ID,false);
        iwxapi.registerApp(APP_ID);
        iwxapi.handleIntent(getIntent(), this);
    }

    @Override
    public void onReq(BaseReq baseReq) {
        finish();
    }

    @Override
    public void onResp(BaseResp baseResp) {
        finish();
    }
}
