
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author revolver
 */
package Kasapaketleri;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;


public class DBManager {
    private Connection conn;
    private String userName;
    private String password;
    private String dbName;
    public DBManager(){
        dbName = "Kasadb";
        userName = "admin";
        password = "1234";
        String driver = "org.h2.Driver";
        try{
            Class.forName(driver);
            //System.out.println("class for name ok!");
        }catch(java.lang.ClassNotFoundException e){
            java.util.logging.Logger.getLogger(e.getMessage());
        }
    }
    public void connect(){
        try{
            conn = DriverManager.getConnection("jdbc:h2:~/name/" + this.dbName , this.userName, this.password);
            //System.out.println("connection ok!");
            // set default schema 
            conn.createStatement().execute("set schema main;"); 
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public int getCurrentSiparisFisNo(){
        Statement s;
        try{
            s = conn.createStatement();
            ResultSet rs = s.executeQuery("select max(fis_no) from orders where date = '" + LocalDate.now().toString() + "';");
            if(rs.next()){
                return rs.getInt(1) + 1; // increment the last fis_no
            }else{
                return 1;
            }
        }catch(SQLException ex){
            java.util.logging.Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }
    
    public Urun getProductByBarcod(String bn){
        Statement s;
        try {
            s = conn.createStatement();
            
            ResultSet rs;
            rs = s.executeQuery("select * from PRODUCTS WHERE BARCODNO = '" + bn + "'");
            if(rs.next()){
                Urun a = new Urun(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3), 
                        rs.getString(4), 
                        rs.getBigDecimal(5), 
                        rs.getString(6),
                        rs.getInt(7));
                return a;
            }
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public boolean checkLogin(String username, String password){
        Statement s;
        try {
            s = conn.createStatement();
            
            ResultSet rs;
            rs = s.executeQuery("Select USERS.PASSWORD from USERS where username='" + username + "'");
            if(rs.next()){
                if(rs.getString(1).equals(password)){
                    return true;
                }
            }
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public void close(){
        try{
           conn.close();
           //System.out.println("Connection close ok!");
        }catch(SQLException e){
            java.util.logging.Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    public ArrayList<Urun> getProducts(){
        // Get all products without the meyve
        Statement s;
        ArrayList<Urun> result = new ArrayList<>();
        try{
            s = conn.createStatement();
            ResultSet rs= s.executeQuery("select * from PRODUCTS where id > 0");
            
            while(rs.next()){
                result.add(new Urun(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3), 
                        rs.getString(4), 
                        rs.getBigDecimal(5), 
                        rs.getString(6),
                        rs.getInt(7)));
            }   
            
            return result;
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    public ArrayList<String> getProductCategories(){
        Statement s;
        ArrayList<String> result = new ArrayList<>();
        try{
            s = conn.createStatement();
            ResultSet rs= s.executeQuery("select * from CATEGORIES");
            
            while(rs.next()){
                result.add(rs.getString(2));
            }   
            
            return result;
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    public void addProduct(
            String urunadi, 
            String urunadi_kisa,
            String barcodu, 
            BigDecimal fiyati,
            int categ){
        
        
        PreparedStatement ps;
        
        try{
            ps = conn.prepareStatement("INSERT INTO PRODUCTS VALUES(default, ?, ?, ?, ?, ?, ?)");
            ps.setString(1, barcodu);
            ps.setString(2, urunadi);
            ps.setString(3, urunadi_kisa);
            ps.setBigDecimal(4, fiyati);
            ps.setString(5, "");
            ps.setInt(6, (short)(categ));
            ps.execute();
            
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public int getCategoryIdByName(String name){
        Statement s;
        try{
            s = conn.createStatement();
            ResultSet rs = s.executeQuery("select id from categories where name = '" + name + "'");
            if(rs.next()){
                return rs.getInt(1);
            }
        }catch(SQLException e){
            java.util.logging.Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, e);
        }
        
        return -1;
    }
    
    public Urun getProductById(int id){
        Statement s;
        ArrayList<Urun> result = new ArrayList<>();
        try{
            s = conn.createStatement();
            ResultSet rs= s.executeQuery("select * from PRODUCTS where id = " +  id);
            if(rs.next()){
                return new Urun(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3), 
                        rs.getString(4), 
                        rs.getBigDecimal(5), 
                        rs.getString(6),
                        rs.getInt(7));
            }
        } 
        catch (SQLException ex) {
            java.util.logging.Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public ArrayList<Urun> getProducstByCategory(String catname){
        Statement s;
        ArrayList<Urun> result = new ArrayList<>();
        try{
            s = conn.createStatement();
            int id = getCategoryIdByName(catname);
            ResultSet rs= s.executeQuery("select * from PRODUCTS where category = " +  id);
            while(rs.next()){
                result.add(new Urun(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3), 
                        rs.getString(4), 
                        rs.getBigDecimal(5), 
                        rs.getString(6),
                        rs.getInt(7)));
            }   
            return result;
        
        } 
        catch (SQLException ex) {
            java.util.logging.Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    public void addNewSiparis(Siparis siparis){
        
        Statement s;
        PreparedStatement s2;
        try{
            s2 = conn.prepareStatement("insert into orderlist values(default, ?, ?, ?)");
            
            s = conn.createStatement();
            s.execute("INSERT INTO ORDERS VALUES(" + 
                        siparis.getId()  + ", " + 
                        siparis.getType() + ", " + 
                        siparis.getFis_no() + ", " +
                        siparis.getUserId() + ", " + 
                        siparis.getStatus() + ", '" +
                        siparis.getDate() + "', '" + 
                        siparis.getTime() + "', '" +
                        siparis.getDateTime() + "');");
            
            for(Urun u : siparis.getUrunler()){
                System.out.println(u.toString());
                s2.setInt(1, u.getId());
                s2.setInt(2, siparis.getId());
                // Meyve nin değişken fiyatı için  fiyat fieldını da orderliste ekliyoruz. Alırken de oradan alacağız.  
                s2.setBigDecimal(3, u.getPrice());
                s2.execute();
            }
            
        }catch(SQLException ex){
            java.util.logging.Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    User getUser(String username) {
        Statement s;
        try {
            s = conn.createStatement();
            
            ResultSet rs;
            rs = s.executeQuery("Select * from USERS where username='" + username + "'");
            if(rs.next()){
                return new User(rs.getInt(1), rs.getString(2), rs.getString(4), rs.getInt(5));
            }
            
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public ArrayList<Siparis> getDailyOutput(){
        Statement s;
        try {
            s = conn.createStatement();
            ArrayList<Siparis> siparisler  = new ArrayList<>();
            ResultSet rs;
            ResultSet orderlist_of_order;
            
            rs = s.executeQuery("Select * from orders where date = '" + new Date(Instant.now().toEpochMilli()) + "'");
            return getSiparislerFromResultSet(rs);
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public ArrayList<Siparis> getWeeklyOutput(){
        Statement s;
        try {
            s = conn.createStatement();
            ResultSet rs;
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH, -7);
            rs = s.executeQuery("Select * from orders where date > '" + new Date(cal.getTimeInMillis()) + "'");
            return getSiparislerFromResultSet(rs);
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    
    public ArrayList<Siparis> getOutputFromTo(LocalDate d1, LocalDate d2){
        
        Statement s;
        try {
            s = conn.createStatement();
            ResultSet rs;
            
            rs = s.executeQuery("Select * from orders where date >= '" + d1.toString() + "' and date <= '" + d2.toString() + "'");
            
            return getSiparislerFromResultSet(rs);
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public ArrayList<Siparis> getOutputFromTo(LocalDateTime d1, LocalDateTime d2){
        Statement s;
        try {
            s = conn.createStatement();
            ResultSet rs;

            String sql = "select * from orders where datetime >= '" + d1.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")) +
                    "' and datetime <= '" + d2.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")) +"' ;";
            
            System.out.println(sql);
            rs = s.executeQuery(sql);
            return getSiparislerFromResultSet(rs);
            
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public ArrayList<Siparis> getOutputFromToOK(LocalDateTime d1, LocalDateTime d2) {
        Statement s;
        try {
            s = conn.createStatement();
            ResultSet rs;

            String sql = "select * from orders where datetime >= '" + d1.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")) +
                    "' and datetime <= '" + d2.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")) +"' and status = 1;";
            
            System.out.println(sql);
            rs = s.executeQuery(sql);
            return getSiparislerFromResultSet(rs);
            
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    Siparis getSiparisByDateFisNo(LocalDate d, String f) {
        // string d should be formetted yymmdd and its lenght should be 6
        Statement s;
        try{
            s = conn.createStatement();
            
            ResultSet rs = s.executeQuery("Select * from orders where date = '" + d + "' and fis_no = " + f + ";");
            if(rs.next()){
                int order_id = rs.getInt(1);
                ResultSet orderlist_of_order = conn.createStatement().executeQuery("Select * from orderlist where order_id = '" + order_id +"'");
                ArrayList<Urun> urunler  = new ArrayList<>();
                while(orderlist_of_order.next()){
                    Urun k = getProductById(orderlist_of_order.getInt("product_id"));
                    if( k == null){ // Ürün silinmiş mi ? 
                        k = new Urun(-1, "", "SİLİNDİ.", "SILINDI", BigDecimal.ZERO, "", 0);
                    }
                    // Meyvenin fiyatı yüzünden hepsinin fiyatını orderlistten çekiyoruz. 
                    // Bu ileride bir ürünün fiyatı değişirse de işe yarayacak. 

                    k.setPrice(orderlist_of_order.getBigDecimal("price"));
                    urunler.add(k);        
                }
                return new Siparis(
                        rs.getInt("id"), 
                        rs.getInt("type"), 
                        rs.getInt("fis_no"),
                        rs.getInt("user_id"),
                        rs.getInt("status"),
                        rs.getDate("date").toLocalDate(), 
                        rs.getTime("time").toLocalTime(), 
                        urunler
                );
            }
        }catch (SQLException ex) {
            
            java.util.logging.Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        return null;
    }


    int getCurrentSiparisNo() {
        Statement s;
        try{
            s = conn.createStatement();
            ResultSet rs = s.executeQuery("select max(id) from orders;");
            if(rs.next()){
                return rs.getInt(1) + 1; 
            }else{
                return 1;
            }
        }catch(SQLException ex){
            java.util.logging.Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    int getCurrentSiparisType() {
    // This have nothing to do with db
        LocalTime tm = LocalTime.now();
        if(tm.getHour() > 13 || tm.getHour() < 4){ // Yemek 
            return 1;   
        }
        return 0;   // Kahvaltı 
    }

    void changeSiparisStatusById(int id) {
        Statement s;
        try{
            s = conn.createStatement();
            s.execute("update orders set status = 0 where id = " + id + ";" );
        }catch(SQLException ex){
            java.util.logging.Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    ArrayList<Urun> getProducstByCategoryId(int id) {
        Statement s;
        ArrayList<Urun> result = new ArrayList<>();
        try{
            s = conn.createStatement();
            ResultSet rs= s.executeQuery("select * from PRODUCTS where category = " +  id);
            while(rs.next()){
                result.add(new Urun(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3), 
                        rs.getString(4), 
                        rs.getBigDecimal(5), 
                        rs.getString(6),
                        rs.getInt(7)));
            }   
            return result;
        } 
        
        catch (SQLException ex) {
            java.util.logging.Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }

    String getUniqueBarcod(int c) {
        Statement s; 
        ResultSet rs ; 
        try{
            rs = conn.createStatement().executeQuery("Select barcodno from products where id = (select max(id) from products where category = " + c + ")" );
            if(rs.next()){
                String ss = rs.getString(1);
                int k = Integer.valueOf(ss);
                k = k+1;
                ss = String.valueOf(k);
                return ss ; 
            }else{
                return null;
            }
            
        }catch(SQLException ex){
            java.util.logging.Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    LocalDateTime getGunSonu() {
    Statement s; 
        ResultSet rs ; 
        try{
            rs = conn.createStatement().executeQuery("Select gunsonu from settings;");
            if(rs.next()){
                return rs.getTimestamp(1).toLocalDateTime();
            }
        }catch(SQLException ex){
            java.util.logging.Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private ArrayList<Siparis> getSiparislerFromResultSet(ResultSet rs) throws SQLException {
        ResultSet orderlist_of_order;
        ArrayList<Siparis> siparisler  = new ArrayList<>();
        
        while(rs.next()){
            int order_id = rs.getInt(1);
            orderlist_of_order = conn.createStatement().executeQuery("Select * from orderlist where order_id = '" + order_id +"'");
            ArrayList<Urun> urunler  = new ArrayList<>();
            while(orderlist_of_order.next()){
                Urun k = getProductById(orderlist_of_order.getInt("product_id"));
                if( k == null){
                    k = new Urun(-1, "", "SİLİNDİ.", "SILINDI", BigDecimal.ZERO, "", 0);
                }
                k.setPrice(orderlist_of_order.getBigDecimal("price"));
                urunler.add(k);
                // Meyvenin fiyatı yüzünden hepsinin fiyatını orderlistten çekiyoruz. 
                // Bu ileride bir ürünün fiyatı değişirse de işe yarayacak. 
            }
            siparisler.add(new Siparis(
                    rs.getInt("id"), 
                    rs.getInt("type"), 
                    rs.getInt("fis_no"),
                    rs.getInt("user_id"),
                    rs.getInt("status"),
                    rs.getDate("date").toLocalDate(), 
                    rs.getTime("time").toLocalTime(), 
                    urunler
            ));
        }
        
        return siparisler;
    }

    void setGunSonu(LocalDateTime yeniSon) {
        Statement s;
        try{
            conn.createStatement().execute("Update Settings set gunsonu = '" + yeniSon.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")) + "'");
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    String getOutputDirectory(){
        Statement s;
        try{
            ResultSet rs = conn.createStatement().executeQuery("Select output_directory from settings; ");
            if(rs.next()){
                return rs.getString("output_directory");
            }
            return null;
        }catch(SQLException ex){
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    boolean getFullScreenMode() {
        Statement s;
        try{
            ResultSet rs = conn.createStatement().executeQuery("Select full_screen from settings; ");
            if(rs.next()){
                return rs.getBoolean("full_screen");
            }
            return false;
        }catch(SQLException ex){
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    void setDigerAyarlar(boolean fullscreenState, String currentDosyaYolu) {
        Statement s;
        try{
            String sql = "Update settings set full_screen = " + fullscreenState + ", output_directory = '" + 
                    currentDosyaYolu + "';";
            System.out.println(sql);
            conn.createStatement().execute(sql);
        }catch(SQLException ex){
            System.out.println(ex.getMessage());
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void setOutputDirectory(String path) {
        Statement s;
        try{
            String sql = "Update settings set  output_directory = '" + path + "';";
            conn.createStatement().execute(sql);
        }catch(SQLException ex){
            System.out.println(ex.getMessage());
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    String getCategoryNameById(int id) {
        Statement s;
        try{
            s = conn.createStatement();
            ResultSet rs = s.executeQuery("select name from categories where id = '" + id + "'");
            if(rs.next()){
                return rs.getString("name");
            }
        }catch(SQLException e){
            java.util.logging.Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, e);
        }
        
        return null;
    }
    
    ArrayList<String> getCategories(){
        Statement s;
        try{
            ArrayList<String> al = new ArrayList<>();
            s = conn.createStatement();
            ResultSet rs = s.executeQuery("select * from categories");
            
            while(rs.next()){
                al.add(rs.getString("name"));
            }
            
            return al;
        }catch(SQLException e){
            java.util.logging.Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, e);
        }
        
        return null;
    }

    void addProduct(Urun urun) {
        PreparedStatement ps;
        
        try{
            ps = conn.prepareStatement("INSERT INTO PRODUCTS VALUES(default, ?, ?, ?, ?, ?, ?)");
            ps.setString(1, urun.getBarcodno());
            ps.setString(2, urun.getName());
            ps.setString(3, urun.getShortName());
            ps.setBigDecimal(4, urun.getPrice());
            ps.setString(5, "");
            ps.setInt(6, urun.getCategory());
            ps.execute();
            
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    void updateProduct(Urun urun) {
        PreparedStatement ps ;
        try {
            ps = conn.prepareStatement("update products set barcodno = ?, long_name = ?, short_name = ?, price = ?, img_path = ?, category = ?  where id = ?");
            ps.setString(1, urun.getBarcodno());
            ps.setString(2, urun.getName());
            ps.setString(3, urun.getShortName());
            ps.setBigDecimal(4, urun.getPrice());
            ps.setString(5, "");
            ps.setInt(6, urun.getCategory());
            ps.setInt(7, urun.getId());
            ps.execute();
            
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void deleteProduct(Urun e) {
        try{
            conn.createStatement().execute("delete from products where id = " + e.getId());
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}