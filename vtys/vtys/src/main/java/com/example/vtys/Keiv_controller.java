package com.example.vtys;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;


public class Keiv_controller {

    public static String  kod_vt, value,baslikin,resim_uzantisi,path;
    public LocalDate tarihin;
    public int ilce_id=0,uye_id=0,kategori_id=0;
    public String secilen;
    ResultSet resultSet =null;
    List<String> lstFile;

    ObservableList kategori_list = FXCollections.observableArrayList();
    ObservableList il_list = FXCollections.observableArrayList();
    ObservableList ilce_list = FXCollections.observableArrayList();
    @FXML
    private Button profil, keiv, beiv, kei, bei, mesaj, cikis,anasayfa,vt_kaydet;

    //Ana sayfadaki butonlar
    @FXML
    private Button btumu, ktumu;
    @FXML
    private DatePicker tarih;
    @FXML
    private TextField baslik;

    @FXML
    private ComboBox<?> kategori_combo, il_combo,ilce_combo;
    @FXML
    private Label uyari;
    @FXML
    private ImageView imageView;

    Veri_tabanı_baglantisi vt = new Veri_tabanı_baglantisi(); //obje oluşturduk.
    Ekran_degis ed = new Ekran_degis(); // ekran değiştirmek için sınıftan nesne ürettik.


    @FXML
    public void initialize () throws SQLException, ClassNotFoundException {
        lstFile= new ArrayList<>();
        lstFile.add("*.img");
        lstFile.add("*.imeg");
        lstFile.add("*.png");
        lstFile.add("*.jpg");
        lstFile.add("*.jpeg");

        resultSet = vt.read("Select Kategori_isim from Kategori");
        while (resultSet.next()) {
           kategori_list.add(resultSet.getString("Kategori_isim")); //combobaxa eklemek için observable liste veritabanından gelen verileri ekledik.
        }
        kategori_combo.getItems().addAll(kategori_list);



        resultSet = vt.read("Select sehiradi from iller");
        while (resultSet.next()) {
            il_list.add(resultSet.getString("sehiradi")); //combobaxa eklemek için observable liste veritabanından gelen verileri ekledik.
        }
        il_combo.getItems().addAll(il_list); //veritabanından alınan iller comboboxa eklenir.

    }


    @FXML
    void il_selected(ActionEvent event) throws SQLException, ClassNotFoundException {
        ilce_list.clear();
        ilce_combo.getItems().clear();

        secilen = (String) il_combo.getValue(); //seçilen şehir

        resultSet = vt.read("Select ilceadi from ilceler where sehirid = (Select plaka from iller where sehiradi = '"+secilen+"');");

        while (resultSet.next()) {
            ilce_list.add(resultSet.getString("ilceadi")); //combobaxa eklemek için observable liste veritabanından gelen verileri ekledik.
        }

       ilce_combo.getItems().addAll(ilce_list); //veritabanından alınan iller comboboxa eklenir.
    }



    @FXML
    void imageChooser (ActionEvent event) {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Word Files", lstFile));
        File f = fc.showOpenDialog(null);
        if (f != null) {
            path = f.getAbsolutePath();
            //System.out.println("Selected File::  " + f.getAbsolutePath());
            Image image = new Image(f.toURI().toString(), 150,150, true, true);//path, PrefWidth, PrefHeight, PreserveRat
            imageView.setImage(image);
        }

    }
    @FXML
    void vt_kaydet_action(ActionEvent event) throws SQLException, ClassNotFoundException {
        baslikin = baslik.getText();
        tarihin = tarih.getValue();
        resim_uzantisi = path;
        File file = new File(path);
        FileInputStream fin = null;
        try {
            fin = new FileInputStream(file);
            byte imagebytearray[] = new byte[(int)file.length()];
            fin.read(imagebytearray);
            resim_uzantisi = Base64.getMimeEncoder().encodeToString(imagebytearray);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //Seçilen kategorisinin kategori tablosundan id'sini bulmak için yaptık
        resultSet = vt.read("Select Kategori_ID from Kategori where Kategori_isim ='"+kategori_combo.getValue()+"';");
        while (resultSet.next()) {
            kategori_id = Integer.parseInt(resultSet.getString("Kategori_ID"));
        }
        //kategori id bulundu.


       //Seçilen il ve ilçenin ilçeler tablosundan ilçe id'sini bulmak için
       resultSet = vt.read("Select id from ilceler where sehirid = (Select plaka from iller where sehiradi = '"+il_combo.getValue()+"') and ilceadi ="+"'"+ilce_combo.getValue()+"';");
        while (resultSet.next()) {
            ilce_id = Integer.parseInt(resultSet.getString("id"));
        }
        //ilçe id bulundu.

        Giris_controller gc = new Giris_controller();
        uye_id = Integer.parseInt(gc.id); //Giriş yapan kullanıcının idsi

        String sql_sorgu = "INSERT INTO Kayip_esya (Uye_ID, baslik, tarih, KategoriID, Resim_uzantisi,ilce_id) VALUES('"+uye_id+"', '"+baslikin+"', '"+tarihin+"', '"+kategori_id+"', '"+resim_uzantisi+"', '"+ilce_id+"');";

        if (baslikin == "" || tarihin == null ||resim_uzantisi == null || kategori_id == 0 ||uye_id == 0 || ilce_id == 0){
            uyari.setText("Alanlar boş bırakılamaz!");
        }
        else if (vt.crud(sql_sorgu) == 1) { //crud fonksiyonuyla veritabanına ekleme işlemi yapar.
            uyari.setText("Kaybolan Eşya Kaydedildi!");

        }
        else {
            uyari.setText("Girilen bilgiler hatalı. Farklı bilgilerle tekrar deneyin.");
        }

    }

    @FXML
    protected void anasayfa(ActionEvent e) throws IOException {
        ed.degis(anasayfa,"anasayfa.fxml", "Kaybettim - Ana Sayfa");
    }

    @FXML
    protected void profil_button(ActionEvent e) throws IOException {
        ed.degis(anasayfa,"profil.fxml", "Kaybettim - Profilim");
    }

    @FXML
    protected void kayipesyagoruntule(ActionEvent e) throws IOException {
        ed.degis(kei,"Kayip_Esyalar.fxml", "Kaybettim - Kayıp Eşyalar Kategoriler");
    }

    @FXML
    protected void bulunanesyagoruntule(ActionEvent e) throws IOException {
        ed.degis(bei,"Bulunan_Esyalar.fxml", "Kaybettim - Bulunan İlanlar Kategoriler");
    }

    @FXML
    protected void keiv_button(ActionEvent event) throws IOException {
        ed.degis(keiv,"kaybolan_esya_ekle.fxml","Kaybettim - Kaybolan Eşya Ekle");
    }

    @FXML
    protected void beiv_button(ActionEvent event) throws IOException {
        ed.degis(beiv,"bulunan_esya_ekle.fxml","Kaybettim - Bulunan Eşya Ekle");
    }

    @FXML
    protected void cikis_button(ActionEvent e) throws IOException {
        ed.degis(cikis,"giris.fxml", "Kaybettim - Giriş Yap");
    }

    @FXML
    protected void mesaj_button(ActionEvent e) throws IOException {
        ed.degis(mesaj,"mesajlar.fxml", "Kaybettim - Mesajlar");
    }

}