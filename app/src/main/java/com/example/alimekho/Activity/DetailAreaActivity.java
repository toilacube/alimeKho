package com.example.alimekho.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.alimekho.Model.Area;
import com.example.alimekho.Model.Employee;
import com.example.alimekho.R;

public class DetailAreaActivity extends AppCompatActivity {

    private TextView id, area, shelf, bin, status;
    private Button btnBackDetail, btnUpdate, btnDelete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_area);
        Bundle bundle = getIntent().getExtras();
        if (bundle==null)
            return;
        Area area= (Area) bundle.get("object_area");

    }
}