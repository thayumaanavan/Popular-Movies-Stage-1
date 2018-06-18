package com.thayumaanavan.popularmovies_stage1.network;

import com.thayumaanavan.popularmovies_stage1.utils.AppConstants;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static Retrofit retrofit;

    public static Retrofit getInstance(){
        if(retrofit == null ){
            retrofit = new Retrofit.Builder()
                    .baseUrl(AppConstants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(buildHttpClient())
                    .build();
        }
        return retrofit;
    }

    public static MovieService getMovieService(){
        return getInstance().create(MovieService.class);
    }


    private static OkHttpClient buildHttpClient(){
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                HttpUrl originalHttpUrl = original.url();

                HttpUrl url = originalHttpUrl.newBuilder()
                                .addQueryParameter(AppConstants.API_KEY,AppConstants.API_KEY_VALUE )
                                .build();

                Request.Builder requestBuilder = original.newBuilder()
                        .url(url);
                Request request = requestBuilder.build();

                return chain.proceed(request);
            }
        });

        return builder.build();
    }
}
