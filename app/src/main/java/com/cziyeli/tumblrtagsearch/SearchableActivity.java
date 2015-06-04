package com.cziyeli.tumblrtagsearch;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by connieli on 6/1/15.
 */
public class SearchableActivity extends AppCompatActivity {
    protected String mCurrQuery = "";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);
    }

    // only if activity launchMode="singleTop"
    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        launchSearch();
    }

    // Every SearchableActivity implements this interface
    protected void launchSearch() {
        if (Intent.ACTION_SEARCH.equals(getIntent().getAction())) {
            setupPostViews();
            handleIntent(getIntent());
        }
    }

    protected void setupPostViews() {};
    protected void handleIntent(Intent intent) {};

    /** SEARCH ACTION BAR **/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();

        // call to getSearchableInfo obtains a SearchableInfo object created from searchable.xml
        // SearchView starts an activity with ACTION_SEARCH intent when user submits a query
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

//        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }
}
