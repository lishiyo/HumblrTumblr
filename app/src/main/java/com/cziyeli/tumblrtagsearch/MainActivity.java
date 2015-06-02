package com.cziyeli.tumblrtagsearch;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.cziyeli.tumblrtagsearch.animations.BounceInDownInfinite;
import com.cziyeli.tumblrtagsearch.models.InternalStorage;
import com.daimajia.androidanimations.library.YoYo;


public class MainActivity extends SearchableActivity {
    private Button showFavsBtn;
    private Button clearFavsBtn;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        YoYo.with(new BounceInDownInfinite())
//                .duration(1000)
                .playOn(findViewById(R.id.homeImage));

        showFavsBtn = (Button) findViewById(R.id.showFavsBtn);
        if (showFavsBtn != null) {
            showFavsBtn.setOnClickListener(mShowFavsListener);
        }

        clearFavsBtn = (Button) findViewById(R.id.clearFavs);
        if (clearFavsBtn != null) {
            clearFavsBtn.setOnClickListener(mClearFavsListener);
        }
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
