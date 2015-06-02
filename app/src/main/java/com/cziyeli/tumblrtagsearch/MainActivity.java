package com.cziyeli.tumblrtagsearch;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.cziyeli.tumblrtagsearch.models.InternalStorage;


public class MainActivity extends SearchableActivity {
    private Button showFavsBtn;
    private Button clearFavsBtn;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        startAnimation();

        showFavsBtn = (Button) findViewById(R.id.showFavsBtn);
        if (showFavsBtn != null) {
            showFavsBtn.setOnClickListener(mShowFavsListener);
        }

        clearFavsBtn = (Button) findViewById(R.id.clearFavs);
        if (clearFavsBtn != null) {
            clearFavsBtn.setOnClickListener(mClearFavsListener);
        }
    }

    private void startAnimation() {
        ImageView loadMoreImage = (ImageView) findViewById(R.id.homeImage);
        Animation testAnim = AnimationUtils.loadAnimation(this, R.anim.fade_infinite);
        loadMoreImage.startAnimation(testAnim);

        //        YoYo.with(new BounceInDownInfinite())
//                .duration(1000)
//                .playOn(findViewById(R.id.homeImage));

    }

    /** SHOW/CLEAR FAVORITED POSTS **/

    private View.OnClickListener mShowFavsListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), FavoritesActivity.class);
            startActivity(intent);
        }
    };

    private View.OnClickListener mClearFavsListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            InternalStorage.clearStorage(getApplicationContext(), CONSTANTS.FAVS_KEY);
            Toast.makeText(MainActivity.this, "wiped!", Toast.LENGTH_SHORT).show();
        }
    };
}
