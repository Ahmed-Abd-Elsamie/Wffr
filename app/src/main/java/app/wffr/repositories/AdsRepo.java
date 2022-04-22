package app.wffr.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.concurrent.TimeUnit;

import app.wffr.api.AccessApi;
import app.wffr.api.AdsApi;
import app.wffr.models.AdCategoryModel;
import app.wffr.models.AdModel;
import app.wffr.utils.Constants;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AdsRepo {

    private static AdsApi adsApi;
    private MutableLiveData<List<AdModel>> ads;
    private MutableLiveData<List<AdCategoryModel>> adsCategory;
    private static AdsRepo instance;
    private MutableLiveData<Boolean> isUpdating = new MutableLiveData<>();

    public static AdsRepo getInstance(int type){
        if (instance == null || type == Constants.REFRESH){
            instance = new AdsRepo();
        }
        return instance;
    }

    public AdsRepo(){
        isUpdating.setValue(true);
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

        adsApi = retrofit.create(AdsApi.class);
    }

    public void getOffersAds(){
        ads = new MutableLiveData<>();
        Call<List<AdModel>> call = adsApi.getAds(
                AccessApi.getHeaders()
        );
        call.enqueue(new Callback<List<AdModel>>() {
            @Override
            public void onResponse(Call<List<AdModel>> call, Response<List<AdModel>> response) {
                if (!response.isSuccessful()){
                    return;
                }
                ads.setValue(response.body());
                isUpdating.postValue(false);
            }

            @Override
            public void onFailure(Call<List<AdModel>> call, Throwable t) {
                isUpdating.postValue(false);
            }
        });
    }

    public void getCategoryAds(int categoryId){
        adsCategory = new MutableLiveData<>();
        Call<List<AdCategoryModel>> call = adsApi.getAdsCategory(
                AccessApi.getHeaders(),
                categoryId
        );
        call.enqueue(new Callback<List<AdCategoryModel>>() {
            @Override
            public void onResponse(Call<List<AdCategoryModel>> call, Response<List<AdCategoryModel>> response) {
                if (!response.isSuccessful()){
                    return;
                }
                adsCategory.setValue(response.body());
                isUpdating.postValue(false);
            }

            @Override
            public void onFailure(Call<List<AdCategoryModel>> call, Throwable t) {
                isUpdating.postValue(false);
            }
        });
    }

    public MutableLiveData<List<AdModel>> getAds(){
        return ads;
    }

    public MutableLiveData<List<AdCategoryModel>> getAdsCategory(){
        return adsCategory;
    }

    public LiveData<Boolean> getIsUpdating(){
        return isUpdating;
    }


}