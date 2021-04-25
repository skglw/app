package com.example.client_app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity);
        final ArrayAdapter <MapItem> adp = new ArrayAdapter <MapItem> (this, android.R.layout.simple_list_item_1);

        ListView lv = findViewById(R.id.lst_maps);
        lv.setAdapter(adp);

        String url = Config.api + "/rpc/get_maps";
        String msg = "{\"tok\": \"" + Config.token + "\"}";
        HTTPRequest req = new HTTPRequest(url, msg, this)
        {
            public void on_result(String res)
            {
                if (res.length() == 0)
                {
                    Toast.makeText(act, "request failed", Toast.LENGTH_SHORT).show();
                    return;
                }

                adp.clear();

                try
                {
                    JSONArray ja = new JSONArray(res);

                    for (int i = 0; i < ja.length(); i++)
                    {
                        JSONObject jo = ja.getJSONObject(i);

                        int map_id = jo.getInt("mid");
                        String map_name = jo.getString("mname");

                        adp.add(new MapItem(map_id, map_name));
                    }
                }
                catch (JSONException ex) { ex.printStackTrace(); }

                adp.notifyDataSetChanged();
            }
        };

        Thread th = new Thread(req);
        th.start();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String name = parent.getItemAtPosition(position).toString();
                MapItem map = (MapItem) parent.getItemAtPosition(position);
                int map_id = map.id;
                Toast.makeText(
                        ListActivity.this,
                        "itemClick: position: " + position + ", id: "+ id + ", name: " + name+ ", map_id: " + map_id,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean exit = false;
    @Override
    public void onBackPressed() {
        if (exit) {
            String url = Config.api + "/rpc/close_session";
            String msg = "{\"tok\": \"" + Config.token + "\"}";
            HTTPRequest req = new HTTPRequest(url, msg, this) {
                public void on_result(String res) {
                    if (res.length() == 0) {
                        Toast.makeText(act, "request failed", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (res.equals("null")) {
                        Toast.makeText(act, "invalid credentials", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if(res.equals("true")){
                        ListActivity.this.finish();
                    }
                    else Toast.makeText(ListActivity.this, res, Toast.LENGTH_SHORT).show();
                }
            };

            Thread th = new Thread(req);
            th.start();
        }
        else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
                    exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }
    }
}