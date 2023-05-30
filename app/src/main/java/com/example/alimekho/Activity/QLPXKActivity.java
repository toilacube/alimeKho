package com.example.alimekho.Activity;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alimekho.Adapter.PNKAdapter;
import com.example.alimekho.Adapter.PXKAdapter;
import com.example.alimekho.DataBase.SQLServerConnection;
import com.example.alimekho.Model.cuaHangXuat;
import com.example.alimekho.Model.phieuNhapKho;
import com.example.alimekho.Model.phieuXuatKho;
import com.example.alimekho.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

public class QLPXKActivity extends AppCompatActivity {
    final Calendar myCalendar= Calendar.getInstance();
    private List<phieuXuatKho> listPXK = new ArrayList<>();
    private List<String> listCHX = new ArrayList<>();
    private List<String> listNPT = new ArrayList<>();
    private PXKAdapter adapter;
    private RecyclerView rcv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qlpxk);
        //select all
        CheckBox checkbox = findViewById(R.id.checkbox);
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()) adapter.selectAll();
                else adapter.unSelectAll();
            }
        });

        //back btn
        Button backBtn = findViewById(R.id.btn_back);
        backBtn.setOnClickListener(view -> startActivity(new Intent(this, HomeActivity.class)));
        //add btn
        CardView addBtn = findViewById(R.id.add_button);
        addBtn.setOnClickListener(view -> startActivity(new Intent(this, CreatePXK1Activity.class)));

        //show
        rcv = findViewById(R.id.rcv);
        listPXK = getListPXK();
        adapter = new PXKAdapter(this, listPXK);
        rcv.setAdapter(adapter);
        rcv.setLayoutManager(new LinearLayoutManager(this));

        //xoa
        ImageView delBtn = findViewById(R.id.icon_delete);
        delBtn.setOnClickListener(view -> adapter.deleteCheckedItems());

        //search
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
        editBtn.setOnClickListener(view -> {
            Dialog dialog = new Dialog(QLPXKActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.edit_pxk);
            Window window = dialog.getWindow();
            if (window == null) return;
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            Spinner spnMaPhieu = dialog.findViewById(R.id.maPhieu);
            Spinner spnCHX = dialog.findViewById(R.id.CHX);
            Spinner spnNPT = dialog.findViewById(R.id.NPT);

            List<String> maPhieus = listPXK.stream().map(phieuXuatKho::getMaPhieu).collect(Collectors.toList());
            spnMaPhieu.setAdapter(new ArrayAdapter<>(QLPXKActivity.this, R.layout.style_spinner_form, maPhieus));
            spnCHX.setAdapter(new ArrayAdapter<>(QLPXKActivity.this, R.layout.style_spinner_form, listCHX));
            spnNPT.setAdapter(new ArrayAdapter<>(QLPXKActivity.this, R.layout.style_spinner_form, listNPT));

            EditText et = dialog.findViewById(R.id.day);
            DatePickerDialog.OnDateSetListener date = (view1, year, month, day) -> {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                et.setText(new SimpleDateFormat("dd/MM/yyyy").format(myCalendar.getTime()));
            };
            et.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new DatePickerDialog(QLPXKActivity.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            });

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
                    SQLServerConnection db = new SQLServerConnection();
                    Connection conn = db.getConnection();
                    try {
                        Statement stm = conn.createStatement();
                        String Query = "set dateformat dmy\nupdate [output_form]" +
                                "\nset emp_id = " + spnNPT.getSelectedItem().toString().split(" - ")[0] +
                                ", supermarket_id = " + spnCHX.getSelectedItem().toString().split(" - ")[0] +
                                ", output_day = " + "'" + et.getText() + "'" +
                                "\n where id = " + spnMaPhieu.getSelectedItem();
                        stm.executeUpdate(Query);
                        stm.close();
                        conn.close();
                        Toast.makeText(QLPXKActivity.this, "Thanh cong", Toast.LENGTH_SHORT).show();
                    } catch (SQLException e) {
                        e.printStackTrace();
                        Toast.makeText(QLPXKActivity.this, "That bai: " + e.toString(), Toast.LENGTH_SHORT).show();
                    }
                    recreate();
                }
            });
            dialog.show();
        });

    }
    private List<phieuXuatKho> getListPXK() {
        List<phieuXuatKho> list = new ArrayList<>();
        SQLServerConnection db = new SQLServerConnection();
        Connection conn = db.getConnection();

        try {
            Statement stm = conn.createStatement();
            String Query = "select f.id, f.output_day, s.name, e.name\n" +
                    "from output_form f, supermarket s, employee e\n" +
                    "where f.emp_id = e.id and f.supermarket_id = s.id and f.is_deleted = 0";
            ResultSet rs = stm.executeQuery(Query);
            while (rs.next()) {
                list.add(new phieuXuatKho(
                        rs.getString(1),
                        new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate(2)),
                        rs.getString(3),
                        rs.getString(4),
                        0.0)
                );
            }

            rs = stm.executeQuery("select id, name from supermarket");
            while (rs.next()) {
                listCHX.add(rs.getString(1) + " - " + rs.getString(2));
            }
            rs = stm.executeQuery("select id, name from employee");
            while (rs.next()) {
                listNPT.add(rs.getString(1) + " - " + rs.getString(2));
            }
            rs.close();
            stm.close();
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
    private void filterList(String newText) {
        ArrayList<phieuXuatKho> filteredList = new ArrayList<>();
        for (phieuXuatKho pxk : listPXK) {
            if (pxk.getMaPhieu().toLowerCase().contains(newText.toLowerCase())
                    || pxk.getNgayXuatKho().toLowerCase().contains(newText.toLowerCase())
                    || pxk.getTenCuaHangXuat().toLowerCase().contains(newText.toLowerCase())
                    || pxk.getTenNV().toLowerCase().contains(newText.toLowerCase()))
            {
                filteredList.add(pxk);
            }
        }
        adapter.setFilteredList(filteredList);
    }
}