package com.example.eugein.cmc_insights.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.eugein.cmc_insights.Model.PostComment;
import com.example.eugein.cmc_insights.R;
import com.example.eugein.cmc_insights.Util.FontUtility;

import java.util.ArrayList;

/**
 * Created by AH on 4/17/2018.
 */

public class CommentsListAdapter extends BaseAdapter {
    ArrayList<PostComment>postComments;
    Context context;
    LayoutInflater inflater;

    public CommentsListAdapter(ArrayList<PostComment> postComments, Context context) {
        this.postComments = postComments;
        this.context = context;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return postComments.size();
    }

    @Override
    public PostComment getItem(int position) {
        return postComments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View mView;
        mView=convertView;
        TextView tv_name,tv_time,tv_comment;
        if (mView==null){
            mView=inflater.inflate(R.layout.list_item_comment,parent,false);
            mView.setTag(R.id.tv_name,mView.findViewById(R.id.tv_name));
            mView.setTag(R.id.tv_time,mView.findViewById(R.id.tv_time));
            mView.setTag(R.id.tv_comment,mView.findViewById(R.id.tv_comment));
        }
        tv_name= (TextView) mView.getTag(R.id.tv_name);
        tv_time= (TextView) mView.getTag(R.id.tv_time);
        tv_comment= (TextView) mView.getTag(R.id.tv_comment);

        tv_name.setTypeface(FontUtility.setFontFace(mView.getContext(),FontUtility.MON_REGULAR));
        tv_time.setTypeface(FontUtility.setFontFace(mView.getContext(),FontUtility.MON_REGULAR));
        tv_comment.setTypeface(FontUtility.setFontFace(mView.getContext(),FontUtility.LORA_REGULAR));

        PostComment comment=postComments.get(position);
        tv_name.setText(comment.getName());
        tv_time.setText(comment.getDate());
        tv_comment.setText(comment.getComment());

        return mView;
    }
}
