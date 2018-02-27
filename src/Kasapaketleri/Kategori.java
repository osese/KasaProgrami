/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Kasapaketleri;

import java.util.ArrayList;

/**
 *
 * @author revolver
 */
public class Kategori {
    private static ArrayList<String> kategori = null;
    public Kategori(){
        if(kategori == null){
            DBManager man = new DBManager();
            man.connect();
            kategori = man.getCategories();
            man.close(); 
        }
    }
    
    public  String fromId(int id){
        return kategori.get(id);
    }
    
    public int fromName(String name){
        for(int i=0; i<kategori.size(); i++){
            if(name.equals(kategori.get(i))){
                return i;
            }
        }
        return -5;
    }
    public ArrayList<String> getCats(){
        return kategori;
    }
}
