package app.wffr.api;

import java.util.List;
import java.util.Map;

import app.wffr.models.City;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;

public interface CitiesApi {
    // GET all ads
    @GET("Citys")
    Call<List<City>> getCities(
            @HeaderMap Map<String, String> headers
    );
}