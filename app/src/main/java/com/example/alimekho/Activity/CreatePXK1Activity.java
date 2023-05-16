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

import com.example.alimekho.Adapter.PNK1Adapter;
import com.example.alimekho.Adapter.PXK1Adapter;
import com.example.alimekho.Model.CTPNK;
import com.example.alimekho.Model.CTPXK;
import com.example.alimekho.Model.nhaCungCap;
import com.example.alimekho.Model.sanPham;
import com.example.alimekho.R;

import java.util.ArrayList;

public class CreatePXK1Activity extends AppCompatActivity {

    private Button btnBackHome, btnContinue, btnBack;
    private RecyclerView recyclerView;
    private SearchView searchView;
    private ArrayList<sanPham> sanPhams, sanPhamDuocChon;
    private PXK1Adapter pxk1Adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_pxk1);
        Init();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        pxk1Adapter = new PXK1Adapter(this, sanPhams);
        recyclerView.setAdapter(pxk1Adapter);
        btnBackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
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
                sanPhamDuocChon = pxk1Adapter.getSanPhamVV();
                startActivity(new Intent(CreatePXK1Activity.this, CreatePXK2Activity.class));
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
    private void filterList(String newText) {
        ArrayList<sanPham> filteredList = new ArrayList<>();
        for (sanPham sanPham : sanPhams) {
            if (sanPham.getMaSP().toLowerCase().contains(newText.toLowerCase())
                    || sanPham.getTenSP().toLowerCase().contains(newText.toLowerCase())){
                filteredList.add(sanPham);
            }
        }
        pxk1Adapter.setFilteredList(filteredList);
    }
    public void Init(){
        btnBackHome = findViewById(R.id.btn_back_createPXK1);
        btnContinue = findViewById(R.id.gdcreatePXK1_btnContinue);
        btnBack = findViewById(R.id.gdcreatePXK1_btnBack);
        recyclerView = findViewById(R.id.gdcreatePXK1_rcv);
        searchView = findViewById(R.id.gdcreatePXK1_sv);
//        sanPham sanPham = new sanPham("A1", "2", 3, 4, "5", "6");
   //     sanPham sanPham1 = new sanPham("1", "2", 3, 4, "5", "6");
        sanPhams = new ArrayList<>();
     //   sanPhams.add(sanPham);
     //   sanPhams.add(sanPham1);
        sanPhamDuocChon = new ArrayList<>();
    }

}