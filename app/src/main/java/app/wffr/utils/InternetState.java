package app.wffr.utils;

import app.wffr.listeners.InternetConnectionListener;

public class InternetState {

    private InternetConnectionListener internetConnectionListener;
    private InternetStateReceiver internetStateReceiver = new InternetStateReceiver();
    public InternetState(InternetConnectionListener internetConnectionListener) {
        this.internetConnectionListener = internetConnectionListener;
    }



}
