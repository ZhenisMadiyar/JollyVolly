package com.example.jv.jollyvolly.tabs.news;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jv.jollyvolly.R;
import com.squareup.picasso.Picasso;

/**
 * Created by madiyarzhenis on 14.09.15.
 */
public class Podrobnee extends Activity {
    TextView description;
    TextView titleAB;
    TextView title;
    TextView likeCount;
    ImageView imageView;
    ImageButton backBtn;
    ImageButton likeBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.podrobnee);


        title = (TextView) findViewById(R.id.textViewPodrTitle);
        titleAB = (TextView) findViewById(R.id.titleABPodr);
        description = (TextView) findViewById(R.id.textViewDesc);
        imageView = (ImageView) findViewById(R.id.imageViewPodr);
        likeCount = (TextView) findViewById(R.id.textViewPodrLikeCount);
        likeBtn = (ImageButton) findViewById(R.id.imageButtonLikePodr);
        backBtn = (ImageButton) findViewById(R.id.imageButtonBack);

        Intent intent = getIntent();

        if (intent.getIntExtra("liked", 0) != 0) {
            likeBtn.setBackgroundResource(R.drawable.like_pressed);
            likeBtn.setEnabled(false);
        }
        title.setText(intent.getStringExtra("title"));
        titleAB.setText(intent.getStringExtra("title"));
        likeCount.setText(intent.getIntExtra("likeCount", 0)+"");
        description.setText(intent.getStringExtra("desc"));
        Picasso.with(this)
                .load(intent.getStringExtra("imageUrl"))
                .into(imageView);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return (true);
        }

        return (super.onOptionsItemSelected(item));
    }
}
