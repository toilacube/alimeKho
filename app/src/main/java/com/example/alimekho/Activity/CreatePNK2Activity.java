package com.example.alimekho.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alimekho.Adapter.PNK2Adapter;
import com.example.alimekho.DataBase.SQLServerConnection;
import com.example.alimekho.Model.nhaCungCap;
import com.example.alimekho.R;
import com.example.alimekho.Model.sanPham;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class CreatePNK2Activity extends AppCompatActivity implements PNK2Adapter.setData{
    private Button btnBackHome, btnContinue, btnAdd, btnBack;
    private RecyclerView recyclerView;
    private TextView txtmaNCC, txtTenNCC, txtdiaChi, txtSDT;
    private SearchView searchView;
    private ArrayList<nhaCungCap> nhaCungCaps;
    private PNK2Adapter pnk2Adapter;
    private SQLServerConnection db = new SQLServerConnection();
    private Connection conn = db.getConnection();
    private int maPhieu;
    private int maNCC;
    private String tenNCC;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_pnk2);
        Init();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        pnk2Adapter = new PNK2Adapter(this, nhaCungCaps);
        recyclerView.setAdapter(pnk2Adapter);
        btnBackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String delete = "DELETE FROM detail_input WHERE form_id = ?";
                    String delete1 = "DELETE FROM input_form WHERE id = ?";
                    PreparedStatement stm = conn.prepareStatement(delete);
                    PreparedStatement stm1 = conn.prepareStatement(delete1);
                    stm.setInt(1, maPhieu);
                    stm1.setInt(1, maPhieu);
                    int rs = stm.executeUpdate();
                    int rs1 = stm1.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                onBackPressed();
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String delete = "DELETE FROM detail_input WHERE form_id = ?";
                    String delete1 = "DELETE FROM input_form WHERE id = ?";
                    PreparedStatement stm = conn.prepareStatement(delete);
                    PreparedStatement stm1 = conn.prepareStatement(delete1);
                    stm.setInt(1, maPhieu);
                    stm1.setInt(1, maPhieu);
                    int rs = stm.executeUpdate();
                    int rs1 = stm1.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                onBackPressed();
                onBackPressed();
            }
        });
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(txtmaNCC.getText().toString().trim())){
                    Toast.makeText(CreatePNK2Activity.this, "Vui lòng chọn nhà cung cấp!", Toast.LENGTH_SHORT).show();
                    return;
                }
                maNCC = Integer.parseInt(txtmaNCC.getText().toString().trim());
                tenNCC = txtTenNCC.getText().toString().trim();
                try {
                    String insert = "INSERT INTO input_form (emp_id, input_day) VALUES(?, GETDATE())";
                    PreparedStatement stm = conn.prepareStatement(insert);
                    SharedPreferences preferences = getSharedPreferences("user info", MODE_PRIVATE);
                    String name = preferences.getString("id", "");
                    stm.setString(1, name);
                    int rs = stm.executeUpdate();
                    String select = "SELECT IDENT_CURRENT('input_form')";
                    Statement stm1 = conn.createStatement();
                    ResultSet rs1 = stm1.executeQuery(select);
                    while(rs1.next()){
                        maPhieu = rs1.getInt(1);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(CreatePNK2Activity.this, CreatePNK1Activity.class);
                intent.putExtra("maPhieu", maPhieu);
                intent.putExtra("maNCC", maNCC);
                intent.putExtra("tenNCC", tenNCC);
                startActivity(intent);
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
    public void setData(nhaCungCap nhaCungCap){
        txtmaNCC.setText(nhaCungCap.getMaNCC());
        txtTenNCC.setText(nhaCungCap.getTenCC());
        txtdiaChi.setText(nhaCungCap.getDiaChi());
        txtSDT.setText(nhaCungCap.getSDT());
    }
    private void filterList(String newText) {
        ArrayList<nhaCungCap> filteredList = new ArrayList<>();
        for (nhaCungCap nhaCungCap : nhaCungCaps) {
            if (nhaCungCap.getTenCC().toLowerCase().contains(newText.toLowerCase())
                    || nhaCungCap.getMaNCC().toLowerCase().contains(newText.toLowerCase())){
                filteredList.add(nhaCungCap);
            }
        }
        pnk2Adapter.setFilteredList(filteredList);
    }
    public void Init(){
        btnBackHome = findViewById(R.id.btn_back_createPNK2);
        btnAdd = findViewById(R.id.gdcreatePNK2_btnAdd);
        btnContinue = findViewById(R.id.gdcreatePNK2_btnContinue);
        btnBack = findViewById(R.id.gdcreatePNK2_btnBack);
        recyclerView = findViewById(R.id.gdcreatePNK2_rcv);
        searchView = findViewById(R.id.gdcreatePNK2_sv);
        txtmaNCC = findViewById(R.id.maNCC);
        txtTenNCC = findViewById(R.id.tenNCC);
        txtdiaChi = findViewById(R.id.diaChi);
        txtSDT = findViewById(R.id.sdt);
        nhaCungCaps = new ArrayList<>();
        try {
            Statement stm = conn.createStatement();
            String getSupplier = "select * from supplier";
            ResultSet rs = stm.executeQuery(getSupplier);
            while (rs.next()) {
                nhaCungCaps.add(new nhaCungCap(String.valueOf(rs.getInt(1)), rs.getString(2), rs.getString(3), rs.getString(4)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void showDialog(){
        Dialog dialog = new Dialog(CreatePNK2Activity.this);
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
                String maNCC = "";
                try {
                    String insert = "INSERT INTO [supplier] ([name], [address], [phone]) VALUES(?,?,?)";
                    PreparedStatement stm = conn.prepareStatement(insert);
                    stm.setString(1, txttenNCC.getText().toString().trim());
                    stm.setString(2, txtdiaChi.getText().toString().trim());
                    stm.setString(3, txtSDT.getText().toString().trim());
                    int rs = stm.executeUpdate();
                    String select = "SELECT IDENT_CURRENT('supplier')";
                    Statement stm1 = conn.createStatement();
                    ResultSet rs1 = stm1.executeQuery(select);
                    while(rs1.next()){
                        maNCC = String.valueOf(rs1.getInt(1));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                nhaCungCap nhaCungCap = new nhaCungCap(maNCC, txttenNCC.getText().toString().trim(),
                        txtdiaChi.getText().toString().trim(), txtSDT.getText().toString().trim());
                nhaCungCaps.add(nhaCungCap);
                pnk2Adapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void setNhaCungCap(nhaCungCap nhaCungCap) {
        txtmaNCC.setText(nhaCungCap.getMaNCC().toString().trim());
        txtTenNCC.setText(nhaCungCap.getTenCC().toString().trim());
        txtdiaChi.setText(nhaCungCap.getDiaChi().toString().trim());
        txtSDT.setText(nhaCungCap.getSDT().toString().trim());
    }
}