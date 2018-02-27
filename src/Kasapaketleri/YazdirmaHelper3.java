/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Kasapaketleri;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;

/*
 * @author revolver
 */

public class YazdirmaHelper3 {

    public static void GunSonuYazdir(ArrayList<Siparis> siparisler) throws PrintException{
        
        DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
        
        String text = "";
        text +=   Character.toString((char)29) +  Character.toString((char)76) +  Character.toString((char)20) +  Character.toString((char)0);
        
        
        // Double h w on 
        text += Character.toString((char)27) + Character.toString((char)33) +  Character.toString((char)16) ;
        text += Character.toString((char)27) + Character.toString((char)33) +  Character.toString((char)32) ;
        
        text += " \n"; // Tarihi Ekle
        
        // Double h w off 
        text += Character.toString((char)27) + Character.toString((char)33) +  Character.toString((char)0) ;
        
        // Siparisleri ekle 
        
        Map<String, Integer> occurences = new HashMap<>();
        Map<String, BigDecimal> isimFiyat = new HashMap<>();
        
        BigDecimal toplamMeyve = BigDecimal.ZERO;
        BigDecimal genelToplam = BigDecimal.ZERO;
        
        for(Siparis siparis : siparisler){
            
            int count = 0;
            for(Urun u : siparis.getUrunler()){
                if(u.getShortName().equalsIgnoreCase("MEYVE")){
                    toplamMeyve = toplamMeyve.add(u.getPrice());
            }else if(occurences.containsKey(u.getShortName())){
                    occurences.put(u.getShortName(), occurences.get(u.getShortName()) + 1);
                }else{
                    occurences.put(u.getShortName(), 1);
                }
                isimFiyat.put(u.getShortName(), u.getPrice());
            }   
            
        }
        
        long i = 0;
        for (Map.Entry<String, Integer> pair : occurences.entrySet()) {
            String  u = pair.getKey();
            int space = 40 - (u.length() + 5 + 5);
            
            int  c = pair.getValue();
            BigDecimal total = isimFiyat.get(u).multiply(new BigDecimal(c));
            genelToplam = genelToplam.add(total);
            text +=  u+ "  x" + c + charMul('.', space)+ "  " + total.toString() + "\n";
        }
        
        if( toplamMeyve != BigDecimal.ZERO){
            text += "MEYVE" + "  "+ charMul('.', 26)+ "  " + toplamMeyve.toString() + "\n";
            genelToplam = genelToplam.add(toplamMeyve);
        }
        
        text += charMul('_', 40) + "\n";
        text += "Toplam: " + charMul(' ', 25) + genelToplam.toString() + "\n";
        text += "\n\n";
        
        
        text += "\n" + "\n" + "\n";
        text += Character.toString((char)10) +  Character.toString((char)10) ;
        
         text += Character.toString((char)27) + Character.toString((char)105);

        System.out.println(text);
        InputStream is = new ByteArrayInputStream(text.getBytes());
        PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet(); 
        
        PrintService[] services = PrintServiceLookup.lookupPrintServices(flavor,null);
        // System.out.println(text);
        
        if(services.length > 0 ){
            DocPrintJob pj = services[0].createPrintJob();
            Doc doc = new SimpleDoc(is, flavor, null);
            
            pj.print(doc, null);
        }
        
    }
    
    public YazdirmaHelper3(Siparis s) throws PrintException{
        DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
        
        
        String text = "";
        text +=   Character.toString((char)29) +  Character.toString((char)76) +  Character.toString((char)20) +  Character.toString((char)0);

        text += s.getDate().toString() + "/";
        text += s.getTime().toString() + "/" +  s.getFis_no() + "\n";
        
        
        
        // Double h w on 
        text += Character.toString((char)27) + Character.toString((char)33) +  Character.toString((char)16) ;
        text += Character.toString((char)27) + Character.toString((char)33) +  Character.toString((char)32) ;
        
        text += "NENEHATUN E BLOK \n";
        
        // Double h w of 
        text += Character.toString((char)27) + Character.toString((char)33) +  Character.toString((char)0) ;
        
        
        
        text += charMul('_', 40) + "\n";
        text += "\n\n";
        
        
        
        for(Urun u : s.getUrunler()){
            String name = u.getShortName();
            text += name;
            
            text += charMul('.', 40 - name.length()-4);
            text += " " + u.getPrice().toString() + "\n";
        }
        
        
        text += charMul('_', 40) + "\n";
        text += "TOPLAM: " + s.getToplam().toString()  + "\n";
        text += charMul('_', 40) + "\n";

        text += "AFIYET OLSUN" + "\n" + "\t\t"; 
        // barcode
        
        text += Character.toString((char)29) + Character.toString((char)72) + Character.toString((char)2); // barcode number

        text +=  Character.toString((char)29) + Character.toString((char)104) + Character.toString((char)60);
        text += Character.toString((char)29) + Character.toString((char)107) + Character.toString((char)70) + Character.toString((char)10) +
                s.getBarcod() ;//Character.toString((char)0);
        // System.out.println(s.getBarcod());
        

        
        text += "\n" + "\n" + "\n";
        text += Character.toString((char)10) +  Character.toString((char)10) ;
        
         text += Character.toString((char)27) + Character.toString((char)105);

        InputStream is = new ByteArrayInputStream(text.getBytes());
        PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet(); 
        
        PrintService[] services = PrintServiceLookup.lookupPrintServices(flavor,null);
        
        if(services.length > 0 ){
            DocPrintJob pj = services[0].createPrintJob();
            Doc doc = new SimpleDoc(is, flavor, null);
            
            pj.print(doc, null);
        }
        
    }
    
    private static String charMul(char x, int mul){
        String s = "";
        for(int i=0; i<mul; i++){
            s +=  Character.toString(x);
        }
        return s;
    }  
}
