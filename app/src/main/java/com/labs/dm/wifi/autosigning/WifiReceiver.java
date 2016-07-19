package com.labs.dm.wifi.autosigning;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Daniel Mroczka on 7/7/2016.
 */
public class WifiReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("", "WifiReceiver");
        NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
        if (info != null && info.isConnected()) {
            new Thread(new CheckThread(context)).start();
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            String ssid = wifiInfo.getSSID();
        }
    }

    public class CheckThread implements Runnable {
        private static final String WALLED_URL = "http://clients3.google.com/generate_204";
        private static final int WALLED_GARDEN_SOCKET_TIMEOUT_MS = 10000;
        private final Context context;

        public CheckThread(Context context) {
            this.context = context;
        }

        @Override
        public void run() {
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(WALLED_URL);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setInstanceFollowRedirects(false);
                urlConnection.setConnectTimeout(WALLED_GARDEN_SOCKET_TIMEOUT_MS);
                urlConnection.setReadTimeout(WALLED_GARDEN_SOCKET_TIMEOUT_MS);
                urlConnection.setUseCaches(false);
                urlConnection.getInputStream();
                // We got a valid response, but not from the real google
                int responseCode = urlConnection.getResponseCode();
                if (responseCode == 204) {
                    Log.i("HttpResponseCode", "OK " + String.valueOf(responseCode));
                    //new ForwardedPage().execute(context, "http://www.google.com");
                } else if (responseCode == 301 || responseCode == 302) {
                    Log.i("HttpResponseCode", "Moved " + String.valueOf(responseCode));
                    String newUrl = urlConnection.getHeaderField("Location");
                    if (newUrl != null) {
                        new ForwardedPage().execute(context, newUrl);
                    }
                } else {
                    Log.i("HttpResponseCode", "Unrecognized code " + String.valueOf(responseCode));
                }

            } catch (IOException e) {
                Log.e("Log", e.getMessage());
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
        }
    }

}