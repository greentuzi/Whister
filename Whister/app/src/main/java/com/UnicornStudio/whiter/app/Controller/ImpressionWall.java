package com.UnicornStudio.whiter.app.Controller;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.UnicornStudio.whiter.app.Model.User;
import com.UnicornStudio.whiter.app.R;
import com.UnicornStudio.whiter.app.ServiceImplement.SocketClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class ImpressionWall extends ActionBarActivity {

    private ImageButton takePhotoBt;

    // View
    private void findView(){
        // To change!
        takePhotoBt = (ImageButton) findViewById(R.id.takePhoto);
    }

    // Controller
    private void setListenser(){
        takePhotoBt.setOnClickListener(clickTakePhotoBt);
    }

    private Button.OnClickListener clickTakePhotoBt = new Button.OnClickListener(){
        @Override
        public void onClick(View v){
            Intent intent = new Intent();
            intent.setClass(ImpressionWall.this,PhotoIntentActivity.class);
            startActivity(intent);
        }

    };

    @TargetApi(Build.VERSION_CODES.FROYO)
    public Bitmap stringtoBitmap(String string){
        //将字符串转换成Bitmap类型
        Bitmap bitmap=null;
        try {
            byte[]bitmapArray;
            bitmapArray= Base64.decode(string, Base64.DEFAULT);
            bitmap= BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    private void setListView(){
        //绑定Layout里面的ListView
        ListView list = (ListView) findViewById(R.id.listview1);

        //生成动态数组，加入数据
        ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
        for(int i=0;i<1;i++)
        {
            SocketClient.getInstance().requestPicWall(i+1);
            HashMap<String, Object> map = new HashMap<String, Object>();
            JSONObject jsonObj = SocketClient.getInstance().getJSON();
            String flag = null;
            try {
                flag = jsonObj.getString("flag");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            System.out.println("flag:" + flag);
            if(flag.equals("picWall")) {
                map.put("nickname", User.getInstance().ID);//图像资源的ID
                String picIntro = "";
                try{
                    picIntro = jsonObj.getString("picIntro");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String bitmapData = "";
                try{
                    bitmapData = jsonObj.getString("picData");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Bitmap bitmap = stringtoBitmap(bitmapData);
                map.put("titile", "whister");
                //map.put("photo", bitmap);
                listItem.add(map);
            }
        }
        //生成适配器的Item和动态数组对应的元素
        SimpleAdapter listItemAdapter = new SimpleAdapter(this,listItem,//数据源
                R.layout.list_view,//ListItem的XML实现
                //动态数组与ImageItem对应的子项
                new String[] {"nickname","titile"},
                //ImageItem的XML文件里面的对应项
                new int[] {R.id.nickname,R.id.titile}
        );

        //添加并且显示
        list.setAdapter(listItemAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_impression_wall);

        findView();
        setListenser();
        setListView();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.impression_wall, menu);
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
