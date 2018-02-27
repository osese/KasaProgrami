/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Kasapaketleri;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.StringConverter;

/**
 *
 * @author revolver
 */
 

public class UrunDuzenle extends Stage{
    ObservableList<Urun> urunListesi;
    SortedList<Urun> sortedList;
    TableView<Urun> urunView;
    ListView<String> warningView;
    ObservableList<String> warningList;
    ArrayList<Urun> eklendi = new ArrayList<>();
    ArrayList<Urun> silindi = new ArrayList<>();
    Set<Urun> guncellendi = new HashSet<>();
    HBox header;
    private FilteredList<Urun> filteredList;
    
    public UrunDuzenle(){
        initUrunListesi();
        initTableView();
        initHeader();
        initWarningList();
        VBox main = new VBox();
        main.getChildren().addAll(header, urunView, warningView);
        VBox.setVgrow(urunView, Priority.ALWAYS);
        Scene scene = new Scene(main, 1366, 768);
        scene.getStylesheets().add(getClass().getResource("urunduzenle.css").toExternalForm());
        this.setScene(scene);
        // this.setFullScreen(true);
    }
    
    private void initHeader(){
        header = new HBox();
        
        // search field 
        ComboBox<String> searchType = new ComboBox();
        searchType.getItems().addAll("id", "barkod no", "isim");
        searchType.getSelectionModel().select(1);
        TextField searchField = new TextField();
        searchField.setPromptText("aranacak kelime");
        
        Button searchButton = new Button("", new ImageView(new Image(getClass().getResourceAsStream("icons/urunduzenle/search.png"))));
        searchButton.setTooltip(new Tooltip("Aramak İçin Tıklayınız"));
        searchButton.setOnAction((event) -> {
            searchButtonAction(searchType.getSelectionModel().getSelectedItem(), searchField.getText());
        });
        
        searchButton.setAlignment(Pos.CENTER);
        searchField.setOnAction((event) -> {
            searchButton.fire();
        });
        
        HBox searchBox = new HBox();
        searchBox.getChildren().addAll(searchType, searchField, searchButton);
        searchBox.setPadding(new Insets(1,1,1,1));
        searchBox.setAlignment(Pos.CENTER_LEFT);
        
        // Filtreleme : kategoriye göre 
        HBox filtreleme = new HBox();
        ComboBox<String> filtreType = new ComboBox<>();
        filtreType.getItems().addAll(new Kategori().getCats());
        filtreType.setOnAction((event) -> {
            applyCatFilter(filtreType.getSelectionModel().getSelectedItem());
        });
        
        filtreleme.getChildren().addAll(new Label("Filtrele (Kategori): "), filtreType);
        filtreleme.setAlignment(Pos.CENTER_LEFT);
        
        
        // Çıkış 
        Button cikisButton = new Button("Çıkış", new ImageView(new Image(getClass().getResourceAsStream("icons/urunduzenle/exit.png"))));
        
        cikisButton.setOnAction((event) -> {
            this.close();
        });
        
        // Kaydet 
        Button kaydetButton = new Button("Değişiklikleri Kaydet", new ImageView(new Image(getClass().getResourceAsStream("icons/urunduzenle/save.png"))));
        kaydetButton.setOnAction((event) -> {
            warningList.clear();
            boolean no_error = false;
            if(eklendi.isEmpty() && silindi.isEmpty() && guncellendi.isEmpty()){
                warningList.add("Herhangi bir değişiklik yapılmadı.");
                return;
            }
            
            for(Urun e : eklendi){
                if(e.getName().equals("")){
                    warningList.add("Ürün ismi boş bırakılamaz. Urun id: " + e.getId());
                    no_error = true;
                }
                if(e.getShortName().equals("")){
                    warningList.add("Fiş ismi boş bırakılamaz. Urun id: " + e.getId());
                    no_error = true;
                }
            }
            
            for(Urun e : guncellendi){
                if(e.getName().equals("")){
                    warningList.add("Ürün ismi boş bırakılamaz. Urun id: " + e.getId());
                    no_error = true;
                }
                if(e.getShortName().equals("")){
                    warningList.add("Fiş ismi boş bırakılamaz. Urun id: " + e.getId());
                    no_error = true;
                }
            }
            
            if(no_error){
                return; 
            }
            
            UrunDuzenleDegisiklikKaydet uddk = new UrunDuzenleDegisiklikKaydet(eklendi, silindi, guncellendi);
            uddk.initOwner(this);
            uddk.initStyle(StageStyle.UTILITY);  // Çare stage utility they say 
            uddk.initModality(Modality.WINDOW_MODAL);
            uddk.showAndWait();
            if(uddk.getResult()){
                warningList.add("Değişiklikler kaydedildi.");
                eklendi.clear();
                silindi.clear();
                guncellendi.clear();
            }
        });
        
        HBox hRight = new HBox();
        HBox.setHgrow(hRight, Priority.ALWAYS);
        
        header.getChildren().addAll(searchBox, new Separator(Orientation.VERTICAL), filtreleme ,
                hRight, new Separator(Orientation.VERTICAL), kaydetButton, cikisButton);
    }
    
    
    private void initUrunListesi(){
        DBManager man = new DBManager();
        man.connect();
        urunListesi = FXCollections.observableArrayList(man.getProducts());
        urunListesi.sort(new Comparator<Urun>() {
            @Override
            public int compare(Urun o1, Urun o2) {
                return o1.getId() < o2.getId() ? 0 : 1;
            }
        });
        urunListesi.addListener(new ListChangeListener<Urun>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends Urun> c) {
                
                System.out.println("List changed!!");
                while(c.next()){
                    for(Urun e : c.getAddedSubList()){
                        eklendi.add(e);
                    }  
                    
                    for(Urun e : c.getRemoved()){
                        if(eklendi.contains(e)){
                            eklendi.remove(e);
                            silindi.remove(e);
                        }else{
                            silindi.add(e);
                        }
                    }
                    
                     
                }
            }
        });
        
        filteredList= new FilteredList<>(urunListesi);
        sortedList = new SortedList<>(filteredList);
        
        man.close();
    }
    
  
    
    private void initTableView(){
        urunView = new TableView<>(sortedList);
        urunView.setPlaceholder(new Label("Hiç bir sonuç yok."));
        urunView.setRowFactory(new Callback<TableView<Urun>, TableRow<Urun>>() {
            @Override
            public TableRow<Urun> call(TableView<Urun> param) {
                final TableRow<Urun> row = new TableRow<>();
                final ContextMenu rowMenu = new ContextMenu();
                MenuItem addItem = new MenuItem("Yeni Urun Ekle");
                addItem.setOnAction((event) -> {
                    // a little hack for adding new row next to each other
                    Urun a = new Urun(row.getItem());
                    urunListesi.add(urunListesi.indexOf(row.getItem()) + 1, a);
                    a.clear();
                    a.setId(urunListesi.size()+1);
                    
                });
                
                MenuItem removeItem = new MenuItem("Sil");
                removeItem.setOnAction((event) -> {
                    urunListesi.remove(row.getItem());
                });
                
                rowMenu.getItems().addAll(addItem, removeItem);
                row.contextMenuProperty().bind(
                Bindings.when(Bindings.isNotNull(row.itemProperty()))
                .then(rowMenu)
                .otherwise((ContextMenu)null));
              return row;
            }
        });
        
        sortedList.comparatorProperty().bind(urunView.comparatorProperty());
        TableColumn<Urun, Integer> identity = new TableColumn("ID");
        identity.setCellValueFactory(new Callback<CellDataFeatures<Urun, Integer>, ObservableValue<Integer>>() {
            @Override
            public ObservableValue<Integer> call(CellDataFeatures<Urun, Integer> param) {
                return new ReadOnlyObjectWrapper(param.getValue().getId());
            }
        });
        identity.setEditable(false);
        identity.setStyle("-fx-font-weight: bold;");
        TableColumn<Urun, String> longName = new TableColumn("Uzun isim");
        longName.setCellValueFactory(new PropertyValueFactory<>("name"));
        longName.setPrefWidth(300);
        longName.setCellFactory(new Callback<TableColumn<Urun, String>, TableCell<Urun, String>>(){
            @Override
            public TableCell<Urun, String> call(TableColumn<Urun, String> param) {
                return  new TextFieldTableCell<>(new StringConverter<String>() {
                    @Override
                    public String toString(String object) {
                        return object;
                    }

                    @Override
                    public String fromString(String string) {
                        return string;
                    }
                });
            }
        });
        
        longName.setOnEditCommit(
                (TableColumn.CellEditEvent<Urun, String> t) -> {
                    t.getRowValue().setName(t.getNewValue());
                    guncellendi.add(t.getRowValue());
                }
        );
        
        
        TableColumn<Urun, String> shortName = new TableColumn("Fiş ismi");
        shortName.setCellValueFactory(new PropertyValueFactory<>("shortName"));
        shortName.setPrefWidth(200);
        shortName.setCellFactory(new Callback<TableColumn<Urun, String>, TableCell<Urun, String>>(){
            @Override
            public TableCell<Urun, String> call(TableColumn<Urun, String> param) {
                return  new TextFieldTableCell<>(new StringConverter<String>() {
                    @Override
                    public String toString(String object) {
                        return object;
                    }

                    @Override
                    public String fromString(String string) {
                        return string;
                    }
                });
            }
        });
        shortName.setOnEditCommit(
                (TableColumn.CellEditEvent<Urun, String> t) -> {
                    t.getRowValue().setShortName(t.getNewValue());
                    guncellendi.add(t.getRowValue());
                }
        );
        
        TableColumn<Urun, String> barcodNo = new TableColumn("Barkod No");
        barcodNo.setCellValueFactory(new PropertyValueFactory<>("Barcodno"));
        barcodNo.setPrefWidth(150);
        barcodNo.setCellFactory(new Callback<TableColumn<Urun, String>, TableCell<Urun, String>>(){
            @Override
            public TableCell<Urun, String> call(TableColumn<Urun, String> param) {
                return  new TextFieldTableCell<>(new StringConverter<String>() {
                    @Override
                    public String toString(String object) {
                        return object;
                    }

                    @Override
                    public String fromString(String string) {
                        return string;
                    }
                });
            }
        });
        
        barcodNo.setOnEditCommit(
                (TableColumn.CellEditEvent<Urun, String> t) -> {
                    t.getRowValue().setBarcodno(t.getNewValue());
                    guncellendi.add(t.getRowValue());

                }
        );
        
        TableColumn<Urun, BigDecimal>  price = new TableColumn("Fiyat");
        price.setCellValueFactory(new PropertyValueFactory<>("price"));
        price.setPrefWidth(70);
        price.setCellFactory(new Callback<TableColumn<Urun, BigDecimal>, TableCell<Urun, BigDecimal>>(){
            @Override
            public TableCell<Urun, BigDecimal> call(TableColumn<Urun, BigDecimal> param) {
                return new TextFieldTableCell<>(new StringConverter<BigDecimal>() {
                    @Override
                    public String toString(BigDecimal a) {
                        return a.toString();
                    }
                    @Override
                    public BigDecimal fromString(String string) {
                        return  new BigDecimal(string);
                    }
                });
            }
        });
        
        price.setOnEditCommit(
                (TableColumn.CellEditEvent<Urun, BigDecimal> t) -> {
                        t.getRowValue().setPrice(t.getNewValue());
                        guncellendi.add(t.getRowValue());

                }
        );
        
        TableColumn category = new TableColumn("Kategori");
        
        category.setCellFactory(new Callback<TableColumn<Urun, String>, TableCell<Urun, String>>(){
            @Override
            public TableCell<Urun, String> call(TableColumn<Urun, String> param) {
                return new ComboBoxTableCell<>(FXCollections.observableArrayList(
                new Kategori().getCats()));
            }
        });
        
        category.setCellValueFactory(new Callback<CellDataFeatures<Urun, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(CellDataFeatures<Urun, String> p) {
            // p.getValue() returns the Person instance for a particular TableView row
            return new ReadOnlyObjectWrapper(p.getValue().getCategoryS());
            }
        });
        category.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Urun, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Urun, String> t) {
                ((Urun) t.getTableView().getItems().get(t.getTablePosition().getRow()))
                    .setCategoryS(t.getNewValue());
                    guncellendi.add(t.getRowValue());
            }
        });
        urunView.getColumns().addAll(identity, barcodNo, longName, shortName, price, category);
        urunView.setEditable(true);
    }

    private void applyCatFilter(String categ) {
        filteredList.setPredicate((t) -> {
            return ((Urun)t).getCategoryS() == categ;
        });
    }

    private void searchButtonAction(String selectedItem, String value) {
        
        if(selectedItem.equals("id")){
            filteredList.setPredicate((t) -> {
                return String.valueOf(t.getId()).equals(value);
            });
        }else if(selectedItem.equals("barkod no")){
            filteredList.setPredicate((t) -> {
                return t.getBarcodno().equals(value);
            });
        }else if(selectedItem.equals("isim")){
            filteredList.setPredicate((t) -> {
                return t.getName().startsWith(value);
            });
        }  
    }

    private void initWarningList() {
        warningList = FXCollections.observableArrayList();
        warningView = new ListView<>(warningList);
        warningView.setPrefHeight(150);
        warningView.setEditable(false);
    }

    
    
}

