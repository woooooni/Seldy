package com.seldy_proj.seldy.util;

public class User {
    String username;
    String nickname;

    public User(String username,String nickname){
        this.username = username;
        this.nickname = nickname;
    }
    public String getUserName(){
        return username;
    }
    public String getNickname(){
        return nickname;
    }
    public void setUsername(String username){
        this.username = username;
    }
    public void  setNickname(String nickname){
        this.nickname = nickname;
    }
}
