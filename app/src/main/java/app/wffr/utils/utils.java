package app.wffr.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.DisplayMetrics;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import app.wffr.R;

public class utils {

    public static boolean checkInternetConnection(Activity context){

        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED || connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            return true;
        } else{
            return false;
        }
    }

    public static boolean checkInternetConnection(Application context){

        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED || connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED){
                Toast.makeText(context, R.string.data_conn, Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(context, R.string.wifi_conn, Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        else{
            return false;
        }
    }

    public static boolean internetIsConnected() {
        try {
            String command = "ping -c 1 google.com";
            return (Runtime.getRuntime().exec(command).waitFor() == 0);
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        if (email.charAt(0) == ' '){
            return false;
        }
        return matcher.matches();
    }

    public static void showInternetDialog(Activity context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(R.layout.internet_state);
        builder.show();
    }

    public static boolean isValidMobile(String phone) {
        if (phone.charAt(0) == ' ' || phone.length() < 11){
            return false;
        }

        if (!phone.startsWith("01")){
            return false;
        }
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }

    public static boolean isValidWord(String word) {
        Pattern pattern = Pattern.compile("[^a-zA-Z0-9\u0600-\u06FF ]");
        Pattern patternFirstChar = Pattern.compile("[0-9]");

        //Pattern pattern = Pattern.compile("[^[A-Za-zأ-يء ]+[A-Za-zأ-يء0-9\\u00C0-\\u017F-'_`~!:،@#{}$/؟%?^,.;&()ًٌٍَُِ+/= ]*$]");

        Matcher matcher = pattern.matcher(word);
        Matcher matcherFirstChar = patternFirstChar.matcher(word.charAt(0)+"");
        if (word.contains("،") || word.contains("؟") || word.contains("؛")){
            return false;
        }
        if (word.charAt(0) == ' ' || matcherFirstChar.find()){
            return false;
        }
        return !matcher.find();
    }

    public static boolean isValidFirstChar(String word) {
        Pattern patternFirstChar = Pattern.compile("[0-9]");
        Matcher matcherFirstChar = patternFirstChar.matcher(word.charAt(0)+"");
        return !matcherFirstChar.find();
    }

    public static void setAppLocale(String localeCode, Context context){
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN_MR1){
            config.setLocale(new Locale(localeCode.toLowerCase()));
        } else {
            config.locale = new Locale(localeCode.toLowerCase());
        }
        resources.updateConfiguration(config, dm);
    }

    private static final int REQUEST_LOCATION_CODE = 44;

    private static String[] PERMISSIONS_LOCATION = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    public static void verifyLocationPermissions(Activity activity) {
        // Check if we have location permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION);
        int permission1 = ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION);

        if ((permission == PackageManager.PERMISSION_GRANTED) && (permission1 == PackageManager.PERMISSION_GRANTED)) {
            return;
        }
        // We don't have permission so prompt the user
        ActivityCompat.requestPermissions(
                activity,
                PERMISSIONS_LOCATION,
                REQUEST_LOCATION_CODE
        );

    }

    public static boolean checkLocationPermissions(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION);
        int permission1 = ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION);
        return permission == PackageManager.PERMISSION_GRANTED && permission1 == PackageManager.PERMISSION_GRANTED;
    }

}