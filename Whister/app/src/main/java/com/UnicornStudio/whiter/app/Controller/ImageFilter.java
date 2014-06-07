package com.UnicornStudio.whiter.app.Controller;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.UnicornStudio.whiter.app.Filter.IImageFilter;
import com.UnicornStudio.whiter.app.Model.ImageFilterAdapter;
import com.UnicornStudio.whiter.app.Model.ProcessImageTask;
import com.UnicornStudio.whiter.app.Model.User;
import com.UnicornStudio.whiter.app.R;
import com.UnicornStudio.whiter.app.ServiceImplement.SocketClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

public class ImageFilter extends ActionBarActivity {


    private ImageView imageView;
    private TextView textView;
    private EditText et_title;
    private String mCurrentPhotoPath;

    private ImageButton bt_ok;
    private Bitmap samllbitmap;
    private String title;
    private void setListenser(){
        bt_ok.setOnClickListener(clickbtok);
    }

    private void receive(){
        JSONObject jsonObj = SocketClient.getInstance().getJSON();
        String flag = null;
        try {
            flag = jsonObj.getString("flag");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(flag);
        if(flag.equals("uploadPicSucceed")){
            Intent intent = new Intent();
            intent.setClass(ImageFilter.this,ImpressionWall.class);
            startActivity(intent);
        }
        else {
            new  AlertDialog.Builder(ImageFilter.this)
                    .setTitle("提示" )
                    .setMessage("上传失败！" )
                    .setPositiveButton("确定" ,  null )
                    .show();
            return;

        }
    }

    @TargetApi(Build.VERSION_CODES.FROYO)
    public String bitmaptoString(Bitmap bitmap){
        String string = null;
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,bStream);
        byte[]bytes = bStream.toByteArray();
        string = Base64.encodeToString(bytes, Base64.DEFAULT);
        return string;

    }

    private Button.OnClickListener clickbtok = new Button.OnClickListener(){
        @Override
        public void onClick(View v){

            title = et_title.getText().toString();
            int uid = User.getInstance().getUid();
//            SocketClient.getInstance().uploadPic(samllbitmap,title,uid);
            String picData = bitmaptoString(samllbitmap);
            System.out.println(picData);
            SocketClient.getInstance().uploadPic(picData,title,uid);
            receive();
        }

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_filter);

        //imageView = (ImageView) findViewById(R.id.imgfilter);
        imageView=(ImageView)findViewById(R.id.imgfilter);
        et_title = (EditText)findViewById(R.id.editText01);
        bt_ok=(ImageButton)findViewById(R.id.ok);
        setListenser();

        Intent intent=getIntent();
        if(intent!=null)
        {
            mCurrentPhotoPath = intent.getStringExtra("url");
            Log.i(mCurrentPhotoPath, "Greentuzi2");
            setPic();
        }
        textView = (TextView) findViewById(R.id.runtime);


        //Bitmap bitmap = BitmapFactory.decodeResource(ImageFilter.this.getResources(), R.drawable.image);
        //imageView.setImageBitmap(bitmap);
        LoadImageFilter();
    }


    private void setPic() {

		/* There isn't enough memory to open up more than a couple camera photos */
		/* So pre-scale the target bitmap into which the file is decoded */

		/* Get the size of the ImageView */
        int targetW = 480;
        int targetH = 343;

		/* Get the size of the image */
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

		/* Figure out which way needs to be reduced less */
        int scaleFactor = 1;
        if ((targetW > 0) || (targetH > 0)) {
            scaleFactor = Math.min(photoW / targetW, photoH / targetH);
        }

		/* Set bitmap options to scale the image decode target */
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

		/* Decode the JPEG file into a Bitmap */
        samllbitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

		/* Associate the Bitmap to the ImageView */
        imageView.setImageBitmap(samllbitmap);
        imageView.setVisibility(View.VISIBLE);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.image_filter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void LoadImageFilter() {
        Gallery gallery = (Gallery) findViewById(R.id.galleryFilter);
        final ImageFilterAdapter filterAdapter = new ImageFilterAdapter(
                ImageFilter.this);
        gallery.setAdapter(new ImageFilterAdapter(ImageFilter.this));
        gallery.setSelection(2);
        gallery.setAnimationDuration(3000);
        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
                IImageFilter filter = (IImageFilter) filterAdapter.getItem(position);
                final ProcessImageTask pro = new ProcessImageTask(ImageFilter.this, filter,textView,imageView,samllbitmap);
                samllbitmap = pro.getImage();
                pro.execute();
            }
        });
    }
}
