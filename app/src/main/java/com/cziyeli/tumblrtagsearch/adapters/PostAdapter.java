package com.cziyeli.tumblrtagsearch.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.cziyeli.tumblrtagsearch.Config;
import com.cziyeli.tumblrtagsearch.R;
import com.cziyeli.tumblrtagsearch.models.Post;

/**
 * https://api.tumblr.com/v2/tagged?tag=lol&api_key=fuiKNFp9vQFvjLNvx4sUwti4Yb5yGutBN4Xh10LXZhhRKjWlV4
 *  blog_name, type, tags[], post_url, note_count, caption
 *  types - photo, text, video
 *  photo - image_permalink, caption
 *  text - title, body
 *  video - video_url, thumbnail_url, caption
 */

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    private Post[] mPosts;
    private Context mContext;
    private static final int TYPE_PHOTO = 0;
    private static final int TYPE_TEXT = 1;
    private static final int TYPE_VIDEO = 2;

    /** CONSTRUCTOR **/
    public PostAdapter(Post[] posts, Context context) {
        Log.d(Config.DEBUG_TAG, "create PostAdapter");
        this.mPosts = posts;
        this.mContext = context;
    }

    /** UTILITIES **/
    public Post getPost(int position) {
        if (mPosts != null && position >=0 && position < mPosts.length) {
            return mPosts[position];
        } else {
            return null;
        }
    }

    @Override
    public int getItemCount() {
        return mPosts == null ? 0 : mPosts.length;
    }

    // Check what type of view is being passed
    @Override
    public int getItemViewType(int position) {
        Post post = getPost(position);

        if ("photo".equals(post.mType)) {
            return TYPE_PHOTO;
        } else {
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
        View itemView;
        Context context = parent.getContext();

        if (viewType == TYPE_PHOTO) {
            itemView = LayoutInflater.from(context).inflate(R.layout.post_item_photo, parent, false);
            return new ViewHolder(itemView, viewType, context);
        } else if (viewType == TYPE_TEXT) {
            itemView = LayoutInflater.from(context).inflate(R.layout.post_item_text, parent, false);
            return new ViewHolder(itemView, viewType, context);
        }

        return null;
    }

    // Called when the item in a row must be displayed
    @Override
    public void onBindViewHolder(PostAdapter.ViewHolder viewHolder, int position) {
        Post post = getPost(position);

        viewHolder.blogName.setText(post.mBlogName);
        viewHolder.noteCount.setText(post.mNoteCount);

        if (viewHolder.postType == TYPE_PHOTO) {

            // for each Photo in Photo[], map photo's url
            if (post.mPhotos != null) {
                String[] photoUrls = new String[post.mPhotos.length];
                String currPhotoUrl;
                for (int i = 0; i < post.mPhotos.length; i++) {
                    currPhotoUrl = post.mPhotos[i].originalPhoto.originalPhotoUrl;
                    photoUrls[i] = currPhotoUrl;
                }

                Log.d(Config.DEBUG_TAG, "post mPhotos has photos, length: " + photoUrls.length);
                viewHolder.photoListAdapter.updateData(photoUrls);
            }

//            String photoUrl = null;
//
//            if (post.mPhotos != null) {
//                // for each photo in mPhotos, send in photo <-- first for now
//                photoUrl =  post.mPhotos[0].originalPhoto.originalPhotoUrl;
//            }
//
//            if (photoUrl != null) {
//                Picasso.with(this.mContext)
//                        .load(photoUrl)
//                        .placeholder(R.mipmap.ic_launcher)
//                        .into(viewHolder.photoMain);
//            } else {
//                viewHolder.photoMain.setImageResource(R.mipmap.ic_launcher);
//            }

            if (post.mCaption != null && post.mCaption.length() > 0) {
                viewHolder.photoCaption.setText(Html.fromHtml(post.mCaption));
            }

        } else if (viewHolder.postType == TYPE_TEXT) {
            Log.d(Config.DEBUG_TAG, "onBindViewHolder TEXT: " + post.mTextTitle);

            if (post.mTextTitle != null && post.mTextTitle.length() > 0) {
                viewHolder.textTitle.setText(Html.fromHtml(post.mTextTitle));
            }
            if (post.mTextBody != null && post.mTextBody.length() > 0) {
                viewHolder.textBody.setText(Html.fromHtml(post.mTextBody));
            }
        }

    }

    // Instantiate all views
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView blogName;
        public TextView noteCount;
        public int postType;

        // PHOTO POSTS
//        public ImageView photoMain;
        public TextView photoCaption;
        public ListView photoListView;
        public PhotoListAdapter photoListAdapter;

        // TEXT POSTS
        public TextView textTitle;
        public TextView textBody;

        public ViewHolder(View itemView, int viewType, Context context) {
            super(itemView);
            postType = viewType;
            blogName = (TextView) itemView.findViewById(R.id.postBlogName);
            noteCount = (TextView) itemView.findViewById(R.id.postNoteCount);

            if (viewType == TYPE_PHOTO) {
//                photoMain = (ImageView) itemView.findViewById(R.id.postImagePerm);
                photoCaption = (TextView) itemView.findViewById(R.id.postPhotoCaption);
                photoListView = (ListView) itemView.findViewById(R.id.postPhotoList);
                photoListAdapter = new PhotoListAdapter(context, LayoutInflater.from(context));
                photoListView.setAdapter(photoListAdapter);
            } else if (viewType == TYPE_TEXT) {
                textTitle = (TextView) itemView.findViewById(R.id.textTitle);
                textBody = (TextView) itemView.findViewById(R.id.textBody);
            }

        }
    }

}
