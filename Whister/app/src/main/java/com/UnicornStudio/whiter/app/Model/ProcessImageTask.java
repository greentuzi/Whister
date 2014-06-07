package com.UnicornStudio.whiter.app.Model;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.UnicornStudio.whiter.app.Filter.IImageFilter;
import com.UnicornStudio.whiter.app.Filter.Image;
import com.UnicornStudio.whiter.app.R;

public class ProcessImageTask extends AsyncTask<Void, Void, Bitmap> {
        private IImageFilter filter;
        private Activity activity = null;
        private TextView textView;
        private ImageView imageView;
        private String url;
        private Bitmap bitmap;
        private Image img = null;

        public ProcessImageTask(Activity activity, IImageFilter imageFilter,TextView textView,ImageView imageView,Bitmap bitmap) {
            this.filter = imageFilter;
            this.activity = activity;
            this.textView = textView;
            this.imageView = imageView;
            this.bitmap = bitmap;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            textView.setVisibility(View.VISIBLE);
        }

        public Bitmap getImage(){
            return img.getImage();
        }
        public Bitmap doInBackground(Void... params) {
            try
            {
                img = new Image(bitmap);
                if (filter != null) {
                    img = filter.process(img);
                    img.copyPixelsFromBuffer();
                }
                return img.getImage();
            }
            catch(Exception e){
                if (img != null && img.destImage.isRecycled()) {
                    img.destImage.recycle();
                    img.destImage = null;
                    System.gc();
                }
            }
            finally{
                if (img != null && img.image.isRecycled()) {
                    img.image.recycle();
                    img.image = null;
                    System.gc();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if(result != null){
                super.onPostExecute(result);
                imageView.setImageBitmap(result);
            }
            textView.setVisibility(View.INVISIBLE);
        }
    }
