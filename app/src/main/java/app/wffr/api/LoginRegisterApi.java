package app.wffr.api;

import java.util.Map;

import app.wffr.models.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface LoginRegisterApi {

    @GET("clients")
    Call<User> loginUser(
            @HeaderMap Map<String, String> headers,
            @Query("email") String email,
            @Query("password") String password
    );

    @POST("clients?imgType=true")
    Call<User> insertUser(
            @HeaderMap Map<String, String> headers,
            @Body User user
    );

    @POST("clients?imgType=true")
    Call<String> updateUser(
            @HeaderMap Map<String, String> headers,
            @Body User user
    );

    @POST("clients?imgType=true")
    Call<String> updateUser(
            @HeaderMap Map<String, String> headers,
            @Query("id") String id,
            @Query("name") String name,
            @Query("email") String email,
            @Query("password") String password,
            @Query("phone") String phone,
            @Query("img") String img,
            @Query("gender") boolean gender
    );

    @POST("clients")
    Call<String> updatePassword(
            @HeaderMap Map<String, String> headers,
            @Query("clientid") String clientId,
            @Query("oldPassword") String oldPass,
            @Query("newPassword") String newPass
    );

    @POST("clients")
    Call<String> recoverPassword(
            @HeaderMap Map<String, String> headers,
            @Query("Email") String email,
            @Query("lang") String language
    );

    @POST("Clients/ActiveClient")
    Call<String> verifyEmail(
            @HeaderMap Map<String, String> headers,
            @Query("ClientID") String clientId
    );

    // only for huawei devices
    /*@POST("Clients/CheckClientCode")
    Call<String> checkVerificationCode(
            @HeaderMap Map<String, String> headers,
            @Query("phone") String phone,
            @Query("code") int code
    );*/

    // only for google play supported devices
    @POST("Clients/ActiveClient")
    Call<String> checkVerificationCode(
            @HeaderMap Map<String, String> headers,
            @Query("ClientID") int userId,
            @Query("code") int code
    );

    @POST("Clients/SentSMSByPhone")
    Call<String> sendVerificationCode(
            @HeaderMap Map<String, String> headers,
            @Query("phone") String phone
    );

    @POST("Clients/ActiveClientDirect")
    Call<String> activateUser(
            @HeaderMap Map<String, String> headers,
            @Query("phone") String phone
    );

    @POST("Clients/foregetPasswordByPhone")
    Call<String> forgetPassword(
            @HeaderMap Map<String, String> headers,
            @Query("phone") String phone,
            @Query("lang") String language
    );





}