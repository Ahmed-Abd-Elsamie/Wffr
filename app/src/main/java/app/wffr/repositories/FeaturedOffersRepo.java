package app.wffr.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import app.wffr.api.AccessApi;
import app.wffr.api.ProductsApi;
import app.wffr.models.FeaturedOffersResponse;
import app.wffr.utils.Constants;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FeaturedOffersRepo {

    private static ProductsApi productsApi;
    private MutableLiveData<FeaturedOffersResponse> featResponse;
    private static FeaturedOffersRepo instance;
    private MutableLiveData<Boolean> isUpdating = new MutableLiveData<>();

    public static FeaturedOffersRepo getInstance(String clientId, int type){
        if (instance == null || type == Constants.REFRESH){
            instance = new FeaturedOffersRepo(clientId);
        }
        return instance;
    }

    public FeaturedOffersRepo(String clientId){
        isUpdating.setValue(true);
        featResponse = new MutableLiveData<FeaturedOffersResponse>();
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

        productsApi = retrofit.create(ProductsApi.class);
        Call<FeaturedOffersResponse> call = productsApi.getFeaturedProducts(AccessApi.getHeaders(), clientId);
        call.enqueue(new Callback<FeaturedOffersResponse>() {
            @Override
            public void onResponse(Call<FeaturedOffersResponse> call, Response<FeaturedOffersResponse> response) {
                if (!response.isSuccessful()){

                    featResponse.setValue(null);
                    return;
                }
                featResponse.setValue(response.body());
                isUpdating.postValue(false);
            }

            @Override
            public void onFailure(Call<FeaturedOffersResponse> call, Throwable t) {
                featResponse.setValue(null);
                isUpdating.postValue(false);
            }
        });

    }

    public MutableLiveData<FeaturedOffersResponse> getFeaturedOffers(){
        return featResponse;
    }

    public LiveData<Boolean> getIsUpdating(){
        return isUpdating;
    }


}
