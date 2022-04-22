package app.wffr.repositories;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import app.wffr.models.City;
import app.wffr.models.User;

public class LocalRepo {

    public static void saveUserData(Activity context, User user){
        SharedPreferences pref = context.getSharedPreferences("auth", 0);
        SharedPreferences.Editor editor = pref.edit();
        if (user != null){
            editor.putString("id", user.getId());
            editor.putString("name", user.getName());
            editor.putString("email", user.getEmail());
            editor.putString("password", user.getPassword());
            editor.putString("phone", user.getPhone());
            editor.putString("img", user.getImg());
            editor.putBoolean("gender", user.getGender());
            editor.putString("creationDate", user.getCreationDate());
            editor.commit();
        }else {
            editor.clear();
            editor.commit();
        }
    }

    public static User getUserData(Context context){
        SharedPreferences pref = context.getSharedPreferences("auth", 0);
        User user = null;
        if (pref.getString("name", null) != null){
            user = new User(
                    pref.getString("id", null),
                    pref.getString("name", null),
                    pref.getString("email", null),
                    pref.getString("password", null),
                    pref.getString("phone", null),
                    pref.getString("img", null),
                    pref.getBoolean("gender", false)
            );
            user.setCreationDate(pref.getString("creationDate", null));
        }
        return user;
    }

    public static void saveCity(Activity context, City city){
        SharedPreferences pref = context.getSharedPreferences("city", 0);
        SharedPreferences.Editor editor = pref.edit();
        if (city != null){
            editor.putString("id", city.getId());
            editor.putString("name", city.getName());
            editor.putString("nameen", city.getNameen());
            editor.commit();
        }else {
            editor.clear();
            editor.commit();
        }
    }

    public static City getCity(Context context){
        SharedPreferences pref = context.getSharedPreferences("city", 0);
        City city = null;
        if (pref.getString("name", null) != null){
            city = new City(
                    pref.getString("id", null),
                    pref.getString("name", null),
                    pref.getString("nameen", null),
                    null
            );
        }
        return city;
    }

    public static void saveLanguage(Activity context, String language){
        SharedPreferences pref = context.getSharedPreferences("language", 0);
        SharedPreferences.Editor editor = pref.edit();
        if (language != null){
            editor.putString("lang", language);
            editor.commit();
        }else {
            editor.clear();
            editor.commit();
        }
    }

    public static String getLanguage(Context context){
        SharedPreferences pref = context.getSharedPreferences("language", 0);
        String language = "ar";
        if (pref.getString("lang", null) != null){
            language = pref.getString("lang", null);
        }
        return language;
    }

    public static void setFirstUse(Context context, boolean first){
        SharedPreferences pref = context.getSharedPreferences("first", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("first", first);
        editor.commit();
    }

    public static boolean isFirstUse(Context context){
        SharedPreferences pref = context.getSharedPreferences("first", 0);
        return pref.getBoolean("first", true);
    }

    public static void saveFeedback(Context context, boolean feedback){
        SharedPreferences pref = context.getSharedPreferences("feedback", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("feedback", feedback);
        editor.commit();
    }

    public static boolean getFeedback(Context context){
        SharedPreferences pref = context.getSharedPreferences("feedback", 0);
        return pref.getBoolean("feedback", false);
    }

}