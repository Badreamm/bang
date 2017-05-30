package cn.xcom.helper.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.xcom.helper.R;

/**
 * Created by zhuchongkun on 16/6/16.
 */
public class PresentRecordAdapter extends RecyclerView.Adapter<PresentRecordAdapter.ViewHolder> {
    private JSONArray jsonArray;

    public PresentRecordAdapter(JSONArray jsonArray){
        this.jsonArray = jsonArray;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_present_record,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {
            JSONObject jo = jsonArray.getJSONObject(position);
            holder.tv_money.setText(jo.getString("money"));
            Date date = new Date();
            date.setTime(Long.parseLong(jo.getString("time"))* 1000);
            holder.tv_time.setText(new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss").format(date));
            int state = jo.getInt("state");//0待审核，1审核通过，-1被拒绝
            if(state == 0){
                holder.tv_state.setText("待审核");
            }else if(state ==1){
                holder.tv_state.setText("审核通过");
            }else if(state == -1){
                holder.tv_state.setText("被拒绝");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return jsonArray.length();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_type,tv_time,tv_money,tv_account,tv_state;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_type= (TextView) itemView.findViewById(R.id.tv_item_present_record_type);
            tv_time= (TextView) itemView.findViewById(R.id.tv_item_present_record_time);
            tv_money= (TextView) itemView.findViewById(R.id.tv_item_present_record_money);
            tv_state = (TextView) itemView.findViewById(R.id.tv_item_present_record_state);
        }
    }
}
