package com.example.helpdesk;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

// Thuộc dạng Adapter // Hiển thị Adapter của từng item trong Listvieww KTV Activity

public class WorksAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<Works> worksList;

    public WorksAdapter(Context context, int layout, List<Works> worksList) {
        this.context = context;
        this.layout = layout;
        this.worksList = worksList;
    }

    @Override
    public int getCount() {
        return worksList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(layout,null);

        //Ánh xạ View
        TextView txtWork_name = (TextView) view.findViewById(R.id.txtWork_name);
        TextView txtDeadline = (TextView) view.findViewById(R.id.txtDeadline);
        ImageView imgStatus = (ImageView) view.findViewById(R.id.imgStatus);

        //Gán giá trị
        Works works = worksList.get(i);

        txtWork_name.setText(works.getWork_name());
        txtDeadline.setText(works.getDeadline());
        imgStatus.setImageResource(works.getStatus());

        return view;
    }
}
