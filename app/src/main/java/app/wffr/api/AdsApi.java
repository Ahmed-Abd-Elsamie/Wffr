package app.wffr.api;

import java.util.List;
import java.util.Map;

import app.wffr.models.AdCategoryModel;
import app.wffr.models.AdModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Query;

public interface AdsApi {
    // GET all ads
    @GET("Ads")
    Call<List<AdModel>> getAds(
            @HeaderMap Map<String, String> headers
    );

    @GET("Ads/GetAdsBanars")
    Call<List<AdCategoryModel>> getAdsCategory(
            @HeaderMap Map<String, String> headers,
            @Query("category_id") int categoryId
    );

}