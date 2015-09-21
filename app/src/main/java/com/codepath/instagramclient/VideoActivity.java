package com.codepath.instagramclient;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;
import android.media.MediaPlayer;

/**
 * Created by litacho on 9/19/15.
 */
public class VideoActivity extends Activity{
    ProgressDialog pDialog;
    VideoView videoView;

    String videoURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        videoURL = getIntent().getStringExtra("videoUrl");
        setContentView(R.layout.video);
        videoView = (VideoView) findViewById(R.id.vvVideo);
        pDialog = new ProgressDialog(VideoActivity.this);
        pDialog.setTitle("Instagram Video");
        pDialog.setMessage("Buffering...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        try {
            MediaController mediaController = new MediaController(
                    VideoActivity.this
            );
            mediaController.setAnchorView(videoView);
            videoView.setMediaController(mediaController);
            videoView.setVideoPath(videoURL);

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        videoView.requestFocus();

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.i("DEBUG", "THIS REALLY HAPPENED!");
                finish();
            }
        });

        videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                pDialog.dismiss();
                videoView.start();
            }
        });
    }
}
