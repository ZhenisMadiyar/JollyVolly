package com.example.jv.jollyvolly;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TabHost;

import com.example.jv.jollyvolly.tabs.SendMail;
import com.example.jv.jollyvolly.tabs.maps.Car;
import com.example.jv.jollyvolly.tabs.maps.Map;
import com.example.jv.jollyvolly.tabs.menu_list.MenuList;
import com.example.jv.jollyvolly.tabs.news.News;

import java.util.ArrayList;
import java.util.HashMap;

public class TabsActivity extends FragmentActivity {

    private TabHost mTabHost;
    private TabsAdapter mTabsAdapter;
    String[] tabContent = {"Map", "Menu", "News", "Send SMS"};
    ArrayList<Integer> tabName;
    Display display;
    private ViewPager mViewPager;
    public static ArrayList<Car> arrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs);

        Intent intent = getIntent();
        arrayList = new ArrayList<>();
        arrayList = intent.getParcelableArrayListExtra("carData");
        Log.i("CAR_DATA_SIZE", arrayList.size() + "");
//        Log.i("CAR_DATA_SIZE_STATIC", SplashActivity.arrayList.size()+"");
        tabName = new ArrayList<>();
        display = getWindowManager().getDefaultDisplay();
        tabName.add(0);
        tabName.add(1);
        tabName.add(2);
        tabName.add(3);

        mTabHost = (TabHost) findViewById(R.id.tabHost);
        mTabHost.setup();

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setOffscreenPageLimit(4);
        View tabView1 = getLayoutInflater().inflate(R.layout.map, mTabHost, false);
        View tabView2 = getLayoutInflater().inflate(R.layout.news, mTabHost, false);
        View tabView3 = getLayoutInflater().inflate(R.layout.menu_list, mTabHost, false);
        View tabView4 = getLayoutInflater().inflate(R.layout.send_sms, mTabHost, false);

        final HashMap<Integer, Class> hm = new HashMap<>();
        hm.put(0, Map.class);
        hm.put(1, News.class);
        hm.put(2, MenuList.class);
        hm.put(3, SendMail.class);

        HashMap<Integer, View> hashMapView = new HashMap<>();
        hashMapView.put(0, tabView1);
        hashMapView.put(1, tabView2);
        hashMapView.put(2, tabView3);
        hashMapView.put(3, tabView4);

        HashMap<Integer,String> hashMapView2=new HashMap<Integer, String>();
        hashMapView2.put(0, "Map");
        hashMapView2.put(1, "News");
        hashMapView2.put(2, "MenuList");
        hashMapView2.put(3, "SendMail");

        Class[] tabNamePosition = new Class[4];
        View[] tabView = new View[4];
        String tabSpecName[]=new String[4];

        for (int i = 0; i < tabName.size(); i++ ) {
            if (hm.containsKey(tabName.get(i))) {
                tabNamePosition[i] = hm.get(tabName.get(i));
                tabView[i] = hashMapView.get(tabName.get(i));
                tabSpecName[i]=hashMapView2.get(tabName.get(i));
            }
        }
        int width = display.getWidth();
        mTabsAdapter = new TabsAdapter(this, mTabHost, mViewPager);
        for (int i = 0; i < tabName.size(); i++) {
            mTabsAdapter.addTab(mTabHost.newTabSpec(tabSpecName[tabName.get(i)])
                    .setIndicator(tabView[tabName.get(i)]), tabNamePosition[tabName.get(i)],null);
                LinearLayout lnLay=(LinearLayout) tabView[tabName.get(i)].findViewById(R.id.tab_layout);
                lnLay.getLayoutParams().width=width/4;

        }
        if (savedInstanceState != null) {
            mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
        }
//        mTabHost.getTabWidget().getChildAt(0).setBackgroundColor(Color.parseColor("#0fa9f0"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tabs, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class TabsAdapter extends FragmentPagerAdapter implements TabHost.OnTabChangeListener,
            ViewPager.OnPageChangeListener {
        private final Context mContext;
        private final TabHost mTabHost;
        private final ViewPager mViewPager;
        private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();



        final class TabInfo {
            private final String tag;
            private final Class<?> clss;
            private final Bundle args;
            TabInfo(final String _tag, final Class<?> _class, final Bundle _args) {
                tag = _tag;
                clss = _class;
                args = _args;
            }
        }
        class DummyTabFactory implements TabHost.TabContentFactory {
            private final Context mContext;
            public DummyTabFactory(final Context context) {
                mContext = context;
            }

            @Override
            public View createTabContent(final String tag) {
                View v = new View(mContext);
                return v;
            }
        }
        public TabsAdapter(final FragmentActivity activity, final TabHost tabHost, final ViewPager pager) {
            super(activity.getSupportFragmentManager());
            mContext = activity;
            mTabHost = tabHost;
            mViewPager = pager;
            mTabHost.setOnTabChangedListener(this);
            mViewPager.setAdapter(this);
            mViewPager.setOnPageChangeListener(this);
        }
        public void addTab(final TabHost.TabSpec tabSpec, final Class<?> clss, final Bundle args) {
            tabSpec.setContent(new DummyTabFactory(mContext));
            String tag = tabSpec.getTag();
            TabInfo info = new TabInfo(tag, clss, args);
            mTabs.add(info);
            mTabHost.addTab(tabSpec);
            notifyDataSetChanged();
        }
        @Override
        public int getCount() {
            return mTabs.size();
        }
        @Override
        public Fragment getItem(final int position) {
            TabInfo info = mTabs.get(position);
            return Fragment.instantiate(mContext, info.clss.getName(), info.args);
        }
        @Override
        public void onTabChanged(final String tabId) {
            int position = mTabHost.getCurrentTab();
            mViewPager.setCurrentItem(position);
        }
        @Override
        public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {
        }
        @Override
        public void onPageSelected(final int position) {
//        TabWidget widget = mTabHost.getTabWidget();
//        int oldFocusability = widget.getDescendantFocusability();
//        widget.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
            mTabHost.setCurrentTab(position);
//        widget.setDescendantFocusability(oldFocusability);
//            for(int i=0; i<mTabHost.getTabWidget().getChildCount(); i++){
//                mTabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#ffffff"));
//            }
//            mTabHost.getTabWidget().getChildAt(position).setBackgroundColor(Color.parseColor("#0fa9f0"));
        }

        @Override
        public void onPageScrollStateChanged(final int state) {

        }

    }
}
