package com.example.alimekho.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.alimekho.Model.sanPham;
import com.example.alimekho.R;

public class DetailSanPham extends AppCompatActivity {
    TextView id, name, soLuong, ngayNhap, NSX, HSD, donGia, phanLoai, donViTinh;
    Button btnBack, btnDelete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_san_pham);

        // Toi khong phan su

    }
}