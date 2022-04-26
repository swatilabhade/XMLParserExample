package com.testing.xmlparserexample.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.testing.xmlparserexample.R;
import com.testing.xmlparserexample.helper.ImageLoader;
import com.testing.xmlparserexample.model.ItemDetails;

import java.util.List;

public class ItemListAdapterClass extends ArrayAdapter<ItemDetails> {
    ImageLoader imgLoader;

    public ItemListAdapterClass(Context context, int resource, List<ItemDetails> objects) {
        super(context, resource, objects);
        imgLoader = new ImageLoader(context);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        LinearLayout row = (LinearLayout) convertView;
        if (null == row) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = (LinearLayout) inflater.inflate(R.layout.list_item, parent,false);
        }
        ImageView iconImg = (ImageView) row.findViewById(R.id.iconImg);
        TextView txtName = (TextView) row.findViewById(R.id.txtName);
        TextView txtid = (TextView) row.findViewById(R.id.txtid);
        try {
            imgLoader.DisplayImage(getItem(position).getLogo(), R.drawable.image_placeholder, iconImg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        txtName.setText(getItem(position).getStationName());
        txtid.setText(String.valueOf(getItem(position).getStationId()));

        return row;
    }
}
