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

public class PNK1Adapter extends RecyclerView.Adapter<PNK1Adapter.PNK1ViewHolder>{
    private Context context;
    private ArrayList<loSanPham> loSanPhams;
    private ArrayList<loSanPham> loSanPhamdc;

    public PNK1Adapter(Context context, ArrayList<loSanPham> loSanPhams) {
        this.context = context;
        loSanPhamdc = new ArrayList<>();
        this.loSanPhams = loSanPhams;
    }

    @NonNull
    @Override
    public PNK1ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_pnk1, parent, false);
        return new PNK1ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull PNK1ViewHolder holder, int position) {
        loSanPham loSanPham = loSanPhams.get(position);
        holder.txtmaLo.setText(loSanPham.getMaLo().toString().trim());
        holder.txtmaSP.setText(loSanPham.getSanPham().getMaSP().toString().trim());
        holder.txttenSP.setText(loSanPham.getSanPham().getTenSP().toString().trim());
        holder.txtdonGia.setText(Double.toString(loSanPham.getDonGia()));
        holder.txtNSX.setText(loSanPham.getNSX().toString().trim());
        holder.txtHSD.setText(loSanPham.getHSD().toString().trim());
        holder.txtsoLuong.setText(String.valueOf(loSanPham.getsLTon()).toString().trim());
        holder.txtsoLuong.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!holder.txtsoLuong.getText().toString().trim().isEmpty()) {
                    holder.txtthanhTien.setText(Double.toString(Double.valueOf(holder.txtsoLuong.getText().toString().trim()) * loSanPham.getDonGia()));
                    loSanPham.setsLChuaXep(Integer.parseInt(holder.txtsoLuong.getText().toString().trim()));
                    loSanPham.setsLTon(Integer.parseInt(holder.txtsoLuong.getText().toString().trim()));
                }
            }
        });
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = holder.checkBox.isChecked();
                String sl = holder.txtsoLuong.getText().toString().trim();
                if (sl.isEmpty()) sl = "0";
                loSanPham.setsLChuaXep(Integer.parseInt(holder.txtsoLuong.getText().toString().trim()));
                loSanPham.setsLTon(Integer.parseInt(holder.txtsoLuong.getText().toString().trim()));
                if(isChecked)
                    loSanPhamdc.add(loSanPham);
                else
                    loSanPhamdc.remove(loSanPham);
            }
        });
    }

    @Override
    public int getItemCount() {
        return loSanPhams == null ? 0 : loSanPhams.size();
    }
    public class PNK1ViewHolder extends RecyclerView.ViewHolder{
        private TextView txtmaSP, txttenSP, txtsoLuong, txtdonGia, txtNSX, txtHSD, txtthanhTien, txtmaLo;
        private LinearLayout linearLayout;
        private CheckBox checkBox;

        public PNK1ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtmaSP = itemView.findViewById(R.id.custompnk1_txtmaSP);
            txtmaLo = itemView.findViewById(R.id.custompnk1_txtmaLO);
            txttenSP = itemView.findViewById(R.id.custompnk1_txttenSP);
            txtdonGia = itemView.findViewById(R.id.custompnk1_txtdonGia);
            txtsoLuong = itemView.findViewById(R.id.custompnk1_txtsoLuong);
            txtNSX = itemView.findViewById(R.id.custompnk1_txtNSX);
            txtHSD = itemView.findViewById(R.id.custompnk1_txtHSD);
            txtthanhTien = itemView.findViewById(R.id.custompnk1_txtThanhTien);
            linearLayout = itemView.findViewById(R.id.custompnk_ln);
            checkBox = itemView.findViewById(R.id.customPNK1_checkbox);
        }
    }
    public ArrayList<loSanPham> getLoSanPhamdc(){
        return loSanPhamdc;
    }
}
