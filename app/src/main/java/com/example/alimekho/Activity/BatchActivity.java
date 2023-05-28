package com.example.alimekho.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
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
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alimekho.Adapter.BatchAdapter;
import com.example.alimekho.Adapter.PNK1Adapter;
import com.example.alimekho.Adapter.SupplierAdapter;
import com.example.alimekho.DataBase.SQLServerConnection;
import com.example.alimekho.Model.loSanPham;
import com.example.alimekho.Model.nhaCungCap;
import com.example.alimekho.Model.sanPham;
import com.example.alimekho.R;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class BatchActivity extends AppCompatActivity {
    private Button btnBack;
    private CardView btnAdd;
    private ImageView btnDelete;
    private CheckBox cb;
    private RecyclerView recyclerView;
    private SearchView searchView;
    final Calendar myCalendar= Calendar.getInstance();
    private ArrayList<loSanPham> loSanPhams;
    private String product_id, name;
    private BatchAdapter batchAdapter;
    private SQLServerConnection db = new SQLServerConnection();
    private Connection conn = db.getConnection();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batch);
        Init();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        batchAdapter = new BatchAdapter(this, loSanPhams);
        recyclerView.setAdapter(batchAdapter);
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
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                batchAdapter.deleteCheckedItems();
            }
        });
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                batchAdapter.setCheckAll(cb.isChecked());
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
    private void filterList(String newText) {
        ArrayList<loSanPham> filteredList = new ArrayList<>();
        for (loSanPham loSanPham : loSanPhams) {
            if (loSanPham.getMaLo().toLowerCase().contains(newText.toLowerCase())
                    || loSanPham.getSanPham().getTenSP().toLowerCase().contains(newText.toLowerCase())){
                filteredList.add(loSanPham);
            }
        }
        batchAdapter.setFilteredList(filteredList);
    }
    public void Init(){
        btnAdd = findViewById(R.id.add_button);
        btnBack = findViewById(R.id.btn_back);
        recyclerView = findViewById(R.id.rcv);
        cb = findViewById(R.id.check_box);
        btnDelete = findViewById(R.id.icon_delete);
        searchView = findViewById(R.id.search_bar);
        loSanPhams= getListLoSP();
    }
    public ArrayList<loSanPham> getListLoSP(){
        ArrayList<loSanPham> l = new ArrayList<>();
        try {
            String select = "SET DATEFORMAT DMY\n" +
                    "SELECT batch.id, product_id, NSX, HSD, unit_price, product.name, quantity, not_stored FROM batch\n" +
                    "JOIN product ON batch.product_id = product.id\n" +
                    "WHERE batch.is_deleted = 0";
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
    public ArrayList<String> getListSP(){
        ArrayList<String> l = new ArrayList<>();
        try {
            String select = "SELECT id, name FROM product\n" +
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
    public ArrayList<String> getMaSP(){
        ArrayList<String> l = new ArrayList<>();
        try {
            String select = "SELECT id FROM product\n" +
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
    public ArrayList<String> getTenSP(){
        ArrayList<String> l = new ArrayList<>();
        try {
            String select = "SELECT name FROM product\n" +
                    "WHERE is_deleted = 0";
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
        Dialog dialog = new Dialog(BatchActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_addsp);
        Window window = dialog.getWindow();
        if (window == null) return;
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView txtdonGia, txtNSX, txtHSD;
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
                Toast.makeText(BatchActivity.this, "Selected", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ArrayAdapter<String> adapter = new ArrayAdapter<>(BatchActivity.this, android.R.layout.simple_spinner_dropdown_item, products);
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
                new DatePickerDialog(BatchActivity.this, date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
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
                new DatePickerDialog(BatchActivity.this, date1,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
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
                int maLo = -1;
                try {
                    String select = "SELECT IDENT_CURRENT('batch')";
                    Statement stm1 = conn.createStatement();
                    ResultSet rs1 = stm1.executeQuery(select);
                    while(rs1.next()){
                        maLo = rs1.getInt(1);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                loSanPham.setMaLo(String.valueOf(maLo));
                loSanPhams.add(loSanPham);
                batchAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}