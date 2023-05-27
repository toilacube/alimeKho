package com.example.alimekho.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alimekho.Adapter.PXK1Adapter;
import com.example.alimekho.Adapter.PXK1_2Adapter;
import com.example.alimekho.DataBase.SQLServerConnection;
import com.example.alimekho.Model.CTPXK;
import com.example.alimekho.Model.loSanPham;
import com.example.alimekho.Model.sanPham;
import com.example.alimekho.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class CreatePXK1_2Activity extends AppCompatActivity {
    SQLServerConnection db = new SQLServerConnection();
    Connection conn = db.getConnection();
    private Button btnBackHome, btnContinue, btnBack;
    private CheckBox checkAll;
    private RecyclerView recyclerView;
    private SearchView searchView;
    private ArrayList<CTPXK> sanPhams;
    private PXK1_2Adapter pxk12Adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_pxk1_2);
        Init();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        Toast.makeText(this, String.valueOf(sanPhams.size()), Toast.LENGTH_SHORT).show();
        pxk12Adapter = new PXK1_2Adapter(this, sanPhams);
        recyclerView.setAdapter(pxk12Adapter);
        btnBackHome.setOnClickListener(v -> onBackPressed());
        btnBack.setOnClickListener(v -> onBackPressed());
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent(CreatePXK1_2Activity.this, CreatePXK2Activity.class));
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
    public void Init(){
        btnBackHome = findViewById(R.id.btn_back_createPXK1);
        btnContinue = findViewById(R.id.gdcreatePXK1_btnContinue);
        btnBack = findViewById(R.id.gdcreatePXK1_btnBack);
        recyclerView = findViewById(R.id.gdcreatePXK1_rcv);
        searchView = findViewById(R.id.gdcreatePXK1_sv);
        checkAll = findViewById(R.id.checkAll);
        sanPhams = CreatePXK1Activity.spSelected();
    }

    private void filterList(String newText) {
        ArrayList<CTPXK> filteredList = new ArrayList<>();
        for (CTPXK sanPham : sanPhams) {
            if (sanPham.getLo().getSanPham().getMaSP().toLowerCase().contains(newText.toLowerCase())
                    || sanPham.getLo().getSanPham().getTenSP().toLowerCase().contains(newText.toLowerCase())){
                filteredList.add(sanPham);
            }
        }

        if (!filteredList.isEmpty()){
            pxk12Adapter.setFilteredList(filteredList);
        }

    }

}