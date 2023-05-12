package com.example.alimekho.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alimekho.Model.CTPNK;
import com.example.alimekho.Model.sanPham;
import com.example.alimekho.R;
import com.example.alimekho.Adapter.PNK1Adapter;

import java.util.ArrayList;

public class CreatePNK1Activity extends AppCompatActivity {
    private Button btnBackHome, btnContinue, btnAdd, btnBack;
    private RecyclerView recyclerView;
    private ArrayList<CTPNK> ctpnks;
    private PNK1Adapter pnk1Adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_pnk1);
        Init();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        pnk1Adapter = new PNK1Adapter(this, ctpnks);
        recyclerView.setAdapter(pnk1Adapter);
        btnBackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CreatePNK1Activity.this, CreatePNK2Activity.class));
            }
        });
    }
    public void Init(){
        btnBackHome = findViewById(R.id.btn_back_createPNK1);
        btnAdd = findViewById(R.id.gdcreatePNK1_btnAdd);
        btnContinue = findViewById(R.id.gdcreatePNK1_btnContinue);
        btnBack = findViewById(R.id.gdcreatePNK1_btnBack);
        recyclerView = findViewById(R.id.gdcreatePNK1_rcv);
        ctpnks = new ArrayList<>();
    }
    public void showDialog(){
        Dialog dialog = new Dialog(this, android.R.style.Theme_Material_Light_Dialog_Presentation);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_addsp);
        TextView txtmaSP, txtdonGia, txttenSP, txtsoLuong, txtNSX, txtHSD;
        txtmaSP = dialog.findViewById(R.id.txtmaSP);
        txttenSP = dialog.findViewById(R.id.txttenSP);
        txtdonGia = dialog.findViewById(R.id.txtdonGia);
        txtsoLuong = dialog.findViewById(R.id.txtsoLuong);
        txtNSX = dialog.findViewById(R.id.txtNSX);
        txtHSD = dialog.findViewById(R.id.txtHSD);
        Button btnAdd = dialog.findViewById(R.id.btnAdd);
        Button btnCancle = dialog.findViewById(R.id.btnBack);
        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sanPham sanPham = new sanPham(txtmaSP.getText().toString().trim(), txttenSP.getText().toString().trim(),
                        Double.valueOf(txtdonGia.getText().toString().trim()),Integer.parseInt(txtsoLuong.getText().toString().trim()),
                        txtNSX.getText().toString().trim(), txtHSD.getText().toString().trim());
                ctpnks.add(new CTPNK("PX123", sanPham));
                pnk1Adapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}