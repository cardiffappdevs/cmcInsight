package com.example.eugein.cmc_insights.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.eugein.cmc_insights.R;
import com.example.eugein.cmc_insights.Util.FontUtility;

import java.util.ArrayList;


/**
 * Created by Eugein on 4/5/18.
 */

public class MenuAdapter extends BaseAdapter {

    private ArrayList<String> menuItems=new ArrayList<>();
    private LayoutInflater mInflater;
    private Context context;

    public MenuAdapter(Context context) {
        this.context = context;
        mInflater=LayoutInflater.from(context);
        menuItems.add("Latest");
        menuItems.add("Blogs");
        menuItems.add("Interviews");
        menuItems.add("White Papers");
        menuItems.add("Connect Media");
        menuItems.add("Register");
    }

    @Override
    public int getCount() {
        return menuItems.size();
    }

    @Override
    public String getItem(int i) {
        return menuItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = view;
        TextView tv_menu_itemitem;

        if (v == null) {
            v = mInflater.inflate(R.layout.list_item_menu, viewGroup, false);
            v.setTag(R.id.tv_menu_item, v.findViewById(R.id.tv_menu_item));
        }

        tv_menu_itemitem = (TextView) v.getTag(R.id.tv_menu_item);
        tv_menu_itemitem.setTypeface(FontUtility.setFontFace(context ,FontUtility.MON_BOLD));

        String item = getItem(i);
        tv_menu_itemitem.setText(item);

        return v;
    }
}
