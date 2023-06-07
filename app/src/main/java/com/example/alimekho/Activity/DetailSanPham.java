package com.example.alimekho.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.alimekho.DataBase.SQLServerConnection;
import com.example.alimekho.Model.sanPham;
import com.example.alimekho.R;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DetailSanPham extends AppCompatActivity {
    TextView id, name , donGia, phanLoai, donViTinh, nhaCC;
    Button btnBack, btnDelete;
    SQLServerConnection db = new SQLServerConnection();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_san_pham);

        sanPham sp = (sanPham) getIntent().getSerializableExtra("SP");
        id = findViewById(R.id.txvMaSP);
        name = findViewById(R.id. txvTenSP);
        phanLoai = findViewById(R.id.txvLoaiSP);
        donViTinh = findViewById(R.id.txvDonViTinh);
        nhaCC = findViewById(R.id.txvNhaCC);
        btnBack = findViewById(R.id.btn_back_san_pham);

        id.setText(sp.getMaSP());
        name.setText(sp.getTenSP());
        donViTinh.setText(sp.getDonViTinh());
       // donGia.setText(Double.toString(sp.getDonGia()));

        Statement stm1 = null;
        try {
            stm1 = db.getConnection().createStatement();
            String type = sp.getPhanLoai();
            String getTypeQuery = "SELECT name FROM product_type where id = " + type;
            ResultSet rsType = stm1.executeQuery(getTypeQuery);

            if(rsType.next())
                phanLoai.setText(rsType.getString(1));

            String supplier = sp.getSupplier_id();
            String getSupplierQuery = "select name from supplier where id = " + supplier;
            ResultSet rsSupplier = stm1.executeQuery(getSupplierQuery);
            if(rsSupplier.next())
                nhaCC.setText(rsSupplier.getString(1));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }



        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SanPhamActivity.class);
                startActivity(intent);
            }
        });

//        btnDelete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                AlertDialog.Builder builder=new AlertDialog.Builder(view.getRootView().getContext());
//                View dialogView= LayoutInflater.from(view.getRootView().getContext()).inflate(R.layout.confirm_delete,null);
//                builder.setView(dialogView);
//                builder.show();
//            }
//        });

    }
}