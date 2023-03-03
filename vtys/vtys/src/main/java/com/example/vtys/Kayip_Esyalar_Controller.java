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

public class Kayip_Esyalar_Controller {
    @FXML
    private Button bei,beiv,cikis,kei,keiv,ktumu,ktumu1,ktumu11,mesaj,profil, anasayfa;

    @FXML
    private VBox kategoriler;

    private static String kategori_id = "0";

    public String getKategori_id() {
        return this.kategori_id;
    }

    // Setter
    public void setKategori_id(String kategori_id) {
        this.kategori_id = kategori_id;
    }

    Ekran_degis ed = new Ekran_degis(); // ekran değiştirmek için sınıftan nesne ürettik.

    public void initialize() throws SQLException {
        String sql_sorgu = "SELECT * FROM Kategori";
        kategoriEkle(sql_sorgu);
    }

    public void kategoriEkle(String sql_sorgu) {
        Veri_tabanı_baglantisi v = new Veri_tabanı_baglantisi();
        ResultSet rs = null;

        try {
            rs = v.read(sql_sorgu);
            HBox satir = new HBox();

            while (rs.next()) {
                if (satir.getChildren().size() == 5) {
                    kategoriler.getChildren().add(satir);
                    satir = new HBox();
                }
                
                Button buton = new Button(rs.getString("Kategori_isim"));
                buton.getStylesheets().add(getClass().getResource("/com/example/vtys/stil.css").toExternalForm());
                buton.getStyleClass().add("kategoriba");
                buton.setMinSize(150, 150);
                buton.setId(String.valueOf(rs.getInt("Kategori_ID")));
                satir.getChildren().add(buton);
                buton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        try {
                            setKategori_id(buton.getId());
                            ed.degis(buton,"Kayip_Esyalar_Liste.fxml", "Kaybettim - " + buton.getText() + " Kategorisinde Kaybolan Eşyalar");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });

            }

            if (satir.getChildren().size() != 0) {
                kategoriler.getChildren().add(satir);
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

