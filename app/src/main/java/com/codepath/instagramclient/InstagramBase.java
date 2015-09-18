package com.codepath.instagramclient;

import android.content.Context;
import android.text.format.DateUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by litacho on 9/17/15.
 */
abstract class InstagramBase {
    public static int MAX_COMMENTS = 2;
    public String username;
    public String url;
    public int height;
    public int width;
    public String caption;
    public String likesCount;
    public String creationTime;
    public int totalComments;
    public String profileImageUrl;
    public String type;
    public ArrayList<Comment> comments;

    public void setCreationDate(String creationTime) {
        //Transform creation timestamp to relative timestamp
        Long intCreationTime = Long.parseLong(creationTime);
        this.creationTime = DateUtils.getRelativeTimeSpanString(intCreationTime * 1000).toString();
        this.creationTime = this.creationTime.replace(" hour ago", "h");
        this.creationTime = this.creationTime.replace(" hours ago", "h");
        this.creationTime = this.creationTime.replace(" minute ago", "m");
        this.creationTime = this.creationTime.replace(" minutes ago", "m");
        this.creationTime = this.creationTime.replace(" week ago", "w");
        this.creationTime = this.creationTime.replace(" weeks ago", "w");
    }

    public void setLikesCount(Integer likes) {
        DecimalFormat formatter = new DecimalFormat("#,###,###,###");
        this.likesCount = formatter.format(likes) + " likes";
    }

    public static String processCaptionsToHTML(String caption) {
        String[] splitCaption = caption.split(" ");

        String htmlString = "";
        for (String str : splitCaption) {
            String result;
            if (str.startsWith("#") || str.startsWith("@")) {
                result = "<font color='#0D3C5F'>" + str + "</font> ";
            } else {
                result = str + " ";
            }
            htmlString += result;
        }

        return htmlString;
    }

    abstract void setView (Context context, InstagramPhotosAdapater.InstagramViewHolder viewHolder);
}
