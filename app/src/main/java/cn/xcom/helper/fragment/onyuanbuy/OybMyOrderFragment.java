package cn.xcom.helper.fragment.onyuanbuy;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import cn.xcom.helper.R;
import cn.xcom.helper.activity.AddressListActivity;
import cn.xcom.helper.activity.OybMyOrderActivity;
import cn.xcom.helper.activity.OybWinRecordActivity;

/**
 * Created by hzh on 2017/7/10.
 */

public class OybMyOrderFragment extends Fragment implements View.OnClickListener {
    private RelativeLayout buyRecordRl,winRecordRl,addressRl;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_oyb_my_oreder,container,false);
        buyRecordRl = (RelativeLayout) view.findViewById(R.id.buy_record_rl);
        winRecordRl = (RelativeLayout) view.findViewById(R.id.win_record_rl);
        addressRl = (RelativeLayout) view.findViewById(R.id.address_rl);
        buyRecordRl.setOnClickListener(this);
        winRecordRl.setOnClickListener(this);
        addressRl.setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buy_record_rl:
                startActivity(new Intent(getContext(), OybMyOrderActivity.class));
                break;
            case R.id.win_record_rl:
                startActivity(new Intent(getContext(), OybWinRecordActivity.class));
                break;
            case R.id.address_rl:
                startActivity(new Intent(getContext(), AddressListActivity.class));
                break;
        }
    }
}
