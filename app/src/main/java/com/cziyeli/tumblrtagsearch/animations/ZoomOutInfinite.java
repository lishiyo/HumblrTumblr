//package com.cziyeli.tumblrtagsearch.animations;
//
//import android.view.View;
//
//import com.daimajia.androidanimations.library.BaseViewAnimator;
//import com.nineoldandroids.animation.ObjectAnimator;
//
///**
// * Created by connieli on 6/2/15.
// */
//
//public class ZoomOutInfinite extends BaseViewAnimator {
//    @Override
//    protected void prepare(View target) {
//        ObjectAnimator oa1 = ObjectAnimator.ofFloat(target, "alpha", 1, 0, 0);
//        ObjectAnimator oa2 = ObjectAnimator.ofFloat(target, "alpha", 1, 0, 0);
//        ObjectAnimator oa3 = ObjectAnimator.ofFloat(target, "scaleX", 1, 0.3f, 0);
//        ObjectAnimator oa4 = ObjectAnimator.ofFloat(target, "scaleY", 1, 0.3f, 0);
//
//        oa1.setRepeatCount(ObjectAnimator.INFINITE);
//        oa2.setRepeatCount(ObjectAnimator.INFINITE);
//        oa3.setRepeatCount(ObjectAnimator.INFINITE);
//        oa4.setRepeatCount(ObjectAnimator.INFINITE);
//
//        getAnimatorAgent().playTogether(
//                oa1, oa2, oa3, oa4
//        );
//    }
//}