package com.fit5046.paindiary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.fit5046.paindiary.R;
import com.fit5046.paindiary.helper.CustomSpinnerItem;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
//reference from internet
//https://www.edureka.co/blog/custom-spinner-in-android
public class CustomSpinnerAdapter extends ArrayAdapter<CustomSpinnerItem> {

    public CustomSpinnerAdapter(@NonNull Context context, ArrayList<CustomSpinnerItem> customList) {
        super(context, 0, customList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_spinner, parent, false);
        }
        CustomSpinnerItem item = getItem(position);
        ImageView spinnerIV = convertView.findViewById(R.id.ivSpinnerLayout);
        TextView spinnerTV = convertView.findViewById(R.id.tvSpinnerLayout);
        if (item != null) {
            spinnerIV.setImageResource(item.getSpinnerItemImage());
            spinnerTV.setText(item.getSpinnerItemName());
        }
        return convertView;
    }

    @Override
    public View getDropDownView(int position, @androidx.annotation.Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_dropdown, parent, false);
        }
        CustomSpinnerItem item = getItem(position);
        ImageView dropDownIV = convertView.findViewById(R.id.ivDropDownLayout);
        TextView dropDownTV = convertView.findViewById(R.id.tvDropDownLayout);
        if (item != null) {
            dropDownIV.setImageResource(item.getSpinnerItemImage());
            dropDownTV.setText(item.getSpinnerItemName());
        }
        return convertView;
    }
}