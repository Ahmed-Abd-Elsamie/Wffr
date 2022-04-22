package app.wffr.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.concurrent.TimeUnit;

import app.wffr.api.AccessApi;
import app.wffr.api.CitiesApi;
import app.wffr.models.City;
import app.wffr.utils.Constants;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CitiesRepo {
    private static CitiesApi citiesApi;
    private MutableLiveData<List<City>> cities;
    private static CitiesRepo instance;
    private MutableLiveData<Boolean> isUpdating = new MutableLiveData<>();

    public static CitiesRepo getInstance(){
        if (instance == null){
            instance = new CitiesRepo();
        }
        return instance;
    }

    public CitiesRepo(){
        isUpdating.setValue(true);
        cities = new MutableLiveData<List<City>>();
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

        citiesApi = retrofit.create(CitiesApi.class);

        Call<List<City>> call = citiesApi.getCities(AccessApi.getHeaders());
        call.enqueue(new Callback<List<City>>() {
            @Override
            public void onResponse(Call<List<City>> call, Response<List<City>> response) {
                if (!response.isSuccessful()){
                    return;
                }
                cities.setValue(response.body());
                isUpdating.postValue(false);
            }

            @Override
            public void onFailure(Call<List<City>> call, Throwable t) {
                isUpdating.postValue(false);
            }
        });

    }

    public MutableLiveData<List<City>> getCities(){
        return cities;
    }

    public LiveData<Boolean> getIsUpdating(){
        return isUpdating;
    }


}
