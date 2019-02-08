package com.example.eugein.cmc_insights.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.eugein.cmc_insights.R;

/**
 * Created by AH on 4/17/2018.
 */

public class MyViewHolder extends RecyclerView.ViewHolder {
    public TextView tv_name, tv_date, tv_comment;

    public MyViewHolder(View itemView) {
        super(itemView);
        tv_name = itemView.findViewById(R.id.tv_name);
        tv_date = itemView.findViewById(R.id.tv_time);
        tv_comment = itemView.findViewById(R.id.tv_comment);
    }

    public View getView(){
        return itemView;
    }
}
