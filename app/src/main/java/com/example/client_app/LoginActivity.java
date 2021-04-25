package com.example.client_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;


public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
    }

    public void on_login_click(View v)
    {
        String name = ((EditText) findViewById(R.id.txt_name)).getText().toString();
        String secret = ((EditText) findViewById(R.id.txt_secret)).getText().toString();

        String url = Config.api + "/rpc/open_session";
        String msg = "{\"uname\": \"" + name + "\", \"usecret\": \"" + secret + "\"}";

        HTTPRequest req = new HTTPRequest(url, msg, this)
        {
            public void on_result(String res)
            {
                if (res.length() == 0)
                {
                    Toast.makeText(act, "request failed", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (res.equals("null"))
                {
                    Toast.makeText(act, "invalid credentials", Toast.LENGTH_SHORT).show();
                    return;
                }

                res = res.replace("\"", ""); // remove quotes
                Toast.makeText(act, "token: " + res, Toast.LENGTH_SHORT).show();
                Config.token = res;

                Intent in = new Intent(act, ListActivity.class);
                startActivity(in);
            }
        };

        Thread th = new Thread(req);
        th.start();
    }
}