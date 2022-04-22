package app.wffr.api;

import java.util.Map;

import app.wffr.models.MarketRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;

public interface MarketRequestApi {

    @POST("shopmobile")
    Call<String> requestNewShop(
            @HeaderMap Map<String, String> headers,
            @Body MarketRequest marketRequest
    );


}