//package com.cziyeli.tumblrtagsearch.adapters;
//
//import android.content.Context;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.ImageView;
//
//import com.cziyeli.tumblrtagsearch.Config;
//import com.cziyeli.tumblrtagsearch.R;
//import com.squareup.picasso.Picasso;
//
///**
// * Plot photoUrls into post_item_photo_row
// */
//
//public class PhotoListAdapter extends BaseAdapter {
//    // sent in a list of photoUrls
//    String[] mPhotoUrls;
//    Context mContext;
//    LayoutInflater mInflater;
//
//    public PhotoListAdapter(Context context, LayoutInflater inflater) {
//        Log.d(Config.DEBUG_TAG, "creating PhotoListAdapter");
//        mContext = context;
//        mInflater = inflater;
//    }
//
//    @Override
//    public int getCount() {
//        return (mPhotoUrls == null) ? 0: mPhotoUrls.length;
//    }
//
//    @Override
//    public String getItem(int position) {
//        if (mPhotoUrls != null && position >= 0 && position < mPhotoUrls.length) {
//            return mPhotoUrls[position];
//        } else {
//            return null;
//        }
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    public void updateData(String[] urls) {
//        this.mPhotoUrls = urls;
//        notifyDataSetChanged();
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        ViewHolder holder;
//
//        if (convertView == null) {
//            convertView = LayoutInflater.from(mContext).inflate(R.layout.post_item_photo_row, parent, false);
//            holder = new ViewHolder();
//            holder.photoView = (ImageView) convertView.findViewById(R.id.postImageItem);
//            convertView.setTag(holder);
//        } else {
//            holder = (ViewHolder) convertView.getTag();
//        }
//
//        String photoUrl = getItem(position);
//
//        Log.d(Config.DEBUG_TAG, "PhotoListAdapter getViw with photoUrl: " + photoUrl);
//        if (photoUrl != null) {
//            Picasso.with(this.mContext)
//                    .load(photoUrl)
//                    .placeholder(R.mipmap.ic_launcher)
//                    .into(holder.photoView);
//        } else {
//            holder.photoView.setImageResource(R.mipmap.ic_launcher);
//        }
//
//        return convertView;
//    }
//
//    // only have to do inflation and finding view by ID once ever per view
//    private static class ViewHolder {
//        public ImageView photoView;
//    }
//
//}
