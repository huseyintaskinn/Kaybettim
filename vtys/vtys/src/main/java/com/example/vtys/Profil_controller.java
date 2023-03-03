package com.example.vtys;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;


public class Profil_controller {

    public static String  kod_vt, value,baslikin,resim_uzantisi,path;
    public LocalDate tarihin;
    public String sql_sorgu,sql_sorgu_u,sql_sorgu_r;
    public int ilce_id=0,uye_id=0,kategori_id=0,belirtec=0;
    public String secilen , imagetobase64;
    ResultSet resultSet =null;
    List<String> lstFile;
    @FXML
    private TextField sifre,soyad,telno,mail,ad;

    @FXML
    private TextArea adres;

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


        //comboboxtaki illeri getirmek için.
        resultSet = vt.read("Select sehiradi from iller");
        while (resultSet.next()) {
            il_list.add(resultSet.getString("sehiradi")); //combobaxa eklemek için observable liste veritabanından gelen verileri ekledik.
        }
        il_combo.getItems().addAll(il_list); //veritabanından alınan iller comboboxa eklenir.


        Giris_controller gc = new Giris_controller();
        uye_id= Integer.parseInt(gc.id);


        resultSet = vt.read("Select * from Uyeler where Uye_ID='"+uye_id+"';");
        while (resultSet.next()) {
            ad.setText(resultSet.getString("Adi")); //combobaxa eklemek için observable liste veritabanından gelen verileri ekledik.
            soyad.setText(resultSet.getString("Soyadi"));
            mail.setText(resultSet.getString("Mail"));
            sifre.setText(resultSet.getString("Sifre"));
            telno.setText(resultSet.getString("TelefonNo"));
            String ImgStr = resultSet.getString("Resim_url");
            imagetobase64 = ImgStr;
            ByteArrayInputStream InputStream = new ByteArrayInputStream(Base64.getMimeDecoder().decode(ImgStr.getBytes(StandardCharsets.UTF_8)));
            Image Img = new Image(InputStream);
            imageView.setImage(Img);
        }

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
            File file = new File(path);
            FileInputStream fin = null;
            try {
                fin = new FileInputStream(file);
                byte imagebytearray[] = new byte[(int)file.length()];
                fin.read(imagebytearray);
                imagetobase64 = Base64.getMimeEncoder().encodeToString(imagebytearray);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            //System.out.println("Selected File::  " + f.getAbsolutePath());
            Image image = new Image(f.toURI().toString(), 200,200, true, true);//path, PrefWidth, PrefHeight, PreserveRat

            imageView.setImage(image);
        }

    }
    @FXML
    void vt_kaydet_action(ActionEvent event) throws SQLException, ClassNotFoundException {

        Giris_controller gc = new Giris_controller();
        uye_id= Integer.parseInt(gc.id);

        if(il_combo.getValue() !=  null && ilce_combo.getValue()!=null) {
            resultSet = vt.read("Select id from ilceler where sehirid = (Select plaka from iller where sehiradi = '" + il_combo.getValue() + "') and ilceadi =" + "'" + ilce_combo.getValue() + "';");
            while (resultSet.next()) {
                ilce_id = Integer.parseInt(resultSet.getString("id"));
            }
            //ilçe id bulundu.

        }


        sql_sorgu = "UPDATE Uyeler SET Adi = '"+ ad.getText() +"',"+ "Soyadi='" + soyad.getText() + "'," + "Mail='" + mail.getText() + "'," + "Sifre='" + sifre.getText() + "'," + "TelefonNo='" + telno.getText() + "' WHERE Uye_ID =" + uye_id + ";";

        if (ad.getText() == "" || soyad.getText() == "" ||mail.getText() == "" || sifre.getText() == "" || telno.getText() == null ){
            uyari.setText("Alanlar boş bırakılamaz!");
        }
        else if (vt.crud(sql_sorgu) == 1) { //crud fonksiyonuyla veritabanında güncelleme işlemi yapar.
            uyari.setText("Bilgiler güncellendi.");

        }
        else {
            uyari.setText("Girilen bilgiler hatalı. Farklı bilgilerle tekrar deneyin.");
        }


        //Adres Tablosu
        sql_sorgu = "UPDATE Adres SET ilce_ID = " +ilce_id + "," + "Detay='" + adres.getText() + "' WHERE Uye_ID =" + uye_id + ";";

        if (adres.getText() == null ) {
            uyari.setText("Alanlar boş bırakılamaz!");
        } else if (vt.crud(sql_sorgu) == 1) { //crud fonksiyonuyla veritabanına ekleme işlemi yapar.
            uyari.setText("Bilgiler Güncellendi");

        } else { // Eğer güncellenmiyorsaaaaaaa

            sql_sorgu = "INSERT INTO Adres (Uye_ID, ilce_ID, Detay) VALUES(" + uye_id + "," + ilce_id + ", '" + adres.getText() + "');";

            if (adres.getText() == null) {
                uyari.setText("Alanlar boş bırakılamaz!");
            }
            else if (vt.crud(sql_sorgu) == 1) { //crud fonksiyonuyla veritabanına ekleme işlemi yapar.
                uyari.setText("Adres Kaydedildi.!");

            }
            else {
                uyari.setText("Girilen bilgiler hatalı. Farklı bilgilerle tekrar deneyin.");
            }
        }

        sql_sorgu = "UPDATE Uyeler SET Resim_url = '"+imagetobase64+"' WHERE Uye_ID = " +  uye_id+ ";";
        vt.crud(sql_sorgu);
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