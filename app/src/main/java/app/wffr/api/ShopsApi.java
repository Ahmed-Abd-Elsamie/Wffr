package app.wffr.api;

import java.util.List;
import java.util.Map;

import app.wffr.models.Branch;
import app.wffr.models.Shop;
import app.wffr.models.ShopModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ShopsApi {
    // GET shops
    @GET("Shop")
    Call<List<Shop>> getShopsByCategory(
            @HeaderMap Map<String, String> headers,
            @Query("ShopCategory") String categoryId,
            @Query("page") int page,
            @Query("size") int size
    );

    @GET("Shop")
    Call<ShopModel> getShopByProductId(
            @HeaderMap Map<String, String> headers,
            @Query("progID") String productId
    );

    @GET("branches")
    Call<List<Branch>> getShopBranches(
            @HeaderMap Map<String, String> headers,
            @Query("Shopid") String shopId
    );

    @POST("assess")
    Call<String> ratShop(
            @HeaderMap Map<String, String> headers,
            @Query("assess") String rating,
            @Query("client") String clientId,
            @Query("shop") String shopId
    );


}