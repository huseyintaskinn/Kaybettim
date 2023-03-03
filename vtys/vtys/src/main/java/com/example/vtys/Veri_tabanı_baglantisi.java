package com.example.vtys;

import java.sql.*;

public class Veri_tabanı_baglantisi {



    public Connection baglan() throws  SQLException, ClassNotFoundException{
        //String connectionUrl = "jdbc:sqlserver://YAR-BILG:1433;database=KaybolanEsya;user=b;password=1234567;encrypt=true;trustServerCertificate=true;";
        String connectionUrl = "jdbc:sqlserver://LAPTOP-GMQFMP6P:1433;database=KaybolanEsya;user=kaybettim;password=Bote2022*;encrypt=true;trustServerCertificate=true;";
        Connection con= DriverManager.getConnection(connectionUrl);
        return con;
    }

    public ResultSet read(String sql_sorgu) throws  SQLException, ClassNotFoundException {
        Veri_tabanı_baglantisi vt = new Veri_tabanı_baglantisi();
        Statement s = vt.baglan().createStatement();
        ResultSet rs = s.executeQuery(sql_sorgu);
        return rs;
    }

    public int crud(String sql_sorgu) throws  SQLException, ClassNotFoundException {
        Veri_tabanı_baglantisi vt = new Veri_tabanı_baglantisi();
        try (Statement statement = vt.baglan().createStatement()) {

            Statement s = vt.baglan().createStatement();
            if (s.executeUpdate(sql_sorgu) == 1)
                return 1;
        } catch (SQLException ex) {
        }
        return 0;
    }


    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        Veri_tabanı_baglantisi vt = new Veri_tabanı_baglantisi();
        ResultSet result = vt.read("SELECT * FROM Uyeler");

        while (result.next()) {
            System.out.println("Şehirler: " + result.getString("mail"));
        }

    }

}
