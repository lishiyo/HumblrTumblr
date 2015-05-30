//package com.cziyeli.tumblrtagsearch;
//
//import android.app.SearchManager;
//import android.content.Context;
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.SearchView;
//import android.util.Log;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.widget.TextView;
//
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.JsonObjectRequest;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
///**
// * Created by connieli on 5/29/15.
// */
//
//public class SearchResultsActivity extends AppCompatActivity {
//    TextView mSearchTagTitle;
//    JsonObjectRequest request;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_results);
//        Log.d(Config.DEBUG_TAG, "onCreate searchactivity");
//        handleIntent(getIntent());
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_main, menu);
//        SearchManager searchManager =
//                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        SearchView searchView =
//                (SearchView) menu.findItem(R.id.search).getActionView();
//        searchView.setSearchableInfo(
//                searchManager.getSearchableInfo(getComponentName()));
//
//        return true;
//    }
//
//// only if launchMode="singleTop"
////    @Override
////    protected void onNewIntent(Intent intent) {
////        handleIntent(intent);
////    }
//
//    private void handleIntent(Intent intent) {
//        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
//            String query = intent.getStringExtra(SearchManager.QUERY);
//            String urlQuery = buildQueryString(query);
//
//            Log.d(Config.DEBUG_TAG, "in searchactivty handleIntent urlQuery: " + urlQuery);
//
//            sendTumblrRequest(urlQuery);
//        }
//    }
//
//    private String buildQueryString(String query){
//        Uri.Builder builtUri = Uri.parse(Config.TAGGED_BASE_URL).buildUpon()
//                .appendQueryParameter("limit", Config.LIMIT)
//                .appendQueryParameter("api_key", Config.API_KEY)
//                .appendQueryParameter("tag", query);
//
//        return builtUri.build().toString();
//    }
//
//    private void sendTumblrRequest(String urlQuery){
//        request = new JsonObjectRequest(urlQuery, null, new ResponseListener(), new ErrorListener());
//        VolleySingleton.getInstance(this).addToRequestQueue(request);
//    }
//
//    private class ResponseListener implements Response.Listener<JSONObject> {
//        @Override
//        public void onResponse(JSONObject response){
//            try {
//                JSONArray jsonData = response.getJSONArray("response");
//                Log.d(Config.DEBUG_TAG, "onResponse " + jsonData);
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    private class ErrorListener implements Response.ErrorListener{
//        @Override
//        public void onErrorResponse(VolleyError error){
//            Log.e("omg android", "createVolley error onResponse");
//        }
//    }
////
////    private void sendTumblrRequest(String query) {
////        // Authenticate via API Key
////        JumblrClient client = new JumblrClient(Config.API_KEY, Config.SECRET);
////        client.setToken(Config.OAUTH_TOKEN, Config.OAUTH_SECRET);
////
////        // Make the request
////        Map<String, Object> params = new HashMap<String, Object>();
////        params.put("limit", Config.LIMIT);
////        List<Post> posts = client.tagged(query, params);
////
//////            Log.d(Config.DEBUG_TAG, "in searchactivty: " + posts);
////
////        for (Post post : posts) {
////            Long id = post.getId();
////            Log.d(Config.DEBUG_TAG, "post id: " + String.valueOf(id));
////        }
////    }
//}
