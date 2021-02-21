package com.klim.habrareader.data.retrofit;

import com.klim.habrareader.BuildConfig;
import com.klim.habrareader.data.retrofit.apis.AllPostsApi;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitProvider {

    private static RetrofitProvider provider;

    private Retrofit retrofit;
    private AllPostsApi allPostsApi;

    public static RetrofitProvider get() {
        if (provider == null) {
            provider = new RetrofitProvider();
        }
        return provider;
    }

    public Retrofit getRetrofit() {
        if (retrofit == null) {

            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl("https://habr.com/ru/")
                    .addConverterFactory(ScalarsConverterFactory.create())
//                    .client(client)
                    .build();
        }
        return retrofit;
    }

    public AllPostsApi getAllPostsApi() {
        if (allPostsApi == null) {
            allPostsApi = getRetrofit().create(AllPostsApi.class);
        }
        return allPostsApi;
    }

}
