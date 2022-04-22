package app.wffr.api;

import java.util.List;
import java.util.Map;

import app.wffr.models.NotificationItem;
import app.wffr.models.Product;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Query;

public interface NotificationsApi {

    @GET("notif")
    Call<List<NotificationItem>> getNotifications(
            @HeaderMap Map<String, String> headers,
            @Query("clientID") String clientId
    );

    @GET("Products")
    Call<List<Product>> getProductsNotifications(
            @HeaderMap Map<String, String> headers,
            @Query("date") String date
    );


}