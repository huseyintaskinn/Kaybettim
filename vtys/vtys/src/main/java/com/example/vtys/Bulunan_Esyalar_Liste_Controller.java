package com.example.vtys;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;

public class Bulunan_Esyalar_Liste_Controller {
    @FXML
    private Button bei,beiv,cikis,kategoriler,anasayfa, kei,keiv,ktumu,ktumu1,ktumu11,mesaj,profil;

    @FXML
    private VBox ilanlar;

    private static String ilan_id = "0", uye_id = "0";

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

    Ekran_degis ed = new Ekran_degis();

    public void initialize() throws SQLException {
        Bulunan_Esyalar_Controller b = new Bulunan_Esyalar_Controller();
        String sql_sorgu = "EXEC BulunanKategoriFiltrele @KategoriID = "+b.getKategori_id() +";";
        ilanEkle(sql_sorgu);
    }

    public void ilanEkle(String sql_sorgu) {
        Veri_tabanı_baglantisi v = new Veri_tabanı_baglantisi();
        ResultSet rs = null;

        try {
            rs = v.read(sql_sorgu);
            HBox satir = new HBox();

            while (rs.next()) {
                if (satir.getChildren().size() == 5) {
                    ilanlar.getChildren().add(satir);
                    satir = new HBox();
                }

                VBox vbox = new VBox();
                vbox.setPrefWidth(150);
                vbox.setPrefHeight(150);
                vbox.getStylesheets().add(getClass().getResource("/com/example/vtys/stil.css").toExternalForm());
                vbox.getStyleClass().add("kutu");

                Label tarih = new Label(rs.getString("tarih"));
                tarih.getStylesheets().add(getClass().getResource("/com/example/vtys/stil.css").toExternalForm());
                tarih.getStyleClass().add("tarihlabel");

                String ImgStr = rs.getString("Resim_uzantisi");
                ByteArrayInputStream InputStream = new ByteArrayInputStream(Base64.getMimeDecoder().decode(ImgStr));
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
                            Anasayfa_controller a = new Anasayfa_controller();
                            a.setUye_id(ayir[1]);
                            a.setIlan_id(ayir[0]);
                            a.setTablo_adi("Bulunan_esya");
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

                satir.getChildren().add(vbox);
            }

            if (satir.getChildren().size() != 0) {
                ilanlar.getChildren().add(satir);
                satir = new HBox();
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
}

