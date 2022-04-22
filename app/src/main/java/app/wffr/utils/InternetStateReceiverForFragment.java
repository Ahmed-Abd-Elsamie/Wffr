package app.wffr.utils;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class InternetStateReceiverForFragment extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (utils.checkInternetConnection((Activity) context)){

        }else {
            /*Toast toast = new Toast(context);
            toast.setDuration(Toast.LENGTH_LONG);

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.internet_state, null);
            toast.setView(view);
            toast.show();*/
            //utils.showInternetDialog((Activity) context);
        }
    }
}