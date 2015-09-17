package com.codepath.instagramclient;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.apache.http.Header;

import java.util.ArrayList;

public class PhotosActivity extends AppCompatActivity {
    public static final String CLIENT_ID = "b87b147ada2f4419b683e5ede0a91820";
    private ArrayList<InstagramPhoto> photos;
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
        ListView lvPhotos = (ListView) findViewById(R.id.lvPhotos);
        lvPhotos.setAdapter(aPhotos);
        fetchPopularPhotos();
    }

    public void fetchPopularPhotos() {
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://api.instagram.com/v1/media/popular?client_id=" + CLIENT_ID;

        client.get(url, null, new JsonHttpResponseHandler() {

            // on success method
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray photosJson = null;
                try {
                    aPhotos.clear();
                    photos.clear();
                    photosJson = response.getJSONArray("data");
                    for (int i = 0; i < photosJson.length(); i++) {
                        JSONObject photoJson = photosJson.getJSONObject(i);
                        InstagramPhoto photo = new InstagramPhoto();
                        photo.username = photoJson.getJSONObject("user").getString("username");
                        photo.caption = photoJson.getJSONObject("caption").getString("text");
                        photo.type = photoJson.getString("type");
                        photo.imageUrl = photoJson.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
                        photo.imageHeight = photoJson.getJSONObject("images").getJSONObject("standard_resolution").getInt("height");
                        photo.setLikesCount(photoJson.getJSONObject("likes").getInt("count"));
                        photo.comments = processComments(photoJson.getJSONObject("comments").getJSONArray("data"));
                        photo.profileImageUrl = photoJson.getJSONObject("user").getString("profile_picture");
                        photo.setCreationDate(photoJson.getString("created_time"));
                        photos.add(photo);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                aPhotos.notifyDataSetChanged();
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("DEBUG", "Something went wrong");
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
                comment.comment = commentJson.getString("text");
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
}
