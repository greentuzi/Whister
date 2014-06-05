package com.UnicornStudio.whiter.app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


public class PhotoIntent extends ActionBarActivity {

    private ImageView iv_image;

    private Button bt_camera;

    private Bitmap photo;

    private File file;

    private String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

    private String saveDir = Environment.getExternalStorageDirectory()
            .getPath() + timeStamp;


    // View
    private void findView(){
        // To do
        //takePhotoBt = (Button) findViewById(R.id.moreButton);
    }

    // Controller
    private void setListenser(){
        // To do
        //takePhotoBt.setOnClickListener(clikTakePhotoBt);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.photo_intent, menu);
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
}
