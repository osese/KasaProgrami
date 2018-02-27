/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Kasapaketleri;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 *
 * @author revolver
 */
public class Giris extends Application{
    public void start(){
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage = new Login();
        //primaryStage = new UrunDuzenle();
        primaryStage.show();
    }
}
