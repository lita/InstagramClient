package com.codepath.instagramclient;

import android.content.Context;
import android.view.View;

import com.squareup.picasso.Picasso;

/**
 * Created by litacho on 9/15/15.
 */
public class InstagramPhoto extends InstagramBase {
    @Override
    void setView(Context context, InstagramPhotosAdapater.InstagramViewHolder viewHolder) {
        // Clear out the image view as we could have left over image from recycled view.
        viewHolder.photo.setImageResource(0);
        viewHolder.video.setVisibility(View.GONE);
        viewHolder.photo.setVisibility(View.VISIBLE);
        // Download the image asynchronously using Picasso
        Picasso.with(context).load(this.url).placeholder(R.drawable.placeholder).into(viewHolder.photo);
    }
}
