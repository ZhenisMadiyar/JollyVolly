package com.example.jv.jollyvolly;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.jv.jollyvolly.tabs.maps.Car;
import com.google.gson.Gson;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by madiyarzhenis on 03.09.15.
 */
public class SplashActivity extends Activity {

    HashMap<String, Object> mapBanner;
    public static ArrayList<Car> arrayList;
    Gson gson;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        progressBar = (ProgressBar) findViewById(R.id.progressBarSplash);
        mapBanner = new HashMap<String, Object>();
        gson = new Gson();
        arrayList = new ArrayList<>();
        if (isOnline()) {
            ParseCloud.callFunctionInBackground("cars", mapBanner, new FunctionCallback<Object>() {
                public void done(Object response, ParseException e) {
                    if (e != null) {

                    } else {
                        arrayList.clear();
                        String json = gson.toJson(response);
                        if (json.equals("[]")) {
                            Toast.makeText(getApplicationContext(), "car NULL", Toast.LENGTH_LONG).show();
                        } else {
                            Log.i("JSON_BANNER", json);
                            try {
                                JSONArray jsonArray = new JSONArray(json);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    JSONObject estimatedData = jsonObject.getJSONObject("estimatedData");
//                                    JSONObject jsonImage = estimatedData.getJSONObject("image");

//                                    String carId = estimatedData.getString("id");
                                    String objectId = jsonObject.getString("objectId");
                                    String status = estimatedData.getString("status");
                                    String time = estimatedData.getString("time");
                                    String lat_lang = estimatedData.getString("lat_lang");
                                    String address = estimatedData.getString("address");
                                    int id_car = estimatedData.getInt("id_car");
//                                    String latLang[] = lat_lang.split(",");
//                                    double lat = Double.parseDouble(latLang[0]);
//                                    double lng = Double.parseDouble(latLang[1]);
//                                    LatLng latLng = new LatLng(lat, lng);
                                    arrayList.add(new Car(objectId, status, lat_lang, time, address, id_car));
                                }
                                progressBar.setVisibility(View.GONE);
                                Intent intent = new Intent(SplashActivity.this, TabsActivity.class);
                                intent.putParcelableArrayListExtra("carData", arrayList);
                                startActivity(intent);
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
//                        Toast.makeText(getApplicationContext(), "banner not NULL", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });
        } else {

        }

    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
