package com.example.vtys;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;

public class incele_controller {

    @FXML
    private Button anasayfa;

    @FXML
    private Button bei;

    @FXML
    private Button beiv;

    @FXML
    private Button cikis;

    @FXML
    private Button kei;

    @FXML
    private Button keiv;

    @FXML
    private Button mesaj;

    @FXML
    private Button profil;

    @FXML
    private Label tarih, konum, baslik, isim;

    @FXML
    private ImageView resim;

    @FXML
    private Button ilansil, mesajat;

    Ekran_degis ed = new Ekran_degis();

    public void initialize() throws SQLException {

        Anasayfa_controller a = new Anasayfa_controller();
        String sql_sorgu = "";

        if (a.getTablo_adi() == "Kayip_esya") {
            sql_sorgu = "SELECT * FROM KayipIlanBilgileri WHERE Kayip_ID = "+a.getIlan_id()+";";
        }
        else {
            sql_sorgu = "SELECT * FROM BulunanIlanBilgileri WHERE Bulunan_ID = "+a.getIlan_id()+";";
        }

        bilgiEkle(sql_sorgu);
    }

    public void bilgiEkle(String sql_sorgu){
        Veri_tabanı_baglantisi v = new Veri_tabanı_baglantisi();
        Anasayfa_controller a = new Anasayfa_controller();
        Giris_controller g = new Giris_controller();
        ResultSet rs = null;

        try {
            rs = v.read(sql_sorgu);
            rs.next();

            mesajlar_controller m = new mesajlar_controller();
            m.setKUye_ID(rs.getInt("Uye_ID"));
            m.setKullaniciadi(rs.getString("AdSoyad"));

            tarih.setText(rs.getString("tarih"));
            konum.setText(rs.getString("sehiradi") +"-"+ rs.getString("ilceadi"));
            baslik.setText(rs.getString("baslik"));
            isim.setText("İlan Sahibi: "+rs.getString("AdSoyad"));

            String ImgStr = rs.getString("Resim_uzantisi");
            ByteArrayInputStream InputStream = new ByteArrayInputStream(Base64.getMimeDecoder().decode(ImgStr.getBytes(StandardCharsets.UTF_8)));
            Image Img = new Image(InputStream);
            resim.setImage(Img);

            if (a.getUye_id().equals(g.id)) {
                ilansil.setVisible(true);
                mesajat.setVisible(false);
            }else{
                mesajat.setVisible(true);
                ilansil.setVisible(false);
            }


        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        v = null;
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

    @FXML
    protected void ilan_sil(ActionEvent e) throws IOException {
        Veri_tabanı_baglantisi v = new Veri_tabanı_baglantisi();
        Anasayfa_controller a = new Anasayfa_controller();
        try {
            if (a.getTablo_adi().equals("Kayip_esya")) {
                v.crud("DELETE FROM Kayip_esya WHERE Kayip_ID = " + a.getIlan_id());
            }else {
                v.crud("DELETE FROM Bulunan_esya WHERE Bulunan_ID = " + a.getIlan_id());
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
        ed.degis(mesaj,"anasayfa.fxml", "Kaybettim - Ana Sayfa");
    }

    @FXML
    protected void mesaj_at(ActionEvent e) throws IOException {
        ed.degis(mesaj,"mesaj.fxml", "Kaybettim - Mesajlar");
    }
}
