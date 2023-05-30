package com.example.alimekho.Activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alimekho.Adapter.PNKAdapter;
import com.example.alimekho.DataBase.SQLServerConnection;
import com.example.alimekho.Model.CTPNK;
import com.example.alimekho.Model.CTPXK;
import com.example.alimekho.Model.nhaCungCap;
import com.example.alimekho.Model.phieuNhapKho;
import com.example.alimekho.Model.sanPham;
import com.example.alimekho.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class QLPNKActivity extends AppCompatActivity {
    private String pcs, npt;
    final Calendar myCalendar= Calendar.getInstance();
    private SQLServerConnection db = new SQLServerConnection();
    private Connection conn = db.getConnection();
    private PNKAdapter pnkAdapter;
    private ArrayList<phieuNhapKho> phieuNhapKhos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qlpnk);
        phieuNhapKhos = getListPNK();
        RecyclerView rcv = findViewById(R.id.rcv);
        pnkAdapter = new PNKAdapter(this, phieuNhapKhos);
        rcv.setAdapter(pnkAdapter);
        rcv.setLayoutManager(new LinearLayoutManager(this));
        //test
        CheckBox checkBox = findViewById(R.id.checkbox);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pnkAdapter.setCheckAll(checkBox.isChecked());
            }
        });
        Button btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(QLPNKActivity.this, HomeActivity.class));
            }
        });
        //them
        CardView addPNK = findViewById(R.id.add_button);
        addPNK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(QLPNKActivity.this, CreatePNK2Activity.class));
            }
        });
        //xoa
        ImageView delBtn = findViewById(R.id.icon_delete);
        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pnkAdapter.deleteCheckedItems();
            }
        });
        SearchView searchView = findViewById(R.id.search_bar);
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
        //sua
        ImageView editBtn = findViewById(R.id.icon_edit);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(QLPNKActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.edit_pnk);
                Window window = dialog.getWindow();
                if (window == null) return;
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                Spinner spinner = dialog.findViewById(R.id.spinnerpcs);
                ArrayList<String> pcss = getmaPNK();
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        pcs = pcss.get(position);
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                ArrayAdapter<String> adapter = new ArrayAdapter<>(QLPNKActivity.this, android.R.layout.simple_spinner_dropdown_item, pcss);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);

                Spinner spinner2 = dialog.findViewById(R.id.spinnernpt);
                ArrayList<String> npts = getNPT();
                ArrayList<String> nptss = getmaNPT();
                spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        npt = nptss.get(position);
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                ArrayAdapter<String> adapter2 = new ArrayAdapter<>(QLPNKActivity.this, android.R.layout.simple_spinner_dropdown_item, npts);
                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner2.setAdapter(adapter2);
                CardView cancel = dialog.findViewById(R.id.huy);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                EditText ngaynhapkho = dialog.findViewById(R.id.ngaynhapkho);
                DatePickerDialog.OnDateSetListener date = (view1, year, month, day) -> {
                    myCalendar.set(Calendar.YEAR, year);
                    myCalendar.set(Calendar.MONTH,month);
                    myCalendar.set(Calendar.DAY_OF_MONTH,day);
                    ngaynhapkho.setText(new SimpleDateFormat("dd/MM/yyyy").format(myCalendar.getTime()));
                };
                ngaynhapkho.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new DatePickerDialog(QLPNKActivity.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                    }
                });
                CardView confirm = dialog.findViewById(R.id.xacnhan);
                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            Statement stm = conn.createStatement();
                            String Query = "set dateformat dmy\nupdate [input_form]" +
                                    "\nset emp_id = " + npt +
                                    ", input_day = " + "'" + ngaynhapkho.getText() + "'" +
                                    "\n where id = " + pcs;
                            stm.executeUpdate(Query);
                            Toast.makeText(QLPNKActivity.this, "Thanh cong", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            recreate();
                        } catch (SQLException e) {
                            e.printStackTrace();
                            Toast.makeText(QLPNKActivity.this, "That bai", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.show();
            }


        });
    }
    private void filterList(String newText) {
        ArrayList<phieuNhapKho> filteredList = new ArrayList<>();
        for (phieuNhapKho phieuNhapKho: phieuNhapKhos) {
            if (phieuNhapKho.getMaPhieu().toLowerCase().contains(newText.toLowerCase())
                    || phieuNhapKho.getNgayNhapKho().toLowerCase().contains(newText.toLowerCase())
                    || phieuNhapKho.getTenNCC().toLowerCase().contains(newText.toLowerCase())
                    || phieuNhapKho.getTenNV().toLowerCase().contains(newText.toLowerCase())){
                filteredList.add(phieuNhapKho);
            }
        }
            if (!filteredList.isEmpty()) {
                pnkAdapter.setFilteredList(filteredList);
            }
    }
    private ArrayList<phieuNhapKho> getListPNK() {
        ArrayList<phieuNhapKho> l = new ArrayList<>();
        try {
            Statement stm = conn.createStatement();
            String getInputform = "SET DATEFORMAT DMy\n" +
                    "select input_form.id, input_form.input_day, employee.name, total from input_form \n" +
                    "LEFT JOIN employee ON input_form.emp_id = employee.id\n" +
                    "WHERE input_form.is_deleted = 0";
            ResultSet rs = stm.executeQuery(getInputform);
            while (rs.next()) {
                String pattern = "dd/MM/yyyy";
                DateFormat df = new SimpleDateFormat(pattern);
                String todayAsString = df.format(rs.getDate(2));
                String tenNCC = "";
                try {
                    Statement stm1 = conn.createStatement();
                    String getInputform1 = "SET DATEFORMAT DMY\n" +
                            "SELECT supplier.name from batch\n" +
                            "LEFT JOIN detail_input ON batch.id = detail_input.batch_id\n" +
                            "LEFT JOIN product ON product.id = batch.product_id\n" +
                            "LEFT JOIN supplier ON product.supplier_id = supplier.id\n" +
                            "WHERE form_id = " + rs.getInt(1);
                    ResultSet rs1 = stm1.executeQuery(getInputform1);
                    if (rs1.next()) {
                        tenNCC = rs1.getString(1);
                    }
                    l.add(new phieuNhapKho(String.valueOf(rs.getInt(1)), todayAsString, tenNCC, rs.getString(3), rs.getDouble(4)));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return l;
    }
    private ArrayList<String> getmaPNK() {
        ArrayList<String> l = new ArrayList<>();
        try {
            Statement stm = conn.createStatement();
            String getInputform = "select id from input_form" +
                    "\nwhere is_deleted = 0";
            ResultSet rs = stm.executeQuery(getInputform);
            while (rs.next()) {
                String temp =  String.valueOf(rs.getInt(1));
                l.add(temp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return l;
    }
    private ArrayList<String> getNCC() {
        ArrayList<String> l = new ArrayList<>();
        try {
            Statement stm = conn.createStatement();
            String getncc = "select id, name from supplier";
            ResultSet rs = stm.executeQuery(getncc);
            while (rs.next()) {
                String temp =  String.valueOf(rs.getInt(1)) + " - " + rs.getString(2);
                l.add(temp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return l;
    }
    private ArrayList<String> getmaNCC() {
        ArrayList<String> l = new ArrayList<>();
        try {
            Statement stm = conn.createStatement();
            String getncc = "select id from supplier";
            ResultSet rs = stm.executeQuery(getncc);
            while (rs.next()) {
                String temp =  String.valueOf(rs.getInt(1));
                l.add(temp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return l;
    }

    private ArrayList<String> getNPT() {
        ArrayList<String> l = new ArrayList<>();
        try {
            Statement stm = conn.createStatement();
            String getnpt = "select id, name from employee";
            ResultSet rs = stm.executeQuery(getnpt);
            while (rs.next()) {
                String temp =  String.valueOf(rs.getInt(1)) + " - " + rs.getString(2);
                l.add(temp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return l;
    }
    private ArrayList<String> getmaNPT() {
        ArrayList<String> l = new ArrayList<>();
        try {
            Statement stm = conn.createStatement();
            String getnpt = "select id from employee";
            ResultSet rs = stm.executeQuery(getnpt);
            while (rs.next()) {
                String temp =  String.valueOf(rs.getInt(1));
                l.add(temp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return l;
    }
}