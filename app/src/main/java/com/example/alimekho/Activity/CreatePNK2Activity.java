package com.example.alimekho.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.alimekho.Adapter.PNK2Adapter;
import com.example.alimekho.Model.nhaCungCap;
import com.example.alimekho.R;
import com.example.alimekho.Model.sanPham;

import java.util.ArrayList;

public class CreatePNK2Activity extends AppCompatActivity {
    private Button btnBackHome, btnContinue, btnAdd, btnBack;
    private RecyclerView recyclerView;
    private TextView txtmaNCC, txtTenNCC, txtdiaChi, txtSDT;
    private SearchView searchView;
    private ArrayList<nhaCungCap> nhaCungCaps;
    private PNK2Adapter pnk2Adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_pnk2);
        Init();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        pnk2Adapter = new PNK2Adapter(this, nhaCungCaps);
        recyclerView.setAdapter(pnk2Adapter);
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
                startActivity(new Intent(CreatePNK2Activity.this, CreatePNK3Activity.class));
            }
        });
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });
    }
    public void setData(nhaCungCap nhaCungCap){
        txtmaNCC.setText(nhaCungCap.getMaNCC());
        txtTenNCC.setText(nhaCungCap.getTenCC());
        txtdiaChi.setText(nhaCungCap.getDiaChi());
        txtSDT.setText(nhaCungCap.getSDT());
    }
    private void filterList(String newText) {
        ArrayList<nhaCungCap> filteredList = new ArrayList<>();
        for (nhaCungCap nhaCungCap : nhaCungCaps) {
            if (nhaCungCap.getTenCC().toLowerCase().contains(newText.toLowerCase())
                    || nhaCungCap.getMaNCC().toLowerCase().contains(newText.toLowerCase())){
                filteredList.add(nhaCungCap);
            }
        }
        pnk2Adapter.setFilteredList(filteredList);
    }
    public void Init(){
        btnBackHome = findViewById(R.id.btn_back_createPNK2);
        btnAdd = findViewById(R.id.gdcreatePNK2_btnAdd);
        btnContinue = findViewById(R.id.gdcreatePNK2_btnContinue);
        btnBack = findViewById(R.id.gdcreatePNK2_btnBack);
        recyclerView = findViewById(R.id.gdcreatePNK2_rcv);
        searchView = findViewById(R.id.gdcreatePNK2_sv);
        txtmaNCC = findViewById(R.id.gdcreatePNK2_txt1);
        txtTenNCC = findViewById(R.id.gdcreatePNK2_txt2);
        txtdiaChi = findViewById(R.id.gdcreatePNK2_txt3);
        txtSDT = findViewById(R.id.gdcreatePNK2_txt4);
        nhaCungCaps = new ArrayList<>();
    }
    public void showDialog(){
        Dialog dialog = new Dialog(this, android.R.style.Theme_Material_Light_Dialog_Presentation);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_addncc);
        TextView txtmaNCC, txttenNCC, txtdiaChi, txtSDT;
        txtmaNCC = dialog.findViewById(R.id.txtmaNCC);
        txttenNCC = dialog.findViewById(R.id.txttenNCC);
        txtdiaChi = dialog.findViewById(R.id.txtdiaChi);
        txtSDT = dialog.findViewById(R.id.txtSDT);
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
                nhaCungCap nhaCungCap = new nhaCungCap(txtmaNCC.getText().toString().trim(), txttenNCC.getText().toString().trim(),
                        txtdiaChi.getText().toString().trim(), txtSDT.getText().toString().trim());
                nhaCungCaps.add(nhaCungCap);
                pnk2Adapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}