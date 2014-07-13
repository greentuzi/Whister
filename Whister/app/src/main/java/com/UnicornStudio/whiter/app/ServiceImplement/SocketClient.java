package com.UnicornStudio.whiter.app.ServiceImplement;

import android.graphics.Bitmap;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

//import net.sf.json.JSONObject;
//import net.sf.json.util.JSONTokener;

public class SocketClient {
	 
	// Client Socket
	private static Socket client;
    private static JSONObject jsonObj;
    private static SocketClient singleton;
    private static boolean getNew;

     public static SocketClient getInstance(){
         if(singleton == null)
             singleton = new SocketClient("172.18.159.243",80);
    //     ;
         return singleton;
     }
    public static JSONObject getJSON(){
        while(!getNew) {;}
            getNew = false;
            return singleton.jsonObj;
    }
	// Listener of Respondences
	private Thread inputListener = null;
	
	public SocketClient(String site, int port){
	    try{
	        client = new Socket(site,port);
            System.out.println("begin");
	        while(!client.isConnected());
	        System.out.println("Client is created! site:"+site+" port:"+port);
	        onConnect();
	    }catch (UnknownHostException e){
	        e.printStackTrace();
	    }catch (IOException e){
	        e.printStackTrace();
	    }
	}
	
	private void onConnect() {

		// Start the Handler of Respondences
		inputListener = new Thread(null, new Runnable(){
			public void run(){
				System.out.println("InputListener: running...");
				respondsHandle();
			}}, "RespondsHandler");
		inputListener.start();
		
		// Some Tests
		//this.register("9999","123");
	}
	
	public void closeSocket(){
	    try{
	        client.close();
	    }catch(IOException e){
	        e.printStackTrace();
	    }
	}
	

	// Handle All the Respondence HERE!
	protected void respondsHandle() {

		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			while(true){
				String data = in.readLine();
				JSONTokener jsonParser = new JSONTokener(data);
				System.out.println("responds handle: jsonParser:\n"+jsonParser.toString());
				jsonObj = (JSONObject)jsonParser.nextValue();
				String flag;
                flag = jsonObj.getString("flag");
               // System.out.println(flag);
                getNew = true;
				//switch(flag.hashCode()){
				  //  case "modifySucceed".hashCode():
					//
					//break;
				//}
			}
		} catch (IOException e) {
			System.out.println("RespondsHandle: Cannot get input stream!");
			e.printStackTrace();
		} catch (JSONException e) {
            e.printStackTrace();
        }
        }
	
	/*=================*/
	/* ALL THE REQUEST */
	/*=================*/
	
	 public void register(String ID, String password){
	 	try {
			PrintWriter out = new PrintWriter(client.getOutputStream());
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("flag", "register");
			jsonObj.put("ID", ID);
			jsonObj.put("password",password);
			out.println(jsonObj.toString());
			out.flush();
		} catch (IOException e1) {
			System.out.println("register: Cannot get output stream!");
			e1.printStackTrace();
		} catch (JSONException e) {
            e.printStackTrace();
        }
     }
	
	 public void login(String ID, String password){
	 	try {
			PrintWriter out = new PrintWriter(client.getOutputStream());
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("flag", "login");
			jsonObj.put("ID", ID);
			jsonObj.put("password",password);
			out.println(jsonObj.toString());
			out.flush();
		} catch (IOException e1) {
			System.out.println("login: Cannot get output stream!");
			e1.printStackTrace();
		} catch (JSONException e) {
            e.printStackTrace();
        }
     }

	 public void modifyUserInfo(int uid,String ID,String nickname,Bitmap portait,boolean sex, String signiture){
	 	try {
			PrintWriter out = new PrintWriter(client.getOutputStream());
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("flag", "login");
			jsonObj.put("ID", ID);
			jsonObj.put("nickname",nickname);
			jsonObj.put("portait",portait);
			jsonObj.put("sex",sex);
			jsonObj.put("signiture",signiture);
			out.println(jsonObj.toString());
			out.flush();
		} catch (IOException e1) {
			System.out.println("modifyUserInfo: Cannot get output stream!");
			e1.printStackTrace();
		} catch (JSONException e) {
            e.printStackTrace();
        }
     }
	 
		public void requestPicWall(int num){
			
			try {
				PrintWriter out = new PrintWriter(client.getOutputStream());
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("flag", "requirePicWall");
				jsonObj.put("num", num);
				out.println(jsonObj.toString());
				out.flush();
			} catch (IOException e1) {
				System.out.println("requestPicWall: Cannot get output stream!");
				e1.printStackTrace();
			} catch (JSONException e) {
                e.printStackTrace();
            }
        }
		
		public void requirePicInfo(int picID){
			
			try {
				PrintWriter out = new PrintWriter(client.getOutputStream());
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("flag", "requirePicWall");
				jsonObj.put("picID", picID);
				out.println(jsonObj.toString());
				out.flush();
			} catch (IOException e1) {
				System.out.println("requirePicInfo: Cannot get output stream!");
				e1.printStackTrace();
			} catch (JSONException e) {
                e.printStackTrace();
            }
        }
		
		public void requireUserShared(int uid,int beginPicNum,int num){
			
			try {
				PrintWriter out = new PrintWriter(client.getOutputStream());
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("flag", "requireUserShared");
				jsonObj.put("uid", uid);
				jsonObj.put("beginPicNum", beginPicNum);
				jsonObj.put("num", num);
				out.println(jsonObj.toString());
				out.flush();
			} catch (IOException e1) {
				System.out.println("requireUserShared: Cannot get output stream!");
				e1.printStackTrace();
			} catch (JSONException e) {
                e.printStackTrace();
            }
        }
		
		public void requireFavorList(int uid,int beginPicNum,int num){
			
			try {
				PrintWriter out = new PrintWriter(client.getOutputStream());
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("flag", "requireFavorList");
				jsonObj.put("uid", uid);
				jsonObj.put("beginPicNum", beginPicNum);
				jsonObj.put("num", num);
				out.println(jsonObj.toString());
				out.flush();
			} catch (IOException e1) {
				System.out.println("requireFavorList: Cannot get output stream!");
				e1.printStackTrace();
			} catch (JSONException e) {
                e.printStackTrace();
            }
        }

		public void favor(int uid,int picID){
			
			try {
				PrintWriter out = new PrintWriter(client.getOutputStream());
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("flag", "favor");
				jsonObj.put("uid", uid);
				jsonObj.put("picID", picID);
				out.println(jsonObj.toString());
				out.flush();
			} catch (IOException e1) {
				System.out.println("favor: Cannot get output stream!");
				e1.printStackTrace();
			} catch (JSONException e) {
                e.printStackTrace();
            }
        }
		
		public void unfavor(int uid,int picID){
			
			try {
				PrintWriter out = new PrintWriter(client.getOutputStream());
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("flag", "unfavor");
				jsonObj.put("uid", uid);
				jsonObj.put("picID", picID);
				out.println(jsonObj.toString());
				out.flush();
			} catch (IOException e1) {
				System.out.println("unfavor: Cannot get output stream!");
				e1.printStackTrace();
			} catch (JSONException e) {
                e.printStackTrace();
            }
        }
		
		public void requireFavorUsers(int picID){
			
			try {
				PrintWriter out = new PrintWriter(client.getOutputStream());
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("flag", "requireFavorUsers");
				jsonObj.put("picID", picID);
				out.println(jsonObj.toString());
				out.flush();
			} catch (IOException e1) {
				System.out.println("requireFavorUsers: Cannot get output stream!");
				e1.printStackTrace();
			} catch (JSONException e) {
                e.printStackTrace();
            }
        }
		
		public void deletePic(int picID){
			
			try {
				PrintWriter out = new PrintWriter(client.getOutputStream());
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("flag", "deletePic");
				jsonObj.put("picID", picID);
				out.println(jsonObj.toString());
				out.flush();
			} catch (IOException e1) {
				System.out.println("deletePic: Cannot get output stream!");
				e1.printStackTrace();
			} catch (JSONException e) {
                e.printStackTrace();
            }
        }
		
		public void uploadPic(String picData,String picIntro,int uid){
			
			try {
				PrintWriter out = new PrintWriter(client.getOutputStream());
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("flag", "uploadPic");
				jsonObj.put("picData", picData);
				jsonObj.put("picIntro", picIntro);
                jsonObj.put("uid",uid);
				out.println(jsonObj.toString());
				out.flush();
			} catch (IOException e1) {
				System.out.println("uploadPic: Cannot get output stream!");
				e1.printStackTrace();
			} catch (JSONException e) {
                e.printStackTrace();
            }
        }
	/*================*/
	/* 	END REQUEST	  */
	/*================*/
}