package app.wffr.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.net.URL;

import app.wffr.R;
import app.wffr.models.Product;
import app.wffr.repositories.LocalRepo;
import app.wffr.ui.activities.ProductPreviewActivity;
import app.wffr.ui.activities.SplashScreenActivity;

public class WffrFirebaseMessagingService  extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Intent notificationIntent = new Intent(this, ProductPreviewActivity.class);
        Product product = new Product();
        product.set_id(remoteMessage.getData().get("product_id"));
        notificationIntent.putExtra("product", product);
        PendingIntent pIntentNotifications = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, 0);

        Intent generalNotification = new Intent(this, SplashScreenActivity.class);
        PendingIntent pIntentGeneralNotifications = PendingIntent.getActivity(getApplicationContext(), 0, generalNotification, 0);

        Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        //Uri notificationSound = Uri.parse("android.resource://" + this.getPackageName() + "/" + R.raw.swiftly);
        Bitmap image = null;
        try {
            URL url = new URL(remoteMessage.getData().get("img"));
            image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch(IOException e) {
            System.out.println(e);
        }
        String lang = LocalRepo.getLanguage(this);
        NotificationManager notificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);

        String notificationChannelId = "my_channel_id_01";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel =
                    new NotificationChannel(notificationChannelId, "My Notifications",
                            NotificationManager.IMPORTANCE_HIGH);

            notificationChannel.setDescription("Channel description");
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();
            notificationChannel.setSound(notificationSound, audioAttributes);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), notificationChannelId);

        builder.setAutoCancel(false)
                .setSmallIcon(R.drawable.playstore)
                .setLargeIcon(image)
                .setContentTitle(remoteMessage.getData().get(lang.equals("en")? "title_en" : "title_ar"))
                .setSound(notificationSound)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        builder.setContentText(remoteMessage.getData().get(lang.equals("en")? "body_en" : "body_ar"));
        if (remoteMessage.getData().get("product_id").equals("0")){
            builder.setContentIntent(pIntentGeneralNotifications);
        }else {
            builder.setContentIntent(pIntentNotifications);
        }
        notificationManager.notify((int) System.currentTimeMillis(), builder.build());

    }


}