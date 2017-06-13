package cn.xcom.helper.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.alipay.share.sdk.openapi.APAPIFactory;
import com.alipay.share.sdk.openapi.APMediaMessage;
import com.alipay.share.sdk.openapi.APTextObject;
import com.alipay.share.sdk.openapi.APWebPageObject;
import com.alipay.share.sdk.openapi.IAPApi;
import com.alipay.share.sdk.openapi.SendMessageToZFB;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnDismissListener;
import com.bigkoo.alertview.OnItemClickListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.xcom.helper.HelperApplication;
import cn.xcom.helper.R;
import cn.xcom.helper.WXpay.Constants;
import cn.xcom.helper.activity.ChatActivity;
import cn.xcom.helper.activity.ConvenienceActivity;
import cn.xcom.helper.activity.DetailAuthenticatinActivity;
import cn.xcom.helper.activity.PacketActivity;
import cn.xcom.helper.activity.PacketDetailActivity;
import cn.xcom.helper.activity.SaleDetailActivity;
import cn.xcom.helper.activity.SpaceImageDetailActivity;
import cn.xcom.helper.activity.SpaceVideoDetialActivity;
import cn.xcom.helper.bean.AuthenticationList;
import cn.xcom.helper.bean.Convenience;
import cn.xcom.helper.bean.Packet;
import cn.xcom.helper.bean.UserInfo;
import cn.xcom.helper.constant.HelperConstant;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.record.SoundView;
import cn.xcom.helper.utils.MyImageLoader;
import cn.xcom.helper.utils.NoScrollGridView;
import cn.xcom.helper.utils.RoundImageView;
import cn.xcom.helper.utils.SingleVolleyRequest;
import cn.xcom.helper.utils.StringPostRequest;
import cn.xcom.helper.utils.TimeUtils;
import cn.xcom.helper.utils.ToastUtil;
import cn.xcom.helper.utils.ToastUtils;
import cn.xcom.helper.view.MapBottomPopWindow;
import cn.xcom.helper.view.SharePopupWindow;
import cn.xcom.helper.view.TextViewExpandableAnimation;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

import static com.tencent.connect.share.QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT;

/**
 * Created by Administrator on 2016/9/25 0025.
 */
public class ConvenienceAdapter extends RecyclerView.Adapter<ConvenienceAdapter.ViewHolder> {
    private List<Convenience> list;
    private List<String> addList;
    private Context context;
    private ImageView imageView;
    private ListGridview listGridview;
    private UserInfo userInfo;
    private Map<Integer, Boolean> states;
    private String videoPic;
    List<AuthenticationList> authenticationLists;
    SharePopupWindow takePhotoPopWin;
    private int flag = 2, wxflag = 1;
    IWXAPI msgApi;
    private Tencent mTencent;
    private BaseUiListener listener;
    private String shaerText;
    public ConvenienceAdapter(List<Convenience> list, Context context) {
        this.list = list;
        this.context = context;
        userInfo = new UserInfo(context);
        userInfo.readData(context);
        states = new HashMap<>();
        msgApi = WXAPIFactory.createWXAPI(context, Constants.APP_ID, false);
        msgApi.registerApp(Constants.APP_ID);
        mTencent = Tencent.createInstance("1105802480", context.getApplicationContext());
        listener = new BaseUiListener();

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.convenience_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Convenience convenience = list.get(position);
        states.put(position, true);
        MyImageLoader.display(NetConstant.NET_DISPLAY_IMG + convenience.getPhoto(), holder.convenience_photo);
        holder.convenience_name.setText(convenience.getName());
        holder.convenience_time.setText(TimeUtils.getDateToString(convenience.getCreate_time() * 1000));
        holder.contentEtv.setText(convenience.getContent());
        holder.contentEtv.setOnStateChangeListener(new TextViewExpandableAnimation.OnStateChangeListener() {
            @Override
            public void onStateChange(boolean isShrink) {
                states.put(position, isShrink);
            }
        });
        holder.convenience_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailAuthenticatinActivity.class);
                intent.putExtra("haveid", true);
                intent.putExtra("userid", convenience.getUserid());
                context.startActivity(intent);
            }
        });
//        holder.tvExpand.setText(mLists.get(position).getText());
        //important! reset its state as where it left
        holder.contentEtv.resetState(states.get(position));
        addList = new ArrayList<>();
        authenticationLists = new ArrayList<>();
        addList.clear();
        if (convenience.getPic().size() > 0) {
            for (int i = 0; i < convenience.getPic().size(); i++) {
                addList.add(NetConstant.NET_DISPLAY_IMG + convenience.getPic().get(i).getPictureurl());
            }
        }

        listGridview = new ListGridview(context, addList, list, position);
        holder.noScrollGridView.setAdapter(listGridview);
        holder.noScrollGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!convenience.getVideo().equals("")) {
                    if (position == convenience.getPic().size() - 1) {
                        String videoUrl = NetConstant.NET_DISPLAY_IMG + convenience.getVideo();
                        Intent intent = new Intent(context, SpaceVideoDetialActivity.class);
                        intent.putExtra("videoUrl", videoUrl);
                        context.startActivity(intent);

                    } else {
                        addList.clear();
                        if (convenience.getPic().size() > 0) {
                            for (int i = 0; i < convenience.getPic().size(); i++) {
                                addList.add(NetConstant.NET_DISPLAY_IMG + convenience.getPic().get(i).getPictureurl());
                            }
                        }
                        Intent intent = new Intent(context, SpaceImageDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("list111", (Serializable) addList);
                        intent.putExtra("position", position);
                        intent.putExtras(bundle);
                        int[] location = new int[2];
                        intent.putExtra("locationX", location[1]);//必须
                        intent.putExtra("locationY", location[0]);//必须
                        intent.putExtra("width", view.getWidth());//必须
                        intent.putExtra("height", view.getHeight());//必须
                        context.startActivity(intent);
                    }
                } else {

                    addList.clear();

                    if (convenience.getPic().size() > 0) {
                        for (int i = 0; i < convenience.getPic().size(); i++) {
                            addList.add(NetConstant.NET_DISPLAY_IMG + convenience.getPic().get(i).getPictureurl());
                        }
                    }
                    Intent intent = new Intent(context, SpaceImageDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("list111", (Serializable) addList);
                    intent.putExtra("position", position);
                    intent.putExtras(bundle);
                    int[] location = new int[2];
                    intent.putExtra("locationX", location[1]);//必须
                    intent.putExtra("locationY", location[0]);//必须
                    intent.putExtra("width", view.getWidth());//必须
                    intent.putExtra("height", view.getHeight());//必须
                    context.startActivity(intent);
                }
            }
        });

        //监听打电话
        holder.convenience_phone.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + list.get(position).getPhone()));
                context.startActivity(intent);
            }
        });
        //聊天
        holder.convenience_message.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("id", convenience.getUserid());
                intent.putExtra("name", convenience.getName());
                context.startActivity(intent);
            }
        });
        //显示删除按钮
        if (convenience.getUserid().

                equals(userInfo.getUserId()))

        {
            holder.iv_jubao.setVisibility(View.GONE);
            holder.iv_shanchu.setVisibility(View.VISIBLE);
            holder.iv_shanchu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertView mAlertView = new AlertView("提示", "你确定删除自己的便民圈？", "取消", new String[]{"确定"}, null, context, AlertView.Style.Alert, new OnItemClickListener() {
                        @Override
                        public void onItemClick(Object o, final int position) {
                            if (position == 0) {
                                String url = NetConstant.DELETE_OWN_POST;
                                StringPostRequest request = new StringPostRequest(url, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String s) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(s);
                                            String status = jsonObject.getString("status");
                                            if (status.equals("success")) {
                                                ToastUtil.showShort(context, "删除成功");
                                                list.remove(position);
                                                notifyDataSetChanged();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError volleyError) {
                                        ToastUtil.Toast(context, "网络错误，请检查");
                                    }
                                });
                                request.putValue("userid", userInfo.getUserId());
                                request.putValue("id", convenience.getMid());
                                SingleVolleyRequest.getInstance(context).addToRequestQueue(request);
                            }

                        }
                    }).setCancelable(true).setOnDismissListener(new OnDismissListener() {
                        @Override
                        public void onDismiss(Object o) {

                        }
                    });
                    mAlertView.show();
                }
            });
        }
        //显示举报按钮
        if (!convenience.getUserid().equals(userInfo.getUserId())) {
            holder.iv_jubao.setVisibility(View.VISIBLE);
            holder.iv_shanchu.setVisibility(View.GONE);
            holder.iv_jubao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertView mAlertView = new AlertView("提示", "你确定举报这条便民圈？", "取消", new String[]{"确定"}, null, context, AlertView.Style.Alert, new OnItemClickListener() {
                        @Override
                        public void onItemClick(Object o, int position) {
                            if (position == 0) {
                                String url = NetConstant.REPORT_OHTHER_POST;
                                StringPostRequest request = new StringPostRequest(url, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String s) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(s);
                                            String status = jsonObject.getString("status");
                                            if (status.equals("success")) {
                                                ToastUtil.showShort(context, "举报成功");
                                                notifyDataSetChanged();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError volleyError) {
                                        ToastUtil.Toast(context, "网络错误，请检查");
                                    }
                                });
                                request.putValue("userid", userInfo.getUserId());

                                request.putValue("refid", convenience.getMid());
                                request.putValue("type", "1");
                                SingleVolleyRequest.getInstance(context).addToRequestQueue(request);
                            }
                        }
                    }).setCancelable(true).setOnDismissListener(new OnDismissListener() {
                        @Override
                        public void onDismiss(Object o) {

                        }
                    });
                    mAlertView.show();
                }
            });
        }

        int soundTime = 0;
        if (!TextUtils.isEmpty(convenience.getSoundtime())) {
            soundTime = Integer.valueOf(convenience.getSoundtime());
        }
        if (TextUtils.isEmpty(convenience.getSound())) {
            holder.soundView.setVisibility(View.GONE);
        } else {
            holder.soundView.setVisibility(View.VISIBLE);
            holder.soundView.init(NetConstant.NET_DISPLAY_IMG + convenience.getSound(), soundTime);
        }

        //判断红包
        if (!TextUtils.isEmpty(convenience.getPicketId())) {
            holder.packetFlag.setVisibility(View.VISIBLE);
            AnimationDrawable animationDrawable = (AnimationDrawable) holder.packetFlag.getDrawable();
            if ("1".equals(convenience.getPicketstate())) {
                animationDrawable.start();
            }else{
                animationDrawable.stop();
            }
        } else {
            holder.packetFlag.setVisibility(View.GONE);
        }
        holder.packetFlag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPacketState(convenience.getPicketId());
            }
        });

        holder.shareIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopFormBottom();
                shaerText = convenience.getContent()+"（要想获取更多信息，请下载51帮App）";
            }
        });
    }

    private void getPacketState(String packetId) {
        String url = NetConstant.GET_PACKET_STATE;
        StringPostRequest request = new StringPostRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String status = jsonObject.getString("status");
                    if (status.equals("success")) {
                        String date = jsonObject.getString("data");
                        Gson gson = new Gson();
                        Packet packet = gson.fromJson(date, Packet.class);
                        String packetState = packet.getState();
                        if ("1".equals(packetState)) {
                            //可以抢
                            Intent intent = new Intent(context, PacketActivity.class);
                            intent.putExtra("packetid", packet.getId());
                            context.startActivity(intent);
                        } else {
                            //直接显示红包信息
                            Intent intent = new Intent(context, PacketDetailActivity.class);
                            intent.putExtra("packet", packet);
                            context.startActivity(intent);

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtil.Toast(context, "网络错误，请检查");
            }
        });
        request.putValue("userid", userInfo.getUserId());
        request.putValue("packetid", packetId);
        SingleVolleyRequest.getInstance(context).addToRequestQueue(request);
    }


    @Override
    public int getItemCount() {
//        return list.size() > 9 ? 9 : list.size();
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private RoundImageView convenience_photo;
        private TextView convenience_name;
        private TextView convenience_time;
        private ImageView convenience_image;
        private ImageView convenience_phone, convenience_message;
        private NoScrollGridView noScrollGridView;
        private ImageView iv_shanchu, iv_jubao;
        private SoundView soundView;
        private TextViewExpandableAnimation contentEtv;
        private ImageView packetFlag;
        private ImageView shareIv;

        public ViewHolder(View itemView) {
            super(itemView);
            convenience_photo = (RoundImageView) itemView.findViewById(R.id.convenience_photo);
            convenience_name = (TextView) itemView.findViewById(R.id.convenience_name);
            convenience_time = (TextView) itemView.findViewById(R.id.convenience_time);
            contentEtv = (TextViewExpandableAnimation) itemView.findViewById(R.id.content_etv);
//            contentEtv = (TextView) itemView.findViewById(R.id.content_etv);
            convenience_message = (ImageView) itemView.findViewById(R.id.convenience_message);
            iv_shanchu = (ImageView) itemView.findViewById(R.id.iv_shanchu);
            iv_jubao = (ImageView) itemView.findViewById(R.id.iv_jubao);
            //  viewHolder.convenience_image = (ImageView) convertView.findViewById(R.id.convenience_image);
            convenience_phone = (ImageView) itemView.findViewById(R.id.convenience_phone);
            noScrollGridView = (NoScrollGridView) itemView.findViewById(R.id.gridview);
            soundView = (SoundView) itemView.findViewById(R.id.sound_view);
            packetFlag = (ImageView) itemView.findViewById(R.id.packet_flag);
            shareIv = (ImageView) itemView.findViewById(R.id.iv_share);
        }

    }

    public void showPopFormBottom() {
        takePhotoPopWin = new SharePopupWindow(context, onClickListener);
        //SharePopupWindow takePhotoPopWin = new SharePopupWindow(this, onClickListener);
        takePhotoPopWin.showAtLocation(HelperApplication.getInstance().getActivities().get(HelperApplication.getInstance().getActivities().size()-1).getCurrentFocus(), Gravity.BOTTOM, 0, 0);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.haoyou:
                    ToastUtils.showToast(context, "微信好友");
                    setting(shaerText);
                    break;
                case R.id.dongtai:
                    ToastUtils.showToast(context, "微信朋友圈");
                    history(shaerText);
                    break;
                case R.id.qq:
                    ToastUtils.showToast(context, "QQ");
                    shareToQQ(shaerText);
                    takePhotoPopWin.dismiss();
                    break;
                case R.id.kongjian:
                    ToastUtils.showToast(context, "QQ空间");
                    shareToQzone(shaerText);
                    takePhotoPopWin.dismiss();
                    break;
                case R.id.zhifubao:
                    ToastUtils.showToast(context, "支付宝");
                    toAlipay(shaerText);
                    takePhotoPopWin.dismiss();
                    break;
            }
        }
    };

    /**
     * 分享到微信好友
     */
    private void setting(String text) {
        //ToastUtils.ToastShort(this, "分享到微信好友");
        wxflag = 0;
        shareWX(text);
        takePhotoPopWin.dismiss();
    }

    /**
     * 分享到微信朋友圈
     */
    private void history(String text) {
        // ToastUtils.ToastShort(this, "分享到微信朋友圈");
        wxflag = 1;
        shareWX(text);
        takePhotoPopWin.dismiss();
    }

    /**
     * 微信
     */
    private void shareWX(String text) {
        WXTextObject textObject = new WXTextObject();
        textObject.text = text;

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = textObject;
        msg.description = text;

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(new Date().getTime());
        req.message = msg;
        req.scene = wxflag == 0 ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
        msgApi.sendReq(req);
    }

    private void shareToQQ(String text) {
        Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, text);
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, text);
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, NetConstant.SHARE_SHOP_H5 + userInfo.getUserId());
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "51帮");
        mTencent.shareToQQ((Activity) context, params, listener);
    }

    private void shareToQzone(String text) {
        Bundle params = new Bundle();
        //分享类型
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, "51帮");//必填
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY,text);//选填
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, NetConstant.SHARE_SHOP_H5 + userInfo.getUserId());//必填
        ArrayList<String> images = new ArrayList<>();
        images.add("http://www.my51bang.com/uploads/ic_logo.png");
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, images);
        mTencent.shareToQzone((Activity) context, params, listener);
    }

    private class BaseUiListener implements IUiListener {
        @Override
        public void onCancel() {
            Toast.makeText(context, "取消分享", Toast.LENGTH_SHORT)
                    .show();
        }

        @Override
        public void onError(UiError uiError) {
            Toast.makeText(context, uiError.errorMessage + "\n" + uiError.errorDetail,
                    Toast.LENGTH_SHORT)
                    .show();
            Log.d("QQshare", uiError.errorMessage + "\n" + uiError.errorDetail);
        }

        @Override
        public void onComplete(Object o) {
//            enableAction(enableActionShareQRCodeActivity.this.action);
        }
    }

    private void toAlipay(String text) {
        //创建工具对象实例，此处的APPID为上文提到的，申请应用生效后，在应用详情页中可以查到的支付宝应用唯一标识
        IAPApi api = APAPIFactory.createZFBApi(context.getApplicationContext(), "2016083001821606", false);
        APTextObject textObject = new APTextObject();
        textObject.text = text;
        //初始化APMediaMessage ，组装分享消息对象
        APMediaMessage mediaMessage = new APMediaMessage();
        mediaMessage.mediaObject = textObject;
        //将分享消息对象包装成请求对象
        SendMessageToZFB.Req req = new SendMessageToZFB.Req();
        req.message = mediaMessage;
        //发送请求
        api.sendReq(req);
    }

}
