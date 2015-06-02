package com.cziyeli.tumblrtagsearch.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cziyeli.tumblrtagsearch.R;

/**
 * Created by connieli on 6/2/15.
 */
public class PostSearchFrag extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.frag_post_results, container, false);
    }
}
