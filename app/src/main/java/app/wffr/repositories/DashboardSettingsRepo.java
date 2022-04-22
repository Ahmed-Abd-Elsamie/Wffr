package app.wffr.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import app.wffr.api.AccessApi;
import app.wffr.api.SettingsApi;
import app.wffr.models.DashboardSettings;
import app.wffr.utils.Constants;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DashboardSettingsRepo {

    private static SettingsApi settingsApi;
    private MutableLiveData<DashboardSettings> settings;
    private static DashboardSettingsRepo instance;
    private MutableLiveData<Boolean> isUpdating = new MutableLiveData<>();

    public DashboardSettingsRepo(){
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .callTimeout(2, TimeUnit.MINUTES)
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS);
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient.build())
                .build();

        settingsApi = retrofit.create(SettingsApi.class);
        settings = new MutableLiveData<DashboardSettings>();
        isUpdating.setValue(true);
        Call<DashboardSettings> call = settingsApi.getDashboardSettings(AccessApi.getHeaders());
        call.enqueue(new Callback<DashboardSettings>() {
            @Override
            public void onResponse(Call<DashboardSettings> call, Response<DashboardSettings> response) {
                if (!response.isSuccessful()){
                    return;
                }
                settings.setValue(response.body());
                isUpdating.postValue(false);
            }

            @Override
            public void onFailure(Call<DashboardSettings> call, Throwable t) {
                isUpdating.postValue(false);
            }
        });
    }

    public static DashboardSettingsRepo getInstance() {
        if (instance != null){
            return instance;
        }
        instance = new DashboardSettingsRepo();
        return instance;
    }


    public MutableLiveData<DashboardSettings> getSettings(){
        return settings;
    }

    public LiveData<Boolean> getIsUpdating(){
        return isUpdating;
    }


}
