package com.cziyeli.tumblrtagsearch.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cziyeli.tumblrtagsearch.R;

/**
 * Created by connieli on 6/1/15.
 */
public class NotFoundFrag extends Fragment {

    public NotFoundFrag() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_not_found, container, false);
    }

}