package com.example.eugein.cmc_insights.Adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.eugein.cmc_insights.Model.Post;
import com.example.eugein.cmc_insights.R;
import com.example.eugein.cmc_insights.Util.FontUtility;

import java.util.ArrayList;

/**
 * Created by Eugein on 4/3/18.
 */

public class PostListAdapter extends BaseAdapter {
    private ArrayList<Post>postArrayList;
    private Context context;
    private LayoutInflater inflater;

    public PostListAdapter(ArrayList<Post> postArrayList, Context context) {
        this.postArrayList = postArrayList;
        this.context = context;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {

        return postArrayList.size();
    }

    @Override
    public Post getItem(int i) {
        return postArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View mView;
        ImageView iv_cat_item;
        TextView tv_title_cat_item;

        mView=view;
        if (mView==null){
            mView=inflater.inflate(R.layout.list_item_post_list,viewGroup,false);
            mView.setTag(R.id.iv_cat_item,mView.findViewById(R.id.iv_cat_item));

            mView.setTag(R.id.tv_title_cat_item,mView.findViewById(R.id.tv_title_cat_item));
        }
        tv_title_cat_item= (TextView) mView.getTag(R.id.tv_title_cat_item);
        iv_cat_item= (ImageView) mView.getTag(R.id.iv_cat_item);



        tv_title_cat_item.setTypeface(FontUtility.setFontFace(mView.getContext(),FontUtility.MON_BOLD));
        Post post=postArrayList.get(i);

        tv_title_cat_item.setText(post.getTitle());
        Glide.with(mView.getContext())
                .load(post.getImage())
                .into(iv_cat_item);

        return mView;
    }
}
