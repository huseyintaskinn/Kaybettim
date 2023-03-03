package com.example.vtys;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;


public class Ekran_degis {

    public void degis(Button button_ismi, String dosya_ismi, String tittle)throws IOException{
        button_ismi.getScene().getWindow().hide();
        Stage button_ismi_2 = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource(dosya_ismi));  //ana menüye gider
        Scene scene = new Scene(root);
        button_ismi_2.setResizable(false);
        button_ismi_2.setScene(scene);
        button_ismi_2.setTitle(tittle);
        button_ismi_2.getIcons().add(new Image(getClass().getResource("/com/example/vtys/icon.png").toExternalForm()));
        button_ismi_2.show();
    }

}
