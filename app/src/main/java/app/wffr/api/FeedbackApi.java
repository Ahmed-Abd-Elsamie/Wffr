package app.wffr.api;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface FeedbackApi {

    @POST("FeadBack")
    Call<String> sendFeedBack(
            @HeaderMap Map<String, String> headers,
            @Query("first") String like,
            @Query("second") String fair,
            @Query("last") String dislike
    );

}
