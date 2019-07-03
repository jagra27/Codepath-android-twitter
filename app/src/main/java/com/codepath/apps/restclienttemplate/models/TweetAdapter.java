package com.codepath.apps.restclienttemplate.models;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class TweetAdapter extends Adapter<TweetAdapter.Viewholder> {
    // pass in the Tweets Array in the constructor
    private List<Tweet> mTweets;
    Context context;

    public TweetAdapter(List<Tweet> tweets){
        mTweets = tweets;
    }
    // for each row, inflate the layout in ViewHolder

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View tweetView = inflater.inflate(R.layout.item_tweet, viewGroup, false);
        ViewHolder viewHolder = new Viewholder(tweetView);

        return (Viewholder) viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder viewholder, int i) {
        // you want to get the data according to its position
        Tweet tweet = mTweets.get(i);

        // populate the view
        viewholder.tvUsername.setText(tweet.user.name);
        viewholder.tvBody.setText(tweet.body);
        viewholder.tvTime.setText(getRelativeTimeAgo(tweet.createdAt));
        Glide.with(context)
                .load(tweet.user.profileImageUrl).into(viewholder.ivProfileImage);
    }

    @Override
    public int getItemCount() {
        // you want to get the size of the list mTweets that reflects the amount of data
        return mTweets.size();
    }


    // bind the values based on the position of the element

    // create Viewholder class
    public static class Viewholder extends RecyclerView.ViewHolder{
        public ImageView ivProfileImage;
        public TextView tvUsername;
        public TextView tvBody;
        public TextView tvTime;


        public Viewholder(View itemView) {
            super(itemView);

            // you have to look them up

            tvUsername = (TextView) itemView.findViewById(R.id.tvUsername);
            tvBody = (TextView) itemView.findViewById(R.id.tvBody);
            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime);
        }
    }
    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }

    public void clear(){
        mTweets.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Tweet> list){
        mTweets.addAll(list);
        notifyDataSetChanged();
    }
}
