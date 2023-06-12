package com.example.alimekho.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alimekho.Model.Employee;
import com.example.alimekho.R;

public class DetailEmployeeActivity extends AppCompatActivity {

    TextView id, name, title, birth, identify, phone;
    Button btnBackDetail, btnUpdate, btnDelete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_employee);
        Bundle bundle = getIntent().getExtras();
        if (bundle==null)
            return;
        Employee employee= (Employee) bundle.get("object_employee");
        id=findViewById(R.id.tv_id);
        name=findViewById(R.id.tv_name);
        title=findViewById(R.id.tv_title);
        birth=findViewById(R.id.tv_birth);
        identify=findViewById(R.id.tv_identify);
        phone=findViewById(R.id.tv_phone);
        btnBackDetail=findViewById(R.id.btn_back_employee);
        btnUpdate=findViewById(R.id.btn_update);
        btnDelete=findViewById(R.id.btn_delete);

        id.setText(employee.getId());
        name.setText(employee.getName());
        title.setText(employee.getTitle());
        birth.setText(employee.getDayOfBirth());
        identify.setText(employee.getIdentify());
        phone.setText(employee.getPhoneNumber());

        if (getSharedPreferences("user info", Context.MODE_PRIVATE).getInt("role", -1) == 2){
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder=new AlertDialog.Builder(view.getRootView().getContext());
                    View dialogView= LayoutInflater.from(view.getRootView().getContext()).inflate(R.layout.confirm_delete,null);
                    builder.setView(dialogView);
                    builder.show();
                }
            });
        } else {
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(DetailEmployeeActivity.this, "Bạn không có quyền thực hiện thao tác này", Toast.LENGTH_SHORT).show();
                }
            });
        }

        btnBackDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EmployeeActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }
}