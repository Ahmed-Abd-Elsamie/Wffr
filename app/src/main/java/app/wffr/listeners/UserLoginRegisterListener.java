package app.wffr.listeners;

import androidx.annotation.NonNull;

import app.wffr.models.User;

public interface UserLoginRegisterListener {

    void onComplete(@NonNull User user, boolean isSuccess);
    void onUpdateComplete(@NonNull String state, boolean isSuccess);

}