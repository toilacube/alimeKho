package com.example.alimekho.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alimekho.Model.CTPNK;
import com.example.alimekho.Model.CTPXK;
import com.example.alimekho.R;

import java.util.ArrayList;

public class PXK3Adapter extends RecyclerView.Adapter<PXK3Adapter.PXK3ViewHolder>{
    private Context context;
    private ArrayList<CTPXK> ctpxks;

    public PXK3Adapter(Context context, ArrayList<CTPXK> ctpxks) {
        this.context = context;
        this.ctpxks = ctpxks;
    }

    @NonNull
    @Override
    public PXK3ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_pxk3, parent, false);
        return new PXK3ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull PXK3ViewHolder holder, int position) {
        CTPXK ctpxk = ctpxks.get(position);
        holder.txtmaSP.setText(ctpxk.getSanPham().getMaSP().toString().trim());
        holder.txttenSP.setText(ctpxk.getSanPham().getTenSP().toString().trim());
        holder.txtdonGia.setText(Double.toString(ctpxk.getSanPham().getDonGia()));
        holder.txtsoLuong.setText(Integer.toString(ctpxk.getSoLuong()));
        holder.txtthanhTien.setText(Double.toString(ctpxk.getThanhTien()));
        holder.txtNSX.setText(ctpxk.getNSX().toString().trim());
        holder.txtHSD.setText(ctpxk.getHSD().toString().trim());

    }

    @Override
    public int getItemCount() {
        return ctpxks == null ? 0 : ctpxks.size();
    }

    public class PXK3ViewHolder extends RecyclerView.ViewHolder{
        private TextView txtmaSP, txttenSP, txtsoLuong, txtdonGia, txtNSX, txtHSD, txtthanhTien;

        public PXK3ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtmaSP = itemView.findViewById(R.id.custompnk1_txtmaSP);
            txttenSP = itemView.findViewById(R.id.custompnk1_txttenSP);
            txtdonGia = itemView.findViewById(R.id.txtdonGia);
            txtsoLuong = itemView.findViewById(R.id.custompnk1_txtsoLuong);
            txtNSX = itemView.findViewById(R.id.custompnk1_txtNSX);
            txtHSD = itemView.findViewById(R.id.custompnk1_txtHSD);
            txtthanhTien = itemView.findViewById(R.id.custompnk1_txtThanhTien);
        }
    }
}

