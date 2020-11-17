package com.example.networks06;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import static android.widget.Toast.LENGTH_SHORT;
import static com.example.networks06.R.id;
import static com.example.networks06.R.layout;

public class MainActivity extends AppCompatActivity {

    private Button btnisconnected, btngetweather;
    private TextView txtviewgetweather;
    City city;
    RequestQueue queue ;
    //String apikey = c087abf185422b4f711420e85c83b7e3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_main);
        btngetweather = findViewById(id.btngetweather);
        txtviewgetweather = findViewById(id.txtviewgetweather);

        //init();

        btngetweather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //txtviewgetweather.setText((CharSequence) city.getWeather());
                init();
            }
        });


    btnisconnected = findViewById(id.btnisconnected);
        btnisconnected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String con = String.valueOf(isConnectedToNetworkinfo(getBaseContext()));
               Toast.makeText(getBaseContext(), con, LENGTH_SHORT).show();
               if (con == "true"){
                   String coninfo = String.valueOf(connectioninfo());
                   Toast.makeText(getBaseContext(), coninfo,Toast.LENGTH_LONG).show();

               }
            }
        });
    }

    public static boolean isConnectedToNetworkinfo(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected()
                && activeNetworkInfo.isAvailable();
    }
    /*
    public String connectioninfo(){
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        String ssid = wifiInfo.getSSID();
        return wifiInfo + ssid;
    }
     */
    public String connectioninfo(){
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return String.valueOf(activeNetworkInfo);
    }

    private void init(){
        city = new City();
        loaddata();
    }

    private void loaddata(){
        final String url =
                "http://api.openweathermap.org/data/2.5/weather?q=Aarhus,dk&APPID=c087abf185422b4f711420e85c83b7e3";
    sendRequest(url);
    }

    private void sendRequest(String url){
        if (queue == null) {
            queue = Volley.newRequestQueue(this);
        }
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // display response
                        Log.d("Response", response.toString());
                        GsonBuilder gsonBuilder = new GsonBuilder();
                        Gson gson = gsonBuilder.create();
                        City city = gson.fromJson(response, City.class);
                        txtviewgetweather.setText("Weather in Aarhus: " + city.getWeather()
                                .get(0).getDescription());
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", String.valueOf(error));
                    }
                }
        );
        queue.add(stringRequest);
        }
}