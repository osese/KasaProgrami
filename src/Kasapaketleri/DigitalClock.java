/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Kasapaketleri;

/**
 *
 * @author revolver
 */
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

 public class DigitalClock extends Label
 {

    public DigitalClock()
    {
        bindToTime();
    }
    
    private void bindToTime() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    String text = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
                    if(text.equals("00:00:00")){
                        System.out.println("Selamxxxxxxxxxxxx");
                    }
                    setText(text);
                }
            }),
            new KeyFrame(Duration.seconds(1)));
        
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }
}
