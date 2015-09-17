package com.codepath.instagramclient;

import android.text.format.DateUtils;

import java.util.ArrayList;

/**
 * Created by litacho on 9/15/15.
 */
public class InstagramPhoto {
    public static int MAX_COMMENTS = 2;
    public String username;
    public String caption;
    public String imageUrl;
    public int imageHeight;
    public int likesCount;
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
}
