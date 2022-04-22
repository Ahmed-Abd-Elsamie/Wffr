package app.wffr.utils;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class InternetStateReceiver extends BroadcastReceiver {

    boolean aBoolean = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!aBoolean){
            if (!utils.checkInternetConnection((Activity) context)) {
                utils.showInternetDialog((Activity) context);
            }
            aBoolean = true;
        }
    }


}