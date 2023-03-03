package com.example.vtys;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;

public class mesajlar_controller {

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
    private VBox mesajlistesi;

    private static int KUye_ID = 0;
    private static String kullaniciadi = "";

    public int getKUye_ID() {
        return this.KUye_ID;
    }

    public void setKUye_ID(int KUye_ID) {
        this.KUye_ID = KUye_ID;
    }

    public String getKullaniciadi() {
        return this.kullaniciadi;
    }

    public void setKullaniciadi(String kullaniciadi) {
        this.kullaniciadi = kullaniciadi;
    }

    Ekran_degis ed = new Ekran_degis();

    public void initialize() throws SQLException {

        Giris_controller g = new Giris_controller();
        String sql_sorgu = "(SELECT Alici_ID FROM Mesaj WHERE Gonderici_ID = "+ g.id +") UNION (SELECT Gonderici_ID FROM Mesaj WHERE Alici_ID = "+ g.id +")";

        mesajListele(sql_sorgu);
    }

    public void mesajListele(String sql_sorgu){
        Veri_tabanı_baglantisi v = new Veri_tabanı_baglantisi();
        Giris_controller g = new Giris_controller();
        ResultSet rs = null;
        ResultSet rs2 = null;
        try {
            rs = v.read(sql_sorgu);
            while (rs.next()){
                VBox vbox = new VBox();
                vbox.getStylesheets().add(getClass().getResource("/com/example/vtys/stil.css").toExternalForm());
                vbox.getStyleClass().add("mesajkutusu");

                rs2 = v.read("SELECT Adi + ' ' +Soyadi AS AdSoyad FROM Uyeler WHERE Uye_ID = " + rs.getInt("Alici_ID"));
                rs2.next();
                Label ad = new Label();
                ad.setText(rs2.getString("AdSoyad"));
                ad.getStylesheets().add(getClass().getResource("/com/example/vtys/stil.css").toExternalForm());
                ad.getStyleClass().add("ad");


                rs2 = v.read("SELECT TOP 1 * FROM Mesaj WHERE (Gonderici_ID = "+ g.id +" AND Alici_ID = "+rs.getInt("Alici_ID")+") OR (Gonderici_ID = "+rs.getInt("Alici_ID")+" AND Alici_ID = "+ g.id +") ORDER BY tarih DESC");
                rs2.next();
                Button mesajmetni = new Button();
                mesajmetni.setText("Son Mesaj: " + rs2.getString("mesaj") + " >>");
                mesajmetni.getStylesheets().add(getClass().getResource("/com/example/vtys/stil.css").toExternalForm());
                mesajmetni.getStyleClass().add("mesajmetin");
                mesajmetni.setId(String.valueOf(rs.getInt("Alici_ID"))+","+ad.getText());
                mesajmetni.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        try {
                            String[] ayir = mesajmetni.getId().split(",");
                            setKUye_ID(Integer.valueOf(ayir[0]));
                            setKullaniciadi(ayir[1]);
                            ed.degis(mesajmetni,"mesaj.fxml", "Kaybettim - " + ayir[1] + " Mesajlar");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });

                vbox.getChildren().add(ad);
                vbox.getChildren().add(mesajmetni);

                mesajlistesi.getChildren().add(vbox);
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
