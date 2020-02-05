package com.codepath.apps.restclienttemplate.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.databinding.ItemTweetBinding;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder> {

    Context context;
    List<Tweet> tweets;

    public TweetsAdapter (Context context, List<Tweet> tweets) {
        this.context = context;
        this.tweets = tweets;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tweet, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Tweet tweet = tweets.get(position);
        holder.bind(tweet);
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    public void clear() {
        tweets.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Tweet> tweetList) {
        tweets.addAll(tweetList);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        final ItemTweetBinding binding;

        ImageView ivProfileImage;
        TextView tvBody;
        TextView tvScreenName;
        TextView tvCreatedAt;
        TextView tvName;

        public ViewHolder(View itemView) {
            super(itemView);

            binding = ItemTweetBinding.bind(itemView);
            ivProfileImage = binding.ivProfileImage;
            tvScreenName = binding.tvScreenName;
            tvBody = binding.tvBody;
            tvCreatedAt = binding.tvCreatedAt;
            tvName = binding.tvName;
        }

        public void bind(@NotNull Tweet tweet) {
            tvBody.setText(tweet.body);
            String screenName = "@" + tweet.user.screenName;
            tvScreenName.setText(screenName);
            tvCreatedAt.setText(tweet.getFormattedCreatedAt());
            tvName.setText(tweet.user.name);
            Glide.with(context).load(tweet.user.profileImageUrl).into(ivProfileImage);
        }
    }
}
