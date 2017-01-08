package com.example.academyreviewandrating;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by יעקב on 03/01/2017.
 */

public class CustomListAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private final String[] itemname;
    private final String[] itemDesc;
    private final Integer[] imgid;

    public CustomListAdapter(Activity context, String[] itemname, Integer[] imgid, String[] itemDesc) {
        super(context, R.layout.mlist_view_course, itemname);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.itemname=itemname;
        this.imgid=imgid;
        this.itemDesc = itemDesc;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.mlist_view_course, null,true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.item_view);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon_view);
        TextView extratxt = (TextView) rowView.findViewById(R.id.my_text_view);

        txtTitle.setText(itemname[position]);
        imageView.setImageResource(imgid[position]);
        extratxt.setText(itemDesc[position]);
        return rowView;
    }

    public void SetCahngeInItemList(String newItem, int pos1, String newDesc){
        itemname[pos1] = newItem;
        itemDesc[pos1] = newDesc;
    }

}
