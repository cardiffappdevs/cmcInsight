package com.example.eugein.cmc_insights;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eugein.cmc_insights.Util.CMCInsightsApi;
import com.example.eugein.cmc_insights.Util.CMCInsightsApplication;
import com.example.eugein.cmc_insights.Util.CheckNetwork;
import com.example.eugein.cmc_insights.Util.FontUtility;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RegisterActivity extends AppCompatActivity {
    private TextView tv_h1_reg, tv_h2_reg, tv_bottom_reg, tv_agree;
    private EditText et_name_reg, et_title_reg, et_company_reg, et_email_reg;
    private Button btn_send;
    private ImageView iv_menu, iv_logo;
    JsonObject registerJob;
    private String name, title, company, email;
    private DrawerLayout drawerLayout;
    private CheckBox chkbox_agree;
    private boolean acceptedTerm = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);

        setupView();
    }

    private void setupView() {
        tv_h1_reg = (TextView) findViewById(R.id.tv_h1_reg);
        tv_h2_reg = (TextView) findViewById(R.id.tv_h2_reg);
//        tv_bottom_reg = (TextView) findViewById(R.id.tv_bottom_reg);
        tv_agree = (TextView) findViewById(R.id.tv_agree);
        tv_h1_reg.setTypeface(FontUtility.setFontFace(this, FontUtility.MON_BOLD));
        tv_h2_reg.setTypeface(FontUtility.setFontFace(this, FontUtility.MON_REGULAR));
        tv_agree.setTypeface(FontUtility.setFontFace(this, FontUtility.MON_REGULAR));
        et_name_reg = (EditText) findViewById(R.id.et_name_reg);
        et_title_reg = (EditText) findViewById(R.id.et_title_reg);
        et_company_reg = (EditText) findViewById(R.id.et_company_reg);
        et_email_reg = (EditText) findViewById(R.id.et_email_reg);

        et_name_reg.setTypeface(FontUtility.setFontFace(this, FontUtility.MON_REGULAR));
        et_title_reg.setTypeface(FontUtility.setFontFace(this, FontUtility.MON_REGULAR));
        et_company_reg.setTypeface(FontUtility.setFontFace(this, FontUtility.MON_REGULAR));
        et_email_reg.setTypeface(FontUtility.setFontFace(this, FontUtility.MON_REGULAR));

        try {
            // https://github.com/android/platform_frameworks_base/blob/kitkat-release/core/java/android/widget/TextView.java#L562-564
            Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
            f.setAccessible(true);
            f.set(et_name_reg, R.drawable.cursor);
            f.set(et_title_reg, R.drawable.cursor);
            f.set(et_company_reg, R.drawable.cursor);
            f.set(et_email_reg, R.drawable.cursor);
        } catch (Exception ignored) {
        }

        iv_menu = (ImageView) findViewById(R.id.iv_menu_toolbar);
        iv_logo = (ImageView) findViewById(R.id.iv_logo);
        iv_logo.setVisibility(View.VISIBLE);
        iv_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            }
        });
        chkbox_agree = (CheckBox) findViewById(R.id.chkbox_agree);
        chkbox_agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chkbox_agree.isChecked()) {
                    acceptedTerm = true;
//                    chkbox_agree.requestFocus();
//                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);


                } else {

                    acceptedTerm = false;
//                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.SHOW_IMPLICIT);SHOW_IMPLICIT

                }
//                Toast.makeText(RegisterActivity.this, String.valueOf(acceptedTerm), Toast.LENGTH_SHORT).show();
            }
        });

        btn_send = (Button) findViewById(R.id.btn_send);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CheckNetwork.isInternetAvailable(RegisterActivity.this)) {
                    String name, title, company, email;
                    name = et_name_reg.getText().toString();
                    title = et_title_reg.getText().toString();
                    company = et_company_reg.getText().toString();
                    email = et_email_reg.getText().toString();
                    if (TextUtils.isEmpty(name)){
                        showAlert("Please enter your name");
                    }
                    if(TextUtils.isEmpty(title)){
                        showAlert("Please enter your title");
                    }
                    if(TextUtils.isEmpty(company) ){
                        showAlert("Please enter your company");
                    }
                    if(TextUtils.isEmpty(email) || !isValidEmail(email)) {
                        showAlert("Please enter valid email");
                    }
                    if (acceptedTerm == false){
                        showAlert(getResources().getString(R.string.accept_false));
                    }
                    if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(title) && !TextUtils.isEmpty(company) && !TextUtils.isEmpty(email) && isValidEmail(email)) {
                        if (acceptedTerm == true) {
                            register(name, title, company, email);
                            chkbox_agree.setChecked(false);
                        }
                    }

                } else {

                    Toast.makeText(RegisterActivity.this, "   No Internet Connection. Make sure\n      wifi or mobile data is turned on.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        iv_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                drawerLayout.openDrawer(Gravity.RIGHT);
            }
        });
    }


    private void register(String name, String title, String company, String email) {
        CMCInsightsApi api = CMCInsightsApplication.getCmcApi(RegisterActivity.this);
        registerJob = new JsonObject();
        registerJob.addProperty("name", name);
        registerJob.addProperty("title", title);
        registerJob.addProperty("company", company);
        registerJob.addProperty("email", email);

        Call<JsonObject> call = api.userRegistration(registerJob);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    JSONObject tmp = null;
                    try {
                        tmp = new JSONObject(response.body().toString());
                        if (tmp.has("error")) {
                            Toast.makeText(RegisterActivity.this, "Unsuccessful Attempt", Toast.LENGTH_SHORT).show();
                            resetETxts();
                        } else {
                            SharedPreferences preferences = getApplicationContext().getSharedPreferences("registerPref", MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putBoolean("registered", true);
                            editor.commit();
                            resetETxts();
                            Toast.makeText(RegisterActivity.this, "You have now successful been registered to CMC Insights.", Toast.LENGTH_LONG).show();
                            Thread thread = new Thread() {
                                @Override
                                public void run() {
                                    try {
                                        Thread.sleep(2000); // As I am using LENGTH_LONG in Toast
                                        RegisterActivity.this.finish();
                                        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            };
                            thread.start();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(RegisterActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    private void resetETxts() {
        et_name_reg.setText("");
        et_title_reg.setText("");
        et_company_reg.setText("");
        et_email_reg.setText("");
        chkbox_agree.setChecked(false);
    }
    private void showAlert(String msg) {
        final AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(RegisterActivity.this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(RegisterActivity.this);
        }
        builder.setTitle("Required Information")
                .setMessage(msg)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();

    }
    public final static boolean isValidEmail(CharSequence target) {
        if (target == null)
            return false;

        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}
