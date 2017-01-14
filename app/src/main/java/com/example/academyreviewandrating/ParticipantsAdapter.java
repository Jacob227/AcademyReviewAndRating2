package com.example.academyreviewandrating;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by יעקב on 09/01/2017.
 */

public class ParticipantsAdapter extends ArrayAdapter<String> {

    private  Activity context;
    private  ArrayList<String> itemname;
    private  Boolean[] imgid;


    private int setAllView;

    public ParticipantsAdapter(Activity context, ArrayList<String> itemname, Boolean[] imgid) {
        super(context, R.layout.mlist_view_course, itemname);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.itemname=itemname;
        this.imgid=imgid;
        this.setAllView = imgid.length;
    }
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.mlsit_view_participance, null,true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.item_view_part);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon_view_part);
        Log.d("In adapter",String.valueOf(position));
        txtTitle.setText(itemname.get(position));
        if (imgid[position]) {
            imageView.setImageResource(R.drawable.v_icon);
        } else {
            imageView.setImageResource(R.drawable.x_icon);
        }
        return rowView;
    }

    public void setBool(Boolean[] iBoolItem){
        imgid = new Boolean[iBoolItem.length];
        System.arraycopy(iBoolItem, 0, imgid, 0, imgid.length);
    }

    /*
    public void setItemnameAndBool(String[] items, Boolean[] iBoolItem) {
        itemname = new String[items.length];
        imgid = new Boolean[iBoolItem.length];
        System.arraycopy(items, 0, itemname, 0, itemname.length);
        System.arraycopy(iBoolItem, 0, imgid, 0, imgid.length);
        setAllView = imgid.length;
    } */

}