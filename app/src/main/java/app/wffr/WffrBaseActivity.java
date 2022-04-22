package app.wffr;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import app.wffr.utils.InternetStateReceiver;

public class WffrBaseActivity extends AppCompatActivity {

    private InternetStateReceiver internetStateReceiver = new InternetStateReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerReceiver(internetStateReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(internetStateReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(internetStateReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }


}