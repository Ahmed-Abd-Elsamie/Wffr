package app.wffr.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.concurrent.TimeUnit;

import app.wffr.api.AccessApi;
import app.wffr.api.ShopsApi;
import app.wffr.models.Branch;
import app.wffr.models.Shop;
import app.wffr.models.ShopModel;
import app.wffr.utils.Constants;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ShopsRepo {

    private static ShopsApi shopsApi;
    private MutableLiveData<List<Shop>> shops;
    private MutableLiveData<ShopModel> shopsByProID;
    private MutableLiveData<List<Branch>> branches;
    private MutableLiveData<String> rating;
    private static ShopsRepo instance;
    private MutableLiveData<Boolean> isUpdating = new MutableLiveData<>();


    public ShopsRepo(){
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

        shopsApi = retrofit.create(ShopsApi.class);
    }

    public void getShopsByCategory(String categoryId, int page, int size){
        shops = new MutableLiveData<List<Shop>>();
        isUpdating.setValue(true);
        Call<List<Shop>> call = shopsApi.getShopsByCategory(
                AccessApi.getHeaders(),
                categoryId,
                page,
                size
        );

        call.enqueue(new Callback<List<Shop>>() {
            @Override
            public void onResponse(Call<List<Shop>> call, Response<List<Shop>> response) {
                if (!response.isSuccessful()){
                    shops.setValue(null);
                    isUpdating.postValue(false);
                    return;
                }
                shops.setValue(response.body());
                isUpdating.postValue(false);
            }

            @Override
            public void onFailure(Call<List<Shop>> call, Throwable t) {
                shops.setValue(null);
                isUpdating.postValue(false);
            }
        });

    }

    public void getShopByProductId(String productId){
        shopsByProID = new MutableLiveData<ShopModel>();
        isUpdating.setValue(true);
        Call<ShopModel> call = shopsApi.getShopByProductId(
                AccessApi.getHeaders(),
                productId
        );

        call.enqueue(new Callback<ShopModel>() {
            @Override
            public void onResponse(Call<ShopModel> call, Response<ShopModel> response) {
                if (!response.isSuccessful()){
                    return;
                }
                shopsByProID.setValue(response.body());
                isUpdating.postValue(false);
            }

            @Override
            public void onFailure(Call<ShopModel> call, Throwable t) {
                isUpdating.postValue(false);
            }
        });

    }

    public void getShopBranches(String shopId){
        branches = new MutableLiveData<List<Branch>>();
        isUpdating.setValue(true);
        Call<List<Branch>> call = shopsApi.getShopBranches(
                AccessApi.getHeaders(),
                shopId
        );

        call.enqueue(new Callback<List<Branch>>() {
            @Override
            public void onResponse(Call<List<Branch>> call, Response<List<Branch>> response) {
                if (!response.isSuccessful()){
                    return;
                }
                branches.setValue(response.body());
                isUpdating.postValue(false);
            }

            @Override
            public void onFailure(Call<List<Branch>> call, Throwable t) {
                isUpdating.postValue(false);
            }
        });

    }

    public void ratShop(String rat, String clientId, String shopId){
        rating = new MutableLiveData<String>();
        isUpdating.setValue(true);
        Call<String> call = shopsApi.ratShop(
                AccessApi.getHeaders(),
                rat,
                clientId,
                shopId
        );

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (!response.isSuccessful()){
                    return;
                }
                rating.setValue(response.body());
                isUpdating.postValue(false);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                isUpdating.postValue(false);
            }
        });

    }

    public MutableLiveData<List<Shop>> getShops(){
        return shops;
    }

    public MutableLiveData<String> getRatResponse(){
        return rating;
    }

    public MutableLiveData<ShopModel> getShopByProductId(){
        return shopsByProID;
    }

    public MutableLiveData<List<Branch>> getShopBranches(){
        return branches;
    }

    public LiveData<Boolean> getIsUpdating(){
        return isUpdating;
    }


}
