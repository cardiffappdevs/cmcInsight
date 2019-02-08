package com.example.eugein.cmc_insights;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.eugein.cmc_insights.Adapter.CommentsAdapter;
import com.example.eugein.cmc_insights.Adapter.CommentsListAdapter;
import com.example.eugein.cmc_insights.Model.Post;
import com.example.eugein.cmc_insights.Model.PostComment;
import com.example.eugein.cmc_insights.Util.CMCInsightsApi;
import com.example.eugein.cmc_insights.Util.CMCInsightsApplication;
import com.example.eugein.cmc_insights.Util.CheckNetwork;
import com.example.eugein.cmc_insights.Util.FontUtility;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.gson.JsonObject;
import com.linkedin.platform.APIHelper;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;
import com.linkedin.platform.listeners.AuthListener;
import com.linkedin.platform.utils.Scope;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostViewActivity extends AppCompatActivity {
    private Post post;
    private TextView tv_toolbar_title, tv_post_title, tv_authNdate, tv_post_details, tv_comment, tv_prev_comment;
    private ImageView iv_post, iv_fb, iv_twitter, iv_arrow, iv_menu, iv_utube, iv_linkedin;
    private ShareDialog shareDialog;
    private boolean isVisible = false;
    private ScrollView scroll;
    private DrawerLayout drawerLayout;
    private PopupWindow regPopupWindow;
    private JsonObject registerJob;
    private EditText et_name_reg, et_title_reg, et_company_reg, et_email_reg;
    private ImageView iv_close;
    //    private ListView lv_prev_comments;
    private ArrayList<PostComment> postComments;
    RecyclerView lv_prev_comments;
    private CommentsAdapter mAdapter;
    private int intentStatus;
    private static final String host = "api.linkedin.com";
    private static final String shareUrl = "https://" + host + "/v1/people/~/shares";
    boolean acceptedTerm = false;
    String temp="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_post_view);
        post = (Post) getIntent().getSerializableExtra("post");
        intentStatus = getIntent().getIntExtra("intentStatus", 0);
        postComments = new ArrayList<>();
        computePackageHash();
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        shareDialog = new ShareDialog(this);
        lv_prev_comments = (RecyclerView) findViewById(R.id.lv_prev_comments);
        mAdapter = new CommentsAdapter(postComments);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        lv_prev_comments.setLayoutManager(mLayoutManager);
        lv_prev_comments.setItemAnimator(new DefaultItemAnimator());
        lv_prev_comments.setAdapter(mAdapter);

        if (CheckNetwork.isInternetAvailable(PostViewActivity.this)) {
            setupView(post, intentStatus);
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    showPopup();
                }
            }, 100);
        } else {
            Toast.makeText(this, "   No Internet Connection. Make sure\n      wifi or mobile data is turned on.", Toast.LENGTH_SHORT).show();


            Thread thread = new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(3000); // As I am using LENGTH_LONG in Toast
                        PostViewActivity.this.finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            thread.start();
        }
    }


    private void showPopup() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        SharedPreferences pref = getSharedPreferences("registerPref", MODE_PRIVATE);
        boolean registered = pref.getBoolean("registered", false);
        if (!registered) {

            LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            final View customView = inflater.inflate(R.layout.register_popupview, null);

            Button btn_send;


            et_name_reg = (EditText) customView.findViewById(R.id.et_name_reg);
            et_title_reg = (EditText) customView.findViewById(R.id.et_title_reg);
            et_company_reg = (EditText) customView.findViewById(R.id.et_company_reg);
            et_email_reg = (EditText) customView.findViewById(R.id.et_email_reg);
            btn_send = (Button) customView.findViewById(R.id.btn_send);
            iv_close = customView.findViewById(R.id.iv_close);
            et_name_reg.setTypeface(FontUtility.setFontFace(this, FontUtility.MON_REGULAR));
            et_title_reg.setTypeface(FontUtility.setFontFace(this, FontUtility.MON_REGULAR));
            et_company_reg.setTypeface(FontUtility.setFontFace(this, FontUtility.MON_REGULAR));
            et_email_reg.setTypeface(FontUtility.setFontFace(this, FontUtility.MON_REGULAR));
            final CheckBox chkbox_agree = (CheckBox) customView.findViewById(R.id.chkbox_agree);
            chkbox_agree.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (chkbox_agree.isChecked()) {
                        acceptedTerm = true;

                    } else {

                        acceptedTerm = false;
                    }
//                Toast.makeText(RegisterActivity.this, String.valueOf(acceptedTerm), Toast.LENGTH_SHORT).show();
                }
            });
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
            regPopupWindow = new PopupWindow(
                    customView,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            customView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            int viewHeight = customView.getMeasuredHeight();
            int viewWidth = customView.getMeasuredWidth();
            int popX = (width - viewWidth) / 4;
            int popY = (height - viewHeight) / 2;
            Log.e("screen-=-=", "scHeight=" + height + ",scWidth=" + width);
            Log.e("view-=-=", "viewHeight=" + viewHeight + ",viewWidth=" + viewWidth);
            Log.e("pop-=-=", "x=" + popX + ",Y=" + popY);
            if (Build.VERSION.SDK_INT >= 21) {
                regPopupWindow.setElevation(5.0f);
            }
            if (android.os.Build.VERSION.SDK_INT >= 24) {


//                int[] a = new int[2];
//                customView.getLocationInWindow(a);
                regPopupWindow.showAtLocation(drawerLayout, Gravity.NO_GRAVITY, 80, popY);
            } else {
                regPopupWindow.showAtLocation(drawerLayout, Gravity.CENTER, 0, 0);
            }
            regPopupWindow.setFocusable(true);
            regPopupWindow.update();
            btn_send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (CheckNetwork.isInternetAvailable(getApplicationContext())) {

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

                        Toast.makeText(getApplicationContext(), "   No Internet Connection. Make sure\n      wifi or mobile data is turned on.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            iv_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    regPopupWindow.dismiss();
                    PostViewActivity.this.finish();
                    onBackPressed();
                }
            });


        }
    }

    private void setupView(final Post post, int intentStatus) {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        tv_comment = (TextView) findViewById(R.id.tv_comment);
//        tv_prev_comment = (TextView) findViewById(R.id.tv_prev_comment);
        tv_comment.setTypeface(FontUtility.setFontFace(this, FontUtility.MON_BOLD));
//        tv_prev_comment.setTypeface(FontUtility.setFontFace(this, FontUtility.MON_REGULAR));
        scroll = (ScrollView) findViewById(R.id.scroll);
        iv_menu = (ImageView) findViewById(R.id.iv_menu_toolbar);
        iv_utube = (ImageView) findViewById(R.id.iv_utube);
        if (!TextUtils.isEmpty(post.getYoutube_id())) {
            iv_utube.setVisibility(View.VISIBLE);
        }
        iv_utube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent youtube = new Intent(PostViewActivity.this, YoutubeViewActivity.class);
                youtube.putExtra("youtube_id", post.getYoutube_id());
                startActivity(youtube);
            }
        });

        tv_toolbar_title = (TextView) findViewById(R.id.tv_toolbar_title);
        tv_post_title = (TextView) findViewById(R.id.tv_post_title);
        tv_authNdate = (TextView) findViewById(R.id.tv_authNdate);
        tv_post_details = (TextView) findViewById(R.id.tv_post_details);
        iv_post = (ImageView) findViewById(R.id.iv_post);
        iv_fb = (ImageView) findViewById(R.id.iv_fb);
        iv_arrow = (ImageView) findViewById(R.id.iv_arrow);
        iv_arrow.setVisibility(View.VISIBLE);
        iv_fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareLinks(post);
            }
        });
        iv_twitter = (ImageView) findViewById(R.id.iv_twitter);
        iv_twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareLinkTwitter(post);
            }
        });
        tv_toolbar_title.setTypeface(FontUtility.setFontFace(this, FontUtility.MON_BOLD));
        tv_authNdate.setTypeface(FontUtility.setFontFace(this, FontUtility.MON_BOLD));
        tv_post_title.setTypeface(FontUtility.setFontFace(this, FontUtility.MON_BOLD));
        tv_post_details.setTypeface(FontUtility.setFontFace(this, FontUtility.MON_REGULAR));
        tv_toolbar_title.setText("CMC INSIGHTS");
        tv_toolbar_title.setVisibility(View.VISIBLE);
        Glide.with(this)
                .load(post.getImage())
                .into(iv_post);
        tv_post_title.setText(post.getTitle());
        for (int i=0;i<post.getAuthor().length();i++){
            temp=temp+Character.toUpperCase(post.getAuthor().charAt(i));
        }
        Log.e("author====",temp);
        tv_authNdate.setText(temp + ": " + post.getDate());


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            tv_post_details.setText(Html.fromHtml(post.getBrief_content(), Html.FROM_HTML_MODE_LEGACY));
        } else {
            tv_post_details.setText(Html.fromHtml(post.getBrief_content()));
        }
        tv_post_details.setMovementMethod(LinkMovementMethod.getInstance());
        iv_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        iv_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                drawerLayout.openDrawer(Gravity.RIGHT);
            }
        });
        tv_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoComment(post, PostViewActivity.this.intentStatus);
            }
        });

        getCommentList(post.getId());
        iv_linkedin = (ImageView) findViewById(R.id.iv_linkedin);
        iv_linkedin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLogin();
            }
        });
    }

    private void computePackageHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.eugein.cmc_insights", PackageManager.GET_SIGNATURES);
            for (Signature s : info.signatures) {
                MessageDigest digest = MessageDigest.getInstance("SHA");
                digest.update(s.toByteArray());
                Log.e("Hash*/*/*/*", Base64.encodeToString(digest.digest(), Base64.DEFAULT));
            }

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private void shareLinks(Post p) {

        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentTitle("Test")
                    .setContentDescription("Test blog")
                    .setContentUrl(Uri.parse(p.getLink()))
                    .build();

            shareDialog.show(linkContent);
        }

    }

    private void shareLinkTwitter(Post p) {
        Intent tweet = new Intent(Intent.ACTION_VIEW);
        String tmp = p.getLink();
        tweet.setData(Uri.parse("https://twitter.com/intent/tweet?url=" + Uri.encode(tmp)));//where message is your string message
        startActivity(tweet);
    }

    //private void shareLinkLinkedIn(Post p){
//    Intent linkedIn = new Intent(Intent.ACTION_VIEW);
//    String tmp = p.getLink();
//    tweet.setData(Uri.parse("https://twitter.com/intent/tweet?url=" + Uri.encode(tmp)));//where message is your string message
//    startActivity(tweet);
//}
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
            drawerLayout.closeDrawer(Gravity.RIGHT);
        } else {
            super.onBackPressed();
        }
    }

    private void register(String name, String title, String company, String email) {
        CMCInsightsApi api = CMCInsightsApplication.getCmcApi(getApplicationContext());
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
                            Toast.makeText(getApplicationContext(), "Unsuccessful Attempt", Toast.LENGTH_SHORT).show();
                            resetETxts();
                        } else {
                            SharedPreferences preferences = getApplicationContext().getSharedPreferences("registerPref", MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putBoolean("registered", true);
                            editor.commit();

                            Toast.makeText(getApplicationContext(), "You have now successful been registered to CMC Insights.", Toast.LENGTH_LONG).show();
                            regPopupWindow.dismiss();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Server Error", Toast.LENGTH_SHORT).show();
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


    }

    public void gotoComment(Post post, int intentStatus) {
        Intent intent = new Intent(PostViewActivity.this, CommentActivity.class);
        intent.putExtra("post_id", post.getId());
        intent.putExtra("intentStatus", this.intentStatus);
        startActivity(intent);
    }

    private void getCommentList(String post_id) {
        CMCInsightsApi api = CMCInsightsApplication.getCmcApi(PostViewActivity.this);
        Call<JsonObject> call = api.getComments(post_id);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e("reponse_comments", response.body().toString());
                if (response.isSuccessful()) {
                    try {
                        JSONObject responseObj = new JSONObject(response.body().toString());
                        JSONArray commentSJarray = responseObj.getJSONArray("comments");
                        if (commentSJarray.length() > 0) {
                            for (int i = 0; i < commentSJarray.length(); i++) {
                                JSONObject commentObj = commentSJarray.getJSONObject(i);
                                PostComment comment = new PostComment();
                                comment.setId(commentObj.getString("id"));
                                comment.setName(commentObj.getString("name"));
                                comment.setEmail(commentObj.getString("email"));
                                comment.setWebsite(commentObj.getString("website"));
                                comment.setDate(commentObj.getString("date"));
                                comment.setComment(commentObj.getString("comment"));

                                postComments.add(comment);
                            }
//                            loadComments(postComments);
                            mAdapter.notifyDataSetChanged();

                        }
//                        tv_prev_comment.setText("Prevoius Comments("+commentSJarray.length()+")");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(PostViewActivity.this, "Server Error !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(PostViewActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
//        return postComments;
    }

    private void loadComments(ArrayList<PostComment> postComments) {
//
//        CommentsListAdapter adapter = new CommentsListAdapter(postComments, PostViewActivity.this);
//        lv_prev_comments.setAdapter(adapter);
//        setListViewHeightBasedOnChildren(lv_prev_comments);
//        scroll.smoothScrollTo(0,0 );
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)


            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    public String buildShareMessage(String comment, String title, String linkUrl) {
        String shareJsonText = "{ \n" +
                "   \"comment\":\"" + comment + "\"," +
                "   \"visibility\":{ " +
                "      \"code\":\"anyone\"" +
                "   }," +
                "   \"content\":{ " +
                "      \"title\":\"" + title + "\"," +

                "      \"submitted-url\":\"" + linkUrl + "\"" +
                "   }" +
                "}";
        Log.e("obj", shareJsonText);
        return shareJsonText;
    }

    public void shareLinkedInMessage(Post post) {
        APIHelper apiHelper = APIHelper.getInstance(getApplicationContext());
        apiHelper.postRequest(PostViewActivity.this, shareUrl, buildShareMessage("comment", "title", post.getLink()), new ApiListener() {
            @Override
            public void onApiSuccess(ApiResponse apiResponse) {
                // ((TextView) findViewById(R.id.response)).setText(apiResponse.toString());
                Toast.makeText(getApplicationContext(), "Share success:  " + apiResponse.toString(),
                        Toast.LENGTH_LONG).show();
                Log.e("task-=-=-=-", "share success" + apiResponse.toString());
            }

            @Override
            public void onApiError(LIApiError error) {
                //   ((TextView) findViewById(R.id.response)).setText(error.toString());
                Toast.makeText(getApplicationContext(), "Share failed " + error.toString(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    // Build the list of member permissions our LinkedIn session requires
    private static Scope buildScope() {
        return Scope.build(Scope.R_BASICPROFILE, Scope.W_SHARE);
    }

    private void handleLogin() {
        LISessionManager.getInstance(getApplicationContext()).init(PostViewActivity.this, buildScope(), new AuthListener() {
            @Override
            public void onAuthSuccess() {
                // Authentication was successful.  You can now do
                // other calls with the SDK.
                shareLinkedInMessage(post);
            }

            @Override
            public void onAuthError(LIAuthError error) {
                Toast.makeText(getApplicationContext(), "login:  " + error.toString(),
                        Toast.LENGTH_LONG).show();
                // Handle authentication errors
            }
        }, true);
    }

    private void showAlert(String msg) {
        final AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(PostViewActivity.this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(PostViewActivity.this);
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
