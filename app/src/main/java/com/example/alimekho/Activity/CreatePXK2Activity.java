package com.example.alimekho.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alimekho.Adapter.PXK2Adapter;
import com.example.alimekho.Adapter.PXK2Adapter;
import com.example.alimekho.Model.cuaHangXuat;
import com.example.alimekho.R;

import java.util.ArrayList;

public class CreatePXK2Activity extends AppCompatActivity {
    private Button btnBackHome, btnContinue, btnAdd, btnBack;
    private RecyclerView recyclerView;
    private TextView txtmaCHX, txtTenCHX, txtdiaChi, txtSDT;
    private SearchView searchView;
    private ArrayList<cuaHangXuat> cuaHangXuats;
    private PXK2Adapter pxk2Adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_pxk2);
        Init();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        pxk2Adapter = new PXK2Adapter(this, cuaHangXuats);
        recyclerView.setAdapter(pxk2Adapter);
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
                startActivity(new Intent(CreatePXK2Activity.this, CreatePXK3Activity.class));
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
    public void setData(cuaHangXuat cuaHangXuat){
        txtmaCHX.setText(cuaHangXuat.getMaCHX());
        txtTenCHX.setText(cuaHangXuat.getTenCHX());
        txtdiaChi.setText(cuaHangXuat.getDiaChi());
        txtSDT.setText(cuaHangXuat.getSDT());
    }
    private void filterList(String newText) {
        ArrayList<cuaHangXuat> filteredList = new ArrayList<>();
        for (cuaHangXuat cuaHangXuat : cuaHangXuats) {
            if (cuaHangXuat.getTenCHX().toLowerCase().contains(newText.toLowerCase())
                    || cuaHangXuat.getMaCHX().toLowerCase().contains(newText.toLowerCase())){
                filteredList.add(cuaHangXuat);
            }
        }
        pxk2Adapter.setFilteredList(filteredList);
    }
    public void Init(){
        btnBackHome = findViewById(R.id.btn_back_createPXK2);
        btnAdd = findViewById(R.id.gdcreatePXK2_btnAdd);
        btnContinue = findViewById(R.id.gdcreatePXK2_btnContinue);
        btnBack = findViewById(R.id.gdcreatePXK2_btnBack);
        recyclerView = findViewById(R.id.gdcreatePXK2_rcv);
        searchView = findViewById(R.id.gdcreatePXK2_sv);
        txtmaCHX = findViewById(R.id.gdcreatePXK2_txt1);
        txtTenCHX = findViewById(R.id.gdcreatePXK2_txt2);
        txtdiaChi = findViewById(R.id.gdcreatePXK2_txt3);
        txtSDT = findViewById(R.id.gdcreatePXK2_txt4);
        cuaHangXuats = new ArrayList<>();
    }
    public void showDialog(){
        Dialog dialog = new Dialog(this, android.R.style.Theme_Material_Light_Dialog_Presentation);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_addchx);
        TextView txtmaNCC, txttenNCC, txtdiaChi, txtSDT;
        txtmaCHX = dialog.findViewById(R.id.txtmaCHX);
        txtTenCHX = dialog.findViewById(R.id.txttenCHX);
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
                cuaHangXuat cuaHangXuat = new cuaHangXuat(txtmaCHX.getText().toString().trim(), txtTenCHX.getText().toString().trim(),
                        txtdiaChi.getText().toString().trim(), txtSDT.getText().toString().trim());
                cuaHangXuats.add(cuaHangXuat);
                pxk2Adapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}