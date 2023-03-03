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
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class Admin_controller {
    @FXML
    private Button profil, keiv, beiv, kei, bei, mesaj, cikis,anasayfa,vt_kaydet;
    public String sql_sorgu;
    public ResultSet resultSet;

    @FXML
    private Label uye_sayisi,çblnn_esya,çkyp_esya,kyp_esya,blnn_esya,kategoriler;

    Veri_tabanı_baglantisi vt = new Veri_tabanı_baglantisi(); //obje oluşturduk.
    Ekran_degis ed = new Ekran_degis(); // ekran değiştirmek için sınıftan nesne ürettik.


    @FXML
    public void initialize () throws SQLException, ClassNotFoundException {

        sql_sorgu = "Select count (*) as uye_sayi from Uyeler";
        resultSet = vt.read(sql_sorgu);
        resultSet.next();
        uye_sayisi.setText(resultSet.getString("uye_sayi"));


        resultSet= vt.read("Select count (*) as sayi from Kayip_esya");
        resultSet.next();
        kyp_esya.setText(resultSet.getString("sayi"));

        resultSet= vt.read("Select count (*) as sayi from Bulunan_esya");
        resultSet.next();
        blnn_esya.setText(resultSet.getString("sayi"));

        sql_sorgu= "SELECT TOP 1 Kategori.Kategori_isim, COUNT(*) AS adet FROM Kayip_esya INNER JOIN Kategori ON Kayip_esya.KategoriID = Kategori.Kategori_ID GROUP BY Kategori.Kategori_isim ORDER BY adet DESC";
        resultSet= vt.read(sql_sorgu);
        resultSet.next();
        çkyp_esya.setText(resultSet.getString("Kategori_isim") + " (" + resultSet.getInt("adet") + ")");

        sql_sorgu= "SELECT TOP 1 Kategori.Kategori_isim, COUNT(*) AS adet FROM Bulunan_esya INNER JOIN Kategori ON Bulunan_esya.KategoriID = Kategori.Kategori_ID GROUP BY Kategori.Kategori_isim ORDER BY adet DESC";
        resultSet= vt.read(sql_sorgu);
        resultSet.next();
        çblnn_esya.setText(resultSet.getString("Kategori_isim") + " (" + resultSet.getInt("adet") + ")");

        sql_sorgu="SELECT tablo.Kategori_isim FROM (SELECT Kategori_isim FROM Kayip_esya INNER JOIN Kategori ON Kayip_esya.KategoriID = Kategori.Kategori_ID UNION ALL SELECT Kategori_isim FROM Bulunan_esya INNER JOIN Kategori ON Bulunan_esya.KategoriID = Kategori.Kategori_ID) AS tablo GROUP BY tablo.Kategori_isim HAVING COUNT(*) > 1";
        resultSet= vt.read(sql_sorgu);
        while (resultSet.next()){
            kategoriler.setText(kategoriler.getText()+"\n"+resultSet.getString(1));
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