package app.wffr.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import app.wffr.ui.activities.NotificationsActivity;

public class NotificationPublisherReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //String message = intent.getStringExtra("message");
        //Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        Intent i = new Intent(context, NotificationsActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(new Intent(i));
    }
}