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

    }
}