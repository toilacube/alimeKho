package com.example.alimekho.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alimekho.Adapter.ViTriSPAdapter;
import com.example.alimekho.DataBase.SQLServerConnection;
import com.example.alimekho.Model.loSanPham;
import com.example.alimekho.Model.sanPham;
import com.example.alimekho.R;

import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ViTriSanPhamActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ViTriSPAdapter adapter;
    SQLServerConnection db;
    Button btnBack;
    SearchView searchView;
    ArrayList <loSanPham> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vi_tri_san_pham);

        findView();
        showViTriSP();
        onClickButton();
        search();
    }

    private void search() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                filterList(s);
                return true;
            }
        });
    }

    private void filterList(String s) {
        ArrayList <loSanPham> filter = new ArrayList<>();
        for(loSanPham loSP: list){
            if(loSP.getSanPham().getTenSP().toLowerCase().contains(s.toLowerCase()))
                filter.add(loSP);
        }
        adapter.setListLoSP(filter);
    }

    private void onClickButton() {

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void findView() {
        recyclerView = findViewById(R.id.rcvViTriSP);
        btnBack = findViewById(R.id.btnBackViTriSP);
        searchView = findViewById(R.id.svViTriSP);
    }

    private void showViTriSP() {

        list = getListLoSP();
        adapter = new ViTriSPAdapter(this, list);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setAdapter(adapter);
    }

    private ArrayList<loSanPham> getListLoSP() {
        ArrayList<loSanPham> list = new ArrayList<>();
        db = new SQLServerConnection();
        String query = "select b.id, b.quantity, b.product_id, p.name from batch b " +
                "join product p on p.id = b.product_id " +
                "where b.id in (select batch_id from distribute)";
        try {
            PreparedStatement stm = db.getConnection().prepareStatement(query);
            ResultSet rs = stm.executeQuery();
            while(rs.next()){
                loSanPham loSP = new loSanPham();
                loSP.setMaLo(Integer.toString(rs.getInt(1)));
                loSP.setsLTon((rs.getInt(2)));
                sanPham sp = new sanPham(Integer.toString(rs.getInt(3)), rs.getString(4));
                loSP.setSanPham(sp);
                list.add(loSP);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}