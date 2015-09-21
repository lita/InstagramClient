package com.codepath.instagramclient;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class PhotosActivity extends AppCompatActivity implements InstagramBase.VideoPlayback {
    public static final String CLIENT_ID = "b87b147ada2f4419b683e5ede0a91820";
    private ArrayList<InstagramBase> photos;
    private InstagramPhotosAdapater aPhotos;
    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchPopularPhotos();
            }
        });
        photos = new ArrayList<>();
        aPhotos = new InstagramPhotosAdapater(this, photos);
        StickyListHeadersListView lvPhotos = (StickyListHeadersListView) findViewById(R.id.lvPhotos);
        lvPhotos.setAdapter(aPhotos);
        fetchPopularPhotos();
    }

    public void processJSON(JSONObject response) {
        JSONArray photosJson = null;
        try {
            aPhotos.clear();
            photos.clear();
            photosJson = response.getJSONArray("data");
            InstagramBase media;
            for (int i = 0; i < photosJson.length(); i++) {
                JSONObject photoJson = photosJson.getJSONObject(i);
                String type = photoJson.getString("type");
                if (type.equals("video")) {
                    media = new InstagramVideo();
                    media.videoUrl = photoJson.getJSONObject("videos").getJSONObject("standard_resolution").getString("url");
                    media.height = photoJson.getJSONObject("videos").getJSONObject("standard_resolution").getInt("height");
                    media.width = photoJson.getJSONObject("videos").getJSONObject("standard_resolution").getInt("width");
                    media.imageUrl = photoJson.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
                    media.setCallback(PhotosActivity.this);
                } else {
                    media = new InstagramPhoto();
                    media.imageUrl = photoJson.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
                    media.height = photoJson.getJSONObject("images").getJSONObject("standard_resolution").getInt("height");
                }
                media.username = photoJson.getJSONObject("user").getString("username");
                try {
                    media.caption = InstagramBase.processCaptionsToHTML(photoJson.getJSONObject("caption").getString("text"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    media.caption = "";
                }

                media.setLikesCount(photoJson.getJSONObject("likes").getInt("count"));
                media.comments = processComments(photoJson.getJSONObject("comments").getJSONArray("data"));
                media.profileImageUrl = photoJson.getJSONObject("user").getString("profile_picture");
                media.setCreationDate(photoJson.getString("created_time"));
                photos.add(media);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        aPhotos.notifyDataSetChanged();
        swipeContainer.setRefreshing(false);
    }

    public void fetchPopularPhotos() {
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://api.instagram.com/v1/media/popular?client_id=" + CLIENT_ID;

        client.get(url, null, new JsonHttpResponseHandler() {

            // on success method
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                processJSON(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject object) {

                Log.i("DEBUG", "Something went wrong");
                try{
                    String fixture = "{\"meta\": {\"code\": 200}, \"data\": {\"attribution\": null, \"tags\": [], \"user\": {\"username\": \"betches\", \"profile_picture\": \"https://igcdn-photos-h-a.akamaihd.net/hphotos-ak-xaf1/t51.2885-19/11849205_492009470973703_1004843911_a.jpg\", \"id\": \"6131548\", \"full_name\": \"BETCHES\"}, \"comments\": {\"count\": 13444, \"data\": [{\"created_time\": \"1442797459\", \"text\": \"@pickles0587\", \"from\": {\"username\": \"kittyykata\", \"profile_picture\": \"https://scontent.cdninstagram.com/hphotos-xfp1/t51.2885-19/10488799_304630413073510_1203032604_a.jpg\", \"id\": \"182681070\", \"full_name\": \"\"}, \"id\": \"1078585721949271391\"}, {\"created_time\": \"1442797460\", \"text\": \"@lauren_irenie but really.\", \"from\": {\"username\": \"devonjane\", \"profile_picture\": \"https://scontent.cdninstagram.com/hphotos-xap1/t51.2885-19/10852707_929057647133884_641086568_a.jpg\", \"id\": \"15821594\", \"full_name\": \"Devon McDermott\"}, \"id\": \"1078585729985557859\"}]}, \"filter\": \"Normal\", \"images\": {\"low_resolution\": {\"url\": \"https://scontent.cdninstagram.com/hphotos-xfa1/t51.2885-15/s320x320/e35/11850182_887247264661887_1990033377_n.jpg\", \"width\": 320, \"height\": 320}, \"thumbnail\": {\"url\": \"https://scontent.cdninstagram.com/hphotos-xfa1/t51.2885-15/s150x150/e35/11850182_887247264661887_1990033377_n.jpg\", \"width\": 150, \"height\": 150}, \"standard_resolution\": {\"url\": \"https://scontent.cdninstagram.com/hphotos-xfa1/t51.2885-15/s640x640/sh0.08/e35/11850182_887247264661887_1990033377_n.jpg\", \"width\": 640, \"height\": 640}}, \"link\": \"https://instagram.com/p/73qRcymDRx/\", \"location\": null, \"created_time\": \"1442789213\", \"users_in_photo\": [], \"caption\": {\"created_time\": \"1442789213\", \"text\": \"Sunday night reflections (@veronleigh)\", \"from\": {\"username\": \"betches\", \"profile_picture\": \"https://igcdn-photos-h-a.akamaihd.net/hphotos-ak-xaf1/t51.2885-19/11849205_492009470973703_1004843911_a.jpg\", \"id\": \"6131548\", \"full_name\": \"BETCHES\"}, \"id\": \"1078516554428593284\"}, \"type\": \"image\", \"id\": \"1078516552876700785_6131548\", \"likes\": {\"count\": 50859, \"data\": [{\"username\": \"so.im.kaylee\", \"profile_picture\": \"https://igcdn-photos-g-a.akamaihd.net/hphotos-ak-xfa1/t51.2885-19/s150x150/11906096_500850806740014_1307417861_a.jpg\", \"id\": \"2205066707\", \"full_name\": \"Kaylee Shepard\"}, {\"username\": \"xsarlibabax\", \"profile_picture\": \"https://igcdn-photos-g-a.akamaihd.net/hphotos-ak-xaf1/t51.2885-19/s150x150/11850024_1911022672456646_1071258611_a.jpg\", \"id\": \"2205205379\", \"full_name\": \"\"}, {\"username\": \"kameakachelle\", \"profile_picture\": \"https://scontent.cdninstagram.com/hphotos-xfa1/t51.2885-19/s150x150/11925626_1885998658292591_1705041111_a.jpg\", \"id\": \"2205242222\", \"full_name\": \"Kamea Kachelle Scott\"}, {\"username\": \"mhsn509\", \"profile_picture\": \"https://igcdn-photos-a-a.akamaihd.net/hphotos-ak-xaf1/t51.2885-19/s150x150/11925771_867936049968336_88630933_a.jpg\", \"id\": \"2205194127\", \"full_name\": \"\\\\u0625\\\\u0644\\\\u0645\\\\u0647\\\\u0625\\\\u0624\\\\u064a\\\\u06210506442511\"}]}}}";
                    JSONObject jsonObj = new JSONObject(fixture);
                    processJSON(jsonObj);
                } catch (JSONException e) {
                    Log.i("DEBUG", "Fixture is broken");
                    e.printStackTrace();
                }
            }
        });
    }

    public ArrayList<Comment> processComments(JSONArray commentsJson) {
        ArrayList<Comment> comments = new ArrayList<>();

        int numToFetchComments = InstagramPhoto.MAX_COMMENTS;
        if (commentsJson.length() < numToFetchComments) {
            numToFetchComments = commentsJson.length();
        }

        for (int i = 0; i < numToFetchComments; i++) {
            Comment comment = new Comment();
            try {
                JSONObject commentJson = commentsJson.getJSONObject(i);
                comment.user = commentJson.getJSONObject("from").getString("username");
                comment.comment = InstagramBase.processCaptionsToHTML(commentJson.getString("text"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            comments.add(comment);
        }

        return comments;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_photos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void launchVideoPlayer(String videoUrl) {
        Intent intent = new Intent(PhotosActivity.this, VideoActivity.class);
        intent.putExtra("videoUrl", videoUrl);
        startActivity(intent);
    }
}
