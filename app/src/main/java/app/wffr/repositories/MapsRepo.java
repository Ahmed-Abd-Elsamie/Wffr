package app.wffr.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.concurrent.TimeUnit;

import app.wffr.api.AccessApi;
import app.wffr.api.LocationApi;
import app.wffr.models.MapsResponse;
import app.wffr.utils.Constants;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapsRepo {

    private static LocationApi locationApi;
    private MutableLiveData<List<MapsResponse>> locations;
    private MutableLiveData<Boolean> isUpdating = new MutableLiveData<>();


    public MapsRepo(){
        isUpdating.setValue(true);
        locations = new MutableLiveData<List<MapsResponse>>();
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

        locationApi = retrofit.create(LocationApi.class);
    }

    public void getLocations(String fromx, String tox, String fromy, String toy){
        Call<List<MapsResponse>> call = locationApi.getNearLocations(AccessApi.getHeaders(), fromx, tox, fromy, toy);
        call.enqueue(new Callback<List<MapsResponse>>() {
            @Override
            public void onResponse(Call<List<MapsResponse>> call, Response<List<MapsResponse>> response) {
                System.out.println("MAAAAPPPPPSSS 1 : " + response.body());
                if (!response.isSuccessful()){
                    System.out.println("MAAAAPPPPPSSS 2 : " + response.body());
                    return;
                }
                locations.setValue(response.body());
                isUpdating.postValue(false);
            }

            @Override
            public void onFailure(Call<List<MapsResponse>> call, Throwable t) {
                System.out.println("MAAAAPPPPPSSS 3 : " + t.getMessage());
                isUpdating.postValue(false);
            }
        });
    }

    public MutableLiveData<List<MapsResponse>> getLocations(){
        return locations;
    }

    public LiveData<Boolean> getIsUpdating(){
        return isUpdating;
    }


}