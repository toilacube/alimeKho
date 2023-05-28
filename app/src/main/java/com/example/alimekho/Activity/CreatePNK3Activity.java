package com.example.alimekho.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alimekho.Adapter.PNK1Adapter;
import com.example.alimekho.Adapter.PNK3Adapter;
import com.example.alimekho.DataBase.SQLServerConnection;
import com.example.alimekho.Model.CTPNK;
import com.example.alimekho.Model.loSanPham;
import com.example.alimekho.Model.sanPham;
import com.example.alimekho.Model.phieuNhapKho;
import com.example.alimekho.R;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class CreatePNK3Activity extends AppCompatActivity {
    private Button btnBackHome, btnComplete, btnBack;
    private TextView txtmaPhieu, txttenNCC, txtngayNhap, txtnhanVien, txttoTal;
    private RecyclerView recyclerView;
    private phieuNhapKho phieuNhapKho;
    private PNK3Adapter pnk3Adapter;
    private SQLServerConnection db = new SQLServerConnection();
    private Connection conn = db.getConnection();
    private ArrayList<loSanPham> loSanPhamdc;
    private ArrayList<loSanPham> loSanPhams;
    private int maPhieu;
    private String tenNCC;
    private  double totalMoney = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_pnk3);

        Init();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        pnk3Adapter = new PNK3Adapter(this, loSanPhams);
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
                    try {
                        for(loSanPham loSanPham : loSanPhams){
                            String insert = "INSERT INTO detail_input(form_id, batch_id, quantity) VALUES(?, ?, ?)";
                            PreparedStatement stm = conn.prepareStatement(insert);
                            stm.setInt(1, maPhieu);
                            stm.setInt(2, Integer.parseInt(loSanPham.getMaLo()));
                            stm.setInt(3, loSanPham.getsLTon());
                            int rs1 = stm.executeUpdate();
                        }
                    } catch (SQLException e) {
                             e.printStackTrace();
                    }
                Toast.makeText(CreatePNK3Activity.this, "Hoàn thành", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), QLPNKActivity.class));
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
        txtnhanVien.setText("Người phụ trách: " + phieuNhapKho.getTenNV().toString().trim());
        txttenNCC.setText("Nhà cung cấp: " + phieuNhapKho.getTenNCC().toString().trim());
        txttoTal.setText("Thành tiền: " + Double.toString(phieuNhapKho.getTotalMoney()));
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
        loSanPhams = new ArrayList<>();
        loSanPhamdc = CreatePNK1Activity.spSelected();
        tenNCC = getIntent().getStringExtra("tenNCC");
        maPhieu = getIntent().getIntExtra("maPhieu", -1);
        for(loSanPham loSanPham : loSanPhamdc){
            loSanPhams.add(loSanPham);
            totalMoney += loSanPham.getDonGia()*loSanPham.getsLTon();
        }
        try {
            Statement stm = conn.createStatement();
            String getInputform = "SET DATEFORMAT DMy\n" +
                    "select input_form.id, input_form.input_day, employee.name, total from input_form \n" +
                    "LEFT JOIN employee ON input_form.emp_id = employee.id\n" +
                    "WHERE input_form.id = " + String.valueOf(maPhieu);
            ResultSet rs = stm.executeQuery(getInputform);
            while (rs.next()) {
                String pattern = "dd/MM/yyyy";
                DateFormat df = new SimpleDateFormat(pattern);
                String todayAsString = df.format(rs.getDate(2));
                phieuNhapKho = new phieuNhapKho(String.valueOf(rs.getInt(1)), todayAsString, tenNCC, rs.getString(3), totalMoney);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}