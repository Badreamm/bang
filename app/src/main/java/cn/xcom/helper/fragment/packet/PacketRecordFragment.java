package cn.xcom.helper.fragment.packet;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import cn.xcom.helper.R;
import cn.xcom.helper.adapter.PacketRecordAdapter;
import cn.xcom.helper.view.DividerItemDecoration;

/**
 * Created by hzh on 2017/6/4.
 * 红包记录
 */

public class PacketRecordFragment extends Fragment {
    public static final int SENDOUT = 11;//发的红包
    public static final int INCOME = 22;//接受的红包

    private int recordType;
    private XRecyclerView mRecyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recordType = getArguments().getInt("type");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_packet_record, container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mRecyclerView = (XRecyclerView) getView().findViewById(R.id.rv_record);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {

            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                mRecyclerView.loadMoreComplete();
            }
        });
        PacketRecordAdapter adapter = new PacketRecordAdapter(getContext(),recordType);
        mRecyclerView.setAdapter(adapter);
    }

    public static PacketRecordFragment newInstance(int type) {
        PacketRecordFragment packetRecordFragment = new PacketRecordFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        packetRecordFragment.setArguments(bundle);
        return packetRecordFragment;
    }

}
