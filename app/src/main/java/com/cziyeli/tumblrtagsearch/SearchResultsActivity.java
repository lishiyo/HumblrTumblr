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
    private String currQueryTag;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);

        currQueryTag = "";

        // setup RecyclerView for Posts
        setupPostViews();

        // handle search
        handleIntent(getIntent());
    }

    private void setupPostViews() {
        this.mRecyclerView = (SuperRecyclerView) findViewById(R.id.list);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        this.mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        this.mAdapter = new PostAdapter(this, getLayoutInflater());
        this.mRecyclerView.setAdapter(mAdapter);
    }

    // only if activity launchMode="singleTop"
    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            setupPostViews();
            handleIntent(intent);
        }
    }

    private String buildQueryString(String query){
        Uri.Builder builtUri = Uri.parse(CONSTANTS.TAGGED_BASE_URL).buildUpon()
                .appendQueryParameter("limit", CONSTANTS.LIMIT)
                .appendQueryParameter("api_key", CONSTANTS.API_KEY)
                .appendQueryParameter("tag", query);

        return builtUri.build().toString();
    }

    private String buildQueryString(String query, String timestamp){
        Uri.Builder builtUri = Uri.parse(CONSTANTS.TAGGED_BASE_URL).buildUpon()
                .appendQueryParameter("limit", CONSTANTS.LIMIT)
                .appendQueryParameter("api_key", CONSTANTS.API_KEY)
                .appendQueryParameter("before", timestamp)
                .appendQueryParameter("tag", query);

        return builtUri.build().toString();
    }

    private void handleIntent(Intent intent) {
        this.currQueryTag = intent.getStringExtra(SearchManager.QUERY);
        String urlQuery = buildQueryString(currQueryTag);
        sendTumblrRequest(urlQuery);
    }

    // MAKE REQUEST TO API **/

    private void sendTumblrRequest(String urlQuery){
        request = new JsonObjectRequest(urlQuery, null, new ResponseListener(), new ErrorListener());
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    // Success response listener for Volley
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
                        String urlQuery = buildQueryString(currQueryTag, lastTimestamp);
                        sendTumblrRequest(urlQuery);

                        // Animate loadmore image
//                        ImageView loadMoreImage = (ImageView) findViewById(R.id.loadMoreView);
//                        Animation testAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade);
//                        testAnim.setRepeatCount(Animation.INFINITE);
//                        testAnim.setRepeatMode(Animation.REVERSE);
//                        loadMoreImage.startAnimation(testAnim);
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
            Log.e(CONSTANTS.DEBUG_TAG, "createVolley error onResponse");
        }
    }

}
