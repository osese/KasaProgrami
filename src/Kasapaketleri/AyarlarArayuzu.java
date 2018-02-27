/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Kasapaketleri;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javax.print.PrintException;

/**
 *
 * @author revolver
 */
public class AyarlarArayuzu extends Stage{
    
    boolean fullscreenState ; 
    String currentDosyaYolu;
    StringProperty warningText;
    public AyarlarArayuzu(){    
        warningText = new SimpleStringProperty("");
        
        VBox  mainbox = new VBox();
        HBox cikti_hb = new HBox();
        VBox dateb_b = new VBox();
        VBox datee_b = new VBox();
        Label dateb_l = new Label("Başlangıç");
        Label datee_l = new Label("Bitiş");
        
        Label cikti_l = new Label("Excell Çıktısı Al");
        cikti_l.setTextAlignment(TextAlignment.CENTER);
        
        Button dateOkey = new Button("Excell Çıktı Al");
        mainbox.setSpacing(20);
        
        DatePicker dateBegin = new DatePicker();
        dateBegin.setValue(LocalDate.now());
        DatePicker dateEnd = new DatePicker();
        dateEnd.setValue(LocalDate.now());
        
        dateOkey.setOnAction((event) -> {
            LocalDate d1 =  dateBegin.getValue();
            LocalDate d2 = dateEnd.getValue();
            DBManager man = new DBManager();
            man.connect();
            String absPath = man.getOutputDirectory();
            
            if(absPath.isEmpty()){
                absPath = System.getProperty("user.home");
                man.setOutputDirectory(absPath);
            }
            
            SiparisExcellOlustur a = new SiparisExcellOlustur();
            ArrayList<Siparis> as = man.getOutputFromTo(d1, d2);
            man.close();
            a.setData(as);
            
            String filename = absPath + "\\"+ d1.toString() + " " + d2.toString() + ".xls";
            System.out.println(filename);
            
            try{
                a.kaydet(filename);
                warningText.setValue("Excel çıktısı başarılı." + filename + " adresine kaydedildi.");
            }catch(IOException ex){
                warningText.setValue("Bir okuma/yazma hatası oluştu.");
            }
            
        });
        
        
        dateb_b.getChildren().addAll(dateb_l, dateBegin);
        dateb_b.setSpacing(20);
        datee_b.getChildren().addAll(datee_l, dateEnd);
        datee_b.setSpacing(20);
        cikti_hb.getChildren().addAll(dateb_b, datee_b);
        cikti_hb.setSpacing(20);
        
        Button gunSonu = new Button("Gün sonu çıktısı al. ");
        gunSonu.setOnAction((event) -> {
            DBManager man = new DBManager();
            man.connect();
            LocalDateTime son = man.getGunSonu();
            LocalDateTime yeniSon = LocalDateTime.now();
            ArrayList<Siparis> siparisler = man.getOutputFromToOK(son, yeniSon);
            man.close();
            try {
                YazdirmaHelper3.GunSonuYazdir(siparisler);
                man.connect();
                man.setGunSonu(yeniSon);
                man.close();
            } catch (PrintException ex) {
                warningText.setValue("Yazdirirken bir hata oluştu");
                System.out.println("Yazdirirken bir hata oluştu");
            }
        });
        
        VBox digerAyarlar = createDigerAyarlar();
        Label warningLabel = new Label();
        
        warningText.addListener((observable) -> {
            warningLabel.setText(warningText.getValue());
        });
        
        mainbox.getChildren().addAll(cikti_l, cikti_hb, dateOkey, new Separator(), gunSonu, new Separator(), digerAyarlar, new Separator(), warningLabel);
        
        mainbox.setPadding(new Insets(20, 20, 20, 20));
        Scene s = new Scene(mainbox, 700, 700);
        s.getStylesheets().add(getClass().getResource("ayarlar.css").toExternalForm());
        this.setScene(s);
    }
    
    private VBox createDigerAyarlar(){
        initDigerAyarlar();
        
        // HBOX 1
        HBox hb1 = new HBox();
        
        // Set radio button 
        RadioButton fs_a = new RadioButton("Kapalı");
        if(fullscreenState){
            fs_a.setText("Açık");
            fs_a.setSelected(true);
        }else{
            fs_a.setText("Kapalı");
            fs_a.setSelected(false);
        }
        
        fs_a.setOnAction((event) -> {
            if("Kapalı".equals(fs_a.getText())){
                fs_a.setText("Açık");
            }else{
                fs_a.setText("Kapalı");
            }
            fullscreenState = !fullscreenState;
            System.out.println(fullscreenState);
        });
        
        hb1.getChildren().addAll(new Label("Tam Ekran"), fs_a);
        hb1.setSpacing(50);
        hb1.setPadding(new Insets(0,0,0,50));
        
        // HBox 2
        HBox hb2 = new HBox();
        DirectoryChooser fc = new DirectoryChooser();
        fc.setTitle("Bir klasör seç ");
        TextField dosyaYolu = new TextField();
        dosyaYolu.setMinWidth(200);
        dosyaYolu.setText(currentDosyaYolu);
        dosyaYolu.setEditable(false);
        
        Button fcButton = new Button("Gözat");
        fcButton.setOnAction((event) -> {
            File f = fc.showDialog(this);
            dosyaYolu.setText(f.getAbsolutePath());
            currentDosyaYolu = f.getAbsolutePath();
        });
        
        hb2.getChildren().addAll(new Label("Excel Klasörü"), dosyaYolu, fcButton);
        hb2.setSpacing(50);
        hb2.setPadding(new Insets(0,0,0,50));
        
        // Kaydetme butonu
        Button uygulaButton  = new Button("Diğer Ayarları Kaydet");
        uygulaButton.setOnAction((event) -> {
            System.out.println(fs_a.getText());
            System.out.println(dosyaYolu.getText());
            saveDigerAyarlar();
            warningText.set("Kaydedildi.");
        });
        
        // Create VBox 
        VBox digerAyarlar = new VBox();
        digerAyarlar.getChildren().addAll(new Label("Diğer Ayarlar"), hb1, hb2);
        digerAyarlar.setSpacing(50);
        digerAyarlar.getChildren().add(uygulaButton);
        
        return digerAyarlar;
    }
    
    private void initDigerAyarlar(){
        DBManager man = new DBManager();
        man.connect();
        fullscreenState = man.getFullScreenMode();
        currentDosyaYolu = man.getOutputDirectory();
        man.close();
    }
    
    private void saveDigerAyarlar(){
        DBManager man = new DBManager();
        man.connect();
        man.setDigerAyarlar(fullscreenState, currentDosyaYolu);
        man.close();
    }
}
