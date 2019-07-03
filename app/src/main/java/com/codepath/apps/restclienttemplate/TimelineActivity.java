package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.TweetAdapter;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity {
    public static final int COMPOSE_TWEET_REQUEST_CODE = 20;
    TwitterClient client;
    RecyclerView rvTweets;
    ArrayList<Tweet> tweets;
    TweetAdapter tweetAdapter;
    SwipeRefreshLayout swipeContainer;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.miCompose:
                composeMessage();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void composeMessage() {
        // first parameter is the context, second is the class of the activity to launch
        Intent i = new Intent(this, ComposeActivity.class);
        startActivityForResult(i, COMPOSE_TWEET_REQUEST_CODE); // brings up the second activity
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == COMPOSE_TWEET_REQUEST_CODE && resultCode == RESULT_OK) {
            Tweet resultTweet = Parcels.unwrap(data.getParcelableExtra(ComposeActivity.RESULT_TWEET_KEY));

            tweets.add(0, resultTweet);
            tweetAdapter.notifyItemInserted(0);
            rvTweets.scrollToPosition(0);
            Toast.makeText(this, "Tweet post succeeded", Toast.LENGTH_LONG).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        client = TwitterApp.getRestClient(this); // to get the restclient you have to get the context of where the app is


        // find the recyclerview

        rvTweets = (RecyclerView) findViewById(R.id.rvTweet);
        // initialize the ArrayList
        tweets = new ArrayList<>();
        // setup the recycler view now
        rvTweets.setLayoutManager(new LinearLayoutManager(this));
        //init TweetAdapter
        tweetAdapter = new TweetAdapter(tweets);
        // now set up the tweet adapter
        rvTweets.setAdapter(tweetAdapter);
        // set refresh so it wont be null
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // nowwwww populate the view
        populateTimeline();
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {


                fetchTimelineAsync(0);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);




    }


    public void fetchTimelineAsync(int page) {
        // Send the network request to fetch the updated data
        // `client` here is an instance of Android Async HTTP
        // getHomeTimeline is an example endpoint.

                // Remember to CLEAR OUT old items before appending in the new ones
                tweetAdapter.clear();
                // ...the data has come back, add new items to your adapter...
                populateTimeline();

                // Now we call setRefreshing(false) to signal refresh has finished
                swipeContainer.setRefreshing(false);
    }




    private void populateTimeline(){
        client.getHomeTimeline(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("TwitterClient", response.toString());
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d("TwitterClient", response.toString());
                // iterate through the JSON array

                for(int i = 0; i < response.length(); i++){
                    // for each entry, deserialize the JSON object
                    // convert each object to a Tweet model
                    Tweet tweet = null;
                    try {
                        tweet = Tweet.fromJSON(response.getJSONObject(i));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    // add that Tweet model to our data source
                    tweets.add(tweet);
                    // notify the adapter that we have added an item
                    tweetAdapter.notifyItemInserted(tweets.size()-1);


                }
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("TwitterClient", responseString);
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("TwitterClient", errorResponse.toString());
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d("TwitterClient", errorResponse.toString());
                throwable.printStackTrace();
            }
        }
        );
    }



}
