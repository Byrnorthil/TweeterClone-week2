package com.codepath.apps.restclienttemplate.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.EndlessRecyclerViewScrollListener;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TweeterApp;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.adapters.TweetsAdapter;
import com.codepath.apps.restclienttemplate.databinding.ActivityTimelineBinding;
import com.codepath.apps.restclienttemplate.databinding.ItemTweetBinding;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.TweetDao;
import com.codepath.apps.restclienttemplate.models.TweetWithUser;
import com.codepath.apps.restclienttemplate.models.User;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class TimelineActivity extends AppCompatActivity {

    public static final String TAG = "TimelineActivity";
    private static int REQUEST_CODE = 20;

    TwitterClient client;
    RecyclerView rvTweets;
    List<Tweet> tweets;
    TweetsAdapter adapter;
    ActivityTimelineBinding binding;
    SwipeRefreshLayout swipeContainer;
    TweetDao tweetDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_timeline);

        client = TweeterApp.getRestClient(this);
        tweetDao = ((TweeterApp) getApplicationContext()).getMyDatabase().tweetDao();

        swipeContainer = binding.swipeContainer;
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                populateHomeTimeLine();
            }
        });


        rvTweets = binding.rvTweets;
        tweets = new ArrayList<>();
        adapter = new TweetsAdapter(this, tweets);
        LinearLayoutManager linearLayout = new LinearLayoutManager(this);
        rvTweets.setLayoutManager(linearLayout);
        rvTweets.setAdapter(adapter);

        rvTweets.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayout) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadMoreData();
            }
        });

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                List<TweetWithUser> tweetWithUsers = tweetDao.recentItems();
                List<Tweet> tweetsFromDB = TweetWithUser.getTweetList(tweetWithUsers);
                adapter.clear();
                adapter.addAll(tweetsFromDB);
            }
        });

        populateHomeTimeLine();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.compose:
                composeTweet();
                return true;
        }
        return false;
    }

    private void composeTweet() {
        Intent intent = new Intent(this, ComposeActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            Tweet tweet = Parcels.unwrap(data.getParcelableExtra("tweet"));
            tweets.add(0, tweet);
            adapter.notifyItemInserted(0);
            rvTweets.smoothScrollToPosition(0);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void loadMoreData() {
        client.getNextPage(tweets.get(tweets.size() - 1).id - 1, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                JSONArray jsonArray = json.jsonArray;
                try {
                    adapter.addAll(Tweet.fromJsonArray(jsonArray));
                } catch (JSONException e) {
                    Log.e(TAG, "Json exception hit", e);
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                statusMessage(statusCode);
            }
        });
    }

    private void statusMessage(int statusCode) {
        String message;

        switch (statusCode) {
            case 429:
                message = "Api limit reached, please try again in a few minutes";
                break;
            default:
                message = "We had trouble connecting to Twitter, is your connection ok?";
        }

        Toast.makeText(this, message, Toast.LENGTH_LONG);
    }

    private void populateHomeTimeLine() {
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d("TimelineActivity", "Success getting JSON!");
                JSONArray jsonArray = json.jsonArray;
                try {
                    adapter.clear();
                    if (jsonArray.length() > 0) {
                        final List<Tweet> tweetsFromNetwork = Tweet.fromJsonArray(jsonArray);
                        adapter.addAll(tweetsFromNetwork);
                        AsyncTask.execute(new Runnable() {
                            @Override
                            public void run() {
                                List<User> usersFromNetwork = User.fromJsonTweetArray(tweetsFromNetwork);
                                tweetDao.insertModel(usersFromNetwork.toArray(new User[0]));
                                tweetDao.insertModel(tweetsFromNetwork.toArray(new Tweet[0]));
                            }
                        });
                    } else {
                        final String DEFAULT_IMAGE = "https://abs.twig.com/sticky/default_profile_images/default_profile_bigger.png";
                        User dummy = new User(1, "Edward", "Byrnorthil", DEFAULT_IMAGE);
                        tweets.add(new Tweet(1, "You don't have any tweets!", "10/30/2002", dummy));
                        adapter.notifyDataSetChanged();
                    }
                    swipeContainer.setRefreshing(false);
                } catch (JSONException e) {
                    Log.e(TAG, "Json exception", e);
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                statusMessage(statusCode);
            }
        });
    }
}
