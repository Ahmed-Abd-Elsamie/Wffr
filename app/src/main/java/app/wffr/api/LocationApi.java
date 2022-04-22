package app.wffr.api;

import java.util.List;
import java.util.Map;

import app.wffr.models.MapsResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Query;

public interface LocationApi {

    @GET("Shop")
    Call<List<MapsResponse>> getNearLocations(
            @HeaderMap Map<String, String> headers,
            @Query("fromx") String fromx,
            @Query("tox") String tox,
            @Query("fromy") String fromy,
            @Query("toy") String toy
    );


}