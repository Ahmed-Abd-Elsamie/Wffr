package app.wffr.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.concurrent.TimeUnit;

import app.wffr.api.AccessApi;
import app.wffr.api.InvoicesApi;
import app.wffr.models.Invoice;
import app.wffr.models.MonthReportResponse;
import app.wffr.models.Product;
import app.wffr.utils.Constants;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PrevOrdersRepo {
    private static InvoicesApi ordersApi;
    private MutableLiveData<List<Invoice>> orders;
    private MutableLiveData<List<Product>> products;
    private MutableLiveData<MonthReportResponse> monthOrders;
    private static PrevOrdersRepo instance;
    private MutableLiveData<Boolean> isUpdating = new MutableLiveData<>();

    public PrevOrdersRepo(){
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

        ordersApi = retrofit.create(InvoicesApi.class);
    }

    public void getPrevOrders(String clientId){
        orders = new MutableLiveData<List<Invoice>>();
        Call<List<Invoice>> call = ordersApi.getPrevOrders(AccessApi.getHeaders(), clientId);
        call.enqueue(new Callback<List<Invoice>>() {
            @Override
            public void onResponse(Call<List<Invoice>> call, Response<List<Invoice>> response) {
                if (!response.isSuccessful()){
                    orders.setValue(null);
                    return;
                }
                orders.setValue(response.body());
                isUpdating.postValue(false);
            }

            @Override
            public void onFailure(Call<List<Invoice>> call, Throwable t) {
                orders.setValue(null);
                isUpdating.postValue(false);
            }
        });
    }

    public void getPrevOrdersProducts(String clientId){
        products = new MutableLiveData<List<Product>>();
        Call<List<Product>> call = ordersApi.getPrevOrdersProducts(AccessApi.getHeaders(), clientId);
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (!response.isSuccessful()){
                    orders.setValue(null);
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

    public void getPrevOrdersByMonth(String shopId, String clientId, String from, String to){
        monthOrders = new MutableLiveData<MonthReportResponse>();
        Call<MonthReportResponse> call = ordersApi.getPrevOrdersByMonth(AccessApi.getHeaders(), shopId, clientId, from, to);
        call.enqueue(new Callback<MonthReportResponse>() {
            @Override
            public void onResponse(Call<MonthReportResponse> call, Response<MonthReportResponse> response) {
                if (!response.isSuccessful()){
                    monthOrders.setValue(null);
                    return;
                }
                monthOrders.setValue(response.body());
                isUpdating.postValue(false);
            }

            @Override
            public void onFailure(Call<MonthReportResponse> call, Throwable t) {
                monthOrders.setValue(null);
                isUpdating.postValue(false);
            }
        });
    }

    public MutableLiveData<List<Invoice>> getPevOrders(){
        return orders;
    }

    public MutableLiveData<List<Product>> getPevOrdersProducts(){
        return products;
    }

    public MutableLiveData<MonthReportResponse> getPevOrdersByMonth(){
        return monthOrders;
    }



    public LiveData<Boolean> getIsUpdating(){
        return isUpdating;
    }


}
