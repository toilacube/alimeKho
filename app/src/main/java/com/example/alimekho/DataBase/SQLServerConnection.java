package com.example.alimekho.DataBase;

import android.os.StrictMode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLServerConnection {
    private String DB_URL = "jdbc:jtds:sqlserver://192.168.1.39:1433/AlimeKho";
    private String DB_USERNAME = "alime";
    private String DB_PASSWORD = "1";
   public SQLServerConnection(){
       StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
       StrictMode.setThreadPolicy(policy);
   }

   public Connection getConnection(){
       Connection connection = null;
       try{ // all SQL relevants must be put in try-catch
           Class.forName("net.sourceforge.jtds.jdbc.Driver");
           connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
       }
       catch (SQLException e){
           e.printStackTrace();
       } catch (ClassNotFoundException e) {
           e.printStackTrace();
       }
       return connection;
   }

}
