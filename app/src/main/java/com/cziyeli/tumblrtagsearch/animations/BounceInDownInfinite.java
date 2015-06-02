package com.cziyeli.tumblrtagsearch.animations;

import android.view.View;

import com.daimajia.androidanimations.library.BaseViewAnimator;
import com.nineoldandroids.animation.ObjectAnimator;

/**
 * Created by connieli on 6/2/15.
 */
public class BounceInDownInfinite extends BaseViewAnimator {
    @Override
    public void prepare(View target) {
        ObjectAnimator oa1 = ObjectAnimator.ofFloat(target, "alpha", 0, 1, 1, 1);
        ObjectAnimator oa2 = ObjectAnimator.ofFloat(target, "translationY", -target.getHeight(), 30, -10, 0);
        oa1.setRepeatCount(ObjectAnimator.INFINITE);
        oa2.setRepeatCount(ObjectAnimator.INFINITE);

        getAnimatorAgent().playTogether(
                oa1, oa2
        );
    }
}