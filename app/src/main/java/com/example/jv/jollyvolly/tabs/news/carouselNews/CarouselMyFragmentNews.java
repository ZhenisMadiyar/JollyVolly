package com.example.jv.jollyvolly.tabs.news.carouselNews;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.jv.jollyvolly.R;
import com.example.jv.jollyvolly.tabs.news.News;
import com.example.jv.jollyvolly.tabs.news.NewsComp;
import com.example.jv.jollyvolly.tabs.news.Podrobnee;
import com.example.jv.jollyvolly.tabs.news.StoreDatabaseNews;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;


public class CarouselMyFragmentNews extends Fragment {
    String news[];
    //    RelativeLayout l;
    StoreDatabaseNews storeDatabase;
    SQLiteDatabase sqdbWrite;
    SQLiteDatabase sqdbRead;

    //    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    public static Fragment newInstance(Activity context, int pos, float scale, ArrayList<NewsComp> comp) {
        Bundle b = new Bundle();
        b.putInt("pos", pos);
        b.putFloat("scale", scale);
        b.putString("objectId", comp.get(pos).getObjectId());
        b.putString("title", comp.get(pos).getTitle());
        b.putString("description", comp.get(pos).getDescription());
        b.putString("imageUrl", comp.get(pos).getImageUrl());
        b.putInt("likeCount", comp.get(pos).getLikeCount());
        b.putInt("liked", comp.get(pos).getLiked());

        return Fragment.instantiate(context, CarouselMyFragmentNews.class.getName(), b);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container == null) {
            Log.i("container", "null");
            return null;
        }
        storeDatabase = new StoreDatabaseNews(getActivity());
        sqdbWrite = storeDatabase.getWritableDatabase();
        sqdbRead = storeDatabase.getReadableDatabase();
        RelativeLayout l = (RelativeLayout) inflater.inflate(R.layout.item_news, container, false);
        final int pos = this.getArguments().getInt("pos");
        final String objectId = this.getArguments().getString("objectId");
        final String title = this.getArguments().getString("title");
        final String description = this.getArguments().getString("description");
        final String imageUrl = this.getArguments().getString("imageUrl");
        final int likeCount = this.getArguments().getInt("likeCount");
        final int liked = this.getArguments().getInt("liked");

        TextView titleTV = (TextView) l.findViewById(R.id.textViewCompTitle);
        TextView descriptionTV = (TextView) l.findViewById(R.id.textViewCompDesc);
        final TextView likeCountTV = (TextView) l.findViewById(R.id.textViewCompLikeCount);
        final TextView podrobnee = (TextView) l.findViewById(R.id.textViewCompPodrobno);
        final ImageButton like = (ImageButton) l.findViewById(R.id.imageButtonLikeComp);


        titleTV.setText(title);
        descriptionTV.setText(description);
        likeCountTV.setText(likeCount + "");

//        if (News.hashMap.get(objectId) != null && News.hashMap.get(objectId) == true) {
//            like.setBackgroundResource(R.drawable.like_pressed);
//            like.setEnabled(false);
//        }


        Log.i("liked_click", liked + "");
        if (liked != 0) {
            like.setBackgroundResource(R.drawable.like_pressed);
            like.setEnabled(false);
        }

        podrobnee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Podrobnee.class);
                intent.putExtra("imageUrl", imageUrl);
                intent.putExtra("title", title);
                intent.putExtra("desc", description);
                intent.putExtra("likeCount", News.arrayListNewsComp.get(pos).getLikeCount());
                intent.putExtra("liked", News.arrayListNewsComp.get(pos).getLiked());
                startActivity(intent);
            }
        });

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int likeCount = Integer.parseInt(likeCountTV.getText().toString()) + 1;
                News.arrayListNewsComp.set(pos, new NewsComp(objectId, title, description, imageUrl, likeCount, 1));
                ParseQuery query = new ParseQuery("NewsCompetition");
                try {
                    ParseObject o = query.get(objectId);
                    o.put("like_count", likeCount);
                    o.save();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
//                News.hashMap.put(objectId, true);
                likeCountTV.setText(likeCount + "");
                like.setEnabled(false);
                like.setBackgroundResource(R.drawable.like_pressed);
                sqdbWrite.execSQL("UPDATE news SET likeCount='" + String.valueOf(likeCount) + "',liked='" + 1 + "' WHERE objectId='" + objectId + "'");
                // updating row
//                    sqdbWrite.update("news", values, "objectId" + "="+objectId,
//                            new String[] {"objectId", "title", "description", "imageUrl", "likeCount", "liked"});
            }
        });

        MyLinearLayoutNews root = (MyLinearLayoutNews) l.findViewById(R.id.root1);
        float scale = this.getArguments().getFloat("scale");
        root.setScaleBoth(scale);
        return l;
    }
}
