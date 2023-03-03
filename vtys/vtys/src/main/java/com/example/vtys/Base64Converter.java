package com.example.vtys;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;

public class Base64Converter {
    public static void main(String[] args){
        try {
            File f = new File("C:\\Users\\husey\\Desktop\\Veritabanı Yönetim Sistemleri\\Yeni klasör\\vtys\\vtys\\src\\main\\resources\\com\\example\\vtys\\icon.png");
            FileInputStream fin = new FileInputStream(f);
            byte imagebytearray[] = new byte[(int)f.length()];
            fin.read(imagebytearray);
            String imagetobase64 = Base64.getMimeEncoder().encodeToString(imagebytearray);
            System.out.println(imagetobase64);
            fin.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
