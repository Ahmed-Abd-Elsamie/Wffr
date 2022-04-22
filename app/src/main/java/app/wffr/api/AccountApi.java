package app.wffr.api;

import java.util.List;
import java.util.Map;

import app.wffr.models.CategoryRatio;
import app.wffr.models.UserAccount;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AccountApi {

    @GET("ClientAccount")
    Call<UserAccount> getUserAccountData(
            @HeaderMap Map<String, String> headers,
            @Query("client") String clientId
    );

    @GET("Products")
    Call<List<CategoryRatio>> getCategoriesRatio(
            @HeaderMap Map<String, String> headers,
            @Query("client") String clientId
    );

    @POST("ClientAccount")
    Call<String> exchangePoints(
            @HeaderMap Map<String, String> headers,
            @Query("id") String clientId
    );


}