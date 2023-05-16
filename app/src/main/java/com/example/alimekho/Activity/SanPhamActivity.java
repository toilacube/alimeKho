package com.example.alimekho.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alimekho.Adapter.SanPhamAdapter;
import com.example.alimekho.DataBase.SQLServerConnection;
import com.example.alimekho.Model.sanPham;
import com.example.alimekho.R;

import java.sql.Connection;
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

        //list = getListSanPham();
        sanPhamAdapter = new SanPhamAdapter(getApplicationContext(), list);
        rcvSanPham.setAdapter(sanPhamAdapter);

        List<sanPham> finalList = list;
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogThemSP();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
            }
        });
    }

    private void showDialogThemSP() {
       sanPham sanPham = new sanPham();

        Dialog dialogView = new Dialog(this, android.R.style.Theme_Material_Light_Dialog_Presentation);
        dialogView.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogView.setContentView(R.layout.dialog_them_san_pham);

        EditText edtTenSP = dialogView.findViewById(R.id.edtTenSP),
                edtDonGia = dialogView.findViewById(R.id.edtDonGia),
                edtNhaCC = dialogView.findViewById(R.id.edtNhaCC),
                edtLoaiSP = dialogView.findViewById(R.id.edtLoaiSP),
                edtDonViTinh = dialogView.findViewById(R.id.edtDonViTinh);

        Button btnAdd = dialogView.findViewById(R.id.btnAdd),
                btnCancel = dialogView.findViewById(R.id.btnCancel);

        // Da them thong tin, an nut Them de them SP vao database
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sanPham.setTenSP(edtTenSP.getText().toString().trim());
                sanPham.setDonGia(Double.valueOf(edtDonGia.getText().toString().trim()));
                sanPham.setPhanLoai(edtLoaiSP.getText().toString().trim());
                sanPham.setDonViTinh(edtDonViTinh.getText().toString().trim());
                sanPham.setSupplier_id(edtNhaCC.getText().toString().trim());

                try {
                    Statement stm = db.getConnection().createStatement();
                    String query = "INSERT INTO product ([name], [unit_price], [unit])\n" +
                            "VALUES\n" +
                            "  ('" +  sanPham.getTenSP() + "', '"
                            + sanPham.getDonGia() + "', '"
                            + sanPham.getDonViTinh() + "');";
                    stm.executeUpdate(query);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                list.add(sanPham);
                sanPhamAdapter.setList(list);

//                sanPham.setTenSP(edtTenSP.getText().toString().trim());
//                sanPham.setDonGia(Double.valueOf(edtDonGia.getText().toString().trim()));
//                sanPham.setHSD(edtHSD.getText().toString().trim());
//                sanPham.setNSX(edtNSX.getText().toString().trim());
//                sanPham.setSoLuong(Integer.valueOf(edtSoLuong.getText().toString().trim()));
//                sanPham.setPhanLoai(edtLoaiSP.getText().toString().trim());
//                sanPham.setDonViTinh(edtDonViTinh.getText().toString().trim());
//                list.add(sanPham);
//                sanPhamAdapter.setList(list);

                sanPhamAdapter.notifyDataSetChanged();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogView.dismiss();
            }
        });

        dialogView.show();
    }


    private List<sanPham> getListSanPham() {
        List <sanPham> list = new ArrayList<>();

        SQLServerConnection db = new SQLServerConnection();
        Connection con = db.getConnection();
        try {
            Statement stm = con.createStatement();
            String query = "select * from product";
            ResultSet resultSet = stm.executeQuery(query);
            while (resultSet.next()){
                sanPham sp = new sanPham();
                sp.setMaSP(resultSet.getString("id"));
                sp.setTenSP(resultSet.getString("name"));
                sp.setDonGia(resultSet.getDouble("unit_price"));
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
