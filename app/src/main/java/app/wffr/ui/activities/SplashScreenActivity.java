package app.wffr.ui.activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import org.jsoup.Jsoup;

import java.io.IOException;

import app.wffr.BuildConfig;
import app.wffr.MainActivity;
import app.wffr.R;
import app.wffr.WffrBaseActivity;
import app.wffr.models.Product;
import app.wffr.repositories.LocalRepo;
import app.wffr.utils.AppData;
import app.wffr.utils.utils;

public class SplashScreenActivity extends WffrBaseActivity {

    private String currentVersion = "";
    private String latestVersion = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new GetLatestVersion().execute();

        utils.setAppLocale(LocalRepo.getLanguage(this), this);
        AppData.appLanguage = LocalRepo.getLanguage(this);



    }

    private void pushWelcomeNotification() {
        Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pIntentNotifications = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, 0);
        NotificationManager notificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);

        String notificationChannelId = "my_channel_id_01";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel =
                    new NotificationChannel(notificationChannelId, "My Notifications",
                            NotificationManager.IMPORTANCE_HIGH);

            notificationChannel.setDescription("Channel description");
            notificationChannel.enableLights(false);
            notificationChannel.setLightColor(Color.RED);
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();
            notificationChannel.setSound(notificationSound, audioAttributes);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), notificationChannelId);
        Drawable myDrawable = getResources().getDrawable(R.drawable.playstore);
        Bitmap largeImg = ((BitmapDrawable) myDrawable).getBitmap();

        builder.setAutoCancel(true)
                .setSmallIcon(R.drawable.playstore)
                .setLargeIcon(largeImg)
                .setContentTitle("مرحبا بك في عائله وفر  \uD83D\uDC69\u200D\uD83D\uDC69\u200D\uD83D\uDC67\u200D\uD83D\uDC66 ")
                .setContentText("مع وفر هاتقدر توفر \uD83D\uDCAA ")
                .setSound(notificationSound)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        builder.setContentIntent(pIntentNotifications);

        notificationManager.notify(1, builder.build());
    }

    private class GetLatestVersion extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                latestVersion = Jsoup.connect("https://play.google.com/store/apps/details?id="
                + getPackageName()).timeout(30000)
                        .get().select("div.hAyfc:nth-child(4)>" +
                                "span:nth-child(2) > div:nth-child(1)" +
                                "> span:nth-child(1)")
                        .first()
                        .ownText();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return latestVersion;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            currentVersion = BuildConfig.VERSION_NAME;
            if (!latestVersion.equals("")){
                float cv = Float.parseFloat(currentVersion);
                float lv = Float.parseFloat(latestVersion);

                if (lv > cv){
                    showUpdateDialog();
                }else {
                    new CountDownTimer(1000, 5000){

                        @Override
                        public void onTick(long millisUntilFinished) {

                        }

                        @Override
                        public void onFinish() {
                            ConnectivityManager connectivityManager = (ConnectivityManager) SplashScreenActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
                            if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED){
                                Toast.makeText(SplashScreenActivity.this, R.string.data_conn, Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(SplashScreenActivity.this, R.string.wifi_conn, Toast.LENGTH_SHORT).show();
                            }
                            Bundle bundle = getIntent().getExtras();
                            if (bundle != null) {
                                if (bundle.containsKey("product_id")){
                                    String pid = bundle.getString("product_id");
                                    if (pid.equals("0")){
                                        Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }else {
                                        Product product = new Product();
                                        product.set_id(pid);
                                        Intent notificationIntent = new Intent(SplashScreenActivity.this, ProductPreviewActivity.class);
                                        notificationIntent.putExtra("product", product);
                                        startActivity(notificationIntent);
                                        finish();
                                    }
                                }else {
                                    Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }


                            }else {
                                Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    }.start();

                    if ((LocalRepo.isFirstUse(SplashScreenActivity.this))){
                        pushWelcomeNotification();
                        LocalRepo.setFirstUse(SplashScreenActivity.this, false);
                    }
                }
            }

        }
    }

    private void showUpdateDialog() {
        android.app.AlertDialog builder = new android.app.AlertDialog.Builder(this).create();
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_update, null);
        builder.setView(view);
        builder.setCancelable(false);
        builder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Button btnOk = view.findViewById(R.id.btn_done);

        btnOk.setOnClickListener(v -> {
            Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=app.wffr"); // missing 'http://' will cause crashed
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });

        builder.show();
    }

}