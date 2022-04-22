package app.wffr.api;

import java.util.Map;

import app.wffr.models.Complaint;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ComplaintsApi {

    @POST("FeadBack")
    Call<String> insertComplaint(
            @HeaderMap Map<String, String> headers,
            @Query("clientID") String clientId,
            @Body Complaint complaint
    );

}