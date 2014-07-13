package com.UnicornStudio.whiter.app.Controller;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.UnicornStudio.whiter.app.Model.User;
import com.UnicornStudio.whiter.app.R;
import com.UnicornStudio.whiter.app.ServiceImplement.SocketClient;

import org.json.JSONException;
import org.json.JSONObject;


public class Signin extends ActionBarActivity {

    private EditText et_email;
    private EditText et_password;
    private EditText et_password_again;
    private ImageView iv_login;

    private String email;
    private String password;
    private String password_again;

    // View
    private void findView(){
        et_email = (EditText) findViewById(R.id.et_email);
        et_password = (EditText) findViewById(R.id.et_password);
        et_password_again = (EditText) findViewById(R.id.et_password_again);
        iv_login = (ImageView) findViewById(R.id.iv_login);
    }

    // Controller
    private void setListenser(){
        iv_login.setOnClickListener(clickLogin);
    }

    private Button.OnClickListener clickLogin = new Button.OnClickListener(){
        @Override
        public void onClick(View v){
            // To do
            email = et_email.getText().toString();
            password = et_password.getText().toString();
            password_again = et_password_again.getText().toString();
            //password_again = password;
            if (!password.equals(password_again)){
                Log.i("password",password);
                Log.i("pass2", password_again);
                new  AlertDialog.Builder(Signin.this)
                        .setTitle("提示" )
                        .setMessage("亲，两次密码不一致哦！" )
                        .setPositiveButton("确定" ,  null )
                        .show();
                return;
            }

            SocketClient.getInstance().register(email,password);
            JSONObject jsonObj = SocketClient.getInstance().getJSON();
            String flag = null;
            try {
                flag = jsonObj.getString("flag");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(flag.equals("registerSucceed")){

                new  AlertDialog.Builder(Signin.this)
                        .setTitle("提示" )
                        .setMessage("Register succeed ！" )
                        .setPositiveButton("确定" ,  null )
                        .show();

                //Intent intent = new Intent();
                //intent.setClass(Signin.this,Whister.class);
               // startActivity(intent);
                SocketClient.getInstance().login(email,password);
                jsonObj = SocketClient.getInstance().getJSON();
                flag = null;
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
                        id  = jsonObj.getString("ID");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    User.getInstance().setUid(n);
                    User.getInstance().ID = id;
                    Intent intent = new Intent();
                    intent.setClass(Signin.this,ImpressionWall.class);
                    startActivity(intent);
                }
                else {
                    new  AlertDialog.Builder(Signin.this)
                            .setTitle("提示" )
                            .setMessage("登陆失败！" )
                            .setPositiveButton("确定" ,  null )
                            .show();
                    Intent intent = new Intent();
                    intent.setClass(Signin.this,Whister.class);
                    startActivity(intent);
                    //return;

                }
            }
            else {
                new  AlertDialog.Builder(Signin.this)
                        .setTitle("提示" )
                        .setMessage("Register Failed！" )
                        .setPositiveButton("确定" ,  null )
                        .show();
                return;

            }



        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        findView();
        setListenser();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.signin, menu);
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
