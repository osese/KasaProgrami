/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Kasapaketleri;

import java.math.BigDecimal;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 *
 * @author irsatkaya
 */
public class UrunEkleArayuzu extends Stage{
    
    
    public UrunEkleArayuzu(){
        DBManager con = new DBManager();
        con.connect();
        ArrayList<String> cats = con.getProductCategories(); 
        con.close();
        
        
        TextField urunadi = new TextField();
        TextField urunadi_kisa = new TextField();
        TextField barcodu = new TextField();
        TextField fiyati = new TextField();
        ComboBox categ = new ComboBox();
        Button tamam = new Button("Ekle");
        
        
        Label urunadi_l = new Label("*Ürün adi: ");
        Label barcod_l = new Label("Barkodu: ");
        Label fiyat_l = new Label("*Fiyat: ");
        Label categ_l = new Label("*Kategori: ");
        Label urunadi_kisa_l = new Label("Ürün adi(kisa): ");
        Label warn = new Label();
        warn.setStyle("-fx-color: red;");
        urunadi.setPromptText("50 karaktere kadar ");
        urunadi_kisa.setPromptText("25 karaktere kadar ");
        fiyati.setPromptText("12.34");
        
        GridPane grid = new GridPane();
        
        grid.add(categ_l, 0, 0);
        grid.add(categ, 1, 0);
        
        grid.add(urunadi_l, 0, 1);
        grid.add(urunadi, 1, 1);
        
        grid.add(urunadi_kisa_l, 0, 2);
        grid.add(urunadi_kisa, 1, 2);
        
        
        grid.add(barcod_l, 0, 3);
        grid.add(barcodu, 1, 3);
        
        grid.add(fiyat_l, 0, 4);
        grid.add(fiyati, 1, 4);
        
        
        grid.add(tamam, 1, 5);
        grid.add(warn, 1, 6);
        
        grid.setHgap(30);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);
        
        
        GridPane.setFillWidth(tamam, Boolean.TRUE);
        barcodu.setEditable(false);
        
        categ.getItems().addAll(cats); // Sets the categories
        categ.getSelectionModel().selectFirst();

        categ.setOnAction((event) -> {
            if(categ.getSelectionModel().getSelectedItem().equals("Hazır")){
                barcodu.setEditable(true);
            }else{
                barcodu.setEditable(false);
            }
        });
        
        
        tamam.setOnAction((ActionEvent event) -> {
            
            // The Check 
            BigDecimal bd;
            String ua = urunadi.getText();
            String uak = urunadi_kisa.getText();
            String b = barcodu.getText();
            int c = categ.getSelectionModel().getSelectedIndex();
            
            boolean everything_is_okey = true;
            
            if( ua.isEmpty()){
              everything_is_okey = false;
              warn.setText("Ürün adı boş bırakılamaz.");
              return;
            }
            if(fiyati.getText().isEmpty()){
                everything_is_okey = false;
                warn.setText("Fiyat kısmı boş bırakılamaz. ");
                return ;
            }else{
                bd = new BigDecimal(fiyati.getText());
                
            }
            
           
            
            if( ua.length() > 50) {
              everything_is_okey = false;
              warn.setText("Urun adi 50 karakterden fazla ");
              return;
            }
            if( uak.length() > 25) {
              everything_is_okey = false;  
              warn.setText("Urun kısa adı 25 karakterden fazla ");
              return ;
            }
            
            if( b.length() > 15){
                everything_is_okey = false;  
                warn.setText("Barcod no 15 karakterden fazla ");
                return;
            }
            
            if( bd.compareTo(new BigDecimal(99.99)) == 1) {
                everything_is_okey = false;
                warn.setText("Fiyat 999.99 dan büyük ");
                return;
            }
            
            if(everything_is_okey){
                con.connect();
                System.out.println(categ.getValue().toString());
                System.out.println(categ.getItems().indexOf(categ.getValue()));
                
                if(c != 2){  // Hazır categorisinde değilse barcodu yoktur. 
                    b = con.getUniqueBarcod(c);
                }
                
                con.addProduct(ua, uak, b, bd, c);
                con.close();
                warn.setText(ua + " eklendi.");
            }
        });
        
        Scene sc = new Scene(grid, 700, 350);
        
        sc.getStylesheets().add(getClass().getResource("urunekle.css").toExternalForm());
        
        this.setTitle("Ürün Ekle");
        this.setScene(sc);
    }
    
    
}
