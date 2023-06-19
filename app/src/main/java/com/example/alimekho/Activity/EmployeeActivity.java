package com.example.alimekho.Activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alimekho.Adapter.EmployeeAdapter;
import com.example.alimekho.DataBase.SQLServerConnection;
import com.example.alimekho.Model.Employee;
import com.example.alimekho.R;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EmployeeActivity extends AppCompatActivity {
    Calendar myCalendar= Calendar.getInstance();
    private RecyclerView recyclerView;
    private EmployeeAdapter employeeAdapter;
    private Button btnAddEmp;
    private Button btnBackEmployee;
SQLServerConnection db = new SQLServerConnection();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);

        btnAddEmp = findViewById(R.id.btn_add_employee);
        btnBackEmployee=findViewById(R.id.btn_back_employee);
        recyclerView=findViewById(R.id.rv_employee);
        employeeAdapter=new EmployeeAdapter(this);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
        employeeAdapter.setData(getListEmployee());
        recyclerView.setAdapter(employeeAdapter);

        if (getSharedPreferences("user info", MODE_PRIVATE).getInt("role", -1) == 2) {
            btnAddEmp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Employee emp = new Employee();

                    AlertDialog.Builder builder=new AlertDialog.Builder(view.getRootView().getContext());
                    View dialogView= LayoutInflater.from(view.getRootView().getContext())
                            .inflate(R.layout.dialog_them_nhanvien,null);
                    AlertDialog dialog = builder.create();
                    dialog.setView(dialogView);

                    ArrayList<String> chucVu = new ArrayList<>();
                    chucVu.add("1. Quản lý");
                    chucVu.add("2. Kế toán");
                    chucVu.add("3. Nhân viên");

                    EditText edtTen, edtNgaySinh, edtCCCD, edtSDT, edtAddress, edtNgayVaoLam;
                    Spinner spinner;

                    edtNgayVaoLam = dialogView.findViewById(R.id.edtNgayVaoLam);
                    edtTen = dialogView.findViewById(R.id.edtTenNhanVien);
                    edtNgaySinh = dialogView.findViewById(R.id.edtNgaySinh);
                    edtCCCD = dialogView.findViewById(R.id.edtCCCD);
                    edtSDT = dialogView.findViewById(R.id.edtSDT);
                    edtAddress = dialogView.findViewById(R.id.edtAddress);
                    spinner = dialogView.findViewById(R.id.spChucVu);
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(EmployeeActivity.this, android.R.layout.simple_spinner_dropdown_item, chucVu);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);

                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            emp.setTitle(Integer.toString(2 - i));
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });

                    DatePickerDialog.OnDateSetListener date = (view1, year, month, day) -> {
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH,month);
                        myCalendar.set(Calendar.DAY_OF_MONTH,day);
                        edtNgaySinh.setText(new SimpleDateFormat("dd/MM/yyyy").format(myCalendar.getTime()));
                    };
                    DatePickerDialog.OnDateSetListener date2 = (view1, year, month, day) -> {
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH,month);
                        myCalendar.set(Calendar.DAY_OF_MONTH,day);
                        edtNgayVaoLam.setText(new SimpleDateFormat("dd/MM/yyyy").format(myCalendar.getTime()));
                    };
                    edtNgaySinh.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new DatePickerDialog(EmployeeActivity.this, date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();

                        }
                    });

                    edtNgayVaoLam.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new DatePickerDialog(EmployeeActivity.this, date2,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                        }
                    });


                    Button btnThem = dialogView.findViewById(R.id.btnUpdate);
                    Button btnHuy = dialogView.findViewById(R.id.btnCancel);

                    btnThem.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            emp.setName(edtTen.getText().toString());
                            emp.setIdentify(edtCCCD.getText().toString());
                            emp.setPhoneNumber(edtSDT.getText().toString());
                            emp.setNgayVaoLam(edtNgayVaoLam.getText().toString());
                            emp.setAddress(edtAddress.getText().toString());
                            String query = "SET DATEFORMAT DMY;\n" +
                                    "INSERT INTO [employee] ([name], [birthday], [indentify], [phone], [address], [start_day], [role], [is_deleted])\n" +
                                    "VALUES\n" +
                                    "  ( N'" + emp.getName() + "', '" + edtNgaySinh.getText() + "', '" + emp.getIdentify() +"', '" +
                                    emp.getPhoneNumber() + "', N'" +  emp.getAddress() + "', '" + edtNgayVaoLam.getText() + "', " +
                                    emp.getTitle() + ", 0)";
                            try {
//                                PreparedStatement stm = db.getConnection().prepareStatement(query);
//                                stm.setString(1, emp.getName());
//                                stm.setString(3, emp.getIdentify());
//                                stm.setString(4, emp.getPhoneNumber());
//                                stm.setString(2, emp.getDayOfBirth());
//                                stm.setString(5, emp.getAddress());
//                                stm.setString(6, emp.getNgayVaoLam());
//                                stm.setString(7, emp.getTitle());
//                                stm.executeUpdate();
                                Statement stm = db.getConnection().createStatement();
                                stm.executeUpdate(query);

                            } catch (SQLException e) {
                                Log.e(TAG, e.toString());
                            }
                            employeeAdapter.setData(getListEmployee());

                            dialog.dismiss();
                        }
                    });

                    btnHuy.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });

                    dialog.show();
                }
            });
        } else {
            btnAddEmp.setOnClickListener(view -> Toast.makeText(EmployeeActivity.this, "Bạn không có quyền thực hiện thao tác này", Toast.LENGTH_SHORT).show());
            btnAddEmp.setOnClickListener(view -> Toast.makeText(EmployeeActivity.this, "Bạn không có quyền thực hiện thao tác này", Toast.LENGTH_SHORT).show());
            btnAddEmp.setOnClickListener(view -> Toast.makeText(EmployeeActivity.this, "Bạn không có quyền thực hiện thao tác này", Toast.LENGTH_SHORT).show());
        }

        btnBackEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private List<Employee> getListEmployee()
    {
        List<Employee> list = new ArrayList<>();
        SQLServerConnection db = new SQLServerConnection();
        try {
            Statement stm = db.getConnection().createStatement();
            String query = "select * from employee where is_deleted < 1";
            ResultSet rs = stm.executeQuery(query);
            while(rs.next()){
                Employee emp = new Employee();
                emp.setId(Integer.toString(rs.getInt("id")));
                emp.setName(rs.getString("name"));

                String pattern = "dd/MM/yyyy";
                DateFormat df = new SimpleDateFormat(pattern);
                emp.setDayOfBirth(df.format(rs.getDate("birthday")));
                emp.setIdentify(rs.getString("indentify"));
                emp.setPhoneNumber(rs.getString("phone"));
                emp.setTitle(Integer.toString(rs.getInt("role")));
                list.add(emp);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return list;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(employeeAdapter!=null){
            employeeAdapter.release();
        }
    }
}