package com.codepath.apps.restclienttemplate.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
@Entity
public class User {
    @ColumnInfo
    @PrimaryKey
    public long id;
    @ColumnInfo
    public String name;
    @ColumnInfo
    public String screenName;
    @ColumnInfo
    public String profileImageUrl;

    public User() {} //Parceler needs this

    public User (long id, String name, String screenName, String profileImageUrl) {
        this.name = name;
        this.screenName = screenName;
        this.profileImageUrl = profileImageUrl;
        this.id = id;
    }

    public static User fromJson(@NotNull JSONObject jsonObject) throws JSONException {
        User user = new User(jsonObject.getLong("id"),
                jsonObject.getString("name"),
                jsonObject.getString("screen_name"),
                jsonObject.getString("profile_image_url_https"));
        return user;
    }

    public static List<User> fromJsonTweetArray(List<Tweet> tweetsFromNetwork) {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < tweetsFromNetwork.size(); ++i) {
            users.add(tweetsFromNetwork.get(i).user);
        }
        return users;
    }
}
