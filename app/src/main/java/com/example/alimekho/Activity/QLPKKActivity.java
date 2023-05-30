package com.example.alimekho.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alimekho.Adapter.PKKAdapter;
import com.example.alimekho.Adapter.SuperMarketAdapter;
import com.example.alimekho.DataBase.SQLServerConnection;
import com.example.alimekho.Model.Employee;
import com.example.alimekho.Model.cuaHangXuat;
import com.example.alimekho.Model.phieuKiemKho;
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

public class QLPKKActivity extends AppCompatActivity {
    final Calendar myCalendar= Calendar.getInstance();
    private List<phieuKiemKho> listPKK;
    private PKKAdapter adapter;
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
        setContentView(R.layout.activity_qlpkk);
        initUI();
        setEventListener();
    }

    private void initUI() {
        listPKK = new ArrayList<>();
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

        backBtn.setOnClickListener(view -> startActivity(new Intent(this, HomeActivity.class)));
        addBtn.setOnClickListener(view -> startActivity(new Intent(this, CreatePKKActivity.class)));
        delBtn.setOnClickListener(view -> adapter.deleteCheckedItems());
        editBtn.setOnClickListener(view -> showEditDialog());
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
        Dialog dialog = new Dialog(QLPKKActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.edit_pkk);

        Window window = dialog.getWindow();
        if (window == null) return;
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Spinner spnMa = dialog.findViewById(R.id.maPhieu);
        spnMa.setAdapter(new ArrayAdapter<>(
                QLPKKActivity.this,
                R.layout.style_spinner_form,
                listPKK.stream().map(phieuKiemKho::getMaPhieu).collect(Collectors.toList())));

        SQLServerConnection db = new SQLServerConnection();
        Connection conn = db.getConnection();
        List<String> listEmp = new ArrayList<>();
        try {
            Statement stm = conn.createStatement();
            String Query = "select id, name from employee";
            ResultSet rs = stm.executeQuery(Query);
            while (rs.next()) {
                listEmp.add(rs.getString("id") + " - " + rs.getString("name"));
            }
            rs.close();
            stm.close();
        } catch (SQLException e) {
            Toast.makeText(this, "Lỗi: " + e.toString(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        Spinner spnNV = dialog.findViewById(R.id.NPT);
        spnNV.setAdapter(new ArrayAdapter<>(
                QLPKKActivity.this,
                R.layout.style_spinner_form,
                listEmp));
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
                new DatePickerDialog(QLPKKActivity.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        CardView btnConfirm = dialog.findViewById(R.id.xacnhan);
        CardView btnCancel = dialog.findViewById(R.id.huy);
        btnCancel.setOnClickListener(v -> dialog.dismiss());
        btnConfirm.setOnClickListener(v -> {
            try {
                Statement stm = conn.createStatement();
                String Query = "set dateformat dmy\nupdate [check_form]\n"+
                        "SET check_date = '" + et.getText() +"', "+
                        "emp_id = N'" + spnNV.getSelectedItem().toString().split(" - ")[0] + "'\n" +
                        "where id = " + spnMa.getSelectedItem().toString();
                stm.executeUpdate(Query);

                Toast.makeText(QLPKKActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
            } catch (SQLException e) {
                e.printStackTrace();
                Toast.makeText(QLPKKActivity.this, "Thất bại: " +e.toString(), Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
            onRestart();
        });
        dialog.show();
    }
    private void renderListCHX() {
        SQLServerConnection db = new SQLServerConnection();
        Connection conn = db.getConnection();

        try {
            Statement stm = conn.createStatement();
            String Query = "select check_form.id, check_form.check_date, employee.name, check_form.is_solved\n"
                    +"from check_form, employee\n"
                    +"where check_form.is_deleted = 0 and check_form.emp_id = employee.id";
            ResultSet rs = stm.executeQuery(Query);
            while (rs.next()) {
                listPKK.add(new phieuKiemKho(
                        rs.getString("id"),
                        new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("check_date")),
                        rs.getString("name"),
                        rs.getInt("is_solved")
                ));
            }

            adapter = new PKKAdapter(this, listPKK);
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
        ArrayList<phieuKiemKho> filteredList = new ArrayList<>();
        for (phieuKiemKho pkk : listPKK) {
            if (pkk.getMaPhieu().toLowerCase().contains(newText.toLowerCase())
                    || pkk.getNgayKK().toLowerCase().contains(newText.toLowerCase())
                    || pkk.getTenNV().toLowerCase().contains(newText.toLowerCase()))
            {
                filteredList.add(pkk);
            }
        }
        adapter.setFilteredList(filteredList);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        listPKK.clear();
        renderListCHX();
    }
}