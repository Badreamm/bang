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
public class IncomeRecordsAdapter extends RecyclerView.Adapter<IncomeRecordsAdapter.ViewHolder> {
    private JSONArray jsonArray;
    public IncomeRecordsAdapter(JSONArray jsonArray){
        this.jsonArray = jsonArray;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_income_records,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {

            JSONObject jsonObject = jsonArray.getJSONObject(position);
            holder.tv_money.setText(jsonObject.getString("money"));

            Date date = new Date();
            date.setTime(Long.parseLong(jsonObject.getString("time"))* 1000);
            holder.tv_time.setText(new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss").format(date));

//            holder.tv_balance.setText("余额:"+jsonObject.getString("balance"));
            holder.tv_type.setText(jsonObject.getString("info"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return jsonArray.length();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_type,tv_time,tv_money,tv_balance;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_type= (TextView) itemView.findViewById(R.id.tv_item_income_records_type);
            tv_time= (TextView) itemView.findViewById(R.id.tv_item_income_records_time);
            tv_money= (TextView) itemView.findViewById(R.id.tv_item_income_records_money);
            tv_balance= (TextView) itemView.findViewById(R.id.tv_item_income_records_balance);
        }
    }
}
