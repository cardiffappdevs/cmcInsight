package com.example.eugein.cmc_insights;

import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eugein.cmc_insights.Util.CMCInsightsApi;
import com.example.eugein.cmc_insights.Util.CMCInsightsApplication;
import com.example.eugein.cmc_insights.Util.CheckNetwork;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.lang.reflect.Field;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentActivity extends AppCompatActivity {
    EditText et_comment, et_name, et_emal, et_website;
    Button btn_post_comment;
    String post_id;
    JsonObject commentJson;
    int intentStatus;
    ImageView iv_menu_toolbar,iv_arrow;
    private DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_comment);
        post_id = getIntent().getStringExtra("post_id");
        intentStatus=getIntent().getIntExtra("intentStatus",intentStatus);
        setUpView(post_id);
    }

    private void setUpView(final String post_id) {
        iv_menu_toolbar = (ImageView) findViewById(R.id.iv_menu_toolbar);
        iv_menu_toolbar.setVisibility(View.INVISIBLE);
        iv_arrow = (ImageView) findViewById(R.id.iv_arrow);
        iv_arrow.setVisibility(View.VISIBLE);

        iv_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
//        iv_menu_toolbar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                drawerLayout.openDrawer(Gravity.RIGHT);
//            }
//        });
        et_comment = (EditText) findViewById(R.id.et_comment);
        et_comment.setSelection(0);
        et_name = (EditText) findViewById(R.id.et_name);
        et_emal = (EditText) findViewById(R.id.et_emal);
        et_website = (EditText) findViewById(R.id.et_website);
        try {
            Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
            f.setAccessible(true);
            f.set(et_comment, R.drawable.cursor);
            f.set(et_name, R.drawable.cursor);
            f.set(et_emal, R.drawable.cursor);
            f.set(et_website, R.drawable.cursor);
        } catch (Exception ignored) {
        }
        btn_post_comment = (Button) findViewById(R.id.btn_post_comment);
        btn_post_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment, name, email, web;
                comment = et_comment.getText().toString();
                name = et_name.getText().toString();
                email = et_emal.getText().toString();
                web = et_website.getText().toString();
                if (TextUtils.isEmpty(name)){
                    showAlert("Please enter your name");
                }
                if (TextUtils.isEmpty(email)) {
                    showAlert("Please enter valid email");
                }
                if (!TextUtils.isEmpty(name )&& !TextUtils.isEmpty(email) ){
                    if (CheckNetwork.isInternetAvailable(CommentActivity.this)) {
                        sendComment(post_id, comment, name, email, web);

                    } else {
                        Toast.makeText(v.getContext(), "   No Internet Connection. Make sure\n      wifi or mobile data is turned on.", Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });


    }


    private void sendComment(String post_id, String comment, String name, String email, String website) {
        CMCInsightsApi api = CMCInsightsApplication.getCmcApi(CommentActivity.this);
        commentJson = new JsonObject();
        commentJson.addProperty("postid", post_id);
        commentJson.addProperty("name", name);
        commentJson.addProperty("comment", comment);
        commentJson.addProperty("email", email);
        commentJson.addProperty("website", website);
        Call<JsonObject> call = api.postComment(commentJson);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e("response_post_comment",response.toString());
                if (response.isSuccessful()) {
                    JsonObject object = response.body().getAsJsonObject();
                    if (object.has("comment")) {
                        final AlertDialog.Builder builder;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            builder = new AlertDialog.Builder(CommentActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                        } else {
                            builder = new AlertDialog.Builder(CommentActivity.this);
                        }
                        builder.setMessage(R.string.comment_success)
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        resetInputs();
                                        CommentActivity.this.finish();
                                        Intent intent=new Intent(CommentActivity.this,PostCategoriesActivity.class);
                                        intent.putExtra("intentStatus",intentStatus);
                                        startActivity(intent);
                                    }
                                })
                                .show();

                    } else {
                        Toast.makeText(CommentActivity.this, "Comment sending unsuccessful !", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(CommentActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(CommentActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void resetInputs() {
        et_comment.setText("");
        et_name.setText("");
        et_emal.setText("");
        et_website.setText("");
    }
    private void showAlert(String msg) {
        final AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(CommentActivity.this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(CommentActivity.this);
        }
        builder.setTitle("Required Information")
                .setMessage(msg)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();

    }
}
