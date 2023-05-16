package com.example.alimekho.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import com.example.alimekho.Activity.CreatePNK2Activity;
import com.example.alimekho.DataBase.SQLServerConnection;
import com.example.alimekho.Model.nhaCungCap;
import com.example.alimekho.Model.sanPham;
import com.example.alimekho.R;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class PNK2Adapter extends Adapter<PNK2Adapter.PNK2ViewHolder>{
    private Context context;
    private ArrayList<nhaCungCap> nhaCungCaps;
    private setData setData;
    private nhaCungCap nhaCungCapVV;
    private CreatePNK2Activity createPNK2Activity;
    public PNK2Adapter(Context context, ArrayList<nhaCungCap> nhaCungCaps) {
        this.context = context;
        this.nhaCungCaps = nhaCungCaps;
        this.setData = (PNK2Adapter.setData) context;
    }

    public interface setData {
        void setNhaCungCap(nhaCungCap nhaCungCap);
    }

    @NonNull
    @Override
    public PNK2ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_pnk2, parent, false);
        return new PNK2ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PNK2ViewHolder holder, int position) {
        nhaCungCap nhaCungCap = nhaCungCaps.get(position);
        holder.txtmaNCC.setText(nhaCungCap.getMaNCC().toString().trim());
        holder.txttenNCC.setText(nhaCungCap.getTenCC().toString().trim());
        holder.txtdiaChi.setText(nhaCungCap.getDiaChi().toString().trim());
        holder.txtSDT.setText(nhaCungCap.getSDT().toString().trim());
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nhaCungCapVV = (new nhaCungCap(nhaCungCap.getMaNCC().toString().trim(), nhaCungCap.getTenCC().toString().trim(),
                        nhaCungCap.getDiaChi().toString().trim(), nhaCungCap.getSDT().toString()));
                setData.setNhaCungCap(nhaCungCapVV);
            }
        });
    }
    @Override
    public int getItemCount() {
        return nhaCungCaps == null ? 0 : nhaCungCaps.size();
    }

    public class PNK2ViewHolder extends RecyclerView.ViewHolder{
        private TextView txtmaNCC, txttenNCC, txtdiaChi, txtSDT;
        private LinearLayout linearLayout;
        public PNK2ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtmaNCC = itemView.findViewById(R.id.custompnk2_txtmaNCC);
            txttenNCC = itemView.findViewById(R.id.custompnk2_txttenNCC);
            txtdiaChi = itemView.findViewById(R.id.custompnk2_txtdiaChi);
            txtSDT = itemView.findViewById(R.id.custompnk2_txtSDT);
            linearLayout = itemView.findViewById(R.id.custompnk_ln);
        }
    }
    public void setFilteredList(ArrayList<nhaCungCap> filteredList) {
        this.nhaCungCaps = filteredList;
        notifyDataSetChanged();
    }
}