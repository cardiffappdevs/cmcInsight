package com.example.eugein.cmc_insights;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eugein.cmc_insights.Util.FontUtility;


public class CMConsultingActivity extends AppCompatActivity {

    private ImageView iv_menu,iv_logo;
    private TextView tv_cmc_title,tv_cmc_details;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_cmconsulting);

        setUpView();
    }

    private void setUpView() {
        iv_menu= (ImageView) findViewById(R.id.iv_menu_toolbar);
        iv_logo= (ImageView) findViewById(R.id.iv_logo);
        iv_logo.setVisibility(View.VISIBLE);
        tv_cmc_title= (TextView) findViewById(R.id.tv_cmc_title);
        tv_cmc_details= (TextView) findViewById(R.id.tv_cmc_details);

        tv_cmc_title.setTypeface(FontUtility.setFontFace(this, FontUtility.MON_BOLD));
        tv_cmc_details.setTypeface(FontUtility.setFontFace(this, FontUtility.MON_REGULAR));

        iv_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CMConsultingActivity.this,MainActivity.class));
            }
        });
        drawerLayout= (DrawerLayout) findViewById(R.id.drawer_layout);
        iv_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                drawerLayout.openDrawer(Gravity.RIGHT);
            }
        });
    }


}
