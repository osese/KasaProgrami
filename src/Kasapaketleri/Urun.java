/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Kasapaketleri;

import java.math.BigDecimal;

/**
 *
 * @author revolver
 */
public class Urun {

  
    private int id;
    private String barcodno;
    private String name;
    private String short_name;
    private BigDecimal  price; 
    private String imagePath; 
    private int category;
    
    
    public Urun(Urun urun){
        this.id = urun.getId(); // Get an auto id
        this.barcodno = urun.getBarcodno(); // auto barcod or ? 
        this.name = urun.getName();
        this.short_name = urun.getShortName();
        this.price = BigDecimal.ZERO;
        this.imagePath = "";
        this.category = urun.getCategory();
    }
    
    public  Urun(int id, String barcodno, String name, String short_name, BigDecimal price, String imagePath, int category){
        this.id = id;
        this.barcodno = barcodno;
        this.name = name;
        this.short_name = short_name;
        this.price = price;
        this.imagePath = imagePath;
        this.category = category;
    }
    
    public String getBarcodno() {
        return barcodno;
    }

    public void setBarcodno(String barcodno) {
        this.barcodno = barcodno;
    }

    public void setCategoryS(String name) {
        this.category = new Kategori().fromName(name);
    }
    
    public String getCategoryS() {
        return new Kategori().fromId(this.category);
    }
    
    public int getCategory() {
        return this.category;
    }

    public void setCategory(int category) {
        this.category = category;
    }
    
    public void setShortName(String short_name){
        this.short_name = short_name;
    }
    
    public String getShortName(){
        return this.short_name;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public String getName(){
        return this.name;
    }
    
    public void setPrice(BigDecimal price){
        this.price = price;
    }
    
    public BigDecimal getPrice(){
        return price;
    }
    
    public void setImagePath(String imagePath){
        this.imagePath =  imagePath;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    int getId() {
        return this.id;
    }
    
    
    @Override
    public String toString(){
        return "name: " + name + "\nprice: " + price +"\n";
    }
    
    public static Urun Meyve(BigDecimal tot){
        return  new Urun(0, "00000000", "MEYVE", "MEYVE", tot, "", -1);
    }
    
    public void clear(){
        name = "";
        barcodno = ""; 
        short_name = "";
        imagePath = "" ;
        id = 0;
        price = BigDecimal.ZERO;
    }
}
