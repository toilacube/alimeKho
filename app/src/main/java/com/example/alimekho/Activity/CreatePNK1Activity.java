package com.example.alimekho.Activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alimekho.DataBase.SQLServerConnection;
import com.example.alimekho.Model.CTPNK;
import com.example.alimekho.Model.CTPXK;
import com.example.alimekho.Model.loSanPham;
import com.example.alimekho.Model.sanPham;
import com.example.alimekho.R;
import com.example.alimekho.Adapter.PNK1Adapter;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class CreatePNK1Activity extends AppCompatActivity {
    private Button btnBackHome, btnContinue, btnAdd, btnBack;
    private RecyclerView recyclerView;
    private String product_id, name;
    final Calendar myCalendar= Calendar.getInstance();
    private ArrayList<loSanPham> loSanPhams;
    private PNK1Adapter pnk1Adapter;
    private SQLServerConnection db = new SQLServerConnection();
    private Connection conn = db.getConnection();
    private int maPhieu;
    private int maNCC;
    private String tenNCC;
    private static ArrayList<loSanPham> losanPhamDuocChon;
    public static ArrayList<loSanPham> spSelected(){
        return losanPhamDuocChon;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_pnk1);

        Init();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        pnk1Adapter = new PNK1Adapter(this, loSanPhams);
        recyclerView.setAdapter(pnk1Adapter);
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
            }
        });
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreatePNK1Activity.this, CreatePNK3Activity.class);
                intent.putExtra("maPhieu", maPhieu);
                intent.putExtra("maNCC", maNCC);
                intent.putExtra("tenNCC", tenNCC);
                losanPhamDuocChon = pnk1Adapter.getLoSanPhamdc();
                startActivity(intent);
            }
        });
    }
    public void Init(){
        btnBackHome = findViewById(R.id.btn_back_createPNK1);
        btnAdd = findViewById(R.id.gdcreatePNK1_btnAdd);
        btnContinue = findViewById(R.id.gdcreatePNK1_btnContinue);
        btnBack = findViewById(R.id.gdcreatePNK1_btnBack);
        recyclerView = findViewById(R.id.gdcreatePNK1_rcv);
        maPhieu = getIntent().getIntExtra("maPhieu", -1);
        maNCC = getIntent().getIntExtra("maNCC", -1);
        tenNCC = getIntent().getStringExtra("tenNCC");
        loSanPhams= getListLoSP();
    }
    public ArrayList<loSanPham> getListLoSP(){
        ArrayList<loSanPham> l = new ArrayList<>();
        try {
            String select = "SET DATEFORMAT DMY\n" +
                    "SELECT batch.id, product_id, NSX, HSD, unit_price, product.name FROM batch\n" +
                    "JOIN product ON batch.product_id = product.id\n" +
                    "WHERE DATEDIFF(DAY, GETDATE(), HSD) > 3 AND batch.is_deleted = 0 AND supplier_id = " + maNCC;
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(select);
            while(rs.next()){
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String NSX = dateFormat.format(rs.getDate("NSX"));
                String HSD = dateFormat.format(rs.getDate("HSD"));
                sanPham sanPham = new sanPham(String.valueOf(rs.getInt("product_id")), rs.getString(6));
                loSanPham loSanPham = new loSanPham(String.valueOf(rs.getInt(1)), NSX, HSD, sanPham, rs.getDouble("unit_price"));
                l.add(loSanPham);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return l;
    }
    public ArrayList<String> getListSP(){
        ArrayList<String> l = new ArrayList<>();
        try {
            String select = "SELECT id, name FROM product\n" +
                    "WHERE supplier_id = " + maNCC;
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
    public ArrayList<String> getMaSP(){
        ArrayList<String> l = new ArrayList<>();
        try {
            String select = "SELECT id FROM product\n" +
            "WHERE supplier_id = " + maNCC;
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
    public ArrayList<String> getTenSP(){
        ArrayList<String> l = new ArrayList<>();
        try {
            String select = "SELECT name FROM product\n" +
                    "WHERE supplier_id = " + maNCC;
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(select);
            while(rs.next()){
                String temp = rs.getString(1);
                l.add(temp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return l;
    }
    public void showDialog(){
        Dialog dialog = new Dialog(CreatePNK1Activity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_addsp);
        Window window = dialog.getWindow();
        if (window == null) return;
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView txtdonGia, txtsoLuong, txtNSX, txtHSD;
        Spinner spinner = dialog.findViewById(R.id.spinnermaSP);
        ArrayList<String> products = getListSP();
        ArrayList<String> products_id = getMaSP();
        ArrayList<String> products_name = getTenSP();
        txtdonGia = dialog.findViewById(R.id.txtdonGia);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                product_id = products_id.get(position);
                name = products_name.get(position);
                Toast.makeText(CreatePNK1Activity.this, "Selected", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ArrayAdapter<String> adapter = new ArrayAdapter<>(CreatePNK1Activity.this, android.R.layout.simple_spinner_dropdown_item, products);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        txtNSX = dialog.findViewById(R.id.txtNSX);
        txtHSD = dialog.findViewById(R.id.txtHSD);
        DatePickerDialog.OnDateSetListener date = (view1, year, month, day) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH,month);
            myCalendar.set(Calendar.DAY_OF_MONTH,day);
            txtNSX.setText(new SimpleDateFormat("dd/MM/yyyy").format(myCalendar.getTime()));
        };
        txtNSX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(CreatePNK1Activity.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        DatePickerDialog.OnDateSetListener date1 = (view1, year, month, day) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH,month);
            myCalendar.set(Calendar.DAY_OF_MONTH,day);
            txtHSD.setText(new SimpleDateFormat("dd/MM/yyyy").format(myCalendar.getTime()));
        };
        txtHSD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(CreatePNK1Activity.this,date1,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
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
                sanPham sanPham = new sanPham(product_id, name);
                loSanPham loSanPham = new loSanPham("",txtNSX.getText().toString().trim(), txtHSD.getText().toString().trim(), sanPham, Double.parseDouble(txtdonGia.getText().toString().trim()));
                try {
                    String insert = "INSERT INTO batch(NSX, HSD, product_id, unit_price, quantity, not_stored, is_deleted) VALUES(?,?,?,?,?,?,0)";
                    PreparedStatement stm = conn.prepareStatement(insert);
                    stm.setString(1, loSanPham.getNSX());
                    stm.setString(2, loSanPham.getHSD());
                    stm.setInt(3, Integer.parseInt(loSanPham.getSanPham().getMaSP()));
                    stm.setDouble(4, loSanPham.getDonGia());
                    stm.setInt(5, 0);
                    stm.setInt(6, 0);
                    int rs = stm.executeUpdate();
                    } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                dialog.dismiss();
                recreate();
            }
        });
        dialog.show();
    }
}