package app.wffr.repositories;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import app.wffr.api.AccessApi;
import app.wffr.api.LoginRegisterApi;
import app.wffr.models.User;
import app.wffr.utils.Constants;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginRegisterRepo {

    private MutableLiveData<User> userResponse;
    private MutableLiveData<String> userUpdateResponse;
    private MutableLiveData<String> phoneVerifyResponse;
    private MutableLiveData<String> passResponse;
    private MutableLiveData<Boolean> isUpdating = new MutableLiveData<>();
    private LoginRegisterApi loginRegisterApi;


    public LoginRegisterRepo (){
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

        loginRegisterApi = retrofit.create(LoginRegisterApi.class);
    }

    public void registerNewUser(User user){
        userResponse = new MutableLiveData<>();
        Call<User> call = loginRegisterApi.insertUser(
                AccessApi.getHeaders(),
                user
        );

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                System.out.println("STATE COOOOOOOOOOOOOOOOOOODE : " + response.code() + "MEEEEESSSAAGEE : " + response.message());
                System.out.println("ERRRRRRRROR : " + response.errorBody() + "URL : " + call.request().url());
                System.out.println("WE ARE HERE 0");
                isUpdating.postValue(false);
                if (!response.isSuccessful()){
                    userResponse.postValue(null);
                    return;
                }
                userResponse.postValue(response.body());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                System.out.println("WE ARE HERE 2");
                isUpdating.postValue(false);
                userResponse.postValue(null);
            }
        });
    }

    public void updateUser(User user){
        userUpdateResponse = new MutableLiveData<>();
        Call<String> call = loginRegisterApi.updateUser(
                AccessApi.getHeaders(),
                user
        );

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                System.out.println("STATE COOOOOOOOOOOOOOOOOOODE : " + response.code() + "MEEEEESSSAAGEE : " + response.message());
                System.out.println("ERRRRRRRROR : " + response.errorBody() + "URL : " + call.request().url());
                System.out.println("WE ARE HERE 0");
                isUpdating.postValue(false);
                if (!response.isSuccessful()){
                    userUpdateResponse.postValue(null);
                    return;
                }
                userUpdateResponse.postValue(response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                System.out.println("WE ARE HERE 2");
                isUpdating.postValue(false);
                userUpdateResponse.postValue(null);
            }
        });
    }

    public void verifyPhone(int userId, int code){
        phoneVerifyResponse = new MutableLiveData<>();
        Call<String> call = loginRegisterApi.checkVerificationCode(
                AccessApi.getHeaders(),
                userId,
                code
        );

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                isUpdating.postValue(false);
                if (!response.isSuccessful()){
                    phoneVerifyResponse.postValue(null);
                    return;
                }
                phoneVerifyResponse.postValue(response.body());
                System.out.println(response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                isUpdating.postValue(false);
                phoneVerifyResponse.postValue(null);
            }
        });
    }

    public void sendVerifyPhoneCode(String phone){
        phoneVerifyResponse = new MutableLiveData<>();
        Call<String> call = loginRegisterApi.sendVerificationCode(
                AccessApi.getHeaders(),
                phone
        );

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                isUpdating.postValue(false);
                if (!response.isSuccessful()){
                    phoneVerifyResponse.postValue(null);
                    return;
                }
                phoneVerifyResponse.postValue(response.body());
                System.out.println(response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                isUpdating.postValue(false);
                phoneVerifyResponse.postValue(null);
            }
        });
    }

    public void resendVerifyCode(String phone){

        /*Call<ResendCodeResponse> call = loginRegisterApi.resendCode(
                AccessApi.getHeaders(),
                phone
        );

        call.enqueue(new Callback<ResendCodeResponse>() {
            @Override
            public void onResponse(Call<ResendCodeResponse> call, Response<ResendCodeResponse> response) {
                isUpdating.postValue(false);
                if (!response.isSuccessful()){
                    ResendCodeResponse verifyResponse = new ResendCodeResponse("0", "Server Error", null);
                    resendCodeResponse.postValue(verifyResponse);
                    return;
                }
                resendCodeResponse.postValue(response.body());
            }

            @Override
            public void onFailure(Call<ResendCodeResponse> call, Throwable t) {
                isUpdating.postValue(false);
                ResendCodeResponse verifyResponse = new ResendCodeResponse("0", "Server Error", null);
                resendCodeResponse.postValue(verifyResponse);
            }
        });*/
    }

    public void resetPassword(String phone, String lang){
        passResponse = new MutableLiveData<>();
        Call<String> call = loginRegisterApi.forgetPassword(
                AccessApi.getHeaders(),
                phone,
                lang
        );

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                isUpdating.postValue(false);
                if (!response.isSuccessful()){
                    passResponse.postValue(null);
                    return;
                }
                passResponse.postValue(response.body());
                System.out.println(response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                isUpdating.postValue(false);
                passResponse.postValue(null);
            }
        });
    }

    public void loginUser(String email, String password){
        userResponse = new MutableLiveData<>();
        Call<User> call = loginRegisterApi.loginUser(
                AccessApi.getHeaders(),
                email,
                password
        );

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                System.out.println("STATE COOOOOOOOOOOOOOOOOOODE : " + response.code() + "MEEEEESSSAAGEE : " + response.message());
                System.out.println("ERRRRRRRROR : " + response.errorBody() + "URL : " + call.request().url());
                System.out.println("WE ARE HERE 0");
                isUpdating.postValue(false);
                if (!response.isSuccessful()){
                    userResponse.postValue(null);
                    return;
                }
                userResponse.postValue(response.body());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                System.out.println("WE ARE HERE 2");
                isUpdating.postValue(false);
                userResponse.postValue(null);
            }
        });
    }

    public MutableLiveData<Boolean> getIsUpdating() {
        return isUpdating;
    }

    public MutableLiveData<User> getServerResponse() {
        return userResponse;
    }

    public MutableLiveData<String> getUserUpdateResponse() {
        return userUpdateResponse;
    }

    public MutableLiveData<String> getVerificationResponse() {
        return phoneVerifyResponse;
    }

    public MutableLiveData<String> getPassResponse() {
        return passResponse;
    }


}