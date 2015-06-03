package com.cziyeli.tumblrtagsearch.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.cziyeli.tumblrtagsearch.R;

/**
 * Created by connieli on 6/2/15.
 */
public class PostSearchFrag extends Fragment {
    protected ImageView mLoadMoreImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.frag_post_results, container, false);
    }

    /** PROGRESS LOADING ANIM **/

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        mLoadMoreImage = (ImageView) getActivity().findViewById(R.id.homeImage);
        startProgressAnimation(getActivity());
    }

    public void startProgressAnimation(Context ctx) {
        if (mLoadMoreImage != null) {
            Animation anim = AnimationUtils.loadAnimation(ctx, R.anim.fade_infinite);
            mLoadMoreImage.startAnimation(anim);
        }
    }
}
