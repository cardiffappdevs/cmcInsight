package com.example.eugein.cmc_insights.Util;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by AH on 12/26/2017.
 */

public interface CMCInsightsApi {

    @GET("post")
    Call<JsonObject> getPostData();

    @POST("userregister")
    Call<JsonObject>userRegistration(@Body JsonObject user);

    @POST("comment")
    Call<JsonObject>postComment(@Body JsonObject comment);

    @GET("comment/{post_id}")
    Call<JsonObject> getComments(@Path("post_id") String post_id);

}
