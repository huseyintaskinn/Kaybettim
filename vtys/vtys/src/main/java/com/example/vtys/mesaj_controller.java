package com.example.vtys;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class mesaj_controller {

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
    private VBox mesajvbox;

    @FXML
    private Button profil, gonder;

    @FXML
    private TextField input_mesaj;

    @FXML
    private Label kullaniciadi;

    mesajlar_controller m = new mesajlar_controller();
    Giris_controller g = new Giris_controller();

    public void initialize() throws SQLException {

        kullaniciadi.setText(m.getKullaniciadi());
        Giris_controller g = new Giris_controller();
        String sql_sorgu = "SELECT FORMAT( tarih, 'dd.MM.yyyy', 'tr-TR') AS gun, FORMAT( tarih, 'HH:MM', 'tr-TR') AS saat, * FROM Mesaj WHERE (Gonderici_ID = "+g.id+" AND Alici_ID = "+ m.getKUye_ID() +") OR (Gonderici_ID = "+ m.getKUye_ID() + " AND Alici_ID = "+ g.id +") ORDER BY tarih";

        mesajListele(sql_sorgu);
    }

    public void mesajListele(String sql_sorgu){
        Veri_tabanı_baglantisi v = new Veri_tabanı_baglantisi();
        Giris_controller g = new Giris_controller();
        ResultSet rs = null;

        String oncekitarih = "";
        try {
            rs = v.read(sql_sorgu);
            while (rs.next()){
                VBox vbox = new VBox();
                vbox.getStylesheets().add(getClass().getResource("/com/example/vtys/stil.css").toExternalForm());
                if (m.getKUye_ID() == rs.getInt("Gonderici_ID")){
                    vbox.getStyleClass().add("mesajblogu");
                }
                else{
                    vbox.getStyleClass().add("mesajblogu2");
                }

                if (!oncekitarih.equals(rs.getString("gun"))){

                    Label gun = new Label();
                    gun.setText(rs.getString("gun"));
                    gun.getStylesheets().add(getClass().getResource("/com/example/vtys/stil.css").toExternalForm());
                    gun.getStyleClass().add("gun");

                    mesajvbox.getChildren().add(gun);

                    oncekitarih = rs.getString("gun");
                }

                Label mesajmetni = new Label();
                mesajmetni.setText(rs.getString("mesaj"));
                mesajmetni.getStylesheets().add(getClass().getResource("/com/example/vtys/stil.css").toExternalForm());
                mesajmetni.getStyleClass().add("mesajmetni");

                Label saat = new Label();
                saat.setText(rs.getString("saat"));
                saat.getStylesheets().add(getClass().getResource("/com/example/vtys/stil.css").toExternalForm());
                saat.getStyleClass().add("saat");

                vbox.getChildren().add(mesajmetni);
                vbox.getChildren().add(saat);

                mesajvbox.getChildren().add(vbox);
            }

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        v = null;
    }


    @FXML
    void mesajkaydet(ActionEvent event) {
        String sql_sorgu = "INSERT INTO Mesaj (Gonderici_ID, Alici_ID ,mesaj) VALUES ("+ g.id +", "+m.getKUye_ID() +" ,'"+input_mesaj.getText()+"' )";
        Veri_tabanı_baglantisi v = new Veri_tabanı_baglantisi();

        try {
            if (v.crud(sql_sorgu) == 1) {
                input_mesaj.setText("");
                mesajvbox.getChildren().clear();
                initialize();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    Ekran_degis ed = new Ekran_degis();
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
