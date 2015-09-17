package com.codepath.instagramclient;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by litacho on 9/15/15.
 */
public class InstagramPhotosAdapater extends ArrayAdapter<InstagramPhoto> {
    private static class InstagramViewHolder {
        TextView caption;
        ImageView photo;
        RoundedImageView profilePhoto;
        TextView username;
        TextView creationTime;
        LinearLayout comments;

    }

    public InstagramPhotosAdapater(Context context, List objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        InstagramPhoto photo = getItem(position);
        InstagramViewHolder viewHolder;

        // Check if this view is recycled or if we need to create a new View.
        if (convertView == null) {
            // Create a new view from template
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent, false);
            viewHolder = new InstagramViewHolder();
            viewHolder.caption = (TextView) convertView.findViewById(R.id.tvCaption);
            viewHolder.photo = (ImageView) convertView.findViewById(R.id.ivPhoto);
            viewHolder.profilePhoto= (RoundedImageView) convertView.findViewById(R.id.rivProfilePhoto);
            viewHolder.username = (TextView) convertView.findViewById(R.id.tvUsername);
            viewHolder.comments = (LinearLayout) convertView.findViewById(R.id.list_comments);
            viewHolder.creationTime = (TextView) convertView.findViewById(R.id.tvCreationTime);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (InstagramViewHolder) convertView.getTag();
        }

        viewHolder.caption.setText(photo.caption);
        viewHolder.comments.removeAllViews();
        for (int i = 0; i < photo.comments.size(); i++) {
            Comment comment = photo.comments.get(i);
            View line = LayoutInflater.from(getContext()).inflate(R.layout.inside_row, parent, false);
            TextView tvComment = (TextView) line.findViewById(R.id.tvComment);
            tvComment.setText(comment.user + " " + comment.comment);
            viewHolder.comments.addView(line);
        }

        // Clear out the image view as we could have left over image from recycled view.
        viewHolder.photo.setImageResource(0);
        // Download the image asynchronously using Picasso
        Picasso.with(getContext()).load(photo.imageUrl).placeholder(R.drawable.placeholder).into(viewHolder.photo);

        // Clear out the profile view
        viewHolder.profilePhoto.setImageResource(0);
        Picasso.with(getContext()).load(photo.profileImageUrl).into(viewHolder.profilePhoto);

        viewHolder.username.setText(photo.username);
        viewHolder.creationTime.setText(photo.creationTime);

        return convertView;
    }
}
