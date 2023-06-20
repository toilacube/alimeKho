package com.example.alimekho.Activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alimekho.Adapter.Area2Adapter;
import com.example.alimekho.Adapter.BatchAdapter;
import com.example.alimekho.DataBase.SQLServerConnection;
import com.example.alimekho.Model.Area;
import com.example.alimekho.Model.loSanPham;
import com.example.alimekho.Model.sanPham;
import com.example.alimekho.R;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class AddBatchToLocatonActivity extends AppCompatActivity {

    RecyclerView rcvBatch, rcvLocation;
    BatchAdapter batchAdapter;
    Area2Adapter areaAdapter;
    SQLServerConnection db;
    Button btnAddDistribute, btnBack;
    Connection conn;
    CheckBox cb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_batch_to_locaton);

        findView();
        setView();
        onClickButton();
    }

    private void onClickButton() {
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                batchAdapter.setCheckAll(cb.isChecked());
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AreaActivity.class));
            }
        });

        btnAddDistribute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Area area = areaAdapter.getSelectedArea();
                ArrayList <loSanPham> loSanPhams = batchAdapter.getSelectedLo();
                ArrayList <loSanPham> templist = new ArrayList<>(loSanPhams);
                for(loSanPham lo: loSanPhams){
                    String query = "exec pro_them_distribute @location_id = ?, @batch_id = ?;";
                    try {
                        PreparedStatement stm = db.getConnection().prepareStatement(query);
                        stm.setString(1, area.getId());
                        stm.setString(2, lo.getMaLo());
                        stm.executeUpdate();
                        Toast.makeText(AddBatchToLocatonActivity.this, "Them thanh cong", Toast.LENGTH_SHORT).show();
                        templist.remove(lo);
                        batchAdapter.setList(templist);
                        batchAdapter.notifyDataSetChanged();
                    } catch (SQLException e) {
                        Toast.makeText(AddBatchToLocatonActivity.this, "Them khong thanh cong", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, e.toString());
                    }
                }

            }
        });
    }

    private void setView() {

        db = new SQLServerConnection();
        conn = db.getConnection();

        batchAdapter = new BatchAdapter(this, getListBatch());
        areaAdapter = new Area2Adapter(this);
        areaAdapter.setData(getListArea());

        rcvBatch.setLayoutManager(new LinearLayoutManager(this));
        rcvBatch.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rcvBatch.setAdapter(batchAdapter);

        rcvLocation.setLayoutManager(new LinearLayoutManager(this));
        rcvLocation.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rcvLocation.setAdapter(areaAdapter);
    }

    private List<Area> getListArea() {
        List<Area>  l = new ArrayList<>();
        try {
            String query = "select l.id, l.zone, l.shelve, t.name " +
                    "from location l " +
                    "join product_type t on t.id = l.type_id " +
                    "where l.is_deleted = 0";
            Statement stm = db.getConnection().createStatement();
            ResultSet rs = stm.executeQuery(query);
            while(rs.next()){
                Area area = new Area();
                area.setId(rs.getString("id"));
                area.setArea(rs.getString("zone"));
                area.setShelve(rs.getString("shelve"));
                area.setType_id(rs.getString("name"));

                String queryBatch = "select b.id, product.id as product_id, product.name as product_name, supplier.name as supplier_name, b.quantity, b.not_stored \n" +
                        "from distribute\n" +
                        "join batch b on b.id = distribute.batch_id\n" +
                        "join product on product.id = product_id\n" +
                        "join supplier on supplier.id = product.supplier_id\n" +
                        "where distribute.location_id = ? ";
                PreparedStatement stmBatch = db.getConnection().prepareStatement(queryBatch);
                stmBatch.setString(1, area.getId());

                ResultSet rsBatch = stmBatch.executeQuery();

                ArrayList<loSanPham> listLo = new ArrayList<>();
                while(rsBatch.next()){
                    loSanPham lo = new loSanPham();

                    lo.setMaLo(Integer.toString(rsBatch.getInt("id")));

                    sanPham sp = new sanPham();
                    sp.setMaSP(Integer.toString(rsBatch.getInt("product_id")));
                    sp.setTenSP(rsBatch.getString("product_name"));
                    sp.setSupplier_id(rsBatch.getString("supplier_name"));
                    lo.setSanPham(sp);
                    lo.setsLTon(rsBatch.getInt("quantity"));
                    lo.setsLChuaXep(rsBatch.getInt("not_stored"));
                    listLo.add(lo);
                }
                area.setListLoSP(listLo);
                l.add(area);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return l;
    }

    private ArrayList<loSanPham> getListBatch() {
        ArrayList<loSanPham> l = new ArrayList<>();
        try {
            String select = "SET DATEFORMAT DMY\n" +
                    "SELECT batch.id, product_id, NSX, HSD, unit_price, product.name, quantity, not_stored FROM batch\n" +
                    "JOIN product ON batch.product_id = product.id\n" +
                    "WHERE batch.is_deleted = 0 " +
                    "and batch.not_stored >= 1";
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(select);
            while(rs.next()){
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String NSX = dateFormat.format(rs.getDate("NSX"));
                String HSD = dateFormat.format(rs.getDate("HSD"));
                sanPham sanPham = new sanPham(String.valueOf(rs.getInt("product_id")), rs.getString(6));
                loSanPham loSanPham = new loSanPham(String.valueOf(rs.getInt(1)), NSX, HSD, sanPham, rs.getInt("quantity"),  rs.getInt("not_stored"),rs.getDouble("unit_price"));
                l.add(loSanPham);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return l;
    }

    private void findView() {
        rcvBatch = findViewById(R.id.rcvLoSP);
        rcvLocation = findViewById(R.id.rcvLocation);
        cb = findViewById(R.id.check_box);
        btnAddDistribute = findViewById(R.id.btnAddDistribute);
        btnBack = findViewById(R.id.btn_back);
    }
}