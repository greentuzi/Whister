package com.UnicornStudio.whiter.app.Model;

public class User {
    private static User singleton;
    private int uid;
    public int picNum;
    public String ID;
    public static User getInstance(){
        if(singleton == null)
            singleton = new User();
        //     ;
        return singleton;
    }
    public User(){picNum = 0;}
    public void setUid(int n){
        uid = n;
    }
    public int getUid(){
        return uid;
    }
}

