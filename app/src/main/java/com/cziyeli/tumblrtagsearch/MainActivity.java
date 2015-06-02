package com.cziyeli.tumblrtagsearch;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.cziyeli.tumblrtagsearch.models.InternalStorage;


public class MainActivity extends SearchableActivity {
    private Button showFavsBtn;
    private Button clearFavsBtn;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        showFavsBtn = (Button) findViewById(R.id.showFavsBtn);
        if (showFavsBtn != null) {
            showFavsBtn.setOnClickListener(mShowFavsListener);
        }

        clearFavsBtn = (Button) findViewById(R.id.clearFavs);
        if (clearFavsBtn != null) {
            clearFavsBtn.setOnClickListener(mClearFavsListener);
        }
    }

    /** SHOW OR CLEAR FAVORITED POSTS **/

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
            InternalStorage.clearStorage(getApplicationContext(), Config.FAVS_KEY);
        }
    };
}
