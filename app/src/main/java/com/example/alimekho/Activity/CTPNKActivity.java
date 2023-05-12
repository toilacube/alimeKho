package com.example.alimekho.Activity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alimekho.Adapter.CTPXKAdapter;
import com.example.alimekho.Model.CTPXK;
import com.example.alimekho.Model.sanPham;
import com.example.alimekho.R;

import java.util.ArrayList;
import java.util.List;

public class CTPNKActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ctpnk);

        //test
        RecyclerView rcv = findViewById(R.id.rcv);
        CTPXKAdapter adapter = new CTPXKAdapter(this, getListCTPXK());
        rcv.setAdapter(adapter);
        rcv.setLayoutManager(new LinearLayoutManager(this));

        //del btn
        Button delbtn = findViewById(R.id.btn_xoa);
        delbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.deleteCheckedItems();
            }
        });

        //edit btn
        Button editbtn = findViewById(R.id.btn_sua);
        editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(CTPNKActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.edit_ctpnk);
                Window window = dialog.getWindow();
                if (window == null) return;
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                CardView cancel = dialog.findViewById(R.id.huy);

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });


                dialog.show();
            }
        });
    }

    private List<CTPXK> getListCTPXK() {
        List<CTPXK> l = new ArrayList<>();
        l.add(new CTPXK("123",
                new sanPham("1234", "Sản phẩm 1", 12000, 5, "22/02/2003", "22/02/2103"),
                0));
        l.add(new CTPXK("123",
                new sanPham("1234", "Sản phẩm 2", 12000, 5, "22/02/2003", "22/02/2103"),
                0));
        l.add(new CTPXK("123",
                new sanPham("1234", "Sản phẩm 3", 13000, 5, "22/02/2003", "22/02/2103"),
                0));
        l.add(new CTPXK("123",
                new sanPham("1234", "Sản phẩm 4", 14000, 5, "22/02/2003", "22/02/2103"),
                0));
        l.add(new CTPXK("123",
                new sanPham("1234", "Sản phẩm 5", 15000, 5, "22/02/2003", "22/02/2103"),
                0));
        l.add(new CTPXK("123",
                new sanPham("1234", "Sản phẩm 6", 1, 5, "22/02/2003", "22/02/2103"),
                0));
        return l;
    }
}