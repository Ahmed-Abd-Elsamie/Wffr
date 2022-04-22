package app.wffr.repositories;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import app.wffr.utils.Constants;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AppRepo {

    private Retrofit retrofit;

    public AppRepo(){

        long t1 = System.currentTimeMillis();
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .callTimeout(2, TimeUnit.MINUTES)
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS);
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient.build())
                .build();
        long t2 = System.currentTimeMillis();

        System.out.println("TIME 1 : " + t1);
        System.out.println("TIME 2 : " + t2);
        System.out.println("TOTAL TIME : " + (t2 - t1));

    }

    public Retrofit getRetrofit() {
        return retrofit;
    }


}