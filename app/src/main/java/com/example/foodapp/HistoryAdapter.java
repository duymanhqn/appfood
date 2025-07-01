package com.example.foodapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class HistoryAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private final ArrayList<String> orders;

    public HistoryAdapter(Activity context, ArrayList<String> orders) {
        super(context, R.layout.item_history, orders);
        this.context = context;
        this.orders = orders;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.item_history, null, true);

        TextView txtOrder = rowView.findViewById(R.id.txtOrder);
        txtOrder.setText(orders.get(position));

        return rowView;
    }
}