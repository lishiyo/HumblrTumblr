//package com.cziyeli.tumblrtagsearch.fragments;
//
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.TextView;
//
//import com.cziyeli.tumblrtagsearch.R;
//
///**
// * Created by connieli on 6/1/15.
// */
//public class SearchResFrag extends Fragment {
//    public Button mShowFavsBtn;
//    public TextView mNotFoundMsg;
//    private View mainFragView;
//
//    public SearchResFrag() {
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        mainFragView = inflater.inflate(R.layout.frag_search_results, container, false);
//        mShowFavsBtn = (Button) mainFragView.findViewById(R.id.showFavsBtn);
//        mNotFoundMsg = (TextView) mainFragView.findViewById(R.id.notFoundMsg);
//
//        return mainFragView;
//    }
//
//    // hide button, show not found message
//    public void switchViews(){
//        if (mShowFavsBtn.getVisibility() == View.VISIBLE) {
//            mShowFavsBtn.setVisibility(View.GONE);
//        } else {
//            mShowFavsBtn.setVisibility(View.VISIBLE);
//        }
//        if (mNotFoundMsg.getVisibility() == View.GONE) {
//            mNotFoundMsg.setVisibility(View.VISIBLE);
//        } else {
//            mNotFoundMsg.setVisibility(View.GONE);
//        }
//    }
//}
