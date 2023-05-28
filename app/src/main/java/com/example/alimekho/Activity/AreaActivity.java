package com.example.alimekho.Activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alimekho.Adapter.AreaAdapter;
import com.example.alimekho.DataBase.SQLServerConnection;
import com.example.alimekho.Model.Area;
import com.example.alimekho.R;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AreaActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AreaAdapter areaAdapter;
    SQLServerConnection db = new SQLServerConnection();
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

        try {
            String query = "select l.id, l.zone, l.shelve, l.available, l.slot, t.name " +
                    "from location l " +
                    "join product_type t on t.id = l.type_id " +
                    "where l.is_deleted = 0";
            Statement stm = db.getConnection().createStatement();
            ResultSet rs = stm.executeQuery(query);
            while(rs.next()){
                Area area = new Area();
                area.setId(rs.getString("id"));
                area.setArea(rs.getString("zone"));
                area.setShelf(rs.getString("shelve"));
                area.setAvailable(rs.getInt("available"));
                area.setSlot(rs.getInt("slot"));
                area.setType_id(rs.getString("name"));

                String queryBatch = "select * from batch where id = (" +
                        "select batch_id from distribute where form_id = ?)";
                PreparedStatement stmBatch = db.getConnection().prepareStatement(queryBatch);
               // stmBatch.setString(1, area.getI);

                list.add(area);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
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