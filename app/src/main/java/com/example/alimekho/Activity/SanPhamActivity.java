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
import com.example.alimekho.Model.sanPham;
import com.example.alimekho.R;

import java.util.ArrayList;
import java.util.List;

public class SanPhamActivity extends AppCompatActivity {
    RecyclerView rcvSanPham;
    SanPhamAdapter sanPhamAdapter;
    Button btnAdd, btnBack;
    List<sanPham> list = new ArrayList<>();
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
//        sanPham sanPham = new sanPham();

        Dialog dialogView = new Dialog(this, android.R.style.Theme_Material_Light_Dialog_Presentation);
        dialogView.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogView.setContentView(R.layout.dialog_them_san_pham);

        EditText edtTenSP = dialogView.findViewById(R.id.edtTenSP),
                edtDonGia = dialogView.findViewById(R.id.edtDonGia),
                edtSoLuong = dialogView.findViewById(R.id.edtSoLuong),
                edtNSX = dialogView.findViewById(R.id.edtNSX),
                edtHSD = dialogView.findViewById(R.id.edtHSD),
                edtLoaiSP = dialogView.findViewById(R.id.edtLoaiSP),
                edtDonViTinh = dialogView.findViewById(R.id.edtDonVi);

        Button btnUpdate = dialogView.findViewById(R.id.btnUpdate),
                btnCancel = dialogView.findViewById(R.id.btnCancel);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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


//    private List<sanPham> getListSanPham() {
//        List <sanPham> list = new ArrayList<>();
//
//        list.add(new sanPham("sp1", "sua dac nong tinh khiet", 30.000,
//                "24/5/2022","14/9/2023" , 100, "thuc pham", "thung"));
//        list.add(new sanPham("sp1", "sua dac nong tinh khiet", 30.000,
//                "24/5/2022","14/9/2023" , 100, "thuc pham", "thung"));
//        list.add(new sanPham("sp1", "sua dac nong tinh khiet", 30.000,
//                "24/5/2022","14/9/2023" , 100, "thuc pham", "thung"));
//        list.add(new sanPham("sp1", "sua dac nong tinh khiet", 30.000,
//                "24/5/2022","14/9/2023" , 100, "thuc pham", "thung"));
//        list.add(new sanPham("sp1", "sua dac nong tinh khiet", 30.000,
//                "24/5/2022","14/9/2023" , 100, "thuc pham", "thung"));
//        list.add(new sanPham("sp1", "sua dac nong tinh khiet", 30.000,
//                "24/5/2022","14/9/2023" , 100, "thuc pham", "thung"));
//        return list;
//    }
}
