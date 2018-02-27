/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Kasapaketleri;

import java.util.Collection;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

/**
 *
 * @author revolver
 */
public class UrunDuzenleDegisiklikKaydet extends Stage{
    
    boolean kaydedildi = false;
    
    public boolean getResult(){
        return kaydedildi;
    }
    
    public  UrunDuzenleDegisiklikKaydet(Collection<Urun> eklendi,
            Collection<Urun> silindi,
            Collection<Urun> guncellendi) {
            
            
            VBox vb = new VBox();
            vb.setPadding(new Insets(10));
            
            vb.getChildren().add(new Label("Değişiklikler kaydedilsin mi ? "));
            
            VBox eklendiVBox = new VBox();
            if(!eklendi.isEmpty()){
                Label eklendiLabel = new Label("Eklendi.");
                eklendiLabel.setFont(Font.font("Sans", FontWeight.BOLD, 12));

                eklendiVBox.getChildren().add(eklendiLabel);
                eklendi.forEach((t) -> {
                    eklendiVBox.getChildren().add(createRow(t, 0));
                });
            }
            
            VBox silindiVBox = new VBox();
            if(!silindi.isEmpty()){
                Label silindiLabel = new Label("Silindi.");
                silindiLabel.setFont(Font.font("Sans", FontWeight.BOLD, 12));

                silindiVBox.getChildren().add(silindiLabel);
                for(Urun e : silindi){
                    silindiVBox.getChildren().add(createRow(e, 1));
                }
            }
            
            VBox guncellendiVBox = new VBox();
            if(!guncellendi.isEmpty()){
                Label guncellendiLabel = new Label("Güncellendi.");
                guncellendiLabel.setFont(Font.font("Sans", FontWeight.BOLD, 12));

                guncellendiVBox.getChildren().add(guncellendiLabel);
                for(Urun e : guncellendi){
                    guncellendiVBox.getChildren().add(createRow(e, 2));
                }
            }
            vb.getChildren().addAll(eklendiVBox, silindiVBox, guncellendiVBox);
            
            HBox hb = new HBox();
            Button onay = new Button("TAMAM", new ImageView(new Image(getClass().getResourceAsStream("icons/urunduzenle/tamam.png"))));
            
            Button iptal = new Button("İPTAL", new ImageView(new Image(getClass().getResourceAsStream("icons/urunduzenle/iptal.png"))));
            hb.getChildren().addAll(onay, iptal);
            
            onay.setOnAction((a) -> {
                DBManager man = new DBManager();
                man.connect();
                
                eklendi.forEach((e) -> {
                    man.addProduct(e);
                });
                
                guncellendi.forEach((e) -> {
                    man.updateProduct(e);
                });
                
                silindi.forEach((e) -> {
                    man.deleteProduct(e);
                });
                
                man.close();
                kaydedildi = true;
                this.close();
            });
            
            iptal.setOnAction((a) -> {
                this.close();
            });
            
            hb.setSpacing(10);
            hb.setAlignment(Pos.CENTER_RIGHT);
            
            VBox.setVgrow(vb, Priority.ALWAYS);
            vb.setSpacing(10);
            
            ScrollPane sp = new ScrollPane(vb);
            
            VBox main = new VBox();
            main.setPrefWidth(600);
            main.getChildren().addAll(sp, hb);
            
            this.setTitle("Değişiklikler Kaydedilsin mi ? ");
            Scene scene = new Scene(main);
            scene.getStylesheets().add(getClass().getResource("urunduzenle.css").toExternalForm());
            this.setScene(scene);
            
    }
    
    
    private HBox createRow(Urun e, int i ){
        HBox row = new HBox();
        row.getChildren().addAll(
                new rowLabel(String.valueOf(e.getId()), i),
                new rowLabel(e.getBarcodno(), i),
                new rowLabel(e.getName(), i),
                new rowLabel(e.getShortName(),i),
                new rowLabel(e.getPrice().toString(),i),
                new rowLabel(e.getCategoryS(),i)
        );
        
        row.setSpacing(1);
        return row;
    }
    class rowLabel extends Label{
        public rowLabel(String s, int i) {
            super(s);
            if(i == 0){
                this.setStyle("-fx-background-color: #FAFFFA;" 
                    + "-fx-padding: 3px;"
                    + "-fx-effect: innershadow(three-pass-box, rgba(0,0,0,0.8), 2, 0, 0, 0);");
            }else if(i == 1){
                this.setStyle("-fx-background-color: #FFFAFA;" 
                    + "-fx-padding: 3px;"
                    + "-fx-effect: innershadow(three-pass-box, rgba(0,0,0,0.8), 2, 0, 0, 0);");
            }else{
                this.setStyle("-fx-background-color: #FAFAFF;" 
                    + "-fx-padding: 3px;"
                    + "-fx-effect: innershadow(three-pass-box, rgba(0,0,0,0.8), 2, 0, 0, 0);");
            }
            
            
            this.setMinWidth(90);
            this.setMaxWidth(90);
            this.setTextAlignment(TextAlignment.CENTER);
            this.setAlignment(Pos.CENTER);
        }
        
    }
}
