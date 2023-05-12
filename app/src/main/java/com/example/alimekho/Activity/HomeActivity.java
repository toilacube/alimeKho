package com.example.alimekho.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.alimekho.Model.Employee;
import com.example.alimekho.R;

public class HomeActivity extends AppCompatActivity {
    CardView cvSanPham, cvNhapKho, cvXuatKho, cvNhanVien;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        cvSanPham = findViewById(R.id.cv_san_pham);
        cvNhanVien = findViewById(R.id.cv_nhan_vien);
        cvNhapKho = findViewById(R.id.cv_nhap_kho);
        cvXuatKho = findViewById(R.id.cv_xuat_kho);

        toActivity();
    }

    private void toActivity() {
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
                Intent intent = new Intent(getApplicationContext(), CreatePNK1Activity.class);
                startActivity(intent);
            }
        });

        cvXuatKho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CreatePXK1Activity.class);
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