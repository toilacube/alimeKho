package com.example.alimekho.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alimekho.Adapter.PNK1Adapter;
import com.example.alimekho.Model.CTPNK;
import com.example.alimekho.Model.sanPham;
import com.example.alimekho.Model.phieuNhapKho;
import com.example.alimekho.R;

import java.util.ArrayList;

public class CreatePNK3Activity extends AppCompatActivity {
    private Button btnBackHome, btnComplete, btnBack;
    private TextView txtmaPhieu, txttenNCC, txtngayNhap, txtnhanVien, txttoTal;
    private RecyclerView recyclerView;
    private ArrayList<CTPNK> ctpnks;
    private phieuNhapKho phieuNhapKho;
    private PNK1Adapter pnk3Adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_pnk3);
        Init();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        pnk3Adapter = new PNK1Adapter(this, ctpnks);
        recyclerView.setAdapter(pnk3Adapter);
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
                Toast.makeText(CreatePNK3Activity.this, "Hoàn thành", Toast.LENGTH_SHORT).show();
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
        txtmaPhieu.setText("Mã phiếu: " + phieuNhapKho.getMaPhieu().toString().trim());
        txtnhanVien.setText("Người phụ trách: " + phieuNhapKho.getMaNV().toString().trim());
        txttenNCC.setText("Nhà cung cấp: " + phieuNhapKho.getTenNCC().toString().trim());
        txttoTal.setText("Thành tiền: " + Integer.toString(phieuNhapKho.getTotalMoney()));
        txtngayNhap.setText("Ngày nhập kho: " + phieuNhapKho.getNgayNhapKho().toString().trim());
    }
    public void Init(){
        btnBackHome = findViewById(R.id.btn_back_createPNK3);
        btnComplete = findViewById(R.id.gdcreatePNK3_btnComplete);
        btnBack = findViewById(R.id.gdcreatePNK3_btnBack);
        recyclerView = findViewById(R.id.gdcreatePNK3_rcv);
        txtmaPhieu = findViewById(R.id.gdcreatePNK3_txtmaPhieu);
        txtngayNhap = findViewById(R.id.gdcreatePNK3_txtngayNhap);
        txttenNCC = findViewById(R.id.gdcreatePNK3_txtNCC);
        txtnhanVien = findViewById(R.id.gdcreatePNK3_txtnguoiPT);
        txttoTal = findViewById(R.id.gdcreatePNK3_txtthanhTien);
        ctpnks = new ArrayList<>();
        phieuNhapKho = new phieuNhapKho("NH122", "23/09/2022", "PM Milk. Co", "Nguyễn Văn A", 12000000);
    }
}