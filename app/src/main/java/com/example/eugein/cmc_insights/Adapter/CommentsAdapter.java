package com.example.eugein.cmc_insights.Adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eugein.cmc_insights.Model.PostComment;
import com.example.eugein.cmc_insights.R;
import com.example.eugein.cmc_insights.Util.FontUtility;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.R.id.list;

/**
 * Created by AH on 4/17/2018.
 */

public class CommentsAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private ArrayList<PostComment> postComments;

    public CommentsAdapter(ArrayList<PostComment> postComments) {
        this.postComments = postComments;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_comment, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        PostComment comment = postComments.get(position);
        holder.tv_name.setText(comment.getName());

        holder.tv_comment.setText(comment.getComment());
        holder.tv_name.setTypeface(FontUtility.setFontFace(holder.getView().getContext(), FontUtility.MON_REGULAR));
        holder.tv_date.setTypeface(FontUtility.setFontFace(holder.getView().getContext(), FontUtility.MON_REGULAR));
        holder.tv_comment.setTypeface(FontUtility.setFontFace(holder.getView().getContext(), FontUtility.MON_REGULAR));
        String date = comment.getDate().substring(0, 10);
        String time = comment.getDate().substring(11, 16);
        Log.e("date", date);
        Log.e("time", time);

        SimpleDateFormat fromDate = new SimpleDateFormat("yyyy-mm-dd", Locale.US);
        SimpleDateFormat toDate = new SimpleDateFormat("MMM dd,yyyy", Locale.US);

       String newd=getFormattedDate(date,"yyyy-MM-dd","MMMM dd,yyyy");
        String newt=getFormattedDate(time,"hh:mm","hh:mma");

        Log.e("newd", newd);
        Log.e("newt", newt);
        holder.tv_date.setText(newd+" at "+newt);
    }

    @Override
    public int getItemCount() {
        return postComments.size();
    }
    public String getFormattedDate(String dateStr, String strReadFormat, String strWriteFormat) {

        String formattedDate = dateStr;

        DateFormat readFormat = new SimpleDateFormat(strReadFormat, Locale.getDefault());
        DateFormat writeFormat = new SimpleDateFormat(strWriteFormat, Locale.getDefault());

        Date date = null;

        try {
            date = readFormat.parse(dateStr);
        } catch (ParseException e) {
        }

        if (date != null) {
            formattedDate = writeFormat.format(date);
        }

        return formattedDate;
    }

}
