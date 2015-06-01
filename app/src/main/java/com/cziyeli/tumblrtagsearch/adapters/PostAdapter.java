package com.cziyeli.tumblrtagsearch.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cziyeli.tumblrtagsearch.Config;
import com.cziyeli.tumblrtagsearch.R;
import com.cziyeli.tumblrtagsearch.models.InternalStorage;
import com.cziyeli.tumblrtagsearch.models.Post;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    public List<Post> mSavedPosts;
    
    private static final int TYPE_PHOTO = 0;
    private static final int TYPE_TEXT = 1;
    private static final int TYPE_VIDEO = 2;
    public static final int TYPE_LINK = 3;

    /** CONSTRUCTOR **/
    public PostAdapter(Context context, LayoutInflater inflater) {
        this.mContext = context;
        this.mInflater = inflater;
        this.mPosts = new ArrayList<>();

        try {
            mSavedPosts = (ArrayList<Post>) InternalStorage.readObject(mContext, Config.FAVS_KEY);
        } catch (ClassNotFoundException e) {
            mSavedPosts = new ArrayList<>();
        } catch (IOException e) {
            mSavedPosts = new ArrayList<>();
            e.printStackTrace();
        }
    }

    public void updateData(ArrayList<Post> data) {
        ArrayList<Post> postResultsArray = new ArrayList<>();

        // filter out !html5_capable videos
        for (Post post : data) {
            if (!post.mType.equals("video") || post.isValidVideo()) {
                postResultsArray.add(post);
            }
        }

        this.mPosts.addAll(postResultsArray);
        notifyDataSetChanged();
        Log.d(Config.DEBUG_TAG, "UPDATE DATA count: " + String.valueOf(getItemCount()));
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

    @Override
    public long getItemId(int position){
        return position;
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
            case "chat":
                return TYPE_TEXT; // same fields as text
            case "video":
                return TYPE_VIDEO;
            case "link":
                return TYPE_LINK;
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

        switch (viewType) {
            case TYPE_PHOTO:
                itemView = mInflater.inflate(R.layout.post_item_photo, parent, false);
                break;
            case TYPE_TEXT:
                itemView = mInflater.inflate(R.layout.post_item_text, parent, false);
                break;
            case TYPE_VIDEO:
                itemView = mInflater.inflate(R.layout.post_item_video, parent, false);
                break;
            case TYPE_LINK:
                itemView = mInflater.inflate(R.layout.post_item_link, parent, false);
            default:
                break;
        }

        ViewHolder vh = (itemView != null) ? new ViewHolder(itemView, viewType) : null;
        vh.savePostBlock.setOnClickListener(mSaveListener);

        return vh;
    }

    /** POST SAVING **/

    private View.OnClickListener mSaveListener = new View.OnClickListener() {
        public void onClick(View v) {
            Post post = (Post) v.getTag();
            createDialog(post);
        }

        private void createDialog(final Post post) {
            AlertDialog.Builder savePostBuild = new AlertDialog.Builder(mContext);
            savePostBuild.setMessage("Save Post to Favorites?");
            savePostBuild.setCancelable(true);
            savePostBuild.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // update mSavedPosts and save to internal storage
                    try {
                        mSavedPosts.add(post);
                        Log.d(Config.DEBUG_TAG, String.valueOf(mSavedPosts.size()));
                        InternalStorage.writeObject(mContext, Config.FAVS_KEY, mSavedPosts);

                    } catch (IOException e) {
                        Log.e(Config.DEBUG_TAG, e.getMessage());
                        e.printStackTrace();
                    }

                    dialog.cancel();
                }
            });
            savePostBuild.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });

            AlertDialog savePostAlert = savePostBuild.create();
            savePostAlert.show();
        }
    };



    // Called when the item in a row must be displayed
    @Override
    public void onBindViewHolder(PostAdapter.ViewHolder viewHolder, int position) {
        Post post = getPost(position);

        viewHolder.savePostBlock.setTag(post);

        // // Every post has blog name, notes count, post source, and maybe tags
        bindBasePost(viewHolder, post);

        switch (viewHolder.postType) {
            case TYPE_PHOTO:
                handlePhotoPosts(viewHolder, post);
                break;
            case TYPE_TEXT:
                handleTextPosts(viewHolder, post);
                break;
            case TYPE_VIDEO:
                handleVideoPosts(viewHolder, post);
                break;
            case TYPE_LINK:
                handleLinkPosts(viewHolder, post);
                break;
        }

    }




    private void bindBasePost(PostAdapter.ViewHolder viewHolder, Post post) {
        viewHolder.blogName.setText(post.mBlogName);
        viewHolder.noteCount.setText(post.mNoteCount + " notes");

        // For post source, set text as blogname or link publisher, with href to post
        viewHolder.postSource.setText(post.sourceToLink());
        viewHolder.postSource.setMovementMethod(LinkMovementMethod.getInstance());

        if (post.mTags.length > 0) {
            viewHolder.postTags.setText(post.tagsToString());
            viewHolder.postTags.setVisibility(View.VISIBLE);
        }
    }

    /** HANDLE POST TYPES - BINDING TO VIEWHOLDER **/
    private void handlePhotoPosts(ViewHolder viewHolder, Post post){

        if (post.mPhotos != null) {
            viewHolder.postPhotoLayout.setVisibility(View.VISIBLE);

            // Remove previous extra photo views
            int numChildViews = viewHolder.postPhotoLayout.getChildCount();
            if (numChildViews > 0) {
                viewHolder.postPhotoLayout.removeViews(1, numChildViews - 1);
            }

            // Photo posts must have at least one photo
            String firstPhotoUrl = post.mPhotos[0].originalPhoto.originalPhotoUrl;

            if (firstPhotoUrl != null) {
                Picasso.with(this.mContext)
                        .load(firstPhotoUrl)
                        .placeholder(Config.PLACEHOLDER_IMG)
                        .into(viewHolder.postPhotoFirst);
            }

            // If we have more than one photo, dynamically generate ImageView and add to postPhotoLayout
            if (post.mPhotos.length > 1) {
                String nextPhotoUrl;

                for (int i = 1; i < post.mPhotos.length; i++) {
                    nextPhotoUrl = post.mPhotos[i].originalPhoto.originalPhotoUrl;
                    Log.d(Config.DEBUG_TAG, "more than one photo, url: " + nextPhotoUrl);

                    ImageView photoView = new ImageView(mContext);
                    photoView.setAdjustViewBounds(true);
                    photoView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    viewHolder.postPhotoLayout.addView(photoView);
                    if (nextPhotoUrl != null) {
                        Picasso.with(this.mContext)
                                .load(nextPhotoUrl)
                                .placeholder(Config.PLACEHOLDER_IMG)
                                .into(photoView);
                    }
                }

            }
        } else { // no photos!
            viewHolder.postPhotoLayout.setVisibility(View.GONE);
        }

        if (post.mCaption != null && post.mCaption.length() > 0) {
            viewHolder.photoCaption.setText(Html.fromHtml(post.mCaption));
            viewHolder.photoCaption.setVisibility(View.VISIBLE);
        }
    }

    private void handleTextPosts(ViewHolder viewHolder, Post post) {
        if (post.mTitle != null && post.mTitle.length() > 0) {
            viewHolder.postTitle.setText(Html.fromHtml(post.mTitle));
            viewHolder.postTitle.setVisibility(View.VISIBLE);
        }

        if (post.mTextBody != null && post.mTextBody.length() > 0) {
            viewHolder.textBody.setText(Html.fromHtml(post.mTextBody));
            viewHolder.textBody.setVisibility(View.VISIBLE);
        }
    }

    private void handleVideoPosts(ViewHolder viewHolder, Post post) {
        if (post.mCaption != null && post.mCaption.length() > 0) {
            viewHolder.videoCaption.setVisibility(View.VISIBLE);
            viewHolder.videoCaption.setText(Html.fromHtml(post.mCaption));
        }
        // take smallest player out of 250, 400, 500 and display in webView
        if (post.mVideoPlayers != null && post.mVideoPlayers.length > 0) {
            Post.VideoPlayer player = post.mVideoPlayers[0];
            viewHolder.videoPlayerView.loadData(player.embedCode, "text/html", "utf-8");
        }
    }

    private void handleLinkPosts(ViewHolder viewHolder, Post post) {
        // Title goes to link url
        if (post.mTitle != null && post.mTitle.length() > 0) {
            viewHolder.postTitle.setText(post.buildLink(post.mLinkUrl, post.mTitle));
            viewHolder.postTitle.setMovementMethod(LinkMovementMethod.getInstance());
            viewHolder.postTitle.setVisibility(View.VISIBLE);
        }
        if (post.mLinkDescription != null && post.mLinkDescription.length() > 0) {
            viewHolder.linkDescription.setText(Html.fromHtml(post.mLinkDescription));
            viewHolder.linkDescription.setVisibility(View.VISIBLE);
        }
    }

    // Instantiate all views
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView blogName;
        public TextView noteCount;
        public TextView postTags;
        public TextView postSource;
        public LinearLayout savePostBlock;
        public int postType;

        // PHOTO POSTS
        public LinearLayout postPhotoLayout;
        public ImageView postPhotoFirst; // we have at least one photo
        public TextView photoCaption;

        // TEXT POSTS
        public TextView postTitle;
        public TextView textBody;

        // VIDEO POSTS
        public WebView videoPlayerView;
        public TextView videoCaption;

        // LINK POSTS
        public TextView linkDescription;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            postType = viewType;
            blogName = (TextView) itemView.findViewById(R.id.postBlogName);
            noteCount = (TextView) itemView.findViewById(R.id.postNoteCount);
            postTags = (TextView) itemView.findViewById(R.id.postTagsRow);
            postSource = (TextView) itemView.findViewById(R.id.postSource);
            savePostBlock = (LinearLayout) itemView.findViewById(R.id.savePostBlock);

            switch (viewType) {
                case TYPE_PHOTO:
                    postPhotoLayout = (LinearLayout) itemView.findViewById(R.id.postPhotoLayout);
                    postPhotoFirst = (ImageView) itemView.findViewById(R.id.postPhotoSingle);
                    photoCaption = (TextView) itemView.findViewById(R.id.postPhotoCaption);
                    break;
                case TYPE_TEXT:
                    postTitle = (TextView) itemView.findViewById(R.id.textTitle);
                    textBody = (TextView) itemView.findViewById(R.id.textBody);
                    break;
                case TYPE_VIDEO:
                    videoCaption = (TextView) itemView.findViewById(R.id.postVideoCaption);
                    videoPlayerView = (WebView) itemView.findViewById(R.id.postVideoPlayer);
                    videoPlayerView.setWebChromeClient(new WebChromeClient());
                    final WebSettings settings = videoPlayerView.getSettings();
                    settings.setDefaultTextEncodingName("utf-8");
                    settings.setJavaScriptEnabled(true);
                    settings.setSupportZoom(true);
                    settings.setBuiltInZoomControls(true);
                    settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
                    settings.setUseWideViewPort(true);
                    break;
                case TYPE_LINK:
                    postTitle = (TextView) itemView.findViewById(R.id.textTitle);
                    linkDescription = (TextView) itemView.findViewById(R.id.linkDescription);
                default:
                    break;
            }

            // Set Save listener
//            savePostBlock.setOnClickListener(this);
        }

//        @Override
//        public void onClick(View view) {
//            Log.d(Config.DEBUG_TAG, "onClick savePostBlock at POSITION: " + getPosition());
//        }
    }

}
