package com.example.eugein.cmc_insights;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.eugein.cmc_insights.Adapter.MenuAdapter;

/**
 * Created by Eugein on 4/5/18.
 */

public class HeaderFragement extends Fragment {

    private Activity activity;
    private DrawerLayout drawerLayout;
    private ListView lv_Menu;
    private RelativeLayout left_slider;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.header_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);

        activity = getActivity();

        setUpHeaderView();
    }

    private void setUpHeaderView() {

        drawerLayout =activity.findViewById(R.id.drawer_layout);
        lv_Menu =activity.findViewById(R.id.lv_Menu);

        left_slider = (RelativeLayout) activity.findViewById(R.id.left_slider);
        left_slider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        MenuAdapter adapter = new MenuAdapter(activity);
        lv_Menu.setAdapter(adapter);


        lv_Menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 5:
                       startActivity(new Intent(activity,RegisterActivity.class));
                        break;
                    case 4:
                        startActivity(new Intent(activity,CMConsultingActivity.class));
                        break;
                    default:
                        Intent intentMain=new Intent(activity,PostCategoriesActivity.class);
                        intentMain.putExtra("index",i);
                        startActivity(intentMain);
                }
            }
        });
    }
}
