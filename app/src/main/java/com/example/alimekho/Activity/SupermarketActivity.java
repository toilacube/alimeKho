package com.example.alimekho.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alimekho.Adapter.SuperMarketAdapter;
import com.example.alimekho.DataBase.SQLServerConnection;
import com.example.alimekho.Model.cuaHangXuat;
import com.example.alimekho.Model.phieuXuatKho;
import com.example.alimekho.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SupermarketActivity extends AppCompatActivity {

    private List<cuaHangXuat> listCHX;
    private SuperMarketAdapter adapter;
    private RecyclerView rcv;
    private Button backBtn;
    private CheckBox selectAll;
    private CardView addBtn;
    private ImageView delBtn;
    private SearchView searchView;
    private ImageView editBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supermarket);
        initUI();
        setEventListener();
    }

    private void initUI() {
        listCHX = new ArrayList<>();
        renderListCHX();
        findViewById();
    }

    private void findViewById() {
        backBtn = findViewById(R.id.btn_back);
        editBtn = findViewById(R.id.icon_edit);
        searchView = findViewById(R.id.search_bar);
        delBtn = findViewById(R.id.icon_delete);
        addBtn = findViewById(R.id.add_button);
        backBtn = findViewById(R.id.btn_back);
        selectAll = findViewById(R.id.checkbox);
    }

    private void setEventListener() {
        selectAll.setOnCheckedChangeListener((compoundButton, b) -> {
            if (compoundButton.isChecked()) adapter.selectAll();
            else adapter.unSelectAll();
        });

        if (getSharedPreferences("user info", MODE_PRIVATE).getInt("role", 0) != 0) {
            addBtn.setOnClickListener(view -> showAddDialog());
            delBtn.setOnClickListener(view -> adapter.deleteCheckedItems());
            editBtn.setOnClickListener(view -> showEditDialog());
        } else {
            addBtn.setOnClickListener(view -> Toast.makeText(this, "Bạn không có quyền thực hiện thao tác này", Toast.LENGTH_SHORT).show());
            delBtn.setOnClickListener(view -> Toast.makeText(this, "Bạn không có quyền thực hiện thao tác này", Toast.LENGTH_SHORT).show());
            editBtn.setOnClickListener(view -> Toast.makeText(this, "Bạn không có quyền thực hiện thao tác này", Toast.LENGTH_SHORT).show());
        }
        backBtn.setOnClickListener(view -> startActivity(new Intent(this, HomeActivity.class)));
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

    private void showEditDialog() {
        Dialog dialog = new Dialog(SupermarketActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_update_supermarket);

        Window window = dialog.getWindow();
        if (window == null) return;
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Spinner spnMa = dialog.findViewById(R.id.ma);
        spnMa.setAdapter(new ArrayAdapter<>(
                SupermarketActivity.this,
                R.layout.style_spinner_form,
                listCHX.stream().map(cuaHangXuat::getMaCHX).collect(Collectors.toList())));
        EditText txtTenCHX, txtdiaChi, txtSDT;

        txtTenCHX = dialog.findViewById(R.id.ten);
        txtdiaChi = dialog.findViewById(R.id.diachi);
        txtSDT = dialog.findViewById(R.id.sdt);
        CardView btnConfirm = dialog.findViewById(R.id.xacnhan);
        CardView btnCancel = dialog.findViewById(R.id.huy);
        btnCancel.setOnClickListener(v -> dialog.dismiss());
        btnConfirm.setOnClickListener(v -> {
            try {
                SQLServerConnection db = new SQLServerConnection();
                Connection conn = db.getConnection();
                Statement stm = conn.createStatement();
                String Query = "update [supermarket]\n"+
                        "SET address = N'" + txtdiaChi.getText().toString().trim() +"', "+
                        "name = N'" + txtTenCHX.getText().toString().trim() + "', " +
                        "phone = '" + txtSDT.getText().toString().trim() + "'\n"
                        + "where id = " + spnMa.getSelectedItem().toString();
                Query = "exec pro_sua_dia_diem_xuat @id = " + spnMa.getSelectedItem().toString() +
                        ", @name = N'" + txtTenCHX.getText().toString().trim() + "', " +
                        "@address = N'" + txtdiaChi.getText().toString().trim() +"', " +
                        "@phone = '" + txtSDT.getText().toString().trim() + "'";
                stm.executeUpdate(Query);

                Toast.makeText(SupermarketActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
            } catch (SQLException e) {
                e.printStackTrace();
                Toast.makeText(SupermarketActivity.this, "Thất bại: " +e.toString(), Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
            recreate();
        });
        dialog.show();
    }

    private void showAddDialog() {
        Dialog dialog = new Dialog(SupermarketActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_addchx);

        Window window = dialog.getWindow();
        if (window == null) return;
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView txtTenCHX, txtdiaChi, txtSDT;

        txtTenCHX = dialog.findViewById(R.id.txttenCHX);
        txtdiaChi = dialog.findViewById(R.id.txtdiaChi);
        txtSDT = dialog.findViewById(R.id.txtSDT);
        Button btnAdd = dialog.findViewById(R.id.btnAdd);
        Button btnCancle = dialog.findViewById(R.id.btnBack);
        btnCancle.setOnClickListener(v -> dialog.dismiss());
        btnAdd.setOnClickListener(v -> {
            cuaHangXuat cuaHangXuat = new cuaHangXuat("",
                    txtTenCHX.getText().toString().trim(),
                    txtSDT.getText().toString().trim(),
                    txtdiaChi.getText().toString().trim());
            try {
                SQLServerConnection db = new SQLServerConnection();
                Connection conn = db.getConnection();
                Statement stm = conn.createStatement();
                String Query = "INSERT INTO [supermarket] ([address], [name], [phone], [is_deleted])\n"+
                        "VALUES (N'" + cuaHangXuat.getDiaChi() +"', "+
                        "N'" + cuaHangXuat.getTenCHX() + "', " +
                        "'" + cuaHangXuat.getSDT() + "'," +
                        "0)";
                Query = "exec pro_them_dia_diem_xuat @name = N'" + cuaHangXuat.getTenCHX() + "', " +
                        "@address = N'" + cuaHangXuat.getDiaChi() +"', " +
                        "@phone = '" + cuaHangXuat.getSDT() + "',";
                stm.executeUpdate(Query);
                try {
                    ResultSet rs = stm.executeQuery(" SELECT IDENT_CURRENT('[supermarket]')");
                    if (rs.next()) cuaHangXuat.setMaCHX(rs.getString(1));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                listCHX.add(cuaHangXuat);
                adapter.notifyDataSetChanged();
                Toast.makeText(SupermarketActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
            } catch (SQLException e) {
                e.printStackTrace();
                Toast.makeText(SupermarketActivity.this, "Thất bại: " +e.toString(), Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
        });
        dialog.show();
    }

    private void renderListCHX() {
        SQLServerConnection db = new SQLServerConnection();
        Connection conn = db.getConnection();

        try {
            Statement stm = conn.createStatement();
            String Query = "select * from supermarket where is_deleted = 0";
            ResultSet rs = stm.executeQuery(Query);
            while (rs.next()) {
                listCHX.add(new cuaHangXuat(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("address"))
                );
            }

            adapter = new SuperMarketAdapter(this, listCHX);
            rcv = findViewById(R.id.rcv);
            rcv.setAdapter(adapter);
            rcv.setLayoutManager(new LinearLayoutManager(this));

            rs.close();
            stm.close();
            conn.close();
        } catch (SQLException e) {
            Toast.makeText(this, "Lỗi: " + e.toString(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
    private void filterList(String newText) {
        ArrayList<cuaHangXuat> filteredList = new ArrayList<>();
        for (cuaHangXuat chx : listCHX) {
            if (chx.getMaCHX().toLowerCase().contains(newText.toLowerCase())
                    || chx.getTenCHX().toLowerCase().contains(newText.toLowerCase())
                    || chx.getDiaChi().toLowerCase().contains(newText.toLowerCase())
                    || chx.getSDT().toLowerCase().contains(newText.toLowerCase()))
            {
                filteredList.add(chx);
            }
        }
        adapter.setFilteredList(filteredList);
    }
}