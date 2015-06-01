package com.cziyeli.tumblrtagsearch.models;

/**
 * https://androidresearch.wordpress.com/2013/04/07/caching-objects-in-android-internal-storage/
 */

import android.content.Context;
import android.util.Log;

import com.cziyeli.tumblrtagsearch.Config;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/** SAVING TO INTERNAL STORAGE **/
public final class InternalStorage {

    private InternalStorage() {}

    public static void writeObject(Context context, String key, Object object) throws IOException {
        Log.d(Config.DEBUG_TAG, "writeObject with key: " + key);

        FileOutputStream fos = context.openFileOutput(key, Context.MODE_PRIVATE);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(object);
        oos.close();
        fos.close();
    }

    public static Object readObject(Context context, String key) throws IOException, ClassNotFoundException {
        Log.d(Config.DEBUG_TAG, "readObject with key: " + key);

        FileInputStream fis = context.openFileInput(key);
        ObjectInputStream ois = new ObjectInputStream(fis);
        Object object = ois.readObject();
        return object;
    }
}