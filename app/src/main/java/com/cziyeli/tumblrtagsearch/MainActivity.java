package com.cziyeli.tumblrtagsearch;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends SearchableActivity {
    private Button showFavsBtn;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        showFavsBtn = (Button) findViewById(R.id.showFavsBtn);
        if (showFavsBtn != null) {
            showFavsBtn.setOnClickListener(mShowFavsListener);
        }
    }

    /** SHOW FAVORITE POSTS **/

    private View.OnClickListener mShowFavsListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), FavoritesActivity.class);
            startActivity(intent);
        }
    };
}
