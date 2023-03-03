package com.example.vtys;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.io.IOException;
import java.sql.*;


public class Giris_controller {

    public static String mailin, sifrein, kod_vt, id;
    private String sifre_vt, mail_vt;
    ResultSet resultSet = null;

    @FXML
    private Button giris_buton,uye_buton;

    @FXML
    private TextField mail;

    @FXML
    private PasswordField sifre;

    @FXML
    private Label uyari;

    Veri_tabanı_baglantisi vt = new Veri_tabanı_baglantisi(); //obje oluşturduk.
    Ekran_degis ed = new Ekran_degis(); // ekran değiştirmek için sınıftan nesne ürettik.


    @FXML
    protected void uyeOlustur(ActionEvent e) throws IOException{
        ed.degis(uye_buton,"Uye_ol.fxml","Kaybettim - Üye Ol");
    }

    @FXML
    protected void girisYap(ActionEvent e) throws IOException, SQLException, ClassNotFoundException {

        sifrein = sifre.getText();
        mailin = mail.getText();

        resultSet = vt.read("Select * from Uyeler");


        while (resultSet.next()) {
            sifre_vt = resultSet.getString("Sifre");
            mail_vt = resultSet.getString("Mail");

            if (sifrein.equals(sifre_vt) && mailin.equals(mail_vt)) {
                id = resultSet.getString("Uye_ID");
                ed.degis(giris_buton,"anasayfa.fxml","Kaybettim - Giriş Yap");
            }
            else {
                uyari.setText("Mailiniz veya şifreniz hatalı");
                mail.setText("");
                sifre.setText("");
            }
        }

    }
}