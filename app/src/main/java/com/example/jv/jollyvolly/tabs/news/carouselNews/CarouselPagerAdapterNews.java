package com.example.jv.jollyvolly.tabs.news.carouselNews;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.TextView;

import com.example.jv.jollyvolly.R;
import com.example.jv.jollyvolly.tabs.news.News;
import com.example.jv.jollyvolly.tabs.news.NewsComp;

import java.util.ArrayList;


public class CarouselPagerAdapterNews extends FragmentPagerAdapter implements
        ViewPager.OnPageChangeListener {

    public MyLinearLayoutNews cur = null;
    public MyLinearLayoutNews next = null;
    public News activity;
    public FragmentManager fm;
    public float scale;
    ArrayList<NewsComp> comp;
    public static int colors[];

    public int dotsCountVideo;
    public TextView[] dotsVideo;
    public CarouselPagerAdapterNews(News activity, FragmentManager fm, ArrayList<NewsComp> comp) {
        super(fm);
        this.fm = fm;
        this.activity = activity;
        this.comp = comp;
//        dotsCountVideo = sale.length;
//        dotsVideo = new TextView[dotsCountVideo];
//        for (int j = 0; j < dotsCountVideo; j++) {
//            dotsVideo[j] = new TextView(activity);
//            dotsVideo[j].setText(Html.fromHtml("&#8226;"));
//            dotsVideo[j].setTextSize(30);
//            dotsVideo[j].setTextColor(activity.getResources().getColor(android.R.color.darker_gray));
////            Information.dotsLayoutVideo.addView(dotsVideo[j]);
//        }
//        dotsVideo[0].setTextColor(activity.getResources().getColor(android.R.color.holo_blue_light));
    }

    @Override
    public Fragment getItem(int position) {
        // make the first pager bigger than others
        if (position == comp.size() * News.LOOPS / 2)
            scale = News.BIG_SCALE;
        else
            scale = News.SMALL_SCALE;

        position = position % comp.size();
        return CarouselMyFragmentNews.newInstance(activity.getActivity(), position, scale, comp);
    }

    @Override
    public int getCount() {
        return comp.size() * News.LOOPS;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (positionOffset >= 0f && positionOffset <= 1f) {
            cur = getRootView(position);
            next = getRootView(position + 1);

            cur.setScaleBoth(News.BIG_SCALE
                    - News.DIFF_SCALE * positionOffset);
            next.setScaleBoth(News.SMALL_SCALE
                    + News.DIFF_SCALE * positionOffset);
        }
//        Log.i("SCROLLED", "+");
    }

    @Override
    public void onPageSelected(int position) {
        Log.i("Selected position", position + "");
        if (position >= News.dotsCountCorp) {
            if (News.dotsCountCorp == 0) {
                Log.i("EQUAL TO", position + "");
            } else {
                position = position % News.dotsCountCorp;
                for (int i = 0; i < News.dotsCountCorp; i++) {
                    if (News.dotsCountCorp + 1 <= position) {
                        i = 0;
                    }
                    News.dotsCorp[i].setTextColor(activity.getResources().getColor(android.R.color.darker_gray));
                }
                News.dotsCorp[position].setTextColor(activity.getResources().getColor(android.R.color.black));
            }
        } else {
            for (int i = 0; i < News.dotsCountCorp; i++) {
                if (News.dotsCountCorp + 1 <= position) {
                    i = 0;
                }
                News.dotsCorp[i].setTextColor(activity.getResources().getColor(android.R.color.darker_gray));
            }
            News.dotsCorp[position].setTextColor(activity.getResources().getColor(android.R.color.black));
        }
//        if (position >= dotsCountVideo) {
//            if (dotsCountVideo == 0) {
//                Log.i("EQUAL TO", position + "");
//            } else {
//                position = position % dotsCountVideo;
//                Log.i("Selected mod position", position+"");
//                for (int i = 0; i < dotsCountVideo; i++) {
//                    if (dotsCountVideo + 1 <= position) {
//                        i = 0;
//                    }
//                    dotsVideo[i].setTextColor(activity.getResources().getColor(android.R.color.darker_gray));
//                }
//                dotsVideo[position].setTextColor(activity.getResources().getColor(android.R.color.holo_blue_light));
//            }
//        } else {
//            for (int i = 0; i < dotsCountVideo; i++) {
//                if (dotsCountVideo + 1 <= position) {
//                    i = 0;
//                }
//                dotsVideo[i].setTextColor(activity.getResources().getColor(android.R.color.darker_gray));
//            }
//            dotsVideo[position].setTextColor(activity.getResources().getColor(android.R.color.holo_blue_light));
//        }
    }
    @Override
    public void onPageScrollStateChanged(int state) {
    }

    private MyLinearLayoutNews getRootView(int position) {
        return (MyLinearLayoutNews)
                fm.findFragmentByTag(this.getFragmentTag(position))
                        .getView().findViewById(R.id.root1);
    }

    private String getFragmentTag(int position) {
        return "android:switcher:" + activity.viewPagerNews.getId() + ":" + position;
    }


    public static void getColor(int[] color) {
        colors = color;
    }
}
