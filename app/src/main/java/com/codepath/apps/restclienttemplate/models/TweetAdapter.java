package com.codepath.apps.restclienttemplate.models;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.R;

import java.util.List;

public class TweetAdapter extends Adapter<TweetAdapter.Viewholder> {
    // pass in the Tweets Array in the constructor
    private List<Tweet> mTweets;
    public TweetAdapter(List<Tweet> tweets){
        mTweets = tweets;
    }
    // for each row, inflate the layout in ViewHolder

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
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


        public Viewholder(View itemView) {
            super(itemView);

            // you have to look them up
            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
            tvUsername = (TextView) itemView.findViewById(R.id.tvUsername);
            tvBody = (TextView) itemView.findViewById(R.id.tvBody);
        }
    }
}
