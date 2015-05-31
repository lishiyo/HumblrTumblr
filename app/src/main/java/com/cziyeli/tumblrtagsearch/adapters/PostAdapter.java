package com.cziyeli.tumblrtagsearch.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cziyeli.tumblrtagsearch.Config;
import com.cziyeli.tumblrtagsearch.R;
import com.cziyeli.tumblrtagsearch.models.Post;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * https://api.tumblr.com/v2/tagged?tag=lol&api_key=fuiKNFp9vQFvjLNvx4sUwti4Yb5yGutBN4Xh10LXZhhRKjWlV4
 *  blog_name, type, tags[], post_url, note_count, caption
 *  types - photo, text, video
 *  photo - image_permalink, caption
 *  text - title, body
 *  video - video_url, thumbnail_url, caption
 */

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    private Context mContext;
    private LayoutInflater mInflater;
    public ArrayList<Post> mPosts;
    private static final int TYPE_PHOTO = 0;
    private static final int TYPE_TEXT = 1;
    private static final int TYPE_VIDEO = 2;
    public static final int TYPE_BUTTON = 99;

    /** CONSTRUCTOR **/
    public PostAdapter(Context context, LayoutInflater inflater) {
        this.mContext = context;
        this.mInflater = inflater;
        this.mPosts = new ArrayList<Post>();
    }

    public void updateData(ArrayList<Post> data) {
        this.mPosts.addAll(data);
        Log.d(Config.DEBUG_TAG, "UPDATE DATA count: " + String.valueOf(getItemCount()));
        notifyDataSetChanged();
    }

    /** UTILITIES **/
    public Post getPost(int position) {
        if (mPosts != null && position >=0 && position < mPosts.size()) {
            return mPosts.get(position);
        } else {
            return null;
        }
    }

    @Override
    public int getItemCount() {
        return mPosts == null ? 0 : mPosts.size();
    }

    // Check what type of view is being passed
    @Override
    public int getItemViewType(int position) {
        Post post = getPost(position);

        switch (post.mType) {
            case "photo":
                return TYPE_PHOTO;
            case "text":
                return TYPE_TEXT;
            case "video":
                return TYPE_VIDEO;
            default:
                return TYPE_TEXT;
        }

    }


    /** VIEWHOLDER **/

    /*
    Change from ListView - rather than checking for a tag on a view in getView() before deciding to create a new instance instead of reusing an old one, RecyclerView API takes care of all the logic.
    Two methods implement view construction and reuse: onCreateViewHolder, onBindViewHolder
    */

    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        Context context = parent.getContext();

        switch (viewType) {
            case TYPE_PHOTO:
                itemView = LayoutInflater.from(context).inflate(R.layout.post_item_photo, parent, false);
                break;
            case TYPE_TEXT:
                itemView = LayoutInflater.from(context).inflate(R.layout.post_item_text, parent, false);
                break;
            case TYPE_VIDEO:
                itemView = LayoutInflater.from(context).inflate(R.layout.post_item_video, parent, false);
                break;
            default:
                break;
        }

        return (itemView != null) ? new ViewHolder(itemView, viewType) : null;

    }

    // Called when the item in a row must be displayed
    @Override
    public void onBindViewHolder(PostAdapter.ViewHolder viewHolder, int position) {
        Post post = getPost(position);

        // Every post has blog name, notes count, and tags
        viewHolder.blogName.setText(post.mBlogName);
        viewHolder.noteCount.setText(post.mNoteCount);
        if (post.mTags.length > 0) {
            viewHolder.postTags.setText(post.tagsToString());
        }

        if (viewHolder.postType == TYPE_PHOTO) {

            if (post.mPhotos != null) {

                Log.d(Config.DEBUG_TAG, "post mPhotos length: " + post.mPhotos.length);

                // Photo posts must have at least one photo
                String firstPhotoUrl = post.mPhotos[0].originalPhoto.originalPhotoUrl;

                if (firstPhotoUrl != null) {
                    Picasso.with(this.mContext)
                            .load(firstPhotoUrl)
                            .placeholder(R.mipmap.ic_launcher)
                            .into(viewHolder.postPhotoFirst);
                } else {
                    viewHolder.postPhotoFirst.setImageResource(R.mipmap.ic_launcher);
                }

                // If we have more than one photo, dynamically generate ImageView and add to postPhotoLayout
                if (post.mPhotos.length > 1) {
                    String nextPhotoUrl;

                    for (int i = 1; i < post.mPhotos.length; i++) {
                        nextPhotoUrl = post.mPhotos[i].originalPhoto.originalPhotoUrl;
                        Log.d(Config.DEBUG_TAG, "more than one photo, url: " + nextPhotoUrl);

                        ImageView photoView = new ImageView(mContext);
                        viewHolder.postPhotoLayout.addView(photoView);
                        if (nextPhotoUrl != null) {
                            Picasso.with(this.mContext)
                                    .load(nextPhotoUrl)
                                    .placeholder(R.mipmap.ic_launcher)
                                    .into(photoView);
                        }
                    }

                }

            }

            if (post.mCaption != null && post.mCaption.length() > 0) {
                viewHolder.photoCaption.setText(Html.fromHtml(post.mCaption));
            }

        } else if (viewHolder.postType == TYPE_TEXT) {
            if (post.mTextTitle != null && post.mTextTitle.length() > 0) {
                viewHolder.textTitle.setText(Html.fromHtml(post.mTextTitle));
            }
            if (post.mTextBody != null && post.mTextBody.length() > 0) {
                viewHolder.textBody.setText(Html.fromHtml(post.mTextBody));
            }
        } else if (viewHolder.postType == TYPE_VIDEO) {
            Log.d(Config.DEBUG_TAG, "viewholder with type video");
            if (post.mCaption != null && post.mCaption.length() > 0) {
                viewHolder.videoCaption.setText(Html.fromHtml(post.mCaption));
            }
            // take narrowest player out of 250, 400, 500 and display in webView
            if (post.mVideoPlayers != null && post.mVideoPlayers.length > 0) {
                Post.VideoPlayer player = post.mVideoPlayers[0];
                viewHolder.videoPlayerView.loadData(player.embedCode, "text/html", "utf-8");
            }
        }

    }

    // Instantiate all views
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView blogName;
        public TextView noteCount;
        public TextView postTags;
        public int postType;

        // PHOTO POSTS
        public LinearLayout postPhotoLayout;
        public ImageView postPhotoFirst; // we have at least one photo
        public TextView photoCaption;

        // TEXT POSTS
        public TextView textTitle;
        public TextView textBody;

        // VIDEO POSTS
        public WebView videoPlayerView;
        public TextView videoCaption;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            postType = viewType;
            blogName = (TextView) itemView.findViewById(R.id.postBlogName);
            noteCount = (TextView) itemView.findViewById(R.id.postNoteCount);
            postTags = (TextView) itemView.findViewById(R.id.postTagsRow);

            switch (viewType) {
                case TYPE_PHOTO:
                    postPhotoLayout = (LinearLayout) itemView.findViewById(R.id.postPhotoLayout);
                    postPhotoFirst = (ImageView) itemView.findViewById(R.id.postPhotoSingle);
                    photoCaption = (TextView) itemView.findViewById(R.id.postPhotoCaption);
                    break;
                case TYPE_TEXT:
                    textTitle = (TextView) itemView.findViewById(R.id.textTitle);
                    textBody = (TextView) itemView.findViewById(R.id.textBody);
                    break;
                case TYPE_VIDEO:
                    videoCaption = (TextView) itemView.findViewById(R.id.postVideoCaption);
                    videoPlayerView = (WebView) itemView.findViewById(R.id.postVideoPlayer);
                    final WebSettings settings = videoPlayerView.getSettings();
                    settings.setDefaultTextEncodingName("utf-8");
                    settings.setJavaScriptEnabled(true);
                    settings.setLoadsImagesAutomatically(true);
                    break;
                default:
                    break;
            }

        }
    }

}
