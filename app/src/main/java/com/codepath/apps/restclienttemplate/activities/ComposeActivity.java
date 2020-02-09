package com.codepath.apps.restclienttemplate.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TweeterApp;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.databinding.ActivityComposeBinding;
import com.codepath.apps.restclienttemplate.fragments.SaveDraftDialogueFragment;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONException;
import org.parceler.Parcels;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import okhttp3.Headers;

public class ComposeActivity extends AppCompatActivity implements SaveDraftDialogueFragment.OnSaveDraftListener {

    public static final String TAG = "ComposeActivity";
    public static final int MAX_TWEET_LENGTH = 140;

    EditText etCompose;
    Button btnTweet;
    TextView tvCount;

    TwitterClient client;
    ActivityComposeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_compose);

        client = TweeterApp.getRestClient(this);

        etCompose = binding.etCompose;
        btnTweet = binding.btnTweet;
        tvCount = binding.tvCount;

        final String tweetChars = getString(R.string.tweet_chars);
        tvCount.setText(MAX_TWEET_LENGTH + tweetChars);
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(openFileInput("draft")));
            String line;
            StringBuffer buffer = new StringBuffer();
            while ((line = input.readLine()) != null) {
                buffer.append(line + "/n");
            }
            buffer.replace(buffer.length() - 2, buffer.length(), "");
            etCompose.setText(buffer.toString());
        } catch (FileNotFoundException e) {
            Log.e(TAG, "File not found", e);
        } catch (IOException e) {
            Log.e(TAG, "IOException",e);
        }

        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tweetContent = etCompose.getText().toString();
                if (tweetContent.isEmpty()) {
                    Toast.makeText(ComposeActivity.this, "Your tweet is empty", Toast.LENGTH_SHORT).show();
                } else if (tweetContent.length() > MAX_TWEET_LENGTH) {
                    Toast.makeText(ComposeActivity.this, "Your tweet is too long", Toast.LENGTH_SHORT).show();
                } else {
                    client.publishTweet(tweetContent, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Headers headers, JSON json) {
                            try {
                                Tweet tweet = Tweet.fromJson(json.jsonObject);
                                Intent intent = new Intent();
                                intent.putExtra("tweet", Parcels.wrap(tweet));
                                setResult(RESULT_OK, intent);
                                finish();
                            } catch (JSONException e) {
                                Log.e(TAG, "JSON exception hit", e);
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                            Log.e(TAG, "onFailure", throwable);
                        }
                    });
                }
            }
        });

        etCompose.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                tvCount.setText(MAX_TWEET_LENGTH - editable.toString().length() + tweetChars);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (etCompose.getText().length() > 0) {
            FragmentManager fm = getSupportFragmentManager();
            SaveDraftDialogueFragment alert = SaveDraftDialogueFragment.newInstance();
            alert.show(fm, "save_alert");
        } else {
            super.onBackPressed();
        }
    }

    public void onSaveDraft(boolean saved) {
        if (saved) {
            try {
                FileOutputStream fos = openFileOutput("draft", MODE_WORLD_WRITEABLE);
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos));
                writer.write(etCompose.getText().toString());
                writer.close();
            } catch (FileNotFoundException e) {
                Log.e(TAG, "File not found", e);
            } catch (IOException e) {
                Log.e(TAG, "IOException", e);
            }
        }
        super.onBackPressed();
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
}
