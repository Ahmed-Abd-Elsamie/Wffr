package app.wffr.api;

import java.util.List;
import java.util.Map;

import app.wffr.models.SocialModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;

public interface SocialApi {

    @GET("Setting/Social")
    Call<List<SocialModel>> getSocialContact(
            @HeaderMap Map<String, String> headers
    );


}