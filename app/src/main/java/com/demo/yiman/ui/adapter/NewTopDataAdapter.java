package com.demo.yiman.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.demo.yiman.R;
import com.demo.yiman.bean.NewTopBean;
import com.demo.yiman.utils.ImageLoaderUtil;

import java.util.List;


public class NewTopDataAdapter extends BaseAdapter {
    private Context mContext;
    private List<NewTopBean> mList;
    public NewTopDataAdapter(Context context, List<NewTopBean> list){
        this.mContext=context;
        this.mList = list;
    }
    @Override
    public int getCount() {
        Log.e("有多少",mList.size()+"");
        return mList!=null?mList.size():0;
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView==null){
            convertView = View.inflate(mContext, R.layout.item_new_top,null);
            holder = new ViewHolder();
            holder.imageView = convertView.findViewById(R.id.iv_new_top);
            holder.textView = convertView.findViewById(R.id.tv_new_name);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        NewTopBean newTopBean = mList.get(position);
        holder.textView.setText(newTopBean.getTitle());
        ImageLoaderUtil.LoadImage(mContext,newTopBean.getImageUrl(),holder.imageView);
        return convertView;
    }
    public class ViewHolder{
        ImageView imageView;
        TextView textView;
    }
}