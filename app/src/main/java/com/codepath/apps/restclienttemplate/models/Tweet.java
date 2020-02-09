package com.codepath.apps.restclienttemplate.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.codepath.apps.restclienttemplate.TimeFormatter;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
@Entity(foreignKeys = @ForeignKey(entity=User.class, parentColumns = "id", childColumns = "userId"))
public class Tweet {
    @ColumnInfo
    @PrimaryKey
    public long id;
    @ColumnInfo
    public String body;
    @ColumnInfo
    public String createdAt;
    @ColumnInfo
    public long userId;

    @Ignore
    public User user;

    public Tweet() {} // empty constructor for Parceler

    public Tweet (long id, String body, String createdAt, User user) {
        this.body = body;
        this.createdAt = createdAt;
        this.user = user;
        this.id = id;
        this.userId = user.id;
    }

    public static Tweet fromJson(@NotNull JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet(jsonObject.getLong("id"),
                jsonObject.getString("text"),
                jsonObject.getString("created_at"),
                User.fromJson(jsonObject.getJSONObject("user")));
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
