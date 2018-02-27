/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Kasapaketleri;

import java.time.LocalDate;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 *
 * @author revolver
 */
class FisDuzeltArayuzu extends Stage{
    private Siparis res ;
    
    public FisDuzeltArayuzu(ObservableList<Urun> obs){
        VBox vbox = new VBox();
        TextField tf = new TextField();
        Label aciklama = new Label("Fiş iptali için fiş numarasını giriniz.");
        aciklama.setFont(Font.font(16));
        Label warningLabel = new Label("");
        warningLabel.setFont(Font.font("Serif", FontWeight.BOLD, 15));
        warningLabel.setStyle("-fx-background-color: red;");
        
        tf.setPromptText("Fiş barkodunu giriniz. ");
        tf.setFont(Font.font("Serif", FontWeight.EXTRA_BOLD, 20));
        tf.setPrefWidth(300);
        
        tf.setOnAction((event) -> {
            String text = tf.getText();
            if(text.length() != 10){
                warningLabel.setText("Siparis Numarası 10 hane olmalıdır.!");
                return;
            }
            
            String d = text.substring(0, 6);
            LocalDate ld = LocalDate.of(Integer.valueOf("20" + d.substring(0,2)),
                                        Integer.valueOf(d.substring(2,4)),
                                        Integer.valueOf(d.substring(4,6)));
            
            String f = text.substring(6, 10);
            f = String.valueOf(Integer.valueOf(f));
            
            DBManager man = new DBManager();
            man.connect();
            
            Siparis res = man.getSiparisByDateFisNo(ld, f);
            if(res == null){
                warningLabel.setText("Böyle bir siparis yok!");
                return;
            }
            
            if(res.getStatus() == 0){
                warningLabel.setText("Sipariş zaten iptal edilmiş!");
                return;
            }
            
            
            obs.clear();
            obs.addAll(res.getUrunler());
            man.changeSiparisStatusById(res.getId());
            man.close();
            tf.clear();
            this.close();
        });
        
        this.setTitle("Fis düzelt");
        vbox.getChildren().addAll(aciklama, new Separator(), tf, new Separator(), warningLabel);
        vbox.setPadding(new Insets(2,2,2,2));
        this.setScene(new Scene(vbox, 300, 300));
        
    }
    
    public Siparis result(){
        return res;
    }
}
