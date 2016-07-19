package com.labs.dm.wifi.autosigning;

import android.content.Context;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

/**
 * Created by Daniel Mroczka on 7/18/2016.
 */
public class ForwardedPage {

    public void execute(Context context, String url) throws IOException {
        String content = Utils.captureHtmlPage(url);
        FileOutputStream outputStream1, outputStream2;

        try {
            outputStream1 = context.openFileOutput(new Date().getTime() + ".txt", Context.MODE_PRIVATE);
            outputStream1.write(content.getBytes());
            outputStream1.close();
            outputStream2 = context.openFileOutput(new Date().getTime() + ".url", Context.MODE_PRIVATE);
            outputStream2.write(url.getBytes());
            outputStream2.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
