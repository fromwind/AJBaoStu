package com.udit.aijiabao.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("all")
public abstract class BaseAdapterInject<T> extends BaseAdapter {
    List<T> dataList = new ArrayList<T>();
    public LayoutInflater mInflater;
    public Context mContext;
 
    public BaseAdapterInject(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }
 
    /**
     * 设置数据，以前数据会清空 <功能详细描述>
     * 
     * @param data
     * @see 
     */
    public void setData(List<T> data) {
        if (dataList == null)
            dataList = new ArrayList<T>();
        dataList.clear();
        if (data != null)
            dataList.addAll(data);
        notifyDataSetChanged();
    }
 
    /**
     * 在原始数据上添加新数据 <功能详细描述>
     * 
     * @param data
     * @see 
     */
    public void addData(List<T> data) {
        if (dataList == null)
            dataList = new ArrayList<T>();
        dataList.addAll(data);
        notifyDataSetChanged();
    }
 
    public void removeAllData() {
        dataList.clear();
        notifyDataSetChanged();
    }
 
    @Override
    public int getCount() {
        if (dataList != null)
            return dataList.size();
        return 0;
    }
 
    @Override
    public T getItem(int position) {
        if (dataList != null)
            try {
                return dataList.get(position);
            } catch (Exception g) {
                return null;
            }
        return null;
    }
 
    @Override
    public long getItemId(int position) {
        return position;
    }
 
    public abstract int getConvertViewId(int position);
 
    public abstract ViewHolderInject<T> getNewHolder(int position);
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderInject<T> holder;
        if (convertView == null) {
            convertView = mInflater.inflate(getConvertViewId(position), null);
            holder = getNewHolder(position);
            x.view().inject(holder,convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolderInject<T>) convertView.getTag();
        }
        holder.loadData(getItem(position), position);
        return convertView;
    }
 
}