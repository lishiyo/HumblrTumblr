package com.cziyeli.tumblrtagsearch;

import android.app.SearchManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cziyeli.tumblrtagsearch.adapters.PostAdapter;
import com.cziyeli.tumblrtagsearch.models.Post;
import com.google.gson.Gson;
import com.malinskiy.superrecyclerview.OnMoreListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by connieli on 6/1/15.
 */
public class SearchResultsActivity extends SearchableActivity {
    private JsonObjectRequest request;
    private SuperRecyclerView mRecyclerView;
    private PostAdapter mAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);

        launchSearch();
    }

    protected void setupPostViews() {
        mRecyclerView = (SuperRecyclerView) findViewById(R.id.list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        this.mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new PostAdapter(this, getLayoutInflater());
        mRecyclerView.setAdapter(mAdapter);
    }

    protected void handleIntent(Intent intent) {
        this.mCurrQuery = intent.getStringExtra(SearchManager.QUERY);
        String urlQuery = buildQueryString(mCurrQuery);
        sendTumblrRequest(urlQuery);
    }

    // only if activity launchMode="singleTop"
    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);

        launchSearch();
    }

    // Refactor
    protected String buildQueryString(String query){
        Uri.Builder builtUri = Uri.parse(CONSTANTS.TAGGED_BASE_URL).buildUpon()
                .appendQueryParameter("limit", CONSTANTS.LIMIT)
                .appendQueryParameter("api_key", CONSTANTS.API_KEY)
                .appendQueryParameter("tag", query);

        return builtUri.build().toString();
    }

    protected String buildQueryString(String query, String timestamp){
        Uri.Builder builtUri = Uri.parse(CONSTANTS.TAGGED_BASE_URL).buildUpon()
                .appendQueryParameter("limit", CONSTANTS.LIMIT)
                .appendQueryParameter("api_key", CONSTANTS.API_KEY)
                .appendQueryParameter("before", timestamp)
                .appendQueryParameter("tag", query);

        return builtUri.build().toString();
    }

    // MAKE REQUEST TO TUMBLR API **/

    private void sendTumblrRequest(String urlQuery){
        request = new JsonObjectRequest(urlQuery, null, new ResponseListener(), new ErrorListener());
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    // Response listeners for Volley
    private class ResponseListener implements Response.Listener<JSONObject> {
        @Override
        public void onResponse(JSONObject response){
            mRecyclerView.hideMoreProgress();

            try {
                String jsonData = response.getString("response");
                Gson gson = new Gson();
                Post[] postResults = gson.fromJson(jsonData, Post[].class);
                final ArrayList<Post> fullPostResults = new ArrayList<>(Arrays.asList(postResults));

                mAdapter.updateData(fullPostResults);

                // Load More
                mRecyclerView.removeMoreListener();
                mRecyclerView.setupMoreListener(new OnMoreListener() {
                    @Override
                    public void onMoreAsked(int numberOfItems, int numberBeforeMore, int currentItemPos) {
                        // Fetch more from Api or DB at the last item position
                        Post lastPost = mAdapter.getPost(currentItemPos + 2);
                        if (lastPost == null) {
                            Toast.makeText(SearchResultsActivity.this, "No more posts!", Toast.LENGTH_LONG).show();
                            return;
                        }
                        String lastTimestamp = lastPost.mTimestamp;
                        String urlQuery = buildQueryString(mCurrQuery, lastTimestamp);
                        sendTumblrRequest(urlQuery);
                    }
                }, 3);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class ErrorListener implements Response.ErrorListener{
        @Override
        public void onErrorResponse(VolleyError error){
            Log.e(CONSTANTS.DEBUG_TAG, "createVolley onErrorResponse");
        }
    }

}
