package com.cziyeli.tumblrtagsearch;

import android.os.Bundle;

import com.cziyeli.tumblrtagsearch.fragments.NotFoundFrag;
import com.cziyeli.tumblrtagsearch.fragments.PostFavoritesFrag;
import com.cziyeli.tumblrtagsearch.models.InternalStorage;
import com.cziyeli.tumblrtagsearch.models.Post;

import java.util.ArrayList;

/**
 * Created by connieli on 6/1/15.
 */

public class FavoritesActivity extends SearchableActivity {
    public ArrayList<Post> mSavedPosts;
    private android.support.v4.app.FragmentTransaction mFT;
    private PostFavoritesFrag mPostResFrag;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favs);
        mSavedPosts = null;

        mFT = getSupportFragmentManager().beginTransaction();

        getFavorites();
    }

    private void getFavorites() {
        // Retrieve the list of posts from internal storage
        if (InternalStorage.hasObjects(this, CONSTANTS.FAVS_KEY)) {
            showPostResults();
        } else {
            showNotFound();
        }
    }

    // Don't add to back stack
    private void showPostResults() {
        // pass mSavedPosts to Bundle
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", mSavedPosts);

        // mPostResFrag's onCreateView sets up recyclerView and adapter
        mPostResFrag = new PostFavoritesFrag();
        mPostResFrag.setArguments(bundle);
        mFT.add(R.id.postsResContainer, mPostResFrag).commit();
    }

    private void showNotFound() {
        NotFoundFrag mNotFoundFrag = new NotFoundFrag();
        mFT.add(R.id.postsResContainer, mNotFoundFrag).commit();
    }

}
