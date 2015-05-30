package com.cziyeli.tumblrtagsearch;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cziyeli.tumblrtagsearch.adapters.PostAdapter;
import com.cziyeli.tumblrtagsearch.models.Post;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {
    private JsonObjectRequest request;
    private RecyclerView mRecyclerView;
    private PostAdapter mAdapter;
    private Post[] mPosts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // setup RecyclerView
        setupViews(mPosts);

        Log.d(Config.DEBUG_TAG, "onCreate MainACTIVITY");
        handleIntent(getIntent());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();

        // call to getSearchableInfo obtains a SearchableInfo object created from searchable.xml
        // SearchView starts an activity with ACTION_SEARCH intent when user submits a query
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    // only if launchMode="singleTop"
    @Override
    protected void onNewIntent(Intent intent) {
        Log.d(Config.DEBUG_TAG, "onNewIntent");
        setIntent(intent);
        handleIntent(intent);
    }

    private String buildQueryString(String query){
        Uri.Builder builtUri = Uri.parse(Config.TAGGED_BASE_URL).buildUpon()
                .appendQueryParameter("limit", Config.LIMIT)
                .appendQueryParameter("api_key", Config.API_KEY)
                .appendQueryParameter("tag", query);

        return builtUri.build().toString();
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            String urlQuery = buildQueryString(query);
            sendTumblrRequest(urlQuery);
        }
    }

    private void setupViews(Post[] posts) {
        mRecyclerView = (RecyclerView)findViewById(R.id.list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new PostAdapter(posts, this);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void sendTumblrRequest(String urlQuery){
        request = new JsonObjectRequest(urlQuery, null, new ResponseListener(), new ErrorListener());
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    private class ResponseListener implements Response.Listener<JSONObject> {
        @Override
        public void onResponse(JSONObject response){
            try {
                String jsonData = response.getString("response");

                Gson gson = new Gson();
                Post[] postResults = gson.fromJson(jsonData, Post[].class);
                setupViews(postResults);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class ErrorListener implements Response.ErrorListener{
        @Override
        public void onErrorResponse(VolleyError error){
            Log.e("omg android", "createVolley error onResponse");
        }
    }
}
