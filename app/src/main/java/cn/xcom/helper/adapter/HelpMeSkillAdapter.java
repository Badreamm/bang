package cn.xcom.helper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import cn.xcom.helper.R;
import cn.xcom.helper.bean.SkillTagInfo;

/**
 * Created by zhuchongkun on 16/6/23.
 */
public class HelpMeSkillAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<SkillTagInfo> mSkillTagInfos;
    public HelpMeSkillAdapter(Context context, ArrayList<SkillTagInfo> skillTagInfos) {
        this.mContext=context;
        if (skillTagInfos==null)
            skillTagInfos=new ArrayList<SkillTagInfo>();
        this.mSkillTagInfos=skillTagInfos;
    }

    @Override
    public int getCount() {
        return mSkillTagInfos.size();
    }

    @Override
    public SkillTagInfo getItem(int position) {
        return mSkillTagInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView==null){
            viewHolder=new ViewHolder();
            convertView= LayoutInflater.from(mContext).inflate(R.layout.item_help_me_skill_tag,null);
            viewHolder.tv_tag= (TextView) convertView.findViewById(R.id.tv_item_help_me_skill_tag);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_tag.setText(mSkillTagInfos.get(position).getSkill_name());
        return convertView;
    }
    private static class ViewHolder{
        TextView tv_tag;
    }
}
