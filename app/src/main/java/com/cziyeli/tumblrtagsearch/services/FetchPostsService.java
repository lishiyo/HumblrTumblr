package com.cziyeli.tumblrtagsearch.services;

import android.app.IntentService;
import android.content.Intent;

import com.cziyeli.tumblrtagsearch.CONSTANTS;
import com.cziyeli.tumblrtagsearch.models.InternalStorage;
import com.cziyeli.tumblrtagsearch.models.Post;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Spawns worker thread, one request at a time - automatically calls stopSelf() when work queue is empty.
 */

public class FetchPostsService extends IntentService {
    public static final String ACTION_RESPONSE = CONSTANTS.PACKAGE_BASE + ".RESPONSE";

    public FetchPostsService() {
        super("FetchPostsService");
    }

    protected void onHandleIntent(Intent intent) {
        getSavedPosts();
    }

    protected void getSavedPosts() {
        ArrayList<Post> posts = null;

        try {
            posts =  (ArrayList<Post>) InternalStorage.readObject(getApplicationContext(), CONSTANTS.FAVS_KEY);
            broadcastResults(posts);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void broadcastResults(ArrayList<Post> posts) {
        Intent intentResponse = new Intent();
        intentResponse.setAction(ACTION_RESPONSE);
        intentResponse.addCategory(Intent.CATEGORY_DEFAULT);
        intentResponse.putExtra("data", posts);
        sendBroadcast(intentResponse);
    }
}