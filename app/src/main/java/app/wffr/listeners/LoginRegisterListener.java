package app.wffr.listeners;

import app.wffr.models.User;

public interface LoginRegisterListener {

    void OnFailure(String message);
    void OnUserRegister(User user);
    void OnUserUpdated(String state);
    void OnCodeSent(String state);
    void OnApiNotFound(String message);
    void OnPhoneVerification(String response);
    void OnCodeResend(String resendCodeResponse);

}