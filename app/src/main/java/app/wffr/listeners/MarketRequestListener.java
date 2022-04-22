package app.wffr.listeners;

import androidx.annotation.NonNull;

public interface MarketRequestListener {

    void onComplete(@NonNull String message, boolean isSuccess);

}