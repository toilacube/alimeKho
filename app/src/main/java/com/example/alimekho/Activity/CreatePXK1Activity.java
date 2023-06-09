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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.alimekho.Adapter.PNK1Adapter;
import com.example.alimekho.Adapter.PXK1Adapter;
import com.example.alimekho.DataBase.SQLServerConnection;
import com.example.alimekho.Model.CTPNK;
import com.example.alimekho.Model.CTPXK;
import com.example.alimekho.Model.loSanPham;
import com.example.alimekho.Model.nhaCungCap;
import com.example.alimekho.Model.phieuXuatKho;
import com.example.alimekho.Model.sanPham;
import com.example.alimekho.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class CreatePXK1Activity extends AppCompatActivity {
    SQLServerConnection db = new SQLServerConnection();
    Connection conn = db.getConnection();
    private Button btnBackHome, btnContinue, btnBack;
    private CheckBox checkAll;
    private RecyclerView recyclerView;
    private SearchView searchView;
    private ArrayList<CTPXK> sanPhams;
    private static ArrayList<CTPXK> sanPhamDuocChon;
    public static ArrayList<CTPXK> spSelected(){
        return sanPhamDuocChon;
    }
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
        btnBackHome.setOnClickListener(v -> onBackPressed());
        btnBack.setOnClickListener(v -> onBackPressed());
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sanPhamDuocChon = pxk1Adapter.getSanPhamVV();
                startActivity( new Intent(CreatePXK1Activity.this, CreatePXK1_2Activity.class));
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
        checkAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()) pxk1Adapter.selectAll();
                else pxk1Adapter.unSelectAll();
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
        sanPhams = new ArrayList<>();
        try {
            Statement stm = conn.createStatement();
            String Query ="select b.id, b.product_id, p.name, b.quantity, b.unit_price, b.NSX, b.HSD\n" +
                    "from batch b, product p\n" +
                    "where p.id = b.product_id";
            ResultSet rs = stm.executeQuery(Query);
            while (rs.next()) {
                sanPhams.add(new CTPXK(
                        new loSanPham(
                                rs.getString("id"),
                                new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("NSX")),
                                new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("HSD")),
                                new sanPham(
                                        rs.getString("product_id"),
                                        rs.getString("name")
                                ),
                                rs.getInt("unit_price")
                        ),
                        rs.getInt("quantity"))
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        sanPhamDuocChon = new ArrayList<>();
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
            pxk1Adapter.setFilteredList(filteredList);
        }

    }

}