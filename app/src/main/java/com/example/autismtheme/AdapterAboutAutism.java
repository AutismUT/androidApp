package com.example.autismtheme;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by sajad on 8/11/16.
 */
public class AdapterAboutAutism extends BaseExpandableListAdapter {

    private Context context;
    ArrayList<ItemAboutAutism> items;


    private class Holder {
        TextView textViewAnswer, textViewQuestion;
        ImageView imageViewGroupArrow;
    }

    public AdapterAboutAutism(Context context, ArrayList<ItemAboutAutism> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return items.get(groupPosition).answer;

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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_child, parent, false);

            holder.textViewAnswer = (TextView) convertView.findViewById(R.id.text_view_answer);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        holder.textViewAnswer.setText(items.get(groupPosition).answer);

        return convertView;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return items.get(groupPosition).question;
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

            holder.textViewQuestion = (TextView) convertView.findViewById(R.id.text_view_group);
            holder.imageViewGroupArrow = (ImageView) convertView.findViewById(R.id.image_view_arrow_group);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        holder.textViewQuestion.setText(items.get(groupPosition).question);
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



}
