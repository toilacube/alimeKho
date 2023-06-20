package com.example.alimekho.Activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alimekho.Adapter.DetailAreaAdapter;
import com.example.alimekho.DataBase.SQLServerConnection;
import com.example.alimekho.Model.Area;
import com.example.alimekho.Model.loSanPham;
import com.example.alimekho.R;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DetailAreaActivity extends AppCompatActivity {
    Area area = new Area();
    TextView id, zone, shelve, type;
    Button btnBackDetail, btnUpdate, btnDelete;
    RecyclerView recyclerView;
    DetailAreaAdapter adapter;
    CheckBox checkBox;
    ImageView imvDelete;
    SQLServerConnection db = new SQLServerConnection();
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

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                if(compoundButton.isChecked()) adapter.setCheckAll(b);
               adapter.setCheckAll(b);
            }
        });

        btnBackDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        imvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDeleteDialog(view);
            }
        });
    }

    private void showDeleteDialog(View view) {

        AlertDialog.Builder builder=new AlertDialog.Builder(view.getRootView().getContext());
        AlertDialog dialog = builder.create();
        View dialogDelete= LayoutInflater.from(view.getRootView().getContext()).inflate(R.layout.confirm_delete,null);
        dialog.setView(dialogDelete);

        Button btnHuy = dialogDelete.findViewById(R.id.btn_huy),
                btnDelete = dialogDelete.findViewById(R.id.btn_delete);

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                List<loSanPham> list =  adapter.getSelectedLo();
                for(loSanPham i: list){
                    String query = "delete distribute where batch_id = ?";
                    PreparedStatement stm = null;
                    try {
                        stm = db.getConnection().prepareStatement(query);
                        stm.setString(1, i.getMaLo());
                        stm.executeUpdate();
                        ArrayList<loSanPham> temp = area.getListLoSP();
                        temp.remove(i);
                        area.setListLoSP(temp);
                        adapter.setListLoSP(temp);
                    } catch (SQLException e) {
                        Log.e(TAG, e.toString());
                    }

                }

                dialog.dismiss();
            }
        });

        dialog.show();
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
        type.setText(area.getType_id());
    }

    private void findView() {
        id = findViewById(R.id.txvMaViTri);
        zone = findViewById(R.id.txvZone);
        shelve = findViewById(R.id.txvShelve);
        type = findViewById(R.id.txvType);
        recyclerView = findViewById(R.id.rec);
        btnBackDetail = findViewById(R.id.btnBackDetailViTri);
        checkBox = findViewById(R.id.cbAll);
        imvDelete = findViewById(R.id.imvDelete);
    }
}