package com.example.helpdesk;

// Adapter hiển thị hình ảnh của Problem trong NV_problemsActivity.

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class HinhAnhAdapter extends BaseAdapter {
    Context myContext;
    int myLayout;

    // Khai báo 4 cấu trúc của Adapter
    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    private class ViewHolder {
            ImageView imgHinh;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowview = view;
        ViewHolder holder = new ViewHolder();

        if (rowview == null)
        {
            rowview = inflater.inflate(myLayout,null);
            holder.imgHinh = (ImageView) rowview.findViewById(R.id.imgproblem);
            rowview.setTag(holder);
        } else {
            holder = (ViewHolder)rowview.getTag();
        }

        Picasso.with(myContext).load("https://cdn.journaldev.com/wp-content/uploads/2016/11/android-image-picker-project-structure.png").into(holder.imgHinh);

        return null;
    }
}
