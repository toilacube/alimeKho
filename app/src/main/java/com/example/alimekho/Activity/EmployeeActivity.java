package com.example.alimekho.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.alimekho.Adapter.EmployeeAdapter;
import com.example.alimekho.Model.Employee;
import com.example.alimekho.R;

import java.util.ArrayList;
import java.util.List;

public class EmployeeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EmployeeAdapter employeeAdapter;
    private Button btnBackEmployee;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);

        btnBackEmployee=findViewById(R.id.btn_back_employee);
        recyclerView=findViewById(R.id.rv_employee);
        employeeAdapter=new EmployeeAdapter(this);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
        employeeAdapter.setData(getListEmployee());
        recyclerView.setAdapter(employeeAdapter);

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
        list.add(new Employee("NV01", "Nguyễn Văn An", "Nhân viên xếp hàng", "30/4/2001", "07598839923", "098756732"));
        list.add(new Employee("NV02", "Trần Nguyễn Văn Bê", "Nhân viên kế toán", "3/8/1994", "02598832323", "0487544732"));

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