package com.example.alimekho.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alimekho.DataBase.SQLServerConnection;
import com.example.alimekho.Model.CTPNK;
import com.example.alimekho.Model.loSanPham;
import com.example.alimekho.R;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class PNK3Adapter extends RecyclerView.Adapter<PNK3Adapter.PNK3ViewHolder> {
    private Context context;
    private ArrayList<loSanPham> loSanPhams;

    public PNK3Adapter(Context context, ArrayList<loSanPham> loSanPhams) {
        this.context = context;
        this.loSanPhams = loSanPhams;
    }

    @NonNull
    @Override
    public PNK3ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_pnk3, parent, false);
        return new PNK3ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PNK3ViewHolder holder, int position) {
        loSanPham loSanPham = loSanPhams.get(position);
        holder.txtmaLo.setText(loSanPham.getMaLo().toString().trim());
        holder.txtmaSP.setText(loSanPham.getSanPham().getMaSP().toString().trim());
        holder.txttenSP.setText(loSanPham.getSanPham().getTenSP().toString().trim());
        holder.txtdonGia.setText(Double.toString(loSanPham.getDonGia()));
        holder.txtNSX.setText(loSanPham.getNSX().toString().trim());
        holder.txtHSD.setText(loSanPham.getHSD().toString().trim());
        holder.txtsoLuong.setText(String.valueOf(loSanPham.getsLTon()).toString().trim());
        holder.txtthanhTien.setText(Double.toString(Double.valueOf(holder.txtsoLuong.getText().toString().trim()) * loSanPham.getDonGia()));
    }

    @Override
    public int getItemCount() {
        return loSanPhams == null ? 0 : loSanPhams.size();
    }

    public class PNK3ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtmaSP, txttenSP, txtsoLuong, txtdonGia, txtNSX, txtHSD, txtthanhTien, txtmaLo;
        private LinearLayout linearLayout;

        public PNK3ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtmaSP = itemView.findViewById(R.id.custompnk3_txtmaSP);
            txtmaLo = itemView.findViewById(R.id.custompnk3_txtmaLO);
            txttenSP = itemView.findViewById(R.id.custompnk3_txttenSP);
            txtdonGia = itemView.findViewById(R.id.custompnk3_txtdonGia);
            txtsoLuong = itemView.findViewById(R.id.custompnk3_txtsoLuong);
            txtNSX = itemView.findViewById(R.id.custompnk3_txtNSX);
            txtHSD = itemView.findViewById(R.id.custompnk3_txtHSD);
            txtthanhTien = itemView.findViewById(R.id.custompnk3_txtThanhTien);
            linearLayout = itemView.findViewById(R.id.custompnk_ln);
        }
    }
}
