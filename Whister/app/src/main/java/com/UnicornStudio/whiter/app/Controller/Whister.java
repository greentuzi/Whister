package com.UnicornStudio.whiter.app.Controller;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.UnicornStudio.whiter.app.Model.User;
import com.UnicornStudio.whiter.app.R;
import com.UnicornStudio.whiter.app.ServiceImplement.SocketClient;

import org.json.JSONException;
import org.json.JSONObject;

public class Whister extends ActionBarActivity {

    private EditText et_email;
    private EditText et_password;
    private TextView tv_intent_singin;
    private ImageView iv_login;

    private String email;
    private String password;

    // View
    private void findView(){
        et_email = (EditText) findViewById(R.id.et_email);
        et_password = (EditText) findViewById(R.id.et_password);
        tv_intent_singin = (TextView) findViewById(R.id.tv_intent_singin);
        iv_login = (ImageView) findViewById(R.id.iv_login);
    }

    // Controller
    private void setListenser(){
        tv_intent_singin.setOnClickListener(clickSingin);
        iv_login.setOnClickListener(clickLogin);
    }

    private Button.OnClickListener clickSingin = new Button.OnClickListener(){
        @Override
        public void onClick(View v){
            // To do
            Intent intent = new Intent();
            intent.setClass(Whister.this,Signin.class);
            startActivity(intent);
        }
    };

    private Button.OnClickListener clickLogin = new Button.OnClickListener(){
        @Override
        public void onClick(View v){
            // To do
            email = et_email.getText().toString();
            password = et_password.getText().toString();

            SocketClient.getInstance().login(email,password);
            JSONObject jsonObj = SocketClient.getInstance().getJSON();
            String flag = null;
            try {
                flag = jsonObj.getString("flag");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            System.out.println(flag);
            if(flag.equals("loginSucceed")){
                int n = -1;
                try {
                    n = jsonObj.getInt("uid");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String id = "";
                try {
                    id = jsonObj.getString("ID");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                User.getInstance().setUid(n);
                User.getInstance().ID = id;
                //Intent intent = new Intent();
                intent.setClass(Whister.this,ImpressionWall.class);
                startActivity(intent);
            }
            else {
                new  AlertDialog.Builder(Whister.this)
                        .setTitle("提示" )
                        .setMessage("登陆失败！" )
                        .setPositiveButton("确定" ,  null )
                        .show();
                return;

            }


        }
    };

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    private void set(){
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()   // or .detectAll() for all detectable problems
                .penaltyLog()
                .build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .penaltyLog()
                .penaltyDeath()
                .build());

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        findView();
        setListenser();

        set();
       // SocketClient.getInstance().register("1","2");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_login, menu);
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
