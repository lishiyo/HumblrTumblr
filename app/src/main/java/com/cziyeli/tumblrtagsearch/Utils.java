package com.cziyeli.tumblrtagsearch;

import android.view.View;

/**
 * General utility helpers
 */

public class Utils {

    /** Toggle View Visibility based on current state or existence of a field **/

    public static void toggleVisibility(View view) {
        if (view.getVisibility() == View.VISIBLE) {
            view.setVisibility(View.GONE);
        } else {
            view.setVisibility(View.VISIBLE);
        }
    }

    public static void toggleVisibility(View view, String attr) {
        if (attr != null && attr.length() > 0) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }

    public static void toggleVisibility(View view, String[] attrArr) {
        if (attrArr.length > 0) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }


}
