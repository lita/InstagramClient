package com.codepath.instagramclient;

import android.content.Context;

import com.squareup.picasso.Picasso;

/**
 * Created by litacho on 9/15/15.
 */
public class InstagramPhoto extends InstagramBase {
    @Override
    void setView(Context context, InstagramPhotosAdapater.InstagramViewHolder viewHolder) {
        // Clear out the image view as we could have left over image from recycled view.
        viewHolder.photo.setImageResource(0);
        // Download the image asynchronously using Picasso
        Picasso.with(context).load(this.imageUrl).placeholder(R.drawable.placeholder).into(viewHolder.photo);
    }

    @Override
    void setCallback(VideoPlayback videoPlayback) {
    }
}
