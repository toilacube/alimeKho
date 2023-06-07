package com.example.alimekho.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alimekho.Adapter.AreaAdapter;
import com.example.alimekho.DataBase.SQLServerConnection;
import com.example.alimekho.Model.Area;
import com.example.alimekho.Model.loSanPham;
import com.example.alimekho.Model.sanPham;
import com.example.alimekho.R;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AreaActivity extends AppCompatActivity {

    RecyclerView recyclerView;AreaAdapter areaAdapter;
    SQLServerConnection db = new SQLServerConnection();;
    Button btnBackArea, btnViTriSP, btnThemViTri;
    SearchView searchView;
    ArrayList <Area> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area);

        findView();
        showArea();
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
        ArrayList <Area> filter = new ArrayList<>();
        for(Area area: list){
            if(area.getType_id().toLowerCase().contains(s.toLowerCase())
            || area.getId().toLowerCase().contains(s.toLowerCase()))
                filter.add(area);
        }
        areaAdapter.setData(filter);

    }

    private void onClickButton() {
        btnThemViTri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogThemViTri(view);
            }
        });
        btnBackArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnViTriSP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Area> list = new ArrayList<>(getListArea());
                Intent intent = new Intent(getApplicationContext(), ViTriSanPhamActivity.class);
                intent.putExtra("area", list);
                startActivity(intent);
            }
        });
    }

    private void showDialogThemViTri(View view) {
        AlertDialog.Builder builder=new AlertDialog.Builder(view.getRootView().getContext());
        View dialogView= LayoutInflater.from(view.getRootView().getContext())
                .inflate(R.layout.dialog_them_vi_tri,null);
        AlertDialog dialog = builder.create();
        dialog.setView(dialogView);

        EditText edtZone = dialogView.findViewById(R.id.edtZone),
                edtShelve = dialogView.findViewById(R.id.edtShelve),
                edtSucChua = dialogView.findViewById(R.id.edtSucChua);
        Button btnAdd = dialogView.findViewById(R.id.btnAdd),
                btnCancel = dialogView.findViewById(R.id.btnCancel);
        Spinner spLoaiSP = dialogView.findViewById(R.id.spLoaiSP);

        ArrayList<String> listLoaiSP = new ArrayList<>();
        listLoaiSP = getLoaiSP();
        ArrayAdapter<String> adapterLoaiSP = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, listLoaiSP);
        adapterLoaiSP.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spLoaiSP.setAdapter(adapterLoaiSP);

        sanPham sanPham = new sanPham();

        spLoaiSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sanPham.setPhanLoai(getIDLoaiSP().get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                sanPham.setPhanLoai(getIDLoaiSP().get(0));
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edtZone.getText().toString().trim().isEmpty())
                    edtZone.setError("Vui lòng nhập thông tin");
                else if(edtShelve.getText().toString().trim().isEmpty())
                    edtShelve.setError("Vui lòng nhập thông tin");
                else if (edtSucChua.getText().toString().trim().isEmpty())
                    edtSucChua.setError("Vui lòng nhập thông tin");
                else{
                    try {
                        String insertQuery = "insert into location (id, zone, shelve, slot, type_id, available, is_deleted)" +
                                "values (?, ?, ?, ?, ?, ?, ?)";
                        SQLServerConnection db = new SQLServerConnection();
                        PreparedStatement stm = db.getConnection().prepareStatement(insertQuery);
                        stm.setString(1, edtZone.getText().toString().trim().toUpperCase()
                        + edtShelve.getText().toString().trim().toUpperCase());
                        stm.setString(2, edtZone.getText().toString().trim().toUpperCase());
                        stm.setString(3, edtShelve.getText().toString().trim().toUpperCase());
                        stm.setString(4, edtSucChua.getText().toString().trim());
                        stm.setString(5, sanPham.getPhanLoai());
                        stm.setString(6, edtSucChua.getText().toString().trim() );
                        stm.setString(7, "0");
                        stm.executeUpdate();
                        dialog.dismiss();
                    } catch (SQLException e) {
                        e.printStackTrace();
                        Toast.makeText(AreaActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        dialog.show();
    }

    private ArrayList<String> getIDLoaiSP() {
        ArrayList<String> l = new ArrayList<>();
        try {
            String select = "SELECT id FROM product_type";
            Statement stm = db.getConnection().createStatement();
            ResultSet rs = stm.executeQuery(select);
            while(rs.next()){
                String temp = String.valueOf(rs.getInt(1));
                l.add(temp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return l;
    }

    private ArrayList<String> getLoaiSP() {
        ArrayList<String> l = new ArrayList<>();
        try {
            String select = "SELECT id, name FROM product_type";
            Statement stm = db.getConnection().createStatement();
            ResultSet rs = stm.executeQuery(select);
            while(rs.next()){
                String temp = String.valueOf(rs.getInt(1)) + " - " + rs.getString(2);
                l.add(temp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return l;
    }

    private void showArea() {
        list = new ArrayList<>(getListArea());
        areaAdapter=new AreaAdapter(this);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
        areaAdapter.setData(list);
        recyclerView.setAdapter(areaAdapter);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        areaAdapter.notifyDataSetChanged();
    }

    private void findView() {
        btnThemViTri = findViewById(R.id.btnThemViTri);
        btnViTriSP = findViewById(R.id.btnViTriSP);
        btnBackArea=findViewById(R.id.btn_back_area);
        recyclerView=findViewById(R.id.rv_area);
        searchView = findViewById(R.id.svArea);
    }

    private List<Area> getListArea() {
        List<Area>  l = new ArrayList<>();
        try {
            String query = "select l.id, l.zone, l.shelve, l.available, l.slot, t.name " +
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
                area.setAvailable(rs.getInt("available"));
                area.setSlot(rs.getInt("slot"));
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
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(areaAdapter!=null){
            areaAdapter.release();
        }
    }
}