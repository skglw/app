package com.example.client_app;


import android.app.Activity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HTTPRequest implements Runnable {

    Activity act;
    String url;
    String msg;

    String request() throws IOException
    {
        URL u = new URL(url);
        HttpURLConnection c = (HttpURLConnection) u.openConnection();

        c.setRequestMethod("POST");
        c.setRequestProperty("Content-Type", "application/json");

        OutputStream os = c.getOutputStream();

        byte [] bo = msg.getBytes();
        os.write(bo);

        InputStream is = c.getInputStream();

        byte [] bi = new byte[1024];
        String r = "";

        while (true)
        {
            int n = is.read(bi);
            if (n < 0) break;

            r += new String(bi, 0, n);
        }

        os.close();
        is.close();

        c.disconnect();

        return r;
    }

    public HTTPRequest(String url, String msg, Activity act)
    {
        this.url = url;
        this.msg = msg;
        this.act = act;
    }

    public void run()
    {
        String r = "";

        try {
            r = request();
        }
        catch (IOException ex) { ex.printStackTrace(); }

        final String finalR = r;
        act.runOnUiThread(new Runnable()
        {
            public void run()
            {
                on_result(finalR);
            }
        });
    }

    public void on_result(String t)
    {
    }
}