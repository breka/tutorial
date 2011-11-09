package org.imogene.android.util.content;

import android.net.Uri;

public class ContentUrisUtils {
    /**
     * Appends the given ID to the end of the path.
     *
     * @param builder to append the ID to
     * @param id to append
     *
     * @return the given builder
     */
    public static Uri.Builder appendId(Uri.Builder builder, String id) {
        return builder.appendEncodedPath(id);
    }

    /**
     * Appends the given ID to the end of the path.
     *
     * @param contentUri to start with
     * @param id to append
     *
     * @return a new URI with the given ID appended to the end of the path
     */
    public static Uri withAppendedId(Uri contentUri, String id) {
        return appendId(contentUri.buildUpon(), id).build();
    }
}
