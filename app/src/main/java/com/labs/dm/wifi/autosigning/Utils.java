package com.labs.dm.wifi.autosigning;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Daniel Mroczka on 7/8/2016.
 */
public class Utils {

    /**
     * Returns context of html page
     *
     * @param url
     * @return
     * @throws IOException
     */
    public static String captureHtmlPage(String url) throws IOException {

        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(url);
        HttpResponse response = client.execute(request);

        String html;
        InputStream in = response.getEntity().getContent();
        StringBuilder str = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                str.append(line);
            }
        } finally {
            in.close();
        }
        html = str.toString();
        return html;
    }
}
