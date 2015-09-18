package com.codepath.instagramclient;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;

/**
 * Created by litacho on 9/17/15.
 */
public class InstagramVideo extends InstagramBase {
    @Override
    void setView(Context context, final InstagramPhotosAdapater.InstagramViewHolder viewHolder) {
        viewHolder.photo.setVisibility(View.GONE);
        viewHolder.video.setVisibility(View.VISIBLE);
        viewHolder.video.setBackgroundResource(0);
        try{
            viewHolder.video.setVideoPath(this.url);
            MediaController mediaController = new MediaController(context);
            mediaController.setAnchorView(viewHolder.video);
            viewHolder.video.setMediaController(mediaController);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        viewHolder.video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            // Close the progress bar and play the video
            public void onPrepared(MediaPlayer mp) {

            }
        });

    }
}
