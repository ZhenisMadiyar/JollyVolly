package com.kz.jv.jollyvolly.tabs.maps.carousel;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.kz.jv.jollyvolly.R;
import com.kz.jv.jollyvolly.tabs.maps.Map;
import com.kz.jv.jollyvolly.tabs.maps.MobiliuzCar;

import java.util.ArrayList;

public class MyPagerAdapter extends FragmentPagerAdapter {

    private MyLinearLayout cur = null;
    private MyLinearLayout next = null;
    private Map context;
    private FragmentManager fm;
    private float scale;
    ArrayList<MobiliuzCar> carList;
    public MyPagerAdapter(Map context, FragmentManager fm, ArrayList<MobiliuzCar> carList) {
        super(fm);
        this.fm = fm;
        this.context = context;
        this.carList = carList;
    }

    @Override
    public Fragment getItem(int position) {
        // make the first pager bigger than others
        if (position == carList.size() * 1000 / 2)
            scale = Map.BIG_SCALE;
        else
            scale = Map.SMALL_SCALE;

        position = position % carList.size();
        return MyFragment.newInstance(context.getActivity(), position, scale, carList);
    }

    @Override
    public int getCount() {
        return carList.size() * 1000;
    }

//    @Override
//    public void onPageScrolled(int position, float positionOffset,
//                               int positionOffsetPixels) {
//
//        if (position >= carList.size()) {
//                position = position % carList.size();
//                Log.i("MOD_POSITION", position + "");
//            context.statusCar.setText(carList.get(position).getStatus());
//        } else {
//            context.statusCar.setText(carList.get(position).getStatus());
//        }
//
////        if (positionOffset >= 0f && positionOffset <= 1f) {
////            cur = getRootView(position);
////            next = getRootView(position + 1);
////
////            cur.setScaleBoth(Map.BIG_SCALE
////                    - Map.DIFF_SCALE * positionOffset);
////            next.setScaleBoth(Map.SMALL_SCALE
////                    + Map.DIFF_SCALE * positionOffset);
////        }
//    }
//
//    @Override
//    public void onPageSelected(int position) {
//    }
//
//    @Override
//    public void onPageScrollStateChanged(int state) {
//    }

    private MyLinearLayout getRootView(int position) {
        return (MyLinearLayout)
                fm.findFragmentByTag(this.getFragmentTag(position))
                        .getView().findViewById(R.id.root);
    }

    private String getFragmentTag(int position) {
        return "android:switcher:" + context.pager.getId() + ":" + position;
    }
}
