package cn.xcom.helper.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import cn.xcom.helper.R;
import cn.xcom.helper.fragment.Authorized.BindAccountAuthorizedFragment;

/**
 * 绑定账户 用户未进行实名认证时，点击发布特卖跳转页
 */

public class BindAccountAuthorizedActivity extends BaseActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_account_authorized);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.fragment_container,new BindAccountAuthorizedFragment());
        transaction.commit();

    }

    public void onBack(View v){
        finish();
    }

}
