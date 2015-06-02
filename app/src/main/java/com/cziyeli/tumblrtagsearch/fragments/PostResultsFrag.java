package com.cziyeli.tumblrtagsearch.fragments;

import android.app.Activity;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cziyeli.tumblrtagsearch.Config;
import com.cziyeli.tumblrtagsearch.R;
import com.cziyeli.tumblrtagsearch.adapters.PostAdapter;
import com.cziyeli.tumblrtagsearch.models.InternalStorage;
import com.cziyeli.tumblrtagsearch.models.Post;
import com.malinskiy.superrecyclerview.SuperRecyclerView;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by connieli on 6/1/15.
 */
public class PostResultsFrag extends Fragment {
    private SuperRecyclerView mRecyclerView;
    private View mView;
    private PostAdapter mAdapter;
    private LayoutInflater mInflater;
    private Activity mActivity;
    public ArrayList<Post> mSavedPosts = null;

    // this method is only called once for this fragment
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mView = inflater.inflate(R.layout.frag_search_results, container, false);
        this.mRecyclerView = (SuperRecyclerView) mView.findViewById(R.id.list);
        this.mInflater = inflater;
        return mView;
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        mActivity = getActivity();

        if (mRecyclerView != null) {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            mAdapter = new PostAdapter(mActivity, mInflater);
            mRecyclerView.setAdapter(mAdapter);

            startIAService(true);

            fetchPosts();
        } else {
            Log.d(Config.DEBUG_TAG, "mRecyclerView NULL!");
        }
    }

    private void fetchPosts(){
        try {
            mSavedPosts = (ArrayList<Post>) InternalStorage.readObject(mActivity, Config.FAVS_KEY);
            mAdapter.updateData(mSavedPosts);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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




    private Intent prepareIntent(boolean isSending) {
        Intent localIntent = new Intent(getActivity(), StartIActivity.class);
        Log.d(Config.DEBUG_TAG, "StartIActivity");
        localIntent.putExtra("incoming", isSending);
        return localIntent;
    }

    private void startIAService(boolean bool) {
        Log.d(Config.DEBUG_TAG, "Start Service");
        Context ctx = getActivity();
        ctx.startService(prepareIntent(bool));
        return;
    }

    public class StartIActivity extends IntentService {
        public StartIActivity() {
            super("StartIActivity");
        }

        protected void onHandleIntent(Intent it) {
            Intent intent = new Intent(it);
            intent.setClass(this, Activity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            Log.d(Config.DEBUG_TAG, "Start Activity");
        }
    }

}