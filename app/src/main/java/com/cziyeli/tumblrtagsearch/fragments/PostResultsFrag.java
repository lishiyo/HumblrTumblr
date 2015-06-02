package com.cziyeli.tumblrtagsearch.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cziyeli.tumblrtagsearch.services.FetchPostsService;
import com.cziyeli.tumblrtagsearch.R;
import com.cziyeli.tumblrtagsearch.adapters.PostAdapter;
import com.cziyeli.tumblrtagsearch.models.Post;
import com.malinskiy.superrecyclerview.SuperRecyclerView;

import java.util.ArrayList;

/**
 * Created by connieli on 6/1/15.
 */
public class PostResultsFrag extends Fragment {
    private View mView;
    private SuperRecyclerView mRecyclerView;
    private PostAdapter mAdapter;
    private LayoutInflater mInflater;
    private Activity mActivity;
    public SavedPostsReceiver mPostsReceiver;
    public ArrayList<Post> mSavedPosts = null;

    // this method is only called once for this fragment
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.frag_search_results, container, false);
        mRecyclerView = (SuperRecyclerView) mView.findViewById(R.id.list);
        mInflater = inflater;
        return mView;
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        mActivity = getActivity();

        if (mRecyclerView != null) {
            // setup RecyclerView Adapter
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            mAdapter = new PostAdapter(mActivity, mInflater);
            mRecyclerView.setAdapter(mAdapter);

            //setup BroadcastReceiver for Activity
            mPostsReceiver = new SavedPostsReceiver();
            IntentFilter intentFilter = new IntentFilter(FetchPostsService.ACTION_RESPONSE);
            intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
            mActivity.registerReceiver(mPostsReceiver, intentFilter);

            startFetchService();
        }
    }

    // clean up resources associated with its View
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        this.mView = null;
        this.mRecyclerView = null;
        this.mInflater = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mActivity.unregisterReceiver(mPostsReceiver);
    }

    /** INTENT SERVICE - FETCH POSTS FROM INTERNAL STORAGE **/

    private void startFetchService() {
        Intent localIntent = new Intent(mActivity, FetchPostsService.class);
        mActivity.startService(localIntent);
    }

    public class SavedPostsReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            mSavedPosts = (ArrayList<Post>) intent.getSerializableExtra("data");
            if (mSavedPosts != null) {
                mAdapter.updateData(mSavedPosts);
            } else {
                Toast.makeText(mActivity, "no favorited posts were found", Toast.LENGTH_LONG).show();
            }
        }
    }

}