package com.example.alimekho.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.alimekho.DataBase.SQLServerConnection;
import com.example.alimekho.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LoginActivity extends AppCompatActivity {

    EditText edtUserName, edtPass;
    Button btnLogin;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtUserName = findViewById(R.id.edtUserName);
        edtPass = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnDangNhap);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userName = edtUserName.getText().toString().trim();
                String pass = edtPass.getText().toString().trim();
                if(userName.isEmpty() || pass.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Vui long nhap ten va mat khau", Toast.LENGTH_LONG).show();
                }
                else {
                        SQLServerConnection db = new SQLServerConnection();
                        Connection conn = db.getConnection();
                        try {
                            Statement stm = conn.createStatement();
                            String checkLoginQuery = "select * from account, employee where user_name='" + userName + "' and password= '" + pass + "'" +
                                    " and account.emp_id = employee.id";
                            ResultSet rs = stm.executeQuery(checkLoginQuery);
                            if (rs.next()) {
                                SharedPreferences sharedPref = getSharedPreferences("user info", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putString("id", rs.getString("emp_id"));
                                editor.putString("name", rs.getString("name"));
                                ///more...
                                editor.apply();


                                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(getApplicationContext(), "Sai ten dang nhap hoac mat khau", Toast.LENGTH_LONG).show();
                            }

                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                }

            }
        });
    }
}

