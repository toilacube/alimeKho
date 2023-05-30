package com.example.alimekho.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.alimekho.Adapter.DetailAreaAdapter;
import com.example.alimekho.Model.Area;
import com.example.alimekho.Model.Employee;
import com.example.alimekho.R;

public class DetailAreaActivity extends AppCompatActivity {
    Area area = new Area();
    TextView id, zone, shelve, slot, type, available;
    Button btnBackDetail, btnUpdate, btnDelete;
    RecyclerView recyclerView;
    DetailAreaAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_area);
        area = (Area) getIntent().getSerializableExtra("object_area");

        findView();
        setView();
        showListLoSP();
        buttonOnClick();
    }

    private void buttonOnClick() {
        btnBackDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void showListLoSP() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
        adapter = new DetailAreaAdapter(this, area.getListLoSP());
        recyclerView.setAdapter(adapter);
    }

    private void setView() {
        id.setText(area.getId());
        zone.setText(area.getArea());
        shelve.setText(area.getShelve());
        slot.setText(Integer.toString(area.getSlot()));
        available.setText(Integer.toString(area.getAvailable()));
        type.setText(area.getType_id());
    }

    private void findView() {
        id = findViewById(R.id.txvMaViTri);
        zone = findViewById(R.id.txvZone);
        shelve = findViewById(R.id.txvShelve);
        slot = findViewById(R.id.txvSlot);
        type = findViewById(R.id.txvType);
        available = findViewById(R.id.txvAvailable);
        recyclerView = findViewById(R.id.rec);
        btnBackDetail = findViewById(R.id.btnBackDetailViTri);
    }
}