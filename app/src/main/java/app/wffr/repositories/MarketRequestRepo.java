package app.wffr.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import app.wffr.api.AccessApi;
import app.wffr.api.MarketRequestApi;
import app.wffr.listeners.MarketRequestListener;
import app.wffr.models.MarketRequest;
import app.wffr.utils.Constants;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MarketRequestRepo {

    private static MarketRequestApi marketRequestApi;
    private MutableLiveData<Boolean> isUpdating = new MutableLiveData<>();
    private MarketRequestListener marketRequestListener;


    public MarketRequestRepo (MarketRequestListener marketRequestListener){
        this.marketRequestListener = marketRequestListener;
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .callTimeout(2, TimeUnit.MINUTES)
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS);
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient.build())
                .build();

        marketRequestApi = retrofit.create(MarketRequestApi.class);
    }

    public void insertMarket(MarketRequest marketRequest){
        isUpdating.setValue(true);

        Call<String> call = marketRequestApi.requestNewShop(
                AccessApi.getHeaders(),
                marketRequest
        );

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                System.out.println("MARKET RES : " + response.body() + "END RES");
                if (!response.isSuccessful()){
                    System.out.println("MARKET RETURN RES : " + response.body() + "END RES");
                    marketRequestListener.onComplete(response.body(), false);
                    return;
                }
                marketRequestListener.onComplete(response.body(), true);
                isUpdating.postValue(false);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                marketRequestListener.onComplete(null, false);
                System.out.println("ERRRROOOOOOOOOOOOOOOOOOOR2 " + t.getMessage());
                isUpdating.postValue(false);
            }
        });
    }

    public LiveData<Boolean> getIsUpdating(){
        return isUpdating;
    }


}