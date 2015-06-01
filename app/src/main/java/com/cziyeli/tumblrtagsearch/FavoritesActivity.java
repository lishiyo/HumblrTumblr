package com.cziyeli.tumblrtagsearch;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.cziyeli.tumblrtagsearch.adapters.PostAdapter;
import com.cziyeli.tumblrtagsearch.fragments.MainActivityStart;
import com.cziyeli.tumblrtagsearch.models.InternalStorage;
import com.cziyeli.tumblrtagsearch.models.Post;
import com.malinskiy.superrecyclerview.SuperRecyclerView;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by connieli on 6/1/15.
 */

public class FavoritesActivity extends AppCompatActivity {
    private SuperRecyclerView mRecyclerView;
    private PostAdapter mAdapter;
    public ArrayList<Post> mSavedPosts;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(Config.DEBUG_TAG, "creating favorites activyt");
        // setup RecyclerView for Posts
        setupPostViews();

        // get Post data
        getFavorites();
    }

    private void setupPostViews() {
        this.mRecyclerView = (SuperRecyclerView) findViewById(R.id.list);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        this.mAdapter = new PostAdapter(this, getLayoutInflater());
        this.mRecyclerView.setAdapter(mAdapter);
    }

    private void getFavorites() {
        // Retrieve the list of posts from internal storage
        try {
            this.mSavedPosts = (ArrayList<Post>) InternalStorage.readObject(this, Config.FAVS_KEY);
            Log.d(Config.DEBUG_TAG, "GOT FAVORITES! size: " + String.valueOf(mSavedPosts.size()));

            for (Post post : mSavedPosts) {
                Log.d(Config.DEBUG_TAG, "SAVED POST: " + post.mPostUrl);
            }

        } catch (ClassNotFoundException e) {
            this.mSavedPosts = null;
            // switch frag
            showNotFound();
        } catch (IOException e) {
            this.mSavedPosts = null;
            showNotFound();
            e.printStackTrace();
        }
    }

    private void showNotFound() {
        Log.d(Config.DEBUG_TAG, "not found!");
        MainActivityStart mainFrag = (MainActivityStart) getSupportFragmentManager().findFragmentById(R.id.mainFrag);
        mainFrag.switchViews();

//        android.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
//        NotFoundFrag mNotFoundFrag = new NotFoundFrag();
//        transaction.add(R.id.main_frag_container, mNotFoundFrag).commit();
    }

}
