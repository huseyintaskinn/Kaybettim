package com.example.vtys;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class Uye_ol_controller {

    public static String adin, soyadin, telin, mailin, sifrein, kod_vt, dosya_fxml, value;
    private String sifre_vt, mail_vt;

    @FXML
    private Button giris_buton,uye_buton;

    @FXML
    private TextField ad, soyad, tel ,mail;

    @FXML
    private PasswordField sifre;

    @FXML
    private Label uyari;

    Veri_tabanı_baglantisi vt = new Veri_tabanı_baglantisi(); //obje oluşturduk.
    Ekran_degis ed = new Ekran_degis(); // ekran değiştirmek için sınıftan nesne ürettik.


    @FXML
    protected void girisYap(ActionEvent e) throws IOException{
        ed.degis(giris_buton,"giris.fxml","Kaybettim - Giriş Yap");
    }

    @FXML
    protected void uyeOl(ActionEvent e) throws IOException, SQLException, ClassNotFoundException {

        adin = ad.getText();
        soyadin = soyad.getText();
        telin = tel.getText();
        sifrein = sifre.getText();
        mailin = mail.getText();

        String sql_sorgu = "INSERT INTO Uyeler (Adi, Soyadi, Mail, Sifre, TelefonNo) VALUES('"+adin+"', '"+soyadin+"', '"+mailin+"', '"+sifrein+"', '"+telin+"');";


        if (adin == "" || soyadin == "" ||telin == "" || sifrein == "" ||mailin == ""){
            uyari.setText("Alanlar boş bırakılamaz!");
        }
        else if (vt.crud(sql_sorgu) == 1) { //crud fonksiyonuyla veritabanına ekleme işlemi yapar.
            uyari.setText("Hesap Oluşturuldu!");
            ad.setText("");
            soyad.setText("");
            tel.setText("");
            mail.setText("");
            sifre.setText("");
        }
        else {
            uyari.setText("Girilen bilgiler hatalı. Farklı bilgilerle tekrar deneyin.");
        }


    }
}