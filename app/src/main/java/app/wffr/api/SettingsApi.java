package app.wffr.api;

import java.util.Map;

import app.wffr.models.DashboardSettings;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;

public interface SettingsApi {

    @GET("Setting")
    Call<DashboardSettings> getDashboardSettings(
            @HeaderMap Map<String, String> headers
    );

}
