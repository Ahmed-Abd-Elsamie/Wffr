package app.wffr.api;

import java.util.List;
import java.util.Map;

import app.wffr.models.FAQModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;

public interface FaqsApi {

    @GET("help")
    Call<List<FAQModel>> getFAQs(
            @HeaderMap Map<String, String> headers
    );

}