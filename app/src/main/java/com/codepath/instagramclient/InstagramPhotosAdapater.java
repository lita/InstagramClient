package com.codepath.instagramclient;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by litacho on 9/15/15.
 */
public class InstagramPhotosAdapater extends ArrayAdapter<InstagramBase> {
    public static class InstagramViewHolder {
        TextView caption;
        ImageView photo;
        RoundedImageView profilePhoto;
        TextView username;
        TextView creationTime;
        LinearLayout comments;
        TextView likes;
        VideoView video;
    }

    public InstagramPhotosAdapater(Context context, List objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        InstagramBase media = getItem(position);
        InstagramViewHolder viewHolder;

        // Check if this view is recycled or if we need to create a new View.
        if (convertView == null) {
            // Create a new view from template
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent, false);
            viewHolder = new InstagramViewHolder();
            viewHolder.caption = (TextView) convertView.findViewById(R.id.tvCaption);
            viewHolder.photo = (ImageView) convertView.findViewById(R.id.ivPhoto);
            viewHolder.video = (VideoView) convertView.findViewById(R.id.vvVideo);
            viewHolder.profilePhoto= (RoundedImageView) convertView.findViewById(R.id.rivProfilePhoto);
            viewHolder.username = (TextView) convertView.findViewById(R.id.tvUsername);
            viewHolder.comments = (LinearLayout) convertView.findViewById(R.id.list_comments);
            viewHolder.creationTime = (TextView) convertView.findViewById(R.id.tvCreationTime);
            viewHolder.likes = (TextView) convertView.findViewById(R.id.tvLikes);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (InstagramViewHolder) convertView.getTag();
        }

        viewHolder.caption.setText(Html.fromHtml(media.caption));
        viewHolder.comments.removeAllViews();
        for (int i = 0; i < media.comments.size(); i++) {
            Comment comment = media.comments.get(i);
            View line = LayoutInflater.from(getContext()).inflate(R.layout.inside_row, parent, false);
            TextView tvComment = (TextView) line.findViewById(R.id.tvComment);
            tvComment.setText(Html.fromHtml(comment.user + " " + comment.comment));
            viewHolder.comments.addView(line);
        }

        // Clear out the image view as we could have left over image from recycled view.
        media.setView(getContext(), viewHolder);

        // Clear out the profile view
        viewHolder.profilePhoto.setImageResource(0);
        Picasso.with(getContext()).load(media.profileImageUrl).into(viewHolder.profilePhoto);

        viewHolder.username.setText(media.username);
        viewHolder.creationTime.setText(media.creationTime);
        viewHolder.likes.setText(media.likesCount);

        return convertView;
    }
}
