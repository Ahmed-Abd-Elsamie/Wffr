package app.wffr.api;

import java.util.Map;

import app.wffr.models.CouponModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface CouponApi {

    @GET("ClientCopouns")
    Call<String> getCoupon(
            @HeaderMap Map<String, String> headers
    );

    @POST("Shop")
    Call<CouponModel> requestCoupon(
            @HeaderMap Map<String, String> headers,
            @Query("product") String productId,
            @Query("client") String clientId
    );

    @GET("ClientCopouns")
    Call<String> checkPrevCoupon(
            @HeaderMap Map<String, String> headers,
            @Query("client") String clientId,
            @Query("shop") String shopId,
            @Query("product") String productId
    );


}