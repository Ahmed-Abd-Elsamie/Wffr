package app.wffr.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import app.wffr.api.AccessApi;
import app.wffr.api.ComplaintsApi;
import app.wffr.api.FeedbackApi;
import app.wffr.listeners.UserComplaintsListener;
import app.wffr.models.Complaint;
import app.wffr.models.FeedbackModel;
import app.wffr.utils.Constants;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ComplaintsRepo {

    private static ComplaintsApi complaintsApi;
    private static FeedbackApi feedbackApi;
    private MutableLiveData<String> feedResponse;
    private MutableLiveData<Boolean> isUpdating = new MutableLiveData<>();
    private UserComplaintsListener userComplaintsListener;


    public ComplaintsRepo (UserComplaintsListener userComplaintsListener){
        this.userComplaintsListener = userComplaintsListener;
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

        complaintsApi = retrofit.create(ComplaintsApi.class);
    }

    public ComplaintsRepo (){
        feedResponse = new MutableLiveData<String>();
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

        feedbackApi = retrofit.create(FeedbackApi.class);
    }

    public void insertComplaint(Complaint complaint, String clientId){
        isUpdating.setValue(true);

        Call<String> call = complaintsApi.insertComplaint(
                AccessApi.getHeaders(),
                clientId,
                complaint
        );

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                System.out.println("ERRRROOOOOOOOOOOOOOOOOOOR 123" + response.message() + "  " + response.body() + call.request().toString());
                if (!response.isSuccessful()){
                    return;
                }
                userComplaintsListener.onComplete(response.body());
                isUpdating.postValue(false);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                System.out.println("ERRRROOOOOOOOOOOOOOOOOOOR2 " + t.getMessage());
                isUpdating.postValue(false);
            }
        });
    }

    public void sendFeedback(FeedbackModel feedbackModel){
        isUpdating.setValue(true);
        Call<String> call = feedbackApi.sendFeedBack(
                AccessApi.getHeaders(),
                feedbackModel.getLike(),
                feedbackModel.getFair(),
                feedbackModel.getDislike()
        );

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (!response.isSuccessful()){
                    return;
                }
                feedResponse.setValue(response.body());
                isUpdating.postValue(false);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                isUpdating.postValue(false);
            }
        });
    }

    public LiveData<Boolean> getIsUpdating(){
        return isUpdating;
    }

    public MutableLiveData<String> getFeedResponse(){
        return feedResponse;
    }


}