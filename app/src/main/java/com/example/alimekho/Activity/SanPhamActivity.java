package com.example.alimekho.Activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alimekho.Adapter.SanPhamAdapter;
import com.example.alimekho.DataBase.SQLServerConnection;
import com.example.alimekho.Model.sanPham;
import com.example.alimekho.R;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SanPhamActivity extends AppCompatActivity {
    RecyclerView rcvSanPham;
    SanPhamAdapter sanPhamAdapter;
    Button btnAdd, btnBack;
    List<sanPham> list = new ArrayList<>();
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_GALLERY = 2;

    SQLServerConnection db = new SQLServerConnection();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_san_pham);

        btnAdd = findViewById(R.id.btnThemSP);
        btnBack = findViewById(R.id.btn_backToHome);
        rcvSanPham = findViewById(R.id.rcvSanPham);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvSanPham.setLayoutManager(linearLayoutManager);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rcvSanPham.addItemDecoration(itemDecoration);

        list = getListSanPham();
        sanPhamAdapter = new SanPhamAdapter(getApplicationContext(), list);
        rcvSanPham.setAdapter(sanPhamAdapter);

        List<sanPham> finalList = list;

        if (getSharedPreferences("user info", MODE_PRIVATE).getInt("role", 0) != 0) {
            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDialogThemSP(view);
                }
            });
        } else {
            btnAdd.setOnClickListener(view -> Toast.makeText(this, "Bạn không có quyền thực hiện thao tác này", Toast.LENGTH_SHORT).show());
        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
            }
        });
    }


    private void showDialogThemSP(View view) {
       sanPham sanPham = new sanPham();

        AlertDialog.Builder builder=new AlertDialog.Builder(view.getRootView().getContext());
        View dialogView= LayoutInflater.from(view.getRootView().getContext())
                .inflate(R.layout.dialog_them_san_pham,null);
        AlertDialog dialog = builder.create();

        // Khai bao cac thanh phan trong dialog
        EditText edtTenSP = dialogView.findViewById(R.id.edtTenSP),
                edtDonViTinh = dialogView.findViewById(R.id.edtDonViTinh);
        Spinner spLoaiSP = (Spinner) dialogView.findViewById(R.id.spLoaiSP),
                spNhaCC = (Spinner) dialogView.findViewById(R.id.spNhaCC);

        Button btnAdd = dialogView.findViewById(R.id.btnAdd),
                btnCancel = dialogView.findViewById(R.id.btnCancel);

        // Khoi tao du lieu cho 2 spinner
        ArrayList<String> listLoaiSP = new ArrayList<>();
        listLoaiSP = getLoaiSP();
        System.out.println(listLoaiSP);
        ArrayAdapter<String> adapterLoaiSP = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, listLoaiSP);
        adapterLoaiSP.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spLoaiSP.setAdapter(adapterLoaiSP);

        ArrayList<String> listNhaCC = new ArrayList<>();
        listNhaCC = getNhaCC();
        ArrayAdapter<String> adapterNhaCC = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, listNhaCC);
        adapterNhaCC.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spNhaCC.setAdapter(adapterNhaCC);
        ArrayList <String> listIDLoaiSP = getIDLoaiSP();
        ArrayList <String> listIDNhaCC = getIDNhaCC();
        spLoaiSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sanPham.setPhanLoai(listIDLoaiSP.get(i));
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                sanPham.setPhanLoai(listIDLoaiSP.get(0));
            }
        });
        spNhaCC.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sanPham.setSupplier_id(listIDNhaCC.get(i));
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                sanPham.setSupplier_id(listIDNhaCC.get(0));
            }
        });



        // Them thong tin san pham, an nut Them de them SP vao database
        // chua xu ly chuoi rong
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sanPham.setTenSP(edtTenSP.getText().toString().trim());
                sanPham.setDonViTinh(edtDonViTinh.getText().toString().trim());

                try {
                    String query = "INSERT INTO product ([name], [unit], [type_id], [supplier_id]) VALUES (?, ?, ?, ?)";
                    query = "exec pro_them_san_pham @name = ?, @unit = ?, @type_id = ?, @supplier_id = ?";
                    PreparedStatement stm = db.getConnection().prepareStatement(query);
                    stm.setString(1, sanPham.getTenSP());
                    stm.setString(2, sanPham.getDonViTinh());
                    stm.setString(3, sanPham.getPhanLoai());
                    stm.setString(4, sanPham.getSupplier_id());

                    stm.executeQuery();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                String select = "SELECT IDENT_CURRENT('product')";
                Statement stm1 = null;
                try {
                    stm1 = db.getConnection().createStatement();
                    ResultSet rs1 = stm1.executeQuery(select);
                    while(rs1.next()) {
                        sanPham.setMaSP(String.valueOf(rs1.getInt(1)));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                list.add(sanPham);
                sanPhamAdapter.setList(list);
                sanPhamAdapter.notifyDataSetChanged();

                recreate();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.setView(dialogView);
        dialog.show();
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
    private ArrayList<String> getIDLoaiSP() {
        ArrayList<String> l = new ArrayList<>();
        try {
            String select = "SELECT id FROM product_type";
            Statement stm = db.getConnection().createStatement();
            ResultSet rs = stm.executeQuery(select);
            while(rs.next()){
                String temp = String.valueOf(rs.getInt("id"));
                l.add(temp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return l;
    }

    public ArrayList<String> getTenLoaiSP() {
        ArrayList<String> l = new ArrayList<>();
        try {
            String select = "SELECT name FROM product_type";
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
    public ArrayList<String> getNhaCC() {
        ArrayList<String> l = new ArrayList<>();
        try {
            String select = "SELECT id, name FROM supplier";
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

    public ArrayList<String> getIDNhaCC() {
        ArrayList<String> l = new ArrayList<>();
        try {
            String select = "SELECT id FROM supplier";
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

    public ArrayList<String> getTenNhaCC() {
        ArrayList<String> l = new ArrayList<>();
        try {
            String select = "SELECT name FROM supplier";
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


    public List<sanPham> getListSanPham() {
        List <sanPham> list = new ArrayList<>();

        SQLServerConnection db = new SQLServerConnection();
        Connection con = db.getConnection();
        try {
            Statement stm = con.createStatement();
            String query = "select * from product where is_deleted = 0";
            ResultSet resultSet = stm.executeQuery(query);
            while (resultSet.next()){
                sanPham sp = new sanPham();
                sp.setMaSP(resultSet.getString("id"));
                sp.setTenSP(resultSet.getString("name"));
                //sp.setDonGia(resultSet.getDouble("unit_price"));
                sp.setDonViTinh(resultSet.getString("unit"));
                sp.setPhanLoai(Integer.toString(resultSet.getInt("type_id")));
                sp.setSupplier_id(Integer.toString(resultSet.getInt("supplier_id")));
                list.add(sp);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}
