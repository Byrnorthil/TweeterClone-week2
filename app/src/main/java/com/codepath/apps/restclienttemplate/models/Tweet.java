package com.codepath.apps.restclienttemplate.models;

import com.codepath.apps.restclienttemplate.TimeFormatter;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class Tweet {
    public String body;
    public String createdAt;
    public User user;
    public long id;

    public Tweet() {} // empty constructor for Parceler

    public Tweet (String body, String createdAt, User user, long id) {
        this.body = body;
        this.createdAt = createdAt;
        this.user = user;
        this.id = id;
    }

    public static Tweet fromJson(@NotNull JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet(jsonObject.getString("text"),
                jsonObject.getString("created_at"),
                User.fromJson(jsonObject.getJSONObject("user")),
                jsonObject.getLong("id"));
        return tweet;
    }

    public static List<Tweet> fromJsonArray(@NotNull JSONArray jsonArray) throws JSONException {
        List<Tweet> tweets = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            tweets.add(fromJson(jsonArray.getJSONObject(i)));
        }
        return tweets;
    }

    public String getFormattedCreatedAt() {
        return TimeFormatter.getTimeDifference(createdAt);
    }
}
