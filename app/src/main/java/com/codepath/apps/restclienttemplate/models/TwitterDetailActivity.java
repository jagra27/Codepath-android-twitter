package com.codepath.apps.restclienttemplate.models;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.R;

import org.parceler.Parcels;

public class TwitterDetailActivity extends AppCompatActivity {
    Tweet tweet;
    User user;
    ImageView ivProfileImage;
    TextView tvUsername;
    TextView tvBody;
    TextView tvTime;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_detail);

        //resolve the view objects
        ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        tvUsername = (TextView) findViewById(R.id.tvUsername);
        tvBody = (TextView) findViewById(R.id.tvBody);
        tvTime = (TextView) findViewById(R.id.tvTime);

        tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra("tweet"));

        tvUsername.setText(tweet.user.screenName);
        tvTime.setText(tweet.createdAt);
        tvBody.setText(tweet.body);


        Glide.with(this)
                .load(tweet.user.profileImageUrl).into(ivProfileImage);


    }
}
