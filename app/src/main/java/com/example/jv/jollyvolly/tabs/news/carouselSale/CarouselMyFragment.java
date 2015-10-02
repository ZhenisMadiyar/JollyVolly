package com.example.jv.jollyvolly.tabs.news.carouselSale;

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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.jv.jollyvolly.R;
import com.example.jv.jollyvolly.tabs.news.News;
import com.example.jv.jollyvolly.tabs.news.NewsSale;
import com.example.jv.jollyvolly.tabs.news.Podrobnee;
import com.example.jv.jollyvolly.tabs.news.StoreDatabaseSale;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class CarouselMyFragment extends Fragment {
    RelativeLayout l;

    StoreDatabaseSale storeDatabase;
    SQLiteDatabase sqdbWrite;
    SQLiteDatabase sqdbRead;
    //    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    public static Fragment newInstance(Activity context, int pos, float scale, ArrayList<NewsSale> sale) {
        Bundle b = new Bundle();
        b.putInt("pos", pos);
        b.putFloat("scale", scale);
        b.putString("objectId", sale.get(pos).getObjectId());
        b.putString("title", sale.get(pos).getTitle());
        b.putString("description", sale.get(pos).getDescription());
        b.putString("imageUrl", sale.get(pos).getImageUrl());
        b.putInt("likeCount", sale.get(pos).getLikeCount());
        b.putInt("liked", sale.get(pos).getLiked());
        return Fragment.instantiate(context, CarouselMyFragment.class.getName(), b);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        storeDatabase = new StoreDatabaseSale(getActivity());
        sqdbWrite = storeDatabase.getWritableDatabase();
        sqdbRead = storeDatabase.getReadableDatabase();
        l = (RelativeLayout) inflater.inflate(R.layout.item_sale, container, false);
        final int pos = this.getArguments().getInt("pos");

        final String objectId = this.getArguments().getString("objectId");
        final String title = this.getArguments().getString("title");
        final String description = this.getArguments().getString("description");
        final String imageUrl = this.getArguments().getString("imageUrl");
        final int likeCount = this.getArguments().getInt("likeCount");
        final int liked = this.getArguments().getInt("liked");

        TextView titleTV = (TextView) l.findViewById(R.id.textViewSaleTitle);
        TextView descriptionTV = (TextView) l.findViewById(R.id.textViewSaleDesc);
        final TextView likeCountTV = (TextView) l.findViewById(R.id.textViewSaleLikeCount);
        TextView podrobnee = (TextView) l.findViewById(R.id.textViewSalePodrobno);
        final ImageButton like = (ImageButton) l.findViewById(R.id.imageButtonLikeSale);
        ImageView imageViewSale = (ImageView) l.findViewById(R.id.imageViewSale);
        Picasso.with(getActivity())
                .load(imageUrl)
                .into(imageViewSale);
        podrobnee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Podrobnee.class);
                intent.putExtra("imageUrl", imageUrl);
                intent.putExtra("title", title);
                intent.putExtra("desc", description);
                intent.putExtra("likeCount", News.arrayListNewsSale.get(pos).getLikeCount());
                intent.putExtra("liked", News.arrayListNewsSale.get(pos).getLiked());
                startActivity(intent);
            }
        });

        titleTV.setText(title);
        descriptionTV.setText(description);
        likeCountTV.setText(News.arrayListNewsSale.get(pos).getLikeCount() + "");
        Log.i("liked_click_sale", News.arrayListNewsSale.get(pos).getLiked() + "");
        if (News.arrayListNewsSale.get(pos).getLiked() != 0) {
            like.setBackgroundResource(R.drawable.like_pressed);
            like.setEnabled(false);
        }
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int likeCount = Integer.parseInt(likeCountTV.getText().toString()) + 1;
                News.arrayListNewsSale.set(pos, new NewsSale(objectId, title, description, imageUrl, likeCount, 1));
                ParseQuery query = new ParseQuery("NewsSale");
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
                sqdbWrite.execSQL("UPDATE sale SET likeCount='" + String.valueOf(likeCount) + "',liked='" + 1 + "' WHERE objectId='" + objectId + "'");
            }
        });

        MyLinearLayout root = (MyLinearLayout) l.findViewById(R.id.rootSale);
        float scale = this.getArguments().getFloat("scale");
        root.setScaleBoth(scale);
        return l;
    }
}
