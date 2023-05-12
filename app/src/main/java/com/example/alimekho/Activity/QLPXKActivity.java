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

import com.example.alimekho.Adapter.PXKAdapter;
import com.example.alimekho.Model.phieuXuatKho;
import com.example.alimekho.R;

import java.util.ArrayList;
import java.util.List;

public class QLPXKActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qlpxk);

        //test
        RecyclerView rcv = findViewById(R.id.rcv);
        PXKAdapter adapter = new PXKAdapter(this, getListPXK());
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
                Dialog dialog = new Dialog(QLPXKActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.edit_pxk);
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
    private List<phieuXuatKho> getListPXK() {
        List<phieuXuatKho> l = new ArrayList<>();
        l.add(new phieuXuatKho("PXK001", "22/02/2003", "Đây là CHX", "NV001", 0));
        l.add(new phieuXuatKho("PXK002", "22/02/2003", "Đây là CHX", "NV001", 0));
        l.add(new phieuXuatKho("PXK003", "22/02/2003", "Đây là CHX", "NV001", 0));
        l.add(new phieuXuatKho("PXK004", "22/02/2003", "Đây là CHX", "NV001", 0));
        l.add(new phieuXuatKho("PXK005", "22/02/2003", "Đây là CHX", "NV001", 0));
        l.add(new phieuXuatKho("PXK006", "22/02/2003", "Đây là CHX", "NV001", 0));
        l.add(new phieuXuatKho("PXK007", "22/02/2003", "Đây là CHX", "NV001", 0));
        l.add(new phieuXuatKho("PXK008", "22/02/2003", "Đây là CHX", "NV001", 0));

        return l;
    }
}