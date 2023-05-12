package com.example.alimekho.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.alimekho.Adapter.AreaAdapter;
import com.example.alimekho.Adapter.EmployeeAdapter;
import com.example.alimekho.Model.Area;
import com.example.alimekho.Model.Employee;
import com.example.alimekho.R;

import java.util.ArrayList;
import java.util.List;

public class AreaActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AreaAdapter areaAdapter;
    private Button btnBackArea;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area);

        btnBackArea=findViewById(R.id.btn_back_area);
        recyclerView=findViewById(R.id.rv_area);
        areaAdapter=new AreaAdapter(this);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
        areaAdapter.setData(getListArea());
        recyclerView.setAdapter(areaAdapter);
        btnBackArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private List<Area> getListArea() {
        List<Area> list = new ArrayList<>();
        list.add(new Area("A13-43", "A", "13", "43", "P4", 16, "Còn Trống"));
        list.add(new Area("B22-13", "B", "22", "13", "P9", 22, "Đã đầy"));
        return list;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(areaAdapter!=null){
            areaAdapter.release();
        }
    }
}