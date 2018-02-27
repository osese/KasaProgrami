/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Kasapaketleri;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 *
 * @author revolver
 */
public class AnlikGoruntu  extends Stage{
    TableView<Map.Entry<String, Integer>> table;
    ArrayList<Siparis> siparisler;
    ObservableList<Map.Entry<String, Integer>> items;
    FilteredList<Map.Entry<String, Integer>> fitems;
    
    HBox searchBox;
    public AnlikGoruntu(){
        
        initSiparisler();
        initTable();
        initSearchBox();
        VBox vbox = new VBox();
        vbox.getChildren().addAll(searchBox, table);
        Scene scene = new Scene(vbox, 400, 400);
        this.setScene(scene);
    }
    
    private void initSearchBox(){
        
        // search field 
        TextField searchField = new TextField();
        searchField.setPromptText("aranacak kelime");
        
        Button searchButton = new Button("", new ImageView(new Image(getClass().getResourceAsStream("icons/urunduzenle/search.png"))));
        searchButton.setOnAction((event) -> {
            fitems.setPredicate(new Predicate<Map.Entry<String, Integer>>() {
                @Override
                public boolean test(Map.Entry<String, Integer> t) {
                    return t.getKey().startsWith(searchField.getText());
                }
            });
        });
        
        searchButton.setAlignment(Pos.CENTER);
        searchField.setOnAction((event) -> {
            searchButton.fire();
        });
        
        searchBox = new HBox();
        searchBox.getChildren().addAll(searchField, searchButton);
        searchBox.setPadding(new Insets(1,1,1,1));
        searchBox.setAlignment(Pos.CENTER_LEFT);
        
    }
    
    private void initSiparisler() {
        DBManager man = new DBManager();
        man.connect();
        siparisler = man.getOutputFromToOK(man.getGunSonu(), LocalDateTime.now());
        man.close();
    }

    private void initTable() {
        Map<String, Integer> map = new HashMap();
        
        for(Siparis s : siparisler){
            for(Urun e : s.getUrunler()){
                if(map.containsKey(e.getName())){
                    map.replace(e.getName(), map.get(e.getName()) + 1);
                }else{
                    map.put(e.getName(), 1);
                }
            }
        }
        
        TableColumn<Map.Entry<String, Integer>, String> nameCol = new TableColumn<>("İsim");
        nameCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map.Entry<String, Integer>, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Map.Entry<String, Integer>, String> param) {
                return new SimpleStringProperty(param.getValue().getKey());
            }
        });
        
        
        TableColumn<Map.Entry<String, Integer>, Integer> countCol = new TableColumn<>("Sayısı");
        countCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map.Entry<String, Integer>, Integer>, ObservableValue<Integer>>() {
            @Override
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Map.Entry<String, Integer>, Integer> param) {
                return new ReadOnlyObjectWrapper<>(param.getValue().getValue());
            }
        });
        
        items = FXCollections.observableArrayList(map.entrySet());
        fitems = new FilteredList<>(items);
        table = new TableView<>(fitems);
        table.getColumns().setAll(nameCol, countCol);
    }
    
}
