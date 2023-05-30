package com.example.alimekho.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.alimekho.Model.Employee;
import com.example.alimekho.R;

public class HomeActivity extends AppCompatActivity {
    CardView cvSanPham, cvNhapKho, cvXuatKho, cvNhanVien,cvBatch, cvOutLocation, cvKiemKho, cvNhaCC
            , cvViTri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        cvNhaCC = findViewById(R.id.cvSupplier);
        cvKiemKho = findViewById(R.id.cvKiemKho);
        cvOutLocation = findViewById(R.id.cvDiaDiemXuat);
        cvBatch = findViewById(R.id.cvBatch);
        cvSanPham = findViewById(R.id.cv_san_pham);
        cvNhanVien = findViewById(R.id.cv_nhan_vien);
        cvNhapKho = findViewById(R.id.cv_nhap_kho);
        cvXuatKho = findViewById(R.id.cv_xuat_kho);
        cvViTri = findViewById(R.id.cv_khu_vuc_kho);

        toActivity();
    }

    private void toActivity() {
        cvNhaCC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SupplierActivity.class);
                startActivity(intent);
            }
        });
        cvKiemKho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), QLPKKActivity.class);
                startActivity(intent);
            }
        });
        cvOutLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SupermarketActivity.class);
                startActivity(intent);
            }
        });
        cvBatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BatchActivity.class);
                startActivity(intent);
            }
        });
        cvViTri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AreaActivity.class);
                startActivity(intent);
            }
        });
        cvSanPham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SanPhamActivity.class);
                startActivity(intent);
            }
        });

        cvNhapKho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), QLPNKActivity.class);
                startActivity(intent);
            }
        });

        cvXuatKho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), QLPXKActivity.class);
                startActivity(intent);
            }
        });

        cvNhanVien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EmployeeActivity.class);
                startActivity(intent);
            }
        });
    }
}