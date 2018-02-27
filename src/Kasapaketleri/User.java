package Kasapaketleri;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author revolver
 * 
 * This class is for the order taker man. 
 */

public class User {
    private int id;
    private String fullname;
    private String username;
    private String password;
    
    public User(){
       
    }
    
    public User(int id , String username, String fullname, int type){
        this.id = id;
        this.fullname = fullname; 
        this.username = username;
        this.password = password;
    }
    
    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
    
    public void setFullname(String f){
        this.fullname = f;
    }
    public void setUsername(String u){
        this.username = u;
    }
    public void setPassword(String p){
        this.password = p;
    }

    public String getFullname() {
        return fullname;
    }

    public String getUsername() {
        return username;
    }
    
    @Override
    public  String toString(){
        return "name: "+ fullname + 
               "username: " + username +
               "password: " + password ;
    }

    public static boolean login(String username, String password){
       return true;
    }
    
    public static boolean logout(){
        return true;
    }
    
}
