package com.example.eugein.cmc_insights;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eugein.cmc_insights.Adapter.PostListAdapter;
import com.example.eugein.cmc_insights.Model.Post;
import com.example.eugein.cmc_insights.Model.PostCategory;
import com.example.eugein.cmc_insights.Util.CMCInsightsApi;
import com.example.eugein.cmc_insights.Util.CMCInsightsApplication;
import com.example.eugein.cmc_insights.Util.CheckNetwork;
import com.example.eugein.cmc_insights.Util.FontUtility;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PostCategoriesActivity extends AppCompatActivity {
    private ArrayList<Post> postArrayList;
    private ImageView iv_menu, iv_logo;
    private ListView lv_post_list;
    private TextView tv_choose_cat, tv_cats_h1, tv_cats_h2;
    private ScrollView sv_cats;
    private DrawerLayout drawerLayout;
    private int intentStatus = 0;
    private ArrayList<Post> allPosts;
    private ArrayList<Post> menuSelectPosts;
    private ArrayList<PostCategory> postCategories;
    private ArrayList<Post> posts;
    private ProgressDialog progressDialog;

    private PopupWindow regPopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_post_categories);
        postCategories = new ArrayList<>();
        posts = new ArrayList<>();
        allPosts = new ArrayList<>();

        intentStatus = getIntent().getIntExtra("index", 0);


        if (CheckNetwork.isInternetAvailable(PostCategoriesActivity.this)) {

            loadPosts(intentStatus);

        } else {
            Toast.makeText(this, "   No Internet Connection. Make sure\n      wifi or mobile data is turned on.", Toast.LENGTH_SHORT).show();

            Thread thread = new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(3000); // As I am using LENGTH_LONG in Toast
                        PostCategoriesActivity.this.finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            thread.start();

        }

    }

    private void setupView(String title, ArrayList<Post> posts) {
        menuSelectPosts = posts;

        iv_menu = (ImageView) findViewById(R.id.iv_menu_toolbar);
        iv_logo = (ImageView) findViewById(R.id.iv_logo);

        iv_logo.setVisibility(View.VISIBLE);
        iv_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PostCategoriesActivity.this, MainActivity.class));
            }
        });

        tv_choose_cat = (TextView) findViewById(R.id.tv_choose_cat);
        tv_choose_cat.setText(R.string.coose_cat);
        tv_cats_h1 = (TextView) findViewById(R.id.tv_cats_h1);
        tv_cats_h1.setText(title);
        tv_cats_h2 = (TextView) findViewById(R.id.tv_cats_h2);
        tv_cats_h2.setText("Latest Financial Insights");

        tv_choose_cat.setTypeface(FontUtility.setFontFace(this, FontUtility.MON_BOLD));
        tv_cats_h1.setTypeface(FontUtility.setFontFace(this, FontUtility.MON_BOLD));
        tv_cats_h2.setTypeface(FontUtility.setFontFace(this, FontUtility.LORA_REGULAR));

        lv_post_list = (ListView) findViewById(R.id.lv_post_list);
        sv_cats = (ScrollView) findViewById(R.id.sv_cats);

        PostListAdapter adapter = new PostListAdapter(posts, PostCategoriesActivity.this);
        lv_post_list.setAdapter(adapter);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        iv_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                drawerLayout.openDrawer(Gravity.RIGHT);
            }
        });

        setListViewHeightBasedOnChildren(lv_post_list);
//        sv_cats.fullScroll(View.FOCUS_UP);
        sv_cats.smoothScrollTo(0, 0);
        lv_post_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                SharedPreferences pref = getSharedPreferences("registerPref", MODE_PRIVATE);
//                boolean registered = pref.getBoolean("registered", false);
//                if (registered) {
                Intent intent = new Intent(PostCategoriesActivity.this, PostViewActivity.class);
                intent.putExtra("post", menuSelectPosts.get(position));
                intent.putExtra("intentStatus", intentStatus);
                startActivity(intent);
//                } else {
//                    LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
//                    View customView = inflater.inflate(R.layout.register_popupview, null);
//                    regPopupWindow=new PopupWindow(
//                            customView,
//                            LinearLayout.LayoutParams.WRAP_CONTENT,
//                            LinearLayout.LayoutParams.WRAP_CONTENT
//                    );
//
//                    if(Build.VERSION.SDK_INT>=21){
//                        regPopupWindow.setElevation(5.0f);
//                    }
//                    regPopupWindow.showAtLocation(drawerLayout,Gravity.CENTER,0,0);
//                    regPopupWindow.setFocusable(true);
//                    regPopupWindow.update();
//                }
            }
        });
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

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
            drawerLayout.closeDrawer(Gravity.RIGHT);
        } else {
            super.onBackPressed();
        }
    }

    private void loadPosts(int intentStatus) {
        progressDialog = new ProgressDialog(PostCategoriesActivity.this);
        progressDialog.setMessage("Fetching data...");
        progressDialog.setCancelable(false);
        progressDialog.show();
//            new SyncPostData().execute("");

        CMCInsightsApi api = CMCInsightsApplication.getCmcApi(PostCategoriesActivity.this);
        Call<JsonObject> call = api.getPostData();
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (response.isSuccessful()) {
                    JSONObject object = null;
                    try {
                        object = new JSONObject(response.body().toString());
                        JSONArray array = object.getJSONArray("post");
                        for (int i = 0; i < array.length(); i++) {
                            posts = new ArrayList<>();
                            JSONObject catObject = array.getJSONObject(i);
                            PostCategory postCategory = new PostCategory();
                            postCategory.setCategory_id(catObject.getString("category_id"));
                            postCategory.setCategory_name(catObject.getString("category_name"));

                            JSONArray postArray = catObject.getJSONArray("post_list");

                            for (int j = 0; j < postArray.length(); j++) {
                                JSONObject postobject = postArray.getJSONObject(j);

                                Post post = new Post();
                                post.setId(postobject.getString("id"));
                                post.setTitle(postobject.getString("title"));
                                post.setSlug(postobject.getString("slug"));
                                post.setBrief_content(postobject.getString("brief_content"));
                                post.setExtended_content(postobject.getString("extended_content"));
                                post.setImage(postobject.getString("image"));
                                post.setYoutube(postobject.getString("youtube"));

                                if (postobject.has("youtube_id")) {
                                    post.setYoutube_id(postobject.getString("youtube_id"));
                                }
                                post.setAuthor(postobject.getString("author"));
                                post.setLink(postobject.getString("link"));
                                post.setDate(postobject.getString("date"));


                                posts.add(post);
                                allPosts.add(post);
                            }
                            postCategory.setPost_list(posts);
                            postCategories.add(postCategory);

                        }
                        progressDialog.dismiss();
                        Log.e("size", String.valueOf(postCategories.size()));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Log.e("result", response.body().toString());
                    switch (PostCategoriesActivity.this.intentStatus) {
                        case 0:

                            setupView("Financial", allPosts);
                            break;
                        case 1:

                            setupView("Blogs", postCategories.get(PostCategoriesActivity.this.intentStatus - 1).getPost_list());
                            break;

                        case 2:

                            setupView("Interviews", postCategories.get(PostCategoriesActivity.this.intentStatus - 1).getPost_list());
                            break;
                        case 3:

                            setupView("White Papers", postCategories.get(PostCategoriesActivity.this.intentStatus - 1).getPost_list());
                            break;

                    }

                } else {


                    Toast.makeText(PostCategoriesActivity.this, "Server error", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
