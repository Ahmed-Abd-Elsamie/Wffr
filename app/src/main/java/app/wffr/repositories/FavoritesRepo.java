package app.wffr.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.concurrent.TimeUnit;

import app.wffr.api.AccessApi;
import app.wffr.api.ProductsApi;
import app.wffr.listeners.FavoriteListener;
import app.wffr.models.FavoriteRequest;
import app.wffr.models.Product;
import app.wffr.utils.Constants;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FavoritesRepo {
    private static ProductsApi productsApi;
    private MutableLiveData<List<Product>> products;
    private MutableLiveData<String> addRemoveResponse;
    private MutableLiveData<Boolean> isUpdating = new MutableLiveData<>();

    public FavoritesRepo(){
        isUpdating.setValue(true);
        products = new MutableLiveData<List<Product>>();
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

    }

    public void getFavProducts(String clientId, Integer page, Integer size){

        Call<List<Product>> call = productsApi.getFavorites(
                AccessApi.getHeaders(),
                clientId,
                page,
                size
        );

        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (!response.isSuccessful()){
                    products.setValue(null);
                    return;
                }
                products.setValue(response.body());
                isUpdating.postValue(false);
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                products.setValue(null);
                isUpdating.postValue(false);
            }
        });
    }

    public void addToFavProducts(FavoriteRequest favoriteRequest){
        addRemoveResponse = new MutableLiveData<>();
        isUpdating.setValue(true);
        Call<String> call = productsApi.addRemoveFavorites(
                AccessApi.getHeaders(),
                favoriteRequest
        );

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                System.out.println("ADD FAV 1 " + response.message() + "  " + response.body() + call.request().toString());
                isUpdating.postValue(false);
                if (!response.isSuccessful()){
                    addRemoveResponse.postValue(null);
                    return;
                }

                addRemoveResponse.postValue(response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                System.out.println("ADD FAV 2 " + t.getMessage());
                isUpdating.postValue(false);
                addRemoveResponse.postValue(null);
            }
        });



    }

    public MutableLiveData<List<Product>> getProducts(){
        return products;
    }

    public LiveData<Boolean> getIsUpdating(){
        return isUpdating;
    }

    public MutableLiveData<String> getAddRemoveResponse() {
        return addRemoveResponse;
    }
}