/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Kasapaketleri;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;


/**
 *
 * @author revolver
 */
public class Login extends Stage{
    
    public Login() {
        AnchorPane anc = new AnchorPane();
        
        VBox vbox = new VBox(20);
        
        vbox.setPrefWidth(200);
        ImageView imgView = new ImageView(new Image(getClass().getResourceAsStream("icons/icon.png")));
        
        TextField nameField = new TextField();
        
        nameField.setPromptText("Kullanici Adi");
        nameField.setFont(Font.font(16.0));
        PasswordField passwordField;
        passwordField = new PasswordField();
        
        passwordField.setPromptText("Şifre");
        passwordField.setFont(Font.font(16.0));
        
        Button loginButton = new Button("Giriş Yap");
        loginButton.setFont(Font.font(16.0));
 
        passwordField.setOnAction((event) -> {
            loginButton.fire();
        });
 
        Label warningLabel = new Label("");
        Stage main = this;
        loginButton.setOnAction(new EventHandler<ActionEvent>(){
           
            @Override
            public void handle(ActionEvent event) {
                DBManager man  = new DBManager();
                
                man.connect();
                if( man.checkLogin(nameField.getText(), passwordField.getText())){
                    
                    User u = man.getUser(nameField.getText());
                    man.close();
                    
                    //SiparisArayuzu siparisArayuzu = new SiparisArayuzu(man.getUser(nameField.getText()));
                    close();
                    new SiparisArayuzu2(u).show();
                    
                }else{
                    warningLabel.setText("Hata: Yanlış kullanıcı adı ya da şifre ! ");
                }
                man.close();
            }
        });
        loginButton.setMaxWidth(Double.MAX_VALUE);
        vbox.getChildren().addAll(imgView, nameField, passwordField, loginButton, warningLabel);
        VBox.setVgrow(loginButton, Priority.ALWAYS);
        vbox.prefWidthProperty().bind(this.widthProperty().multiply(0.10));
        vbox.setAlignment(Pos.CENTER);
        anc.getChildren().add(vbox);
        AnchorPane.setTopAnchor(vbox, 5.0);
        AnchorPane.setBottomAnchor(vbox, 5.0);
        AnchorPane.setRightAnchor(vbox, 5.0);
        AnchorPane.setLeftAnchor(vbox, 5.0);
        anc.setPadding(new Insets(30, 100, 30, 100));
        Scene s = new Scene(anc, 400, 400);
        
        // s.getStylesheets().add(Login.class.getResource("general.css").toExternalForm());
        
        this.setTitle("Kasiyer Programi: Giriş");
        this.setScene(s);
        this.setResizable(false);
    }
    
}
