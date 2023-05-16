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

        sanPham sp = (sanPham) getIntent().getSerializableExtra("SP");
        id = findViewById(R.id.txvMaSP);
        name = findViewById(R.id.tv_name);
        donGia = findViewById(R.id.txvDonGia);
        phanLoai = findViewById(R.id.txvLoaiSP);
        donViTinh = findViewById(R.id.txvDonViTinh);
        btnBack = findViewById(R.id.btn_back_san_pham);

        id.setText(sp.getMaSP());
        name.setText(sp.getTenSP());
        soLuong.setText(Integer.toString(sp.getSoLuong()));
        donGia.setText(Double.toString(sp.getDonGia()));
        NSX.setText(sp.getNSX());
        HSD.setText(sp.getHSD());
        phanLoai.setText(sp.getPhanLoai());
        donViTinh.setText(sp.getDonViTinh());

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SanPhamActivity.class);
                startActivity(intent);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(view.getRootView().getContext());
                View dialogView= LayoutInflater.from(view.getRootView().getContext()).inflate(R.layout.confirm_delete,null);
                builder.setView(dialogView);
                builder.show();
            }
        });


    }
}