/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Kasapaketleri;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.AccessibleRole;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToolBar;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javax.print.PrintException;

/**
 *
 * @author revolver
 */

class CircleButton extends Button{
    CircleButton(String text, Image img){
        super(text, new ImageView(img));
        this.setShape(new Circle());
        this.setContentDisplay(ContentDisplay.CENTER);
    }
}

public class SiparisArayuzu2 extends Stage {
    private BigDecimal meyveTotal;
    ObservableList<Urun> urunListesi;
    DBManager dbManager;
    User currentUser;
    private int fisNo;
    private int siparisNo;
    private int siparisType;
    Label fisNoLabel;
    TextField barkodField;
    
    public SiparisArayuzu2(User user){
        currentUser = user;
        initVars();
        BorderPane mainPane = new BorderPane();
        ToolBar header = createHeader();
        VBox leftPane = createLeftPane();
        VBox center = createCenter();
        
        mainPane.setTop(new VBox(new TitledPane("", header), new Separator()));
        mainPane.setLeft(new HBox(leftPane, new Separator(Orientation.VERTICAL)));
        mainPane.setCenter(center);
       
        // set scene 
        Scene scene = new Scene(mainPane, 1366, 768);
        scene.getStylesheets().add(getClass().getResource("general.css").toExternalForm());
        
        // set the focus to the text field, after a keypressed 
        scene.setOnKeyPressed((event) -> {
           barkodField.requestFocus();
        });

        this.setScene(scene);
        this.setTitle("Sipariş Alma");
        dbManager.connect();
        boolean fsMode = dbManager.getFullScreenMode();
        dbManager.close();
        this.setFullScreen(fsMode);
    }
    
    private void initVars(){
        dbManager = new DBManager();
        meyveTotal = new BigDecimal("0.00");
        urunListesi = FXCollections.observableArrayList();
        fisNoLabel = new Label(); // selam 
        fisNoLabel.setText("Fiş No: "+ this.fisNoLabel);
        fisNoLabel.setFont(Font.font("Monospaced", FontWeight.BOLD, 16));
        barkodField = new TextField();   
        barkodField.setPromptText("Ürün barkodu giriniz.");
        barkodField.setOnAction((event) -> { 
            dbManager.connect();
            Urun a;
            a = dbManager.getProductByBarcod(barkodField.getText());
            dbManager.close();
            if(a != null){
                urunListesi.add(a);
            }
            barkodField.setText("");
        });
        setFisNoSiparisNoType();
        fisNoLabel.setText("Fiş No: " + this.fisNo);
    }
    private ToolBar createHeader(){
        ToolBar header = new ToolBar();
        // fis düzenleme butonu 
        
        // Thats something called logo are u want ? 
        // Button logo = HeaderButtons("", "icons/small-icon.png", (event)->{});
        
        Button anlikGoruntu = HeaderButtons("Anlık Rapor", "icons/snapshot.png", (event) -> {
            AnlikGoruntu ag = new AnlikGoruntu();
            ag.show();
        });
        
        Button duzelt = HeaderButtons("Fiş İptal", "icons/receipt.png",(event) -> {
            FisDuzeltArayuzu fda = new FisDuzeltArayuzu(urunListesi);
            // set the resulting observable list to the current list
            fda.initOwner(SiparisArayuzu2.this);
            fda.initModality(Modality.WINDOW_MODAL);
            fda.show();
        });
        
        Button urunEkle = HeaderButtons("Ürün Düzenle", "icons/edit.png", (event)-> {
            UrunDuzenle ud = new UrunDuzenle();
            ud.show();
        });
        
        Button ayarlar = HeaderButtons("Ayarlar", "icons/settings.png", (event) ->{
            AyarlarArayuzu a = new AyarlarArayuzu();
            a.initOwner(this);
            a.initModality(Modality.WINDOW_MODAL);
            a.show();
        });
            
        Button cikis = HeaderButtons("Çıkış", "icons/logout.png", (event)->{
            this.close(); 
            new Login().show(); 
        });
        
        Button kapat = HeaderButtons("Kapat", "icons/shutdown.png", (event)->{ this.close();});
        
        Label currentDate = new Label(LocalDateTime.now().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)));
        currentDate.setFont(Font.font("Serif", FontWeight.SEMI_BOLD, 16));
        
        Label userName = new Label("Kullanıcı: " + currentUser.getFullname());
        userName.setFont(Font.font("Serif", FontWeight.SEMI_BOLD, 16));
        
        HBox aligner = new HBox();
        aligner.setMaxWidth(Double.MAX_VALUE);
        aligner.setMinWidth(100);
        HBox.setHgrow(aligner, Priority.ALWAYS);
        header.getItems().addAll(currentDate, userName, aligner, anlikGoruntu, duzelt, urunEkle, ayarlar, cikis, kapat);
        
        return header;
    }
    private Button HeaderButtons(String text, String path, EventHandler<ActionEvent> event){
        Button b1 = new Button(text, new ImageView(new Image(getClass().getResourceAsStream(path))));
        b1.setContentDisplay(ContentDisplay.TOP);
        b1.setFont(Font.font(12));
        b1.setOnAction(event);
        
        return b1;
    }
    private VBox createLeftPane(){
        VBox left_box = new VBox();
        left_box.setPadding(new Insets(0, 4, 0, 0));
        HBox toplamBolumu = new HBox();
        HBox siparisAraclar = new HBox();
        
        TableView<Urun> siparis = createSiparisTablosu();
        VBox.setVgrow(siparis, Priority.ALWAYS);
        
        // Toplamı gösteren bölüm 
        Label totalPriceLabel = new Label("Toplam: ");
        Label totalPrice = new Label("\u20BA 0.00");
        urunListesi.addListener(new ListChangeListener(){
            // total price listens urunListesi changes
            @Override
            public void onChanged(ListChangeListener.Change c) {
                BigDecimal total = new BigDecimal("0.00");
                
                for(int i=0; i<urunListesi.size(); i++){
                    total = total.add(urunListesi.get(i).getPrice());
                }
                
                totalPrice.setText("\u20BA " + total.toString());
            }
        });
        toplamBolumu.setAlignment(Pos.CENTER_RIGHT);
        toplamBolumu.setSpacing(20);
        
        toplamBolumu.getChildren().addAll(totalPriceLabel, totalPrice);
        totalPriceLabel.setFont(Font.font("", FontWeight.EXTRA_BOLD, 20));
        totalPrice.setFont(Font.font(20));
        
        // Siparis Aracları 
        Button onayButonu = new Button("Onayla", new ImageView(new Image(getClass().getResourceAsStream("icons/success.png"))));
        onayButonu.setOnAction((event) -> {
            if(urunListesi.isEmpty()){  // if siparis listesi boş 
                return;
            }
            onayla();
        });
        
        Button hepsiniSilButton = new Button("Hepsini Sil", new ImageView(new Image(getClass().getResourceAsStream("icons/delete.png"))));
        hepsiniSilButton.setOnAction((event) -> {
            urunListesi.clear();
        });
        
        Button yazdirButonu = new Button("Yazdır", new ImageView(new Image(getClass().getResourceAsStream("icons/print.png"))));
        
        yazdirButonu.setOnAction((event) -> {
            if(urunListesi.isEmpty()){
                return; 
            }
            onaylaVeYazdir();
        });
        siparisAraclar.getChildren().addAll(onayButonu, hepsiniSilButton, yazdirButonu);
        
        siparisAraclar.getChildren().forEach( (t) -> {
            ((Button)t).setFont(Font.font(12));
            ((Button)t).setPrefSize(100, 100);
            ((Button)t).setContentDisplay(ContentDisplay.TOP);
        });
        siparisAraclar.setPadding(new Insets(10,0, 10, 0));
        siparisAraclar.setSpacing(10);
        siparisAraclar.setAlignment(Pos.CENTER);
        
        left_box.getChildren().addAll(fisNoLabel, siparis,toplamBolumu,siparisAraclar, new Separator(), barkodField);
        return left_box;
    }
    private VBox createCenter(){
        VBox main = new VBox();
        TabPane category = createCategoriesTabs();
        VBox.setVgrow(category, Priority.ALWAYS);
        HBox sikKullanilanlar = getSikKullanilanlar();
        main.getChildren().addAll(category, sikKullanilanlar);
        return main;
    }
    private TabPane createCategoriesTabs(){
        TabPane category = new TabPane();
        dbManager.connect();
        ArrayList<Urun> au = dbManager.getProducts();
        ArrayList<String> ac = dbManager.getProductCategories();
        dbManager.close();
        
        dbManager.connect();
        
        for(int i=0; i<ac.size(); i++){   // categorilerin içinde dolanır
            if(i == 2 || i == 4){ // Hazir tabını ve Gözdeleri ignorela
                continue;
            }
            TilePane tp1  = new TilePane();
            
            tp1.setOrientation(Orientation.HORIZONTAL); //Tile Pane içindeki elemanların konum şeklinin belirlenmesi
            tp1.setPrefTileHeight(95);  //TilePane elemanlarının yüksekliğinini belirlenmesi
            tp1.setPrefTileWidth(95); //TilePane elemanlarının genişliğinin belirlenmesi
            
            tp1.setPrefColumns(8); //TilePane elemanlarının bir satırda kaç kolon olmasının belirlenmesi
            
            tp1.setVgap(4);
            tp1.setHgap(4);
            ArrayList<Urun> au_l = dbManager.getProducstByCategory(ac.get(i));
            
            for(int j=0; j<au_l.size(); j++){  // categoriler iiçindeki her bir yemek
                
                Button b1 = new Button(au_l.get(j).getName()); // Ürünün adı 
                Text fiyatText = new Text("\u20BA " + au_l.get(j).getPrice().toString());
                fiyatText.setFont(Font.font("Sans", FontWeight.BOLD, 15));
                b1.setGraphic(fiyatText);
                b1.setContentDisplay(ContentDisplay.BOTTOM);
                
                b1.setPrefSize(90, 90);
                
                b1.setFont(Font.font(14));
                b1.setWrapText(true);
                b1.setTextAlignment(TextAlignment.CENTER);
                Urun urun = au_l.get(j);
                b1.setOnAction((event) -> {
                    urunListesi.add(urun);
                });
                tp1.getChildren().add(b1);
            }
                
            Tab e = new Tab();
            e.setClosable(false);
            
            try{
                e.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("icons/category/" +ac.get(i) + ".png"))));
            }catch(java.lang.NullPointerException ex){
                System.out.println("Bir resim yok. '" + ac.get(i) + ".png'");
            }
            e.setContent(new ScrollPane(tp1));            
            category.getTabs().add(e);
        }
        dbManager.close();
        
        category.getTabs().add(createMeyveTab());
        category.setTabMaxHeight(76);
        category.setTabMinHeight(76);
        category.setTabMinWidth(100);
        category.setTabMaxWidth(100);
        
        return category;
    }  
    private Tab createMeyveTab(){
        // meyve kategori 
        Tab meyveTab  = new Tab();
        //meyveTab.setText("Meyveler");
        meyveTab.setClosable(false);
        meyveTab.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("icons/category/Meyveler.png"))));
        
        VBox meyveAll = new VBox();
        TextField meyveTotalTextField = new TextField("0.00");
        
        meyveTotalTextField.setEditable(false);
        meyveTotalTextField.setFont(Font.font("Monospaced", FontWeight.EXTRA_BOLD, 30));
        
        HBox meyveParalar = new HBox();
        
        Button meyveB5 = new Button("5 KURUŞ");
        Button meyveB10 = new Button("10 KURUŞ");
        Button meyveB25 = new Button("25 KURUŞ");
        Button meyveB50 = new Button("50 KURUŞ");
        Button meyveB100 = new Button("1 TL");
        
       
        
        meyveB5.setOnAction( (event) -> {
            meyveTotal  = meyveTotal.add(new BigDecimal("0.05"));
            meyveTotalTextField.setText(meyveTotal.toString());
            
        });
        meyveB10.setOnAction( (event) -> {
            meyveTotal  = meyveTotal.add(new BigDecimal("0.1"));
            meyveTotalTextField.setText(meyveTotal.toString());
        });
        
        meyveB25.setOnAction( (event) -> {
            meyveTotal  = meyveTotal.add(new BigDecimal("0.25"));
            meyveTotalTextField.setText(meyveTotal.toString());
        });
        meyveB50.setOnAction( (event) -> {
            meyveTotal  = meyveTotal.add(new BigDecimal("0.5"));
            meyveTotalTextField.setText(meyveTotal.toString());
        });
        meyveB100.setOnAction( (event) -> {
            meyveTotal  = meyveTotal.add(new BigDecimal("1.00"));
            meyveTotalTextField.setText(meyveTotal.toString());
        });
        
        
        meyveParalar.getChildren().addAll(meyveB5, meyveB10, meyveB25, meyveB50, meyveB100);
        meyveParalar.getChildren().forEach((t) -> {
           ((Button)t).setMinSize(100, 100);
           ((Button)t).setFont(Font.font("Monospaced", FontWeight.EXTRA_BOLD, 20));
        });
        
        meyveParalar.setSpacing(20);
        meyveParalar.setAlignment(Pos.CENTER);
        Button meyveTamam = new Button("", new ImageView(new Image(getClass().getResourceAsStream("icons/success.png"))));
        Button meyveSil = new Button("", new ImageView(new Image(getClass().getResourceAsStream("icons/error-64.png"))));
        
        
        
        HBox meyveIslemler = new HBox();
        
        meyveIslemler.getChildren().addAll(meyveTamam, meyveSil);
        meyveIslemler.setSpacing(20);
        
        meyveSil.setOnAction((event) -> {
            meyveTotal = BigDecimal.ZERO;
            meyveTotalTextField.setText("0.00");
        });
        
        meyveIslemler.setAlignment(Pos.CENTER);
        meyveTamam.setOnAction((event) -> {
            // IMPORTANT  meyve id sini elle giriyoruz, db de ki ile aynı olmalı 
            if(meyveTotal != BigDecimal.ZERO){
                urunListesi.add(Urun.Meyve(meyveTotal));
                meyveSil.fire();  
            }
        });
        

        meyveAll.getChildren().addAll(meyveTotalTextField, meyveParalar, meyveIslemler);
        meyveAll.setSpacing(40);
        meyveAll.setPadding(new Insets(40, 0, 0, 0));
        
        meyveTab.setContent(meyveAll);
        
        return meyveTab;
    }
    private TableView createSiparisTablosu(){
        TableView siparis_listesi = new TableView();
        siparis_listesi.setStyle("-fx-table-cell-border-color: transparent;");
        siparis_listesi.setPlaceholder(new Label("Henüz bir ürün yok!"));
        siparis_listesi.setItems(urunListesi);
        siparis_listesi.setAccessibleRole(AccessibleRole.MENU);
        
        TableColumn<Urun, String> tc_name = new TableColumn<>("Ürün Adı");
        tc_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        tc_name.setMinWidth(200);
        tc_name.setMaxWidth(200);
        
        TableColumn<Urun, BigDecimal> tc_price = new TableColumn<>("Fiyat");
        tc_price.setCellValueFactory(new PropertyValueFactory<>("price"));
        tc_price.setMinWidth(100);
        tc_price.setMaxWidth(100);
        
        
        TableColumn<Urun, String> tc_action = new TableColumn<>("");
        tc_action.setMinWidth(75);
        tc_action.setMaxWidth(75);
        
        siparis_listesi.getColumns().addAll(tc_name, tc_price,  tc_action);
        
        Image silImage = new Image(getClass().getResourceAsStream("icons/error.png"));
        
        Callback<TableColumn<Urun, String>, TableCell<Urun, String>> cellFactory
                = //
                new Callback<TableColumn<Urun, String>, TableCell<Urun, String>>() {
            @Override
            public TableCell call(final TableColumn<Urun, String> param) {
                final TableCell<Urun, String> cell = new TableCell<Urun, String>() {

                    final CircleButton btn = new CircleButton("", silImage);
                    
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            btn.setOnAction(event -> {
                                // btn action  silme işlemi
                                siparis_listesi.getItems().remove(this.getIndex());
                            });
                            setGraphic(btn);
                            setText(null);
                            
                        }
                    }
                };
                return cell;
            }
        };
        
        tc_action.setCellFactory(cellFactory);
        return siparis_listesi;
    }
    private void onayla(){
        dbManager.connect();
        dbManager.addNewSiparis(new Siparis(this.siparisNo, this.siparisType, this.fisNo, this.currentUser.getId(), new ArrayList(urunListesi)));
        urunListesi.clear();
        dbManager.close();
        setFisNoSiparisNoType();
        fisNoLabel.setText("Fiş No: " + this.fisNo); 
    }
    
    private void onaylaVeYazdir(){
        dbManager.connect();
        Siparis a = new Siparis(this.siparisNo, this.siparisType, this.fisNo, this.currentUser.getId(), new ArrayList(urunListesi));
        
        try {
            YazdirmaHelper3 yh = new YazdirmaHelper3(a);
        } catch (PrintException ex) {
            dbManager.close();
            return;
        }
        
        dbManager.addNewSiparis(a);
        urunListesi.clear();
        dbManager.close();
        setFisNoSiparisNoType();
        fisNoLabel.setText("Fiş No: " + this.fisNo);
    }
    
    private void setFisNoSiparisNoType() {
        dbManager.connect();
        this.siparisNo   = dbManager.getCurrentSiparisNo();
        this.fisNo       = dbManager.getCurrentSiparisFisNo();
        this.siparisType = dbManager.getCurrentSiparisType();
        dbManager.close();
    }
    
    private HBox getSikKullanilanlar() {
        HBox hbox = new HBox();
        dbManager.connect();
        ArrayList<Urun> urunler = dbManager.getProducstByCategoryId(4);
        dbManager.close();
        if(urunler == null){
            return hbox;
        }
        hbox.setSpacing(5);
        hbox.setAlignment(Pos.CENTER);
        hbox.setMinHeight(90);
        for(Urun i : urunler){
            Button b1 = new Button(i.getName());
            b1.setPrefSize(110, 90);
            
            Text fiyatText = new Text("\u20BA " + i.getPrice().toString());
            fiyatText.setFont(Font.font("Monospaced", FontWeight.BOLD, 16));
            b1.setGraphic(fiyatText);
            b1.setContentDisplay(ContentDisplay.BOTTOM);
            b1.setOnAction((event) -> { 
                urunListesi.add(i);
            });
            hbox.getChildren().add(b1);
        }
        
        return hbox;
    }
}