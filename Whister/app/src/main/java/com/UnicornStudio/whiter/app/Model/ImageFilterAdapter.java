package com.UnicornStudio.whiter.app.Model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import com.UnicornStudio.whiter.app.Filter.*;
import com.UnicornStudio.whiter.app.R;

import java.util.ArrayList;
import java.util.List;


    public class ImageFilterAdapter extends BaseAdapter {
        private class FilterInfo {
            public int filterID;
            public IImageFilter filter;

            public FilterInfo(int filterID, IImageFilter filter) {
                this.filterID = filterID;
                this.filter = filter;
            }
        }

        private Context mContext;
        private List<FilterInfo> filterArray = new ArrayList<FilterInfo>();

        public ImageFilterAdapter(Context c) {
            mContext = c;
            //v0.1
            filterArray.add(new FilterInfo(R.drawable.invert_filter, new InvertFilter()));
            filterArray.add(new FilterInfo(R.drawable.blackwhite_filter, new BlackWhiteFilter()));
            filterArray.add(new FilterInfo(R.drawable.edge_filter, new EdgeFilter()));
            filterArray.add(new FilterInfo(R.drawable.pixelate_filter, new PixelateFilter()));
            filterArray.add(new FilterInfo(R.drawable.neon_filter, new NeonFilter()));
            filterArray.add(new FilterInfo(R.drawable.bigbrother_filter, new BigBrotherFilter()));
            filterArray.add(new FilterInfo(R.drawable.monitor_filter, new MonitorFilter()));
            filterArray.add(new FilterInfo(R.drawable.relief_filter, new ReliefFilter()));
            filterArray.add(new FilterInfo(R.drawable.brightcontrast_filter,new BrightContrastFilter()));
        }

        public int getCount() {
            return filterArray.size();
        }

        public Object getItem(int position) {
            return position < filterArray.size() ? filterArray.get(position).filter
                    : null;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            Bitmap bmImg = BitmapFactory
                    .decodeResource(mContext.getResources(),
                            filterArray.get(position).filterID);
            int width = 100;// bmImg.getWidth();
            int height = 100;// bmImg.getHeight();
            bmImg.recycle();
            ImageView imageview = new ImageView(mContext);
            imageview.setImageResource(filterArray.get(position).filterID);
            imageview.setLayoutParams(new Gallery.LayoutParams(width, height));
            imageview.setScaleType(ImageView.ScaleType.FIT_CENTER);
            return imageview;
        }
    }
