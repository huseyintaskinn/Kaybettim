package com.example.vtys;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.event.ActionEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import java.io.File;
import java.sql.*;

public class Anasayfa_controller {

    //Navigasyondaki butonlar
    @FXML
    private Button anasayfa, profil, keiv, beiv, kei, bei, mesaj, cikis, istatistik;

    //Ana sayfadaki butonlar
    @FXML
    private Button btumu, ktumu;

    @FXML
    private HBox khbox, bhbox;

    Ekran_degis ed = new Ekran_degis(); // ekran değiştirmek için sınıftan nesne ürettik.

    private static String ilan_id = "0", uye_id = "0", tablo_adi = "";

    public String getIlan_id() {
        return this.ilan_id;
    }

    // Setter
    public void setIlan_id(String ilan_id) {
        this.ilan_id = ilan_id;
    }

    public String getUye_id() {
        return this.uye_id;
    }

        // Setter
    public void setUye_id(String uye_id) {
        this.uye_id = uye_id;
    }

    public String getTablo_adi() {
        return this.tablo_adi;
    }

    // Setter
    public void setTablo_adi(String tablo_adi) {
        this.tablo_adi = tablo_adi;
    }

    public void initialize() throws SQLException {

        Giris_controller g = new Giris_controller();

        if (g.id.equals("1")) {
            istatistik.setVisible(true);
        } else{
            istatistik.setVisible(false);
        }

        String sql_sorgu = "SELECT Kayip_ID, Uye_ID, tarih, Resim_uzantisi, baslik, sehiradi, ilceadi FROM KayipIlanBilgileri";
        String sql_sorgu2 = "SELECT Bulunan_ID, Uye_ID, tarih, Resim_uzantisi, baslik, sehiradi, ilceadi FROM BulunanIlanBilgileri";

        kutuEkle(sql_sorgu, "Kayip_esya");
        kutuEkle(sql_sorgu2,"Bulunan_esya");
    }

    public void kutuEkle(String sql_sorgu, String name){
        Veri_tabanı_baglantisi v = new Veri_tabanı_baglantisi();
        ResultSet rs = null;

        try {
            rs = v.read(sql_sorgu);

            while (rs.next()) {
                VBox vbox = new VBox();
                vbox.setPrefWidth(150);
                vbox.setPrefHeight(150);
                vbox.getStylesheets().add(getClass().getResource("/com/example/vtys/stil.css").toExternalForm());
                vbox.getStyleClass().add("kutu");

                Label tarih = new Label(rs.getString("tarih"));
                tarih.getStylesheets().add(getClass().getResource("/com/example/vtys/stil.css").toExternalForm());
                tarih.getStyleClass().add("tarihlabel");

                String ImgStr = rs.getString("Resim_uzantisi");
                ByteArrayInputStream InputStream = new ByteArrayInputStream(Base64.getMimeDecoder().decode(ImgStr.getBytes(StandardCharsets.UTF_8)));
                Image Img = new Image(InputStream);
                ImageView imageView = new ImageView(Img);
                imageView.setFitWidth(150);
                imageView.setFitHeight(70);
                imageView.setPreserveRatio(false);
                imageView.setStyle("-fx-padding: 5px;-fx-border-insets: 5px; -fx-background-insets: 5px;");

                Label baslik = new Label(rs.getString("baslik"));
                baslik.getStylesheets().add(getClass().getResource("/com/example/vtys/stil.css").toExternalForm());
                baslik.getStyleClass().add("basliklabel");

                Label konum = new Label(rs.getString("sehiradi") +"-"+ rs.getString("ilceadi"));
                konum.getStylesheets().add(getClass().getResource("/com/example/vtys/stil.css").toExternalForm());
                konum.getStyleClass().add("konumlabel");

                Button incele = new Button("İncele");
                incele.getStylesheets().add(getClass().getResource("/com/example/vtys/stil.css").toExternalForm());
                incele.getStyleClass().add("inceleb");
                incele.setMinSize(150,35);
                incele.setId(String.valueOf(rs.getInt(1))+","+String.valueOf(rs.getInt(2))+","+rs.getString("baslik"));
                incele.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        try {
                            String[] ayir = incele.getId().split(",");
                            setIlan_id(ayir[0]);
                            setUye_id(ayir[1]);
                            setTablo_adi(name);
                            ed.degis(incele,"incele.fxml", "Kaybettim - " + ayir[2]);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });

                vbox.getChildren().add(tarih);
                vbox.getChildren().add(imageView);
                vbox.getChildren().add(baslik);
                vbox.getChildren().add(konum);
                vbox.getChildren().add(incele);

                if (name == "Kayip_esya"){
                    khbox.getChildren().add(vbox);
                }else {
                    bhbox.getChildren().add(vbox);
                }
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        v = null;
    }

    @FXML
    protected void anasayfayagit(ActionEvent e) throws IOException {
        ed.degis(anasayfa,"anasayfa.fxml", "Kaybettim - Ana Sayfa");
    }

    @FXML
    protected void profil_buton(ActionEvent e) throws IOException {
        ed.degis(anasayfa,"profil.fxml", "Kaybettim - Profilim");
    }

    @FXML
    protected void kayipesyagoruntule(ActionEvent e) throws IOException {
        ed.degis(ktumu,"Kayip_Esyalar.fxml", "Kaybettim - Kayıp Eşyalar Kategoriler");
    }

    @FXML
    protected void bulunanesyagoruntule(ActionEvent e) throws IOException {
        ed.degis(btumu,"Bulunan_Esyalar.fxml", "Kaybettim - Bulunan İlanlar Kategoriler");
    }

    @FXML
    protected void keiv_buton(ActionEvent event) throws IOException {
        ed.degis(keiv,"kaybolan_esya_ekle.fxml","Kaybettim - Kaybolan Eşya Ekle");
    }

    @FXML
    protected void beiv_buton(ActionEvent event) throws IOException {
        ed.degis(beiv,"bulunan_esya_ekle.fxml","Kaybettim - Bulunan Eşya Ekle");
    }

    @FXML
    protected void cikisyap(ActionEvent e) throws IOException {
        ed.degis(cikis,"giris.fxml", "Kaybettim - Giriş Yap");
    }

    @FXML
    protected void mesajlar_buton(ActionEvent e) throws IOException {
        ed.degis(mesaj,"mesajlar.fxml", "Kaybettim - Mesajlar");
    }

    @FXML
    protected void istatistik_buton(ActionEvent e) throws IOException {
        ed.degis(istatistik,"admin.fxml", "Kaybettim - Admin");
    }
}
