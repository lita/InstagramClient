package com.codepath.instagramclient;

import android.content.Context;
import android.view.View;

/**
 * Created by litacho on 9/17/15.
 */
public class InstagramVideo extends InstagramBase {

    @Override
    void setView(Context context, final InstagramPhotosAdapater.InstagramViewHolder viewHolder) {
        viewHolder.photo.setImageResource(0);
        // Download the image asynchronously using Picasso
        viewHolder.photo.setImageResource(R.drawable.video_placeholder);
        viewHolder.photo.setClickable(true);
        final String videoUrl = this.videoUrl;
        viewHolder.photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null) {
                    callback.launchVideoPlayer(videoUrl);
                }
            }
        });
    }

    public void setCallback(VideoPlayback callback) {
        this.callback = callback;
    }
}
