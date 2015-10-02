package com.example.jv.jollyvolly.tabs.maps;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jv.jollyvolly.R;
import com.example.jv.jollyvolly.TabsActivity;
import com.example.jv.jollyvolly.tabs.maps.carousel.MyPagerAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by madiyarzhenis on 31.08.15.
 */
public class Map extends Fragment implements LocationListener {
    private FragmentActivity myContext;
    private static final LatLng DAVAO = new LatLng(7.0722, 125.6131);
    public GoogleMap map;
    private static View view;

    int FIRST_PAGE;
    public final static float BIG_SCALE = 1.0f;
    public final static float SMALL_SCALE = 0.7f;
    public final static float DIFF_SCALE = BIG_SCALE - SMALL_SCALE;
    public ViewPager pager;
    //    ArrayList<Car> arrayListCar;
    MyPagerAdapter adapter;
    public TextView statusCar;
    Marker marker_me;
    Marker marker_cars;
    Marker[] markers;
    Location location;
    double min = Double.MAX_VALUE;
    int posi;
    int nearPos;
    Gson gson;
    String api_token;
    final ArrayList<MobiliuzCar> mobiliuzCars = new ArrayList<MobiliuzCar>();
    final Handler handler = new Handler();
    int count = 0;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            Log.i("Size_array_list", TabsActivity.arrayList.size() + "");
            view = inflater.inflate(R.layout.map_fragment, container, false);
            android.app.FragmentManager fragManager = myContext.getFragmentManager();
//            map = ((MapFragment) fragManager.findFragmentById(R.id.map)).getMap();
//            map.setMyLocationEnabled(true);
//            map.getMyLocation();
//            addMarkersToMap();

            pager = (ViewPager) view.findViewById(R.id.myviewpager);
            statusCar = (TextView) view.findViewById(R.id.status_car);
            gson = new Gson();
//            arrayListCar = new ArrayList<>();
//            arrayListCar.add(new Car("1", "Working", new LatLng(43.209127, 76.652925), "9:00-21:00", "Kaskelen", "imageUrl"));
//            arrayListCar.add(new Car("2", "Not working", new LatLng(43.191348, 76.885653), "9:00-21:00", "Park", "imageUrl"));
//            arrayListCar.add(new Car("3", "Working", new LatLng(43.242731, 76.825884), "9:00-21:00", "Raiymbek", "imageUrl"));
//            arrayListCar.add(new Car("4", "On base", new LatLng(43.236864, 76.921980), "9:00-21:00", "Stadion", "imageUrl"));
            int PAGES = TabsActivity.arrayList.size();
            markers = new Marker[PAGES];
            int LOOPS = 1000;
            FIRST_PAGE = PAGES * LOOPS / 2;

            // Getting Google Play availability status
            int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());

            // Showing status
            if (status != ConnectionResult.SUCCESS) { // Google Play Services are not available

                int requestCode = 10;
                Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, getActivity(), requestCode);
                dialog.show();

            } else {
                map = ((MapFragment) fragManager.findFragmentById(R.id.map)).getMap();
                map.setMyLocationEnabled(true);
                // Getting LocationManager object from System Service LOCATION_SERVICE
                final LocationManager locationManager = (LocationManager) getActivity().getSystemService(getActivity().LOCATION_SERVICE);

                // Creating a criteria object to retrieve provider
                Criteria criteria = new Criteria();

                // Getting the name of the best provider
                final String provider = locationManager.getBestProvider(criteria, true);

                // Getting Current Location
                location = locationManager.getLastKnownLocation(provider);

                LocationListener locationListener = new LocationListener() {
                    public void onLocationChanged(Location location) {
                        // redraw the marker when get location update.
                        drawMarker(location);
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    @Override
                    public void onProviderEnabled(String provider) {

                    }

                    @Override
                    public void onProviderDisabled(String provider) {

                    }
                };
//                if (location != null) {
                //PLACE THE INITIAL MARKER
                drawMarker(location);
//                }

                locationManager.requestLocationUpdates(provider, 20000, 0, locationListener);
            }
            // Set margin for pages as a negative number, so a part of next and
            // previous pages will be showed
//            pager.setPageMargin(-200);
//            final View mapView = myContext.getFragmentManager().findFragmentById(R.id.map).getView();
//            if (mapView.getViewTreeObserver().isAlive()) {
//                mapView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//                    @SuppressLint("NewApi")
//                    @Override
//                    public void onGlobalLayout() {
//                        LatLngBounds.Builder bld = new LatLngBounds.Builder();
//                        for (int i = 0; i <= 2; i++) {
//                            String latLang[] = TabsActivity.arrayList.get(i).getLatLang().split(",");
//                            double lat = Double.parseDouble(latLang[0]);
//                            double lng = Double.parseDouble(latLang[1]);
//                            LatLng ll = new LatLng(lat, lng);
//                            bld.include(ll);
//                        }
//                        LatLngBounds bounds = bld.build();
//                        map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 10));
//                        mapView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//
//                    }
//                });
//            }


////             Getting LocationManager object from System Service LOCATION_SERVICE
//            LocationManager locationManager = (LocationManager) getActivity().getSystemService(getActivity().LOCATION_SERVICE);
//            // Creating a criteria object to retrieve provider
//            Criteria criteria = new Criteria();
//            // Getting the name of the best provider
//            String provider = locationManager.getBestProvider(criteria, true);
//            // Getting Current Location
//            Location location = locationManager.getLastKnownLocation(provider);
//            onLocationChanged(location);
//            locationManager.requestLocationUpdates(provider, 20000, 0, this);
            //-=-=-=-=-=-=
//            CameraPosition cameraPosition =
//                    new CameraPosition.Builder()
//                            .target(new LatLng(43.003212, 79.224234))
//                            .bearing(45)
//                            .tilt(90)
//                            .zoom(map.getCameraPosition().zoom)
//                            .build();
//            map.animateCamera(
//                    CameraUpdateFactory.newCameraPosition(cameraPosition),
//                    1500,
//                    new GoogleMap.CancelableCallback() {
//
//                        @Override
//                        public void onFinish() {
//                        }
//
//                        @Override
//                        public void onCancel() {
//                        }
//                    }
//            );
//            Marker davao = map.addMarker(new MarkerOptions().position(DAVAO).title("Davao City").
//                    snippet("Ateneo de Davao University"));
//
//            // zoom in the camera to Davao city
//            map.moveCamera(CameraUpdateFactory.newLatLngZoom(DAVAO, 15));
//
//            // animate the zoom process
//            map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
        } catch (InflateException e) {
        /* map is already there, just return view as it is */
        } catch (SecurityException eSec) {

        }


        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position >= TabsActivity.arrayList.size()) {
                    position = position % TabsActivity.arrayList.size();
                    Log.i("MOD_POSITION", position + "");
                    statusCar.setText(mobiliuzCars.get(position).getStatus());

//                    String latLang[] = TabsActivity.arrayList.get(position).getLatLang().split(",");
//                    double lat = Double.parseDouble(latLang[0]);
//                    double lng = Double.parseDouble(latLang[1]);
                    LatLng latLng = new LatLng(mobiliuzCars.get(position).getLat(), mobiliuzCars.get(position).getLng());
//                    Marker davao = map.addMarker(new MarkerOptions().position(latLng).title("Almaty").
//                            snippet(TabsActivity.arrayList.get(position).getAddress()));

                    // zoom in the camera to Davao city
//                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                    // animate the zoom process
//                    map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);

//                    CameraUpdate center =
//                            CameraUpdateFactory.newLatLng(latLng);
//                    CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
//                    map.moveCamera(center);
//                    map.animateCamera(zoom, 2000, null);
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(latLng)
                            .zoom(15)
                            .bearing(45)
                            .tilt(20)
                            .build();
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                    map.animateCamera(cameraUpdate);
                } else {
                    statusCar.setText(mobiliuzCars.get(position).getStatus());

//                    String latLang[] = TabsActivity.arrayList.get(position).getLatLang().split(",");
//                    double lat = Double.parseDouble(latLang[0]);
//                    double lng = Double.parseDouble(latLang[1]);
                    LatLng latLng = new LatLng(mobiliuzCars.get(position).getLat(), mobiliuzCars.get(position).getLng());

//                    Marker davao = map.addMarker(new MarkerOptions().position(latLng).title("Almaty").
//                            snippet(TabsActivity.arrayList.get(position).getAddress()));

                    // zoom in the camera to Davao city
//                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
//
//                    // animate the zoom process
//                    map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);

                    CameraUpdate center =
                            CameraUpdateFactory.newLatLng(latLng);
                    CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
                    map.moveCamera(center);
                    map.animateCamera(zoom, 2000, null);

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        return view;
    }

    private void drawMarker(final Location location) {
        // Remove any existing markers on the map

        Timer timer = new Timer();
        map.clear();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @SuppressWarnings("unchecked")
                    public void run() {
                        try {
                            Log.i("Refreshed", "Refreshed");
                            mobiliuzCars.clear();
                            HashMap<String, Object> parameter = new HashMap<String, Object>();
                            final HashMap<String, Object> parameter1 = new HashMap<String, Object>();
                            //get cars from mobiliuz
                            ParseCloud.callFunctionInBackground("access_token", parameter, new FunctionCallback<Object>() {
                                @Override
                                public void done(Object o, ParseException e) {
                                    if (e == null) {
                                        String response = gson.toJson(o);
                                        Log.i("access_token", response);
                                        if (response != null) {
                                            try {
                                                JSONObject jsonObject = new JSONObject(response);
                                                if (jsonObject.getBoolean("success")) {
                                                    api_token = jsonObject.getString("api_token");

                                                    parameter1.put("object_id", api_token);
                                                    ParseCloud.callFunctionInBackground("get_cars_list", parameter1, new FunctionCallback<Object>() {
                                                        @Override
                                                        public void done(Object o, ParseException e) {
                                                            if (e == null) {
                                                                String response = gson.toJson(o);
                                                                Log.i("access_token_car", response);
                                                                if (response != null) {
                                                                    try {
                                                                        JSONObject jsonObject = new JSONObject(response);
                                                                        JSONArray cars = jsonObject.getJSONArray("objects");
                                                                        Log.i("Cars_Size", cars.length() + "");
                                                                        for (int i = 0; i < cars.length(); i++) {
                                                                            JSONObject objects = cars.getJSONObject(i);
                                                                            String name = objects.getString("model");
                                                                            int id = objects.getInt("id");

                                                                            JSONArray last_pos = objects.getJSONArray("last_position");
                                                                            double lat = (double) last_pos.get(0);
                                                                            double lot = (double) last_pos.get(1);
                                                                            Log.i("NAME", name + "");
                                                                            Log.i("LAT_LNG", lat + "," + lot + "");
//                                                        mobiliuzCars.add(new MobiliuzCar(id, lat, lot));
                                                                            for (int j = 0; j < TabsActivity.arrayList.size(); j++) {
                                                                                if (id == TabsActivity.arrayList.get(j).id_car) {
                                                                                    Log.i("HELLO", "WORLD");
                                                                                    mobiliuzCars.add(new MobiliuzCar(id, TabsActivity.arrayList.get(j).getAddress(), TabsActivity.arrayList.get(j).getTime(),
                                                                                            TabsActivity.arrayList.get(j).getStatus(), TabsActivity.arrayList.get(j).getImageUrl(), lat, lot));
                                                                                }
                                                                            }
                                                                        }
                                                                        adapter = new MyPagerAdapter(Map.this, getFragmentManager(), mobiliuzCars);
                                                                        pager.setAdapter(adapter);
                                                                        // Set current item to the middle page so we can fling to both
                                                                        // directions left and right
                                                                        pager.setCurrentItem(FIRST_PAGE);
                                                                        // Necessary or the pager will only have one extra page to show
                                                                        // make this at least however many pages you can see
                                                                        pager.setOffscreenPageLimit(3);
                                                                        final LatLng currentPosition = new LatLng(location.getLatitude(), location.getLongitude());
                                                                        if (count == 0) {
                                                                            marker_me = map.addMarker(new MarkerOptions()
                                                                                    .position(currentPosition)
                                                                                    .snippet("Lat:" + location.getLatitude() + "Lng:" + location.getLongitude())
                                                                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                                                                                    .title("ME"));
//                                                                        map.addMarker(markerPositionsMe);

//            Marker davao = map.addMarker(new MarkerOptions().position(DAVAO).title("Davao City").
//                    snippet("Ateneo de Davao University"));
                                                                            for (int i = 0; i < mobiliuzCars.size(); i++) {
//            String latLang[] = TabsActivity.arrayList.get(i).getLatLang().split(",");
//            double lat = Double.parseDouble(latLang[0]);
//            double lng = Double.parseDouble(latLang[1]);
                                                                                LatLng ll = new LatLng(mobiliuzCars.get(i).getLat(), mobiliuzCars.get(i).getLng());
                                                                                BitmapDescriptor bitmapMarker = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
//            switch (Cars.get(i).getState()) {
//                case 0:
//                    bitmapMarker = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
//                    Log.i(TAG, "RED");
//                    break;
//                case 1:
//                    bitmapMarker = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
//                    Log.i(TAG, "GREEN");
//                    break;
//                case 2:
//                    bitmapMarker = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE);
//                    Log.i(TAG, "ORANGE");
//                    break;
//                default:
//                    bitmapMarker = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
//                    Log.i(TAG, "DEFAULT");
//                    break;
//            }
                                                                                marker_cars = map.addMarker(new MarkerOptions().position(ll).title(mobiliuzCars.get(i).getAddress())
                                                                                        .snippet(mobiliuzCars.get(i).getAddress()).icon(bitmapMarker));
                                                                                markers[i] = marker_cars;
                                                                                nearPos = CalculationByDistance(currentPosition, ll, i);


                                                                            }

                                                                            final View mapView = myContext.getFragmentManager().findFragmentById(R.id.map).getView();
                                                                            if (mapView.getViewTreeObserver().isAlive()) {
                                                                                mapView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                                                                                    @SuppressLint("NewApi")
                                                                                    @Override
                                                                                    public void onGlobalLayout() {
                                                                                        LatLngBounds.Builder bld = new LatLngBounds.Builder();
                                                                                        for (int i = 0; i <= 2; i++) {
                                                                                            if (i == 0) {
                                                                                                Log.i("NearPos", nearPos + "");
//                            String latLang[] = TabsActivity.arrayList.get(nearPos).getLatLang().split(",");
//                            double lat = Double.parseDouble(latLang[0]);
//                            double lng = Double.parseDouble(latLang[1]);
//                                double minIndex = radiusRange.indexOf(Collections.min(radiusRange));
//                                Log.i("RadiusSize", radiusRange.size() + "");
//                                for (int j = 0; j < radiusRange.size(); j++) {
//                                    if (radiusRange.get(j) == minIndex) {
//                                        Log.i("HERE", TabsActivity.arrayList.get(j).getAddress());
//                                    }
//                                }
//                                Log.i("MinIndex", minIndex+"");
                                                                                                LatLng ll = new LatLng(mobiliuzCars.get(i).getLat(), mobiliuzCars.get(i).getLng());
                                                                                                bld.include(ll);
                                                                                            }
                                                                                            if (i == 1) {
                                                                                                bld.include(currentPosition);
                                                                                            }
                                                                                        }
//                        pager.setCurrentItem(nearPos);
                                                                                        LatLngBounds bounds = bld.build();
                                                                                        map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
                                                                                        mapView.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                                                                                    }
                                                                                });
                                                                            }
                                                                            count++;
                                                                        } else {
                                                                            Log.i("Marker_Size", markers.length + "");
                                                                            for (int k = 0; k < markers.length; k++) {
                                                                                BitmapDescriptor bitmapMarker = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
                                                                                LatLng ll = new LatLng(mobiliuzCars.get(k).getLat(), mobiliuzCars.get(k).getLng());
                                                                                markers[k].setPosition(ll);
                                                                                markers[k].setTitle(mobiliuzCars.get(k).getAddress());
                                                                                markers[k].setSnippet(mobiliuzCars.get(k).getAddress());
//                                                                                markers[k].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.heart));
                                                                            }
                                                                            marker_me.setPosition(currentPosition);
                                                                            count++;
                                                                        }

                                                                    } catch (JSONException e1) {
                                                                        e1.printStackTrace();
                                                                    }
                                                                }
                                                            } else {
                                                                Log.i("error_car", e.getMessage());
                                                            }
                                                        }
                                                    });
                                                } else
                                                    Toast.makeText(getActivity(), "Problem with api_token", Toast.LENGTH_SHORT).show();
                                            } catch (JSONException e1) {
                                                e1.printStackTrace();
                                            }
                                        }
                                    } else {
                                        Log.i("error_token", e.getMessage());
                                    }
                                }
                            });
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 45000);


    }

    public int CalculationByDistance(LatLng StartP, LatLng EndP, int pos) {

        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);

        if (valueResult < min) {
            min = valueResult;
            posi = pos;
            Log.i("min_rad", min + "");
            Log.i("min_pos", posi + "");
        }
        return posi;
    }

    private void addMarkersToMap() {
        map.clear();
        for (int i = 0; i < TabsActivity.arrayList.size(); i++) {
            String latLang[] = TabsActivity.arrayList.get(i).getLatLang().split(",");
            double lat = Double.parseDouble(latLang[0]);
            double lng = Double.parseDouble(latLang[1]);
            LatLng ll = new LatLng(lat, lng);
            BitmapDescriptor bitmapMarker = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
//            switch (Cars.get(i).getState()) {
//                case 0:
//                    bitmapMarker = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
//                    Log.i(TAG, "RED");
//                    break;
//                case 1:
//                    bitmapMarker = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
//                    Log.i(TAG, "GREEN");
//                    break;
//                case 2:
//                    bitmapMarker = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE);
//                    Log.i(TAG, "ORANGE");
//                    break;
//                default:
//                    bitmapMarker = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
//                    Log.i(TAG, "DEFAULT");
//                    break;
//            }
            map.addMarker(new MarkerOptions().position(ll).title(TabsActivity.arrayList.get(i).getAddress())
                    .snippet(TabsActivity.arrayList.get(i).getAddress()).icon(bitmapMarker));

//            Log.i(TAG,"Car number "+i+"  was added " +mMarkers.get(mMarkers.size()-1).getId());
        }
    }

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }


    @Override
    public void onLocationChanged(Location loc) {
//        // Getting latitude of the current location
//        double latitude = location.getLatitude();
//
//        // Getting longitude of the current location
//        double longitude = location.getLongitude();
//
//        // Creating a LatLng object for the current location
//        LatLng latLng = new LatLng(latitude, longitude);
//
//        // Showing the current location in Google Map
//        map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//
//        // Zoom in the Google Map
//        map.animateCamera(CameraUpdateFactory.zoomTo(15));
//        map.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude))
//                .title("You are here!").snippet("Consider yourself located"));
        loc.getLatitude();
        loc.getLongitude();

        String text = "My current location is: " +
                "Latitud = " + loc.getLatitude() +
                "Longitud = " + loc.getLongitude();

        Log.i("current_location", text);
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(getActivity(), "Gps Enabled", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(getActivity(), "Gps Disabled", Toast.LENGTH_SHORT).show();

    }
}
