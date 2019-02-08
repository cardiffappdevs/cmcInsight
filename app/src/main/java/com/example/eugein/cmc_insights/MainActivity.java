package com.example.eugein.cmc_insights;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eugein.cmc_insights.Model.Post;
import com.example.eugein.cmc_insights.Model.PostCategory;
import com.example.eugein.cmc_insights.Util.CMCInsightsApi;
import com.example.eugein.cmc_insights.Util.CMCInsightsApplication;
import com.example.eugein.cmc_insights.Util.CheckNetwork;
import com.example.eugein.cmc_insights.Util.FontUtility;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private ImageView iv_menu, iv_logo;
    private TextView tv_home_title, tv_home_desc;

    private RelativeLayout rl_focus;


    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);


        setUpView();

    }

    private void setUpView() {
        toolbar = (Toolbar) findViewById(R.id.tool_bar_home);
        iv_menu = toolbar.findViewById(R.id.iv_menu_toolbar);
        iv_logo = (ImageView) findViewById(R.id.iv_logo);

        iv_menu.setVisibility(View.VISIBLE);
        iv_logo.setVisibility(View.VISIBLE);

        tv_home_title = (TextView) findViewById(R.id.tv_home_title);
        tv_home_title.setText("Enter");
        tv_home_desc = (TextView) findViewById(R.id.tv_home_desc);
        tv_home_title.setTypeface(FontUtility.setFontFace(this, FontUtility.MON_BOLD));
        tv_home_desc.setTypeface(FontUtility.setFontFace(this, FontUtility.MON_REGULAR));
        rl_focus = (RelativeLayout) findViewById(R.id.rl_focus);

        rl_focus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, PostCategoriesActivity.class);
                startActivity(intent);


            }
        });
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
            drawerLayout.closeDrawer(Gravity.RIGHT);
        }
        iv_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(Gravity.RIGHT);
            }
        });

    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
            drawerLayout.closeDrawer(Gravity.RIGHT);
        } else {
            super.onBackPressed();
        }
    }


}
