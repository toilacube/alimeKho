package com.example.alimekho.Activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alimekho.Adapter.EmployeeAdapter;
import com.example.alimekho.DataBase.SQLServerConnection;
import com.example.alimekho.Model.Employee;
import com.example.alimekho.R;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class EmployeeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EmployeeAdapter employeeAdapter;
    private Button btnAddEmp;
    private Button btnBackEmployee;
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

        btnAddEmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(view.getRootView().getContext());
                View dialogView= LayoutInflater.from(view.getRootView().getContext())
                        .inflate(R.layout.dialog_them_nhanvien,null);
                AlertDialog dialog = builder.create();
                dialog.setView(dialogView);
                dialog.show();
            }
        });

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