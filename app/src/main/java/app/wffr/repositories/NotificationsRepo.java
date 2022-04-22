package app.wffr.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.concurrent.TimeUnit;

import app.wffr.api.AccessApi;
import app.wffr.api.NotificationsApi;
import app.wffr.models.NotificationItem;
import app.wffr.models.Product;
import app.wffr.utils.Constants;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NotificationsRepo {

    private static NotificationsApi notificationsApi;
    private MutableLiveData<List<NotificationItem>> notifications;
    private MutableLiveData<List<Product>> productNotifications;
    private MutableLiveData<Boolean> isUpdating = new MutableLiveData<>();

    public NotificationsRepo(){
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
        notificationsApi = retrofit.create(NotificationsApi.class);
    }

    public void getNotifications(String clientId){
        notifications = new MutableLiveData<List<NotificationItem>>();
        isUpdating.setValue(true);
        Call<List<NotificationItem>> call = notificationsApi.getNotifications(
                AccessApi.getHeaders(),
                clientId
        );

        call.enqueue(new Callback<List<NotificationItem>>() {
            @Override
            public void onResponse(Call<List<NotificationItem>> call, Response<List<NotificationItem>> response) {
                if (!response.isSuccessful()){
                    notifications.setValue(null);
                    isUpdating.postValue(false);
                    return;
                }
                notifications.setValue(response.body());
                isUpdating.postValue(false);
            }

            @Override
            public void onFailure(Call<List<NotificationItem>> call, Throwable t) {
                notifications.setValue(null);
                isUpdating.postValue(false);
            }
        });

    }

    public void getProductsNotifications(String date){
        productNotifications = new MutableLiveData<List<Product>>();
        isUpdating.setValue(true);
        Call<List<Product>> call = notificationsApi.getProductsNotifications(
                AccessApi.getHeaders(),
                date
        );

        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (!response.isSuccessful()){
                    System.out.println("NOTIF 1");
                    productNotifications.setValue(null);
                    isUpdating.postValue(false);
                    return;
                }
                System.out.println("NOTIF 2");

                productNotifications.setValue(response.body());
                isUpdating.postValue(false);
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                System.out.println("NOTIF 3");

                productNotifications.setValue(null);
                isUpdating.postValue(false);
            }
        });

    }

    public MutableLiveData<List<NotificationItem>> getNotifications(){
        return notifications;
    }

    public MutableLiveData<List<Product>> getProductNotifications(){
        return productNotifications;
    }

    public LiveData<Boolean> getIsUpdating(){
        return isUpdating;
    }

}