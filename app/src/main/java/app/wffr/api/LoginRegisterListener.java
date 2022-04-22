package app.wffr.api;

public interface LoginRegisterListener {

    void onFailure(String message);
    void onVerificationFailed(String error);
    void onVerificationComplete(String code);
    void onVerificationCodeSent(String message);

}