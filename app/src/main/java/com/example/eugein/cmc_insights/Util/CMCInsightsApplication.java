package com.example.eugein.cmc_insights.Util;

import android.app.Application;
import android.content.Context;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by AH on 12/26/2017.
 */

public class CMCInsightsApplication extends Application {
    private static CMCInsightsApi CMCInsightsApi;
    private static Gson gson;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static CMCInsightsApi getCmcApi(Context context) {
        if (CMCInsightsApi == null) {
            CMCInsightsApi = createCmcApi(context);
        }
        return CMCInsightsApi;
    }

    public static CMCInsightsApi createCmcApi(final Context context) {

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request original = chain.request();

                Request request = original.newBuilder()
                        .header("Content-Type", "application/json")
                        .header("Accept", "application/json")
                        .method(original.method(), original.body())
                        .build();

                return chain.proceed(request);
            }
        });


        OkHttpClient client = httpClient.build();
        String endPoint = KeyValues.HOST;

        Retrofit restAdapter = new Retrofit.Builder()
                .baseUrl(endPoint)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CMCInsightsApi = restAdapter.create(CMCInsightsApi.class);
        return CMCInsightsApi;
    }

    public static Gson getGson() {
        if (gson == null) {
            gson = new Gson();
        }
        return gson;
    }

}
