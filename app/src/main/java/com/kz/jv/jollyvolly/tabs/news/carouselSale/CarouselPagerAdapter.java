package com.kz.jv.jollyvolly.tabs.news.carouselSale;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.TextView;

import com.kz.jv.jollyvolly.R;
import com.kz.jv.jollyvolly.tabs.news.News;
import com.kz.jv.jollyvolly.tabs.news.NewsSale;

import java.util.ArrayList;


public class CarouselPagerAdapter extends FragmentPagerAdapter implements
        ViewPager.OnPageChangeListener {

    private MyLinearLayout cur = null;
    private MyLinearLayout next = null;
    private News activity;
    private FragmentManager fm;
    private float scale;
    ArrayList<NewsSale> sale;
    public static int colors[];

    private int dotsCountVideo;
    private TextView[] dotsVideo;
    public CarouselPagerAdapter(News activity, FragmentManager fm, ArrayList<NewsSale> sale) {
        super(fm);
        this.fm = fm;
        this.activity = activity;
        this.sale = sale;
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
        if (position == sale.size() * News.LOOPS / 2)
            scale = News.BIG_SCALE;
        else
            scale = News.SMALL_SCALE;

        position = position % sale.size();
        return CarouselMyFragment.newInstance(activity.getActivity(), position, scale, sale);
    }

    @Override
    public int getCount() {
        return sale.size() * News.LOOPS;
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
        Log.i("SCROLLED", "+");
    }

    @Override
    public void onPageSelected(int position) {
        Log.i("Selected position", position + "");

        if (position >= News.dotsCount) {
            if (News.dotsCount == 0) {
                Log.i("EQUAL TO", position + "");
            } else {
                position = position % News.dotsCount;
                for (int i = 0; i < News.dotsCount; i++) {
                    if (News.dotsCount + 1 <= position) {
                        i = 0;
                    }
                    News.dots[i].setTextColor(activity.getResources().getColor(android.R.color.darker_gray));
                }
                News.dots[position].setTextColor(activity.getResources().getColor(android.R.color.black));
            }
        } else {
            for (int i = 0; i < News.dotsCount; i++) {
                if (News.dotsCount + 1 <= position) {
                    i = 0;
                }
                News.dots[i].setTextColor(activity.getResources().getColor(android.R.color.darker_gray));
            }
            News.dots[position].setTextColor(activity.getResources().getColor(android.R.color.black));
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

    private MyLinearLayout getRootView(int position) {
        return (MyLinearLayout)
                fm.findFragmentByTag(this.getFragmentTag(position))
                        .getView().findViewById(R.id.rootSale);
    }

    private String getFragmentTag(int position) {
        return "android:switcher:" + activity.viewPagerSale.getId() + ":" + position;
    }


    public static void getColor(int[] color) {
        colors = color;
    }
}
