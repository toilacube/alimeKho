package com.example.alimekho.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alimekho.Adapter.PXK3Adapter;
import com.example.alimekho.Model.CTPXK;
import com.example.alimekho.Model.phieuXuatKho;
import com.example.alimekho.R;

import java.util.ArrayList;

public class CreatePXK3Activity extends AppCompatActivity {
    private Button btnBackHome, btnComplete, btnBack;
    private TextView txtmaPhieu, txttenCHX, txtngayNhap, txtnhanVien, txttoTal;
    private RecyclerView recyclerView;
    private ArrayList<CTPXK> ctpxks;
    private com.example.alimekho.Model.phieuXuatKho phieuXuatKho;
    private PXK3Adapter pxk3Adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_pxk3);
        Init();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        pxk3Adapter = new PXK3Adapter(this, ctpxks);
        recyclerView.setAdapter(pxk3Adapter);
        setData();
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CreatePXK3Activity.this, "Hoàn thành", Toast.LENGTH_SHORT).show();
            }
        });
        btnBackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    public void setData(){
        txtmaPhieu.setText("Mã phiếu: " + phieuXuatKho.getMaPhieu().toString().trim());
        txtnhanVien.setText("Người phụ trách: " + phieuXuatKho  .getMaNV().toString().trim());
        txttenCHX.setText("Cửa hàng xuất: " + phieuXuatKho.getTenCuaHangXuat().toString().trim());
        txttoTal.setText("Thành tiền: " + Integer.toString(phieuXuatKho.getTotalMoney()));
        txtngayNhap.setText("Ngày xuất kho: " + phieuXuatKho.getNgayXuatKho().toString().trim());
    }
    public void Init(){
        btnBackHome = findViewById(R.id.btn_back_createPXK3);
        btnComplete = findViewById(R.id.gdcreatePXK3_btnComplete);
        btnBack = findViewById(R.id.gdcreatePXK3_btnBack);
        recyclerView = findViewById(R.id.gdcreatePXK3_rcv);
        txtmaPhieu = findViewById(R.id.gdcreatePXK3_txtmaPhieu);
        txtngayNhap = findViewById(R.id.gdcreatePXK3_txtngayNhap);
        txttenCHX= findViewById(R.id.gdcreatePXK3_txtCHX);
        txtnhanVien = findViewById(R.id.gdcreatePXK3_txtnguoiPT);
        txttoTal = findViewById(R.id.gdcreatePXK3_txtthanhTien);
        ctpxks = new ArrayList<>();
        phieuXuatKho = new phieuXuatKho("NH122", "23/09/2022", "PM Milk. Co", "Nguyễn Văn A", 12000000);
    }
}