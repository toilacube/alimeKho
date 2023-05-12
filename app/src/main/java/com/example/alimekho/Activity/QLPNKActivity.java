package com.example.alimekho.Activity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alimekho.Adapter.PNKAdapter;
import com.example.alimekho.Model.phieuNhapKho;
import com.example.alimekho.R;

import java.util.ArrayList;
import java.util.List;

public class QLPNKActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qlpnk);

        //test
        RecyclerView rcv = findViewById(R.id.rcv);
        PNKAdapter adapter = new PNKAdapter(this, getListPNK());
        rcv.setAdapter(adapter);
        rcv.setLayoutManager(new LinearLayoutManager(this));

        //them

        //xoa
        ImageView delBtn = findViewById(R.id.icon_delete);
        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.deleteCheckedItems();
            }
        });

        //sua
        ImageView editBtn = findViewById(R.id.icon_edit);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(QLPNKActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.edit_pnk);
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
    private List<phieuNhapKho> getListPNK() {
        List<phieuNhapKho> l = new ArrayList<>();
        l.add(new phieuNhapKho("123", "22/02/2222", "Nhà CC BS 1", "NV01", 0));
        l.add(new phieuNhapKho("123", "22/02/2223", "Nhà CC BS 2", "NV01", 0));
        l.add(new phieuNhapKho("123", "22/02/2224", "Nhà CC BS 3", "NV01", 0));
        l.add(new phieuNhapKho("123", "22/02/2225", "Nhà CC BS 4", "NV01", 0));
        l.add(new phieuNhapKho("123", "22/02/2226", "Nhà CC BS 5", "NV01", 0));
        l.add(new phieuNhapKho("123", "22/02/2227", "Nhà CC BS 6", "NV01", 0));
        return l;
    }
}