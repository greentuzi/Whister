package com.UnicornStudio.whiter.app.Controller;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.UnicornStudio.whiter.app.R;
import com.UnicornStudio.whiter.app.ServiceImplement.SocketClient;


public class MainActivity extends ActionBarActivity {

    // Intent to Impression wall activity.
    private Button iWallBt;


    // View
    private void findView(){
        iWallBt = (Button) findViewById(R.id.impressionWallButton);
    }

    // Controller
    private void setListenser(){
        iWallBt.setOnClickListener(clikIwallBt);
    }

    private Button.OnClickListener clikIwallBt = new Button.OnClickListener(){
        @Override
        public void onClick(View v){
            Intent intent = new Intent();
            intent.setClass(MainActivity.this,ImpressionWall.class);
            startActivity(intent);
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      ///  SocketClient.getInstance().register("3","3");

        findView();
        setListenser();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
