package com.example.autismtheme;

/**
 * Created by sajad on 8/11/16.
 */

import android.content.Context;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;

import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by sajad on 8/11/16.
 */
public class AdapterHelp extends BaseExpandableListAdapter {

    private Context context;
    ArrayList<HelpItem> items;

    public AdapterHelp(Context context, ArrayList<HelpItem> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return items.get(childPosition).description;

    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        Holder holder;

        if (convertView == null) {
            holder = new Holder();
            convertView = LayoutInflater.from(context).inflate(R.layout.child_item_help, parent, false);

            holder.textViewDescription = (TextView) convertView.findViewById(R.id.text_view_description);
            holder.imageViewDescription = (ImageView)convertView.findViewById(R.id.image_view_description);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        holder.textViewDescription.setText(context.getResources().getString(items.get(groupPosition).description));
        if(items.get(groupPosition).image!=-1)
           Glide.with(context).load(items.get(groupPosition).image).into(holder.imageViewDescription);

        return convertView;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return items.get(groupPosition).titleText;
    }

    @Override
    public int getGroupCount() {
        return items.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        Holder holder;

        if (convertView == null) {
            holder = new Holder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_group, parent, false);

            holder.textViewTitle = (TextView) convertView.findViewById(R.id.text_view_group);
            holder.imageViewGroupArrow = (ImageView) convertView.findViewById(R.id.image_view_arrow_group);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        holder.textViewTitle.setText(items.get(groupPosition).titleText);
        if (isExpanded) {
            holder.imageViewGroupArrow.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_keyboard_arrow_down_black_24dp));
        } else {
            holder.imageViewGroupArrow.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_keyboard_arrow_left_black_24dp));
        }

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    private class Holder {
        TextView textViewDescription, textViewTitle;
        ImageView imageViewGroupArrow;
        ImageView imageViewDescription;
    }

}

