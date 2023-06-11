package com.example.alimekho.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alimekho.Adapter.PNK2Adapter;
import com.example.alimekho.Adapter.SupplierAdapter;
import com.example.alimekho.DataBase.SQLServerConnection;
import com.example.alimekho.Model.nhaCungCap;
import com.example.alimekho.Model.phieuNhapKho;
import com.example.alimekho.R;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class SupplierActivity extends AppCompatActivity {
    private Button btnBack;
    private CardView btnAdd;
    private ImageView btnEdit, btnDelete;
    private CheckBox cb;
    private RecyclerView recyclerView;
    private SearchView searchView;
    private ArrayList<nhaCungCap> nhaCungCaps;
    private SupplierAdapter supplierAdapter;
    private SQLServerConnection db = new SQLServerConnection();
    private Connection conn = db.getConnection();
    String temp = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier);
        Init();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        supplierAdapter = new SupplierAdapter(this, nhaCungCaps);
        recyclerView.setAdapter(supplierAdapter);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
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
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(SupplierActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.edit_ncc);
                Window window = dialog.getWindow();
                if (window == null) return;
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                Spinner spinner = dialog.findViewById(R.id.spinnerncc);
                TextView txttenNCC = dialog.findViewById(R.id.tenNCC);
                TextView txtdiaChi = dialog.findViewById(R.id.diaChi);
                TextView txtSDT = dialog.findViewById(R.id.SDT);
                ArrayList<String> suppliers = getListNCC();
                ArrayList<String> supplierss = getmaNCC();
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        temp = supplierss.get(position);
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                ArrayAdapter<String> adapter = new ArrayAdapter<>(SupplierActivity.this, android.R.layout.simple_spinner_dropdown_item, suppliers);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
                CardView cancel = dialog.findViewById(R.id.huy);

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                CardView confirm = dialog.findViewById(R.id.xacnhan);
                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            String Update = "UPDATE supplier\n" +
                                    "SET name = ?, phone = ?, address = ?\n" +
                                    "WHERE id = ?";
                            Update = "exec pro_sua_nha_cung_cap @name = ?, @phone = ?, @address = ?, @maNCC = ?";
                            PreparedStatement stm = conn.prepareStatement(Update);
                            stm.setString(1, txttenNCC.getText().toString().trim());
                            stm.setString(2, txtSDT.getText().toString().trim());
                            stm.setString(3, txtdiaChi.getText().toString().trim());
                            stm.setString(4, temp);
                            stm.executeUpdate();
                            Toast.makeText(SupplierActivity.this, "Thanh cong", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            recreate();
                        } catch (SQLException e) {
                            e.printStackTrace();
                            Toast.makeText(SupplierActivity.this, "That bai", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.show();
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                supplierAdapter.deleteCheckedItems();
            }
        });
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                supplierAdapter.setCheckAll(cb.isChecked());
            }
        });

    }
    public void showDialog(){
        Dialog dialog = new Dialog(SupplierActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_addncc);
        Window window = dialog.getWindow();
        if (window == null) return;
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView txttenNCC, txtdiaChi, txtSDT;
        txttenNCC = dialog.findViewById(R.id.txttenNCC);
        txtdiaChi = dialog.findViewById(R.id.txtdiaChi);
        txtSDT = dialog.findViewById(R.id.txtSDT);
        Button btnAdd = dialog.findViewById(R.id.btnAdd);
        Button btnCancle = dialog.findViewById(R.id.btnBack);
        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String insert = "INSERT INTO [supplier] ([name], [address], [phone], [is_deleted]) VALUES(?,?,?,0)";
                    insert = "exec pro_them_nha_cung_cap @name = ?,@address = ?, @phone = ? ";
                    PreparedStatement stm = conn.prepareStatement(insert);
                    stm.setString(1, txttenNCC.getText().toString().trim());
                    stm.setString(2, txtdiaChi.getText().toString().trim());
                    stm.setString(3, txtSDT.getText().toString().trim());
                    int rs = stm.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
                recreate();
            }
        });
        dialog.show();
    }
    private void filterList(String newText) {
        ArrayList<nhaCungCap> filteredList = new ArrayList<>();
        for (nhaCungCap nhaCungCap : nhaCungCaps) {
            if (nhaCungCap.getTenCC().toLowerCase().contains(newText.toLowerCase())
                    || nhaCungCap.getMaNCC().toLowerCase().contains(newText.toLowerCase())){
                filteredList.add(nhaCungCap);
            }
        }
        supplierAdapter.setFilteredList(filteredList);
    }
    public void Init(){
        btnBack = findViewById(R.id.btn_back);
        btnAdd = findViewById(R.id.add_button);
        btnEdit = findViewById(R.id.icon_edit);
        btnDelete = findViewById(R.id.icon_delete);
        recyclerView = findViewById(R.id.rcv);
        searchView = findViewById(R.id.search_bar);
        cb = findViewById(R.id.check_box);
        nhaCungCaps = new ArrayList<>();
        try {
            Statement stm = conn.createStatement();
            String getSupplier = "select * from supplier\n" +
                    "WHERE is_deleted = 0";
            ResultSet rs = stm.executeQuery(getSupplier);
            while (rs.next()) {
                nhaCungCaps.add(new nhaCungCap(String.valueOf(rs.getInt(1)), rs.getString(2), rs.getString(3), rs.getString(4)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getListNCC(){
        ArrayList<String> l = new ArrayList<>();
        try {
            String select = "select id, name from supplier\n" +
                    "WHERE is_deleted = 0";
            Statement stm = conn.createStatement();
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
    public ArrayList<String> getmaNCC(){
        ArrayList<String> l = new ArrayList<>();
        try {
            String select = "select id from supplier\n" +
                    "WHERE is_deleted = 0";
            Statement stm = conn.createStatement();
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
}