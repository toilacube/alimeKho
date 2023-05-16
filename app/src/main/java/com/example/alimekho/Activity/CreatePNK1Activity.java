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
    private String product;
    final Calendar myCalendar= Calendar.getInstance();
    private ArrayList<CTPNK> ctpnks;
    private PNK1Adapter pnk1Adapter;
    private SQLServerConnection db = new SQLServerConnection();
    private Connection conn = db.getConnection();
    private int maPhieu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_pnk1);
        Init();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        pnk1Adapter = new PNK1Adapter(this, ctpnks);
        recyclerView.setAdapter(pnk1Adapter);
        btnBackHome.setOnClickListener(new View.OnClickListener() {
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
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreatePNK1Activity.this, CreatePNK2Activity.class);
                intent.putExtra("maPhieu", maPhieu);
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
        ctpnks = new ArrayList<>();
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
    }
    public ArrayList<String> getListSP(){
        ArrayList<String> l = new ArrayList<>();
        try {
            String select = "SELECT id, name FROM product";
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
            String select = "SELECT id FROM product";
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
    public void showDialog(){
        Dialog dialog = new Dialog(CreatePNK1Activity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_addsp);
        Window window = dialog.getWindow();
        if (window == null) return;
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView txtdonGia, txttenSP, txtsoLuong, txtNSX, txtHSD;
        Spinner spinner = dialog.findViewById(R.id.spinnermaSP);
        ArrayList<String> products = getListSP();
        ArrayList<String> productss = getMaSP();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                product = productss.get(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ArrayAdapter<String> adapter = new ArrayAdapter<>(CreatePNK1Activity.this, android.R.layout.simple_spinner_dropdown_item, products);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        txttenSP = dialog.findViewById(R.id.txttenSP);
        txtdonGia = dialog.findViewById(R.id.txtdonGia);
        txtsoLuong = dialog.findViewById(R.id.txtsoLuong);
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
                sanPham sanPham = new sanPham(product, txttenSP.getText().toString().trim(),
                        Double.valueOf(txtdonGia.getText().toString().trim()));
                CTPNK ctpnk = new CTPNK(String.valueOf(maPhieu), sanPham, Integer.parseInt(txtsoLuong.getText().toString().trim()),txtNSX.getText().toString().trim(), txtHSD.getText().toString().trim());
                ctpnks.add(ctpnk);
                pnk1Adapter.notifyDataSetChanged();

                try {
                    String insert = "INSERT INTO detail_input VALUES(?,?,?,?,?,?)";
                    PreparedStatement stm = conn.prepareStatement(insert);
                    stm.setInt(1, Integer.parseInt(ctpnk.getMaPNK()));
                    stm.setInt(2, Integer.parseInt(ctpnk.getSanPham().getMaSP()));
                    stm.setInt(3, ctpnk.getSoLuong());
                    String pattern = "MM/dd/yyyy";
                    DateFormat df = new SimpleDateFormat(pattern);
                    stm.setDate(4, (Date) df.parse(ctpnk.getNSX().toString().trim()));
                    stm.setDate(5, (Date) df.parse(ctpnk.getHSD().toString().trim()) );
                    stm.setInt(6, 0);
                    int rs = stm.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}