package com.example.alimekho.Activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.widget.Button;
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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alimekho.Adapter.PNKAdapter;
import com.example.alimekho.DataBase.SQLServerConnection;
import com.example.alimekho.Model.nhaCungCap;
import com.example.alimekho.Model.phieuNhapKho;
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
    private String pcs, ncc, npt;
    final Calendar myCalendar= Calendar.getInstance();
    private SQLServerConnection db = new SQLServerConnection();
    private Connection conn = db.getConnection();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qlpnk);

        //test
        RecyclerView rcv = findViewById(R.id.rcv);
        PNKAdapter adapter = new PNKAdapter(this, getListPNK());
        rcv.setLayoutManager(new LinearLayoutManager(this));
        rcv.setAdapter(adapter);
        Button btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        //them
        CardView addPNK = findViewById(R.id.add_button);
        addPNK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(QLPNKActivity.this, CreatePNK1Activity.class));
            }
        });
        //xoa
        ImageView delBtn = findViewById(R.id.icon_delete);
        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.deleteCheckedItems();
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

                Spinner spinner1 = dialog.findViewById(R.id.spinnerncc);
                ArrayList<String> nccs = getNCC();
                ArrayList<String> nccss = getmaNCC();
                spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        ncc = nccss.get(position);
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                ArrayAdapter<String> adapter1 = new ArrayAdapter<>(QLPNKActivity.this, android.R.layout.simple_spinner_dropdown_item, nccs);
                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner1.setAdapter(adapter1);

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
                                    ", supplier_id = " + ncc +
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
    private ArrayList<phieuNhapKho> getListPNK() {
        ArrayList<phieuNhapKho> l = new ArrayList<>();
        try {
            Statement stm = conn.createStatement();
            String getInputform = "select input_form.id, input_form.input_day, supplier.name, employee.name, total from input_form \n" +
                    "LEFT JOIN supplier ON input_form.supplier_id = supplier.id\n" +
                    "LEFT JOIN employee ON input_form.emp_id = employee.id";
            ResultSet rs = stm.executeQuery(getInputform);
            while (rs.next()) {
                String pattern = "dd/MM/yyyy";
                DateFormat df = new SimpleDateFormat(pattern);
                String todayAsString = df.format(rs.getDate(2));
                l.add(new phieuNhapKho(String.valueOf(rs.getInt(1)), todayAsString, rs.getString(3), rs.getString(4), rs.getDouble(5)));
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
            String getInputform = "select id from input_form";
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