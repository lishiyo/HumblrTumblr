package com.cziyeli.tumblrtagsearch.models;

/**
 * https://androidresearch.wordpress.com/2013/04/07/caching-objects-in-android-internal-storage/
 */

import android.content.Context;
import android.util.Log;

import com.cziyeli.tumblrtagsearch.Config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/** SAVING TO INTERNAL STORAGE **/
public final class InternalStorage {

    private InternalStorage() {}

    public static void writeObject(Context context, String key, Object object) throws IOException {
        FileOutputStream fos = context.openFileOutput(key, Context.MODE_PRIVATE);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(object);
        oos.close();
        fos.close();
    }

    public static Object readObject(Context context, String key) throws IOException, ClassNotFoundException {
        FileInputStream fis = context.openFileInput(key);
        ObjectInputStream ois = new ObjectInputStream(fis);
        Object object = ois.readObject();
        return object;
    }

    public static void clearStorage(Context context, String key) {
        Log.d(Config.DEBUG_TAG, "clear Storage " + key);
        context.deleteFile(key);
    }

    public static boolean hasObjects(Context context, String key) {
        File file = context.getFileStreamPath(key);
        return (file != null && file.exists());
    }
}