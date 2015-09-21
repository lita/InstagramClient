package com.codepath.instagramclient;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by litacho on 9/15/15.
 */
public class InstagramPhotosAdapater extends BaseAdapter implements StickyListHeadersAdapter {
    ArrayList<InstagramBase> medias;
    Context thisContext;

    public static class InstagramViewHolder {
        TextView caption;
        ImageView photo;
        RoundedImageView profilePhoto;
        TextView username;
        TextView creationTime;
        LinearLayout comments;
        TextView likes;
    }

    public static class InstagramHeaderViewHolder {
        RoundedImageView profilePhoto;
        TextView username;
        TextView creationTime;
    }

    public InstagramPhotosAdapater(Context context, ArrayList objects) {
        medias = objects;
        thisContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        InstagramBase media = medias.get(position);
        InstagramViewHolder viewHolder;

        // Check if this view is recycled or if we need to create a new View.
        if (convertView == null) {
            // Create a new view from template
            convertView = LayoutInflater.from(thisContext).inflate(R.layout.item_photo, parent, false);
            viewHolder = new InstagramViewHolder();
            viewHolder.caption = (TextView) convertView.findViewById(R.id.tvCaption);
            viewHolder.photo = (ImageView) convertView.findViewById(R.id.ivPhoto);
            viewHolder.comments = (LinearLayout) convertView.findViewById(R.id.list_comments);
            viewHolder.likes = (TextView) convertView.findViewById(R.id.tvLikes);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (InstagramViewHolder) convertView.getTag();
        }

        viewHolder.caption.setText(Html.fromHtml(media.caption));
        viewHolder.comments.removeAllViews();
        for (int i = 0; i < media.comments.size(); i++) {
            Comment comment = media.comments.get(i);
            View line = LayoutInflater.from(thisContext).inflate(R.layout.inside_row, parent, false);
            TextView tvComment = (TextView) line.findViewById(R.id.tvComment);
            tvComment.setText(Html.fromHtml(comment.user + " " + comment.comment));
            viewHolder.comments.addView(line);
        }

        // Clear out the image view as we could have left over image from recycled view.
        media.setView(thisContext, viewHolder);

        viewHolder.likes.setText(media.likesCount);

        return convertView;
    }

    @Override
    public int getCount() {
        return medias.size();
    }

    @Override
    public Object getItem(int position) {
        return medias.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        InstagramBase media = medias.get(position);

        InstagramHeaderViewHolder holder;
        if (convertView == null) {
            holder = new InstagramHeaderViewHolder();
            convertView = LayoutInflater.from(thisContext).inflate(R.layout.header, parent, false);
            holder.username = (TextView) convertView.findViewById(R.id.tvUsername);
            holder.creationTime = (TextView) convertView.findViewById(R.id.tvCreationTime);
            holder.profilePhoto = (RoundedImageView) convertView.findViewById(R.id.rivProfilePhoto);
            convertView.setTag(holder);
        } else {
            holder = (InstagramHeaderViewHolder) convertView.getTag();
        }


        // Clear out the profile view
        holder.profilePhoto.setImageResource(0);
        Picasso.with(thisContext).load(media.profileImageUrl).into(holder.profilePhoto);

        holder.username.setText(media.username);
        holder.creationTime.setText(media.creationTime);

        return convertView;

    }

    @Override
    public long getHeaderId(int i) {
        return i;
    }

    public void clear() {
        medias.clear();
    }
}
