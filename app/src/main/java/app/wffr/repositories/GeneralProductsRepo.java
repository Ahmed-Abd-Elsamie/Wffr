package app.wffr.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.TimeUnit;

import app.wffr.api.AccessApi;
import app.wffr.api.ProductsApi;
import app.wffr.models.Product;
import app.wffr.models.ProductPreviewResponse;
import app.wffr.models.SearchModel;
import app.wffr.utils.Constants;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class GeneralProductsRepo {

    private static ProductsApi productsApi;
    private MutableLiveData<List<Product>> products;
    private MutableLiveData<SearchModel> searchModelProducts;
    private MutableLiveData<ProductPreviewResponse> productData;
    private MutableLiveData<SearchModel> generalProducts;
    private MutableLiveData<Boolean> isUpdating = new MutableLiveData<>();

    private static GeneralProductsRepo instance;


    public static GeneralProductsRepo getInstance(int type){
        if (instance == null || type == Constants.REFRESH){
            instance = new GeneralProductsRepo();
        }
        return instance;
    }

    public GeneralProductsRepo() {
        isUpdating.setValue(true);
        products = new MutableLiveData<>();
        searchModelProducts = new MutableLiveData<>();
        productData = new MutableLiveData<>();
        generalProducts = new MutableLiveData<>();
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

    public void getProducts(String category, boolean show, boolean type, String from, String to){
        Call<List<Product>> call = productsApi.getProducts(AccessApi.getHeaders(), category, show, type, from, to);
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(@NotNull Call<List<Product>> call, @NotNull Response<List<Product>> response) {
                System.out.println("RANGE BAR : " + response.body());
                if (!response.isSuccessful()){
                    System.out.println("RANGE BAR RETURN : " + response.body());
                    return;
                }
                products.setValue(response.body());
                isUpdating.postValue(false);
            }

            @Override
            public void onFailure(@NotNull Call<List<Product>> call, @NotNull Throwable t) {
                products.setValue(null);
                isUpdating.postValue(false);
            }
        });
    }

    public void getGeneralProducts(String category, String from, String to, String key, Integer page, Integer size, Integer pageProduct){
        Call<SearchModel> call = productsApi.getGeneralProducts(AccessApi.getHeaders(), category, from, to, key, page, size, pageProduct);
        call.enqueue(new Callback<SearchModel>() {
            @Override
            public void onResponse(@NotNull Call<SearchModel> call, @NotNull Response<SearchModel> response) {
                System.out.println("SEARCH PRO : " + response.message());
                if (!response.isSuccessful()){
                    generalProducts.setValue(null);
                    isUpdating.postValue(false);
                    System.out.println("SEARCH PRO RETURN : " + response.message());
                    return;
                }
                generalProducts.setValue(response.body());
                isUpdating.postValue(false);
            }

            @Override
            public void onFailure(@NotNull Call<SearchModel> call, @NotNull Throwable t) {
                System.out.println("SEARCH PRO ERROR : " + t.getMessage());
                generalProducts.setValue(null);
                isUpdating.postValue(false);
            }
        });
    }

    public void getProductData(String productId, String clientId){
        Call<ProductPreviewResponse> call = productsApi.getProductData(AccessApi.getHeaders(), productId, clientId);
        call.enqueue(new Callback<ProductPreviewResponse>() {
            @Override
            public void onResponse(@NotNull Call<ProductPreviewResponse> call, @NotNull Response<ProductPreviewResponse> response) {
                System.out.println("SEARCH PRO : " + response.body() + "ERR" + response.errorBody());
                if (!response.isSuccessful()){
                    productData.setValue(null);
                    isUpdating.postValue(false);
                    return;
                }
                productData.setValue(response.body());
                isUpdating.postValue(false);
            }

            @Override
            public void onFailure(@NotNull Call<ProductPreviewResponse> call, @NotNull Throwable t) {
                productData.setValue(null);
                isUpdating.postValue(false);
            }
        });
    }

    public void getShopProducts(String shopId){
        Call<SearchModel> call = productsApi.getShopProducts(AccessApi.getHeaders(), shopId);
        call.enqueue(new Callback<SearchModel>() {
            @Override
            public void onResponse(@NotNull Call<SearchModel> call, @NotNull Response<SearchModel> response) {
                System.out.println("SEARCH PRO : " + response.message());
                if (!response.isSuccessful()){
                    generalProducts.setValue(null);
                    isUpdating.postValue(false);
                    System.out.println("SEARCH PRO RETURN : " + response.message());
                    return;
                }
                generalProducts.setValue(response.body());
                isUpdating.postValue(false);
            }

            @Override
            public void onFailure(@NotNull Call<SearchModel> call, @NotNull Throwable t) {
                System.out.println("SEARCH PRO ERROR : " + t.getMessage());
                generalProducts.setValue(null);
                isUpdating.postValue(false);
            }
        });
    }

    public MutableLiveData<List<Product>> getProducts(){
        return products;
    }

    public MutableLiveData<SearchModel> getGeneralProducts(){
        return generalProducts;
    }

    public MutableLiveData<ProductPreviewResponse> getProductData(){
        return productData;
    }

    public LiveData<Boolean> getIsUpdating(){
        return isUpdating;
    }


}