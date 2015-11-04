package com.example.jv.jollyvolly.tabs.news;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jv.jollyvolly.R;
import com.example.jv.jollyvolly.tabs.news.carouselNews.CarouselPagerAdapterNews;
import com.example.jv.jollyvolly.tabs.news.carouselSale.CarouselPagerAdapter;
import com.google.gson.Gson;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by madiyarzhenis on 31.08.15.
 */
public class News extends Fragment {
    private FragmentActivity myContext;
    public static int LOOPS = 1000;
    //    public static int FIRST_PAGE = PAGES * LOOPS / 2;
    public static float BIG_SCALE = 1.0f;
    public static float SMALL_SCALE = 0.7f;
    public static float DIFF_SCALE = BIG_SCALE - SMALL_SCALE;
    public ViewPager viewPagerSale;
    public ViewPager viewPagerNews;
    public CarouselPagerAdapter adapter;
    public static CarouselPagerAdapterNews adapterNews;
    HashMap<String, Object> parameters;
    Gson gson;

    public static ArrayList<NewsComp> arrayListNewsComp = new ArrayList<>();
    public static ArrayList<NewsSale> arrayListNewsSale = new ArrayList<>();
    public static HashMap<String, Boolean> hashMap = new HashMap<>();

    public static LinearLayout dotsLayoutSale;
    public static int dotsCount;
    public static TextView[] dots;

    public static LinearLayout dotsLayoutCorp;
    public static int dotsCountCorp;
    public static TextView[] dotsCorp;

    StoreDatabaseNews storeDatabase;
    SQLiteDatabase sqdbWrite;
    SQLiteDatabase sqdbRead;

    StoreDatabaseSale storeDatabaseSale;
    SQLiteDatabase sqdbWriteSale;
    SQLiteDatabase sqdbReadSale;
    private static View view;

    public static final String MyPrefs2 = "MyPrefs2";
    SharedPreferences preferences;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        View view = inflater.inflate(R.layout.news_fragment, container, false);
        android.app.FragmentManager fragManager = myContext.getFragmentManager();

        gson = new Gson();

        viewPagerSale = (ViewPager) view.findViewById(R.id.viewpagersale);
        dotsLayoutSale = (LinearLayout) view.findViewById(R.id.viewPagerCountDotsVideo);
        dotsLayoutCorp = (LinearLayout) view.findViewById(R.id.viewPagerCountDotsCorp);


        storeDatabaseSale = new StoreDatabaseSale(getActivity());
        sqdbWriteSale = storeDatabaseSale.getWritableDatabase();
        sqdbReadSale = storeDatabaseSale.getReadableDatabase();

        preferences = getActivity().getSharedPreferences(MyPrefs2, Context.MODE_PRIVATE);
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        if (preferences.getString("last_date", null) == null) {
            String now = format.format(Calendar.getInstance().getTime());
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("last_date", now);
            editor.apply();
            Log.i("PREFERENCES", "@null");
        } else {
            String lastDate = preferences.getString("last_date", "");
            String now = format.format(Calendar.getInstance().getTime());
            Date d1 = null;
            Date d2 = null;
            try {
                d1 = format.parse(lastDate);
                d2 = format.parse(now);

                //in milliseconds!
                long diff = d2.getTime() - d1.getTime();

                long diffSeconds = diff / 1000 % 60;
                long diffMinutes = diff / (60 * 1000) % 60;
                long diffHours = diff / (60 * 60 * 1000) % 24;
                long diffDays = diff / (24 * 60 * 60 * 1000);

                Log.i("diffDays", diffDays + "");
                Log.i("diffHours", diffHours + "");
                Log.i("diffMinutes", diffMinutes + "");
                Log.i("diffSeconds", diffSeconds + "");
                if (diffDays == 1) {
                    refersh();
                    Toast.makeText(getActivity(), "REFRESHED!", Toast.LENGTH_LONG).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Cursor cursorSale = sqdbReadSale.query("sale", new String[]
                        {"objectId, title, description, imageUrl, likeCount, liked"},
//                "id="+getPostition()+" AND tag="+getTag(), // The columns for the WHERE clause
//                "name=" + "'" + intent.getStringExtra("name") + "'", // The columns for the WHERE clause
                null,
                null, // The values for the WHERE clause
                null, // don't group the rows
                null, // don't filter by row groups
                null // The sort order
        );

        parameters = new HashMap<>();
        if (cursorSale.getCount() == 0) {
            storeDatabaseSale.cleanTable(sqdbReadSale);
            ParseCloud.callFunctionInBackground("news_sale", parameters, new FunctionCallback<Object>() {
                @Override
                public void done(Object response, ParseException e) {
                    if (e != null) {

                    } else {
                        String json = gson.toJson(response);
                        if (json.equals("[]")) {
                            Toast.makeText(getActivity(), "news NULL", Toast.LENGTH_LONG).show();
                        } else {
                            Log.i("JSON_SALE", json);
                            try {
                                JSONArray jsonArray = new JSONArray(json);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    JSONObject estimatedData = jsonObject.getJSONObject("estimatedData");
                                    JSONObject jsonImage = estimatedData.getJSONObject("image");

                                    String objectId = jsonObject.getString("objectId");
                                    String imageUrl = jsonImage.getString("url");
                                    String title = estimatedData.getString("title");
                                    String description = estimatedData.getString("description");
                                    int like_count = estimatedData.getInt("like_count");
                                    arrayListNewsSale.add(new NewsSale(objectId, title, description, imageUrl, like_count, 0));
                                    String insertQuery = "INSERT INTO sale (objectId, title, description, imageUrl, likeCount, liked)" +
                                            "VALUES ('" + objectId + "','" + title + "','" + description + "'" +
                                            ",'" + imageUrl + "','" + like_count + "" + "','" + 0 + "')";
                                    sqdbWriteSale.execSQL(insertQuery);
                                }
                                adapter = new CarouselPagerAdapter(News.this, getFragmentManager(), arrayListNewsSale);
                                viewPagerSale.setAdapter(adapter);
                                viewPagerSale.setOnPageChangeListener(adapter);
                                // Set current item to the middle page so we can fling to both
                                // directions left and right
                                viewPagerSale.setCurrentItem(arrayListNewsSale.size() * LOOPS / 2);
                                // Necessary or the pager will only have one extra page to show
                                // make this at least however many pages you can see
                                viewPagerSale.setOffscreenPageLimit(2);
                                // Set margin for pages as a negative number, so a part of next and
                                // previous pages will be showed
                                // viewPagerSale.setPageMargin(-200);
                                dotsCount = arrayListNewsSale.size();
                                Log.i("CountSale", arrayListNewsSale.size() + "");
                                dots = new TextView[dotsCount];
                                for (int j = 0; j < dotsCount; j++) {
                                    dots[j] = new TextView(getActivity());
                                    dots[j].setText(Html.fromHtml("&#8226;"));
                                    dots[j].setTextSize(30);
                                    dots[j].setTextColor(getResources().getColor(android.R.color.darker_gray));
                                    dotsLayoutSale.addView(dots[j]);
                                }
                                dots[0].setTextColor(getResources().getColor(android.R.color.black));
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                }
            });
        } else {
//            arrayListNewsComp.clear();
            while (cursorSale.moveToNext()) {
                Log.i("DatabaseSale", "isNotNull");
                String objectId = cursorSale.getString(cursorSale.getColumnIndex("objectId"));
                Log.i("objectIdSqliteSale", objectId);
                String title = cursorSale.getString(cursorSale.getColumnIndex("title"));
                String description = cursorSale.getString(cursorSale.getColumnIndex("description"));
                String imageUrl = cursorSale.getString(cursorSale.getColumnIndex("imageUrl"));
                String likeCount = cursorSale.getString(cursorSale.getColumnIndex("likeCount"));
                int like_count = Integer.parseInt(likeCount);
                int liked = cursorSale.getInt(cursorSale.getColumnIndex("liked"));
                Log.i("LikeCountSale", likeCount);
                Log.i("liked_cursorSale", liked + "");
                arrayListNewsSale.add(new NewsSale(objectId, title, description, imageUrl, like_count, liked));
                Log.i("DatabaseNewsCursorSale", title + " " + likeCount);
            }
            adapter = new CarouselPagerAdapter(News.this, getFragmentManager(), arrayListNewsSale);
            viewPagerSale.setAdapter(adapter);
            viewPagerSale.setOnPageChangeListener(adapter);
            // Set current item to the middle page so we can fling to both
            // directions left and right
            viewPagerSale.setCurrentItem(arrayListNewsSale.size() * LOOPS / 2);
            // Necessary or the pager will only have one extra page to show
            // make this at least however many pages you can see
            viewPagerSale.setOffscreenPageLimit(2);
            // Set margin for pages as a negative number, so a part of next and
            // previous pages will be showed
            // viewPagerSale.setPageMargin(-200);
            dotsCount = arrayListNewsSale.size();
            Log.i("CountSale", arrayListNewsSale.size() + "");
            dots = new TextView[dotsCount];
            for (int j = 0; j < dotsCount; j++) {
                dots[j] = new TextView(getActivity());
                dots[j].setText(Html.fromHtml("&#8226;"));
                dots[j].setTextSize(30);
                dots[j].setTextColor(getResources().getColor(android.R.color.darker_gray));
                dotsLayoutSale.addView(dots[j]);
            }
            dots[0].setTextColor(getResources().getColor(android.R.color.black));

            cursorSale.close();
        }


        storeDatabase = new StoreDatabaseNews(getActivity());
        sqdbWrite = storeDatabase.getWritableDatabase();
        sqdbRead = storeDatabase.getReadableDatabase();

        Cursor cursor = sqdbRead.query("news", new String[]
                        {"objectId, title, description, imageUrl, likeCount, liked"},
//                "id="+getPostition()+" AND tag="+getTag(), // The columns for the WHERE clause
//                "name=" + "'" + intent.getStringExtra("name") + "'", // The columns for the WHERE clause
                null,
                null, // The values for the WHERE clause
                null, // don't group the rows
                null, // don't filter by row groups
                null // The sort order
        );

        viewPagerNews = (ViewPager) view.findViewById(R.id.viewpagernews);
        Log.i("CursorSize", cursor.getCount() + "");
        if (cursor.getCount() == 0) {
            Log.i("DatabaseNews", "Null");
            storeDatabase.cleanTable(sqdbRead);
            ParseCloud.callFunctionInBackground("news_competition", parameters, new FunctionCallback<Object>() {
                @Override
                public void done(Object response, ParseException e) {
                    if (e != null) {

                    } else {
                        String json = gson.toJson(response);
                        if (json.equals("[]")) {
                            Toast.makeText(getActivity(), "newsComp NULL", Toast.LENGTH_LONG).show();
                        } else {
                            Log.i("JSON_COMP", json);
                            try {
                                JSONArray jsonArray = new JSONArray(json);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    JSONObject estimatedData = jsonObject.getJSONObject("estimatedData");
                                    JSONObject jsonImage = estimatedData.getJSONObject("image");

                                    String objectId = jsonObject.getString("objectId");
                                    String imageUrl = jsonImage.getString("url");
                                    String title = estimatedData.getString("title");
                                    String description = estimatedData.getString("description");
                                    int like_count = estimatedData.getInt("like_count");
                                    arrayListNewsComp.add(new NewsComp(objectId, title, description, imageUrl, like_count, 0));
                                    Log.i("objectIdSqlite", objectId);
                                    String insertQuery = "INSERT INTO news (objectId, title, description, imageUrl, likeCount, liked)" +
                                            "VALUES ('" + objectId + "','" + title + "','" + description + "'" +
                                            ",'" + imageUrl + "','" + like_count + "" + "','" + 0 + "')";
                                    sqdbWrite.execSQL(insertQuery);
                                }
                                adapterNews = new CarouselPagerAdapterNews(News.this, getFragmentManager(), arrayListNewsComp);
                                viewPagerNews.setAdapter(adapterNews);
                                viewPagerNews.setOnPageChangeListener(adapterNews);
                                // Set current item to the middle page so we can fling to both
                                // directions left and right
                                viewPagerNews.setCurrentItem(arrayListNewsComp.size() * LOOPS / 2);
                                // Necessary or the pager will only have one extra page to show
                                // make this at least however many pages you can see
                                viewPagerNews.setOffscreenPageLimit(2);
                                Log.i("2", arrayListNewsComp.get(1).getDescription());
                                adapterNews.notifyDataSetChanged();
                                // Set margin for pages as a negative number, so a part of next and
                                // previous pages will be showed
//                            viewPagerNews.setPageMargin(-50);dotsCount = arrayListNewsSale.size();
                                dotsCountCorp = arrayListNewsComp.size();
                                Log.i("CountComp", arrayListNewsComp.size() + "");
                                dotsCorp = new TextView[dotsCountCorp];
                                for (int j = 0; j < dotsCountCorp; j++) {
                                    dotsCorp[j] = new TextView(getActivity());
                                    dotsCorp[j].setText(Html.fromHtml("&#8226;"));
                                    dotsCorp[j].setTextSize(30);
                                    dotsCorp[j].setTextColor(getResources().getColor(android.R.color.darker_gray));
                                    dotsLayoutCorp.addView(dotsCorp[j]);
                                }
                                dotsCorp[0].setTextColor(getResources().getColor(android.R.color.black));
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                }
            });
        } else {
//            arrayListNewsComp.clear();
            while (cursor.moveToNext()) {
                Log.i("DatabaseNews", "isNotNull");
                String objectId = cursor.getString(cursor.getColumnIndex("objectId"));
                Log.i("objectIdSqlite", objectId);
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String description = cursor.getString(cursor.getColumnIndex("description"));
                String imageUrl = cursor.getString(cursor.getColumnIndex("imageUrl"));
                String likeCount = cursor.getString(cursor.getColumnIndex("likeCount"));
                int like_count = Integer.parseInt(likeCount);
                int liked = cursor.getInt(cursor.getColumnIndex("liked"));
                Log.i("LikeCount", likeCount);
                Log.i("liked_cursor", liked + "");
                arrayListNewsComp.add(new NewsComp(objectId, title, description, imageUrl, like_count, liked));
                Log.i("DatabaseNewsCursor", title + " " + likeCount);
            }
            adapterNews = new CarouselPagerAdapterNews(News.this, getFragmentManager(), arrayListNewsComp);
            viewPagerNews.setAdapter(adapterNews);
            viewPagerNews.setOnPageChangeListener(adapterNews);
            // Set current item to the middle page so we can fling to both
            // directions left and right
            viewPagerNews.setCurrentItem(arrayListNewsComp.size() * LOOPS / 2);
            // Necessary or the pager will only have one extra page to show
            // make this at least however many pages you can see
            viewPagerNews.setOffscreenPageLimit(2);
//            Log.i("2", arrayListNewsComp.get(1).getDescription());
            // Set margin for pages as a negative number, so a part of next and
            // previous pages will be showed
//                            viewPagerNews.setPageMargin(-50);dotsCount = arrayListNewsSale.size();
            dotsCountCorp = arrayListNewsComp.size();
            Log.i("CountComp", arrayListNewsComp.size() + "");
            dotsCorp = new TextView[dotsCountCorp];
            for (int j = 0; j < dotsCountCorp; j++) {
                dotsCorp[j] = new TextView(getActivity());
                dotsCorp[j].setText(Html.fromHtml("&#8226;"));
                dotsCorp[j].setTextSize(30);
                dotsCorp[j].setTextColor(getResources().getColor(android.R.color.darker_gray));
                dotsLayoutCorp.addView(dotsCorp[j]);
            }
            dotsCorp[0].setTextColor(getResources().getColor(android.R.color.black));

            cursor.close();
        }
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }

    private void refersh() {
        storeDatabase.cleanTable(sqdbRead);
        storeDatabaseSale.cleanTable(sqdbReadSale);
        Log.i("Cache", "Cleaned");
//        Toast.makeText(getApplicationContext(), "All databases cleaned!", Toast.LENGTH_SHORT).show();
    }
}
