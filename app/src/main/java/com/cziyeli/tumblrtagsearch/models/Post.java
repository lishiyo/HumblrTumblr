package com.cziyeli.tumblrtagsearch.models;

import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * https://api.tumblr.com/v2/tagged?tag=lol&api_key=fuiKNFp9vQFvjLNvx4sUwti4Yb5yGutBN4Xh10LXZhhRKjWlV4
 *  blog_name, type, tag[], post_url, note_count, caption, timestamp (to load more)
 *  types - photo, text, video
 *  photo - image_permalink, caption, photo[]
 *  text - title, body
 *  video - video_url, thumbnail_url, caption
 */

public class Post implements Serializable {

    @SerializedName("blog_name")
    public String mBlogName;

    @SerializedName("type")
    public String mType;

    @SerializedName("timestamp")
    public String mTimestamp;

    @SerializedName("post_url")
    public String mPostUrl;

    @SerializedName("note_count")
    public String mNoteCount;

    @SerializedName("tags")
    public String[] mTags;

    @SerializedName("caption")
    public String mCaption; // both Photos and Videos

    // Photo Posts
    @SerializedName("image_permalink")
    public String mImagePermalink;

    // photos: [ { caption, alt_sizes, original_size: {} }, { caption, alt_sizes, original_size } ]
    @SerializedName("photos")
    public Photo[] mPhotos;

    // Text Posts
    @SerializedName("title")
    public String mTitle;

    @SerializedName("body")
    public String mTextBody;

    // Video Posts - only support html5 capable
    // player: [ { width, embed_code }, { width, embed_code } ]
    @SerializedName("player")
    public VideoPlayer[] mVideoPlayers;

    @SerializedName("video_url") // Use for <video> tags
    public String mVideoUrl;

    @SerializedName("thumbnail_url")
    public String mVideoThumbnailUrl;

    @SerializedName("html5_capable")
    public String mHtml5capable;

    // Link Posts
    @SerializedName("description")
    public String mLinkDescription;

    @SerializedName("url")
    public String mLinkUrl;

    @SerializedName("publisher")
    public String mLinkPublisher;

    public static class Photo implements Serializable {
        @SerializedName("caption")
        public String photoCaption;

        @SerializedName("original_size")
        public OriginalPhoto originalPhoto;

        public static class OriginalPhoto implements Serializable {
            @SerializedName("url")
            public String originalPhotoUrl;
        }
    }

    public static class VideoPlayer implements Serializable {
        @SerializedName("width")
        public String width;

        @SerializedName("embed_code")
        public String embedCode;
    }

    /** UTILITIES **/

    // parse a post's url for post id
    public String getPostId(String path) {
        String delimiters = "//|/";
        String[] paths = path.split(delimiters);
        return paths[3];
    }

    // For post source, return href to post with display text blog or link publisher
    public Spanned sourceToLink() {
        if (this.mType.equals("link")) {
            return buildLink(mPostUrl, mLinkPublisher);
        } else {
            return buildLink(mPostUrl, mBlogName);
        }
    }

    public Spanned buildLink(String url, String displayText) {
        String link = "<a href=\"" + url + "\">" + displayText + "</a>";
        return Html.fromHtml(link);
    }

    public String tagsToString() {
        String[] convertedTags = new String[this.mTags.length];
        for (int i = 0; i < this.mTags.length; i++) {
            String tag = "#" + mTags[i];
            convertedTags[i] = tag;
        }
        return TextUtils.join(" ", convertedTags);
    }

    public boolean isValidVideo() {
        return (mHtml5capable != null && mHtml5capable.equals("true"));
    }
}
