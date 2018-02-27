/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Kasapaketleri;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;


/**
 *
 * @author revolver
 */
public class Siparis { 
    
    private ArrayList<Urun> urunler;
    private LocalDate date;
    private LocalTime time;
    private int user_id;
    private int fis_no; // fis_no is a daily siparis no
    private int id;
    private int type;
    private int status;
    // getting in 
    Siparis(int id, int type, int fis_no, int user_id, ArrayList<Urun> urunler){
        this.id = id;
        this.type = type;
        this.fis_no = fis_no;
        this.user_id = user_id;
        this.status = 1;
        this.date = LocalDate.now();
        this.time = LocalTime.now();
        this.urunler = urunler;
        
    }
    
    // getting out 
    Siparis(int id, int type, int fis_no, int user_id, int status, LocalDate date, LocalTime time, ArrayList<Urun> urunler){
        this.id = id;
        this.type = type;
        this.fis_no = fis_no;
        this.user_id = user_id;
        this.status = status ;
        this.date = date;
        this.time = time;
        this.urunler = urunler;
    }
    
    public int getStatus() {
        return status;
    }
    
    public int getType() {
        return type;
    }

    public int getUser_id() {
        return user_id;
    }
    
    public int getFis_no() {
        return fis_no;
    }
    
    public int getId() {
        return id;
    }

    LocalDate  getDate() {
        return date;
    }

    LocalTime getTime() {
        return time;
    }
    
    public int getUserId() {
        return user_id;
    }
    
    public ArrayList<Urun> getUrunler(){
        return this.urunler;
    }

    BigDecimal getToplam() {
        BigDecimal total = new BigDecimal(0);
        for(Urun u : urunler){
            total = total.add(u.getPrice());
        }
        return total;
    }
    
    
    public String getStatusS(){
        if(status == 1){
            return "OLUMLU";
        }
        return "İPTAL";
    }
    
    public String getTypeS(){
        if(type == 0){
            return "KAHVALTI";
        }
        return "YEMEK";
    }
    
    public String getBarcod(){
        // Bar code system : ITF  k = 10  digits
        String d = this.getDate().toString();
        String f = String.valueOf(fis_no);
        String d1 = d.substring(2, 4);
        d1 += d.substring(5, 7);
        d1 += d.substring(8, 10);
        int left = 4 - f.length();
        for(int i=0; i<left; i++){
            d1 += "0";
        }
        d1 += f;
        return d1;
    }

    @Override
    public String toString() {
        
        String s = this.id + "\t" + this.getTypeS() + "\t" + this.fis_no + "\t"+
                this.user_id + "\t" + this.getStatusS() + "\t" + this.date + this.time+"\n";
        
        for(Urun a : this.urunler){
            s += a.toString();
        }
        return s;
    }    

    String getDateTime() {
        return this.date.atTime(this.time).toString();
    }
}
