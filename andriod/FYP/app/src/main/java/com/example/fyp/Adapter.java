package com.example.fyp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

public class Adapter extends BaseAdapter {
    private Context mContext;
    private List<HashMap<String, Object>> dataList;

    public Adapter(Context context, List<HashMap<String, Object>> dataList) {
        this.mContext = context;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public HashMap<String, Object> getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.myxml, null);
            holder.idName = (TextView) convertView.findViewById(R.id.id_name);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.text_room = (TextView) convertView.findViewById(R.id.bed_room_number);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.idName.setText((String) getItem(position).get("id_name"));
        holder.name.setText((String) getItem(position).get("name"));
        holder.text_room.setText((String) getItem(position).get("bed_room_number"));
        return convertView;
    }

    final class ViewHolder {
        TextView idName;
        TextView name;
        TextView text_room;
    }
}
