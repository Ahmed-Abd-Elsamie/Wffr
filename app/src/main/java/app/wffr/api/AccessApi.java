package app.wffr.api;

import java.util.HashMap;
import java.util.Map;

public class AccessApi {

    static {
        System.loadLibrary("native-lib");
    }

    public static Map<String, String> getHeaders(){
        Map<String, String> headers = new HashMap<>();
        headers.put("access", stringFromJNI());
        headers.put("Accept", "application/json");
        return headers;
    }

    public static native String stringFromJNI();

}