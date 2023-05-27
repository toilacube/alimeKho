package com.example.alimekho.Adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alimekho.Model.CTPXK;
import com.example.alimekho.R;

import java.util.ArrayList;

public class PXK1_2Adapter extends RecyclerView.Adapter<PXK1_2Adapter.PXK12ViewHolder>{
    private Context context;
    private ArrayList<CTPXK> sanPhams;

    public PXK1_2Adapter(Context context, ArrayList<CTPXK> sanPhams) {
        this.context = context;
        this.sanPhams = sanPhams;
    }

    @NonNull
    @Override
    public PXK12ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_pxk1_2, parent, false);
        return new PXK12ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PXK12ViewHolder holder, int position) {

        CTPXK sanPham = sanPhams.get(position);
        holder.txtmaLo.setText(sanPham.getLo().getMaLo());
        holder.txtmaSP.setText(sanPham.getLo().getSanPham().getMaSP().toString().trim());
        holder.txttenSP.setText(sanPham.getLo().getSanPham().getTenSP().toString().trim());
        holder.txtdonGia.setText(Integer.toString((int)sanPham.getLo().getDonGia()));
        holder.txtsoLuong.setText(Integer.toString(sanPham.getSoLuong()));
        holder.txtthanhTien.setText(Integer.toString((int)sanPham.getThanhTien()));

        holder.txtsoLuong.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                if(!holder.txtsoLuong.getText().toString().trim().isEmpty()) {
                    holder.txtthanhTien.setText(Integer.toString((int)(Double.valueOf(holder.txtsoLuong.getText().toString().trim()) * sanPham.getLo().getDonGia())));
                    sanPham.setSoLuong(Integer.parseInt(holder.txtsoLuong.getText().toString().trim()));
                } else {
                    holder.txtthanhTien.setText("0");
                }
            }
        });
        holder.txtNSX.setText(sanPham.getLo().getNSX().toString().trim());
        holder.txtHSD.setText(sanPham.getLo().getHSD().toString().trim());
    }

    @Override
    public int getItemCount() {
        return sanPhams == null ? 0 : sanPhams.size();
    }


    public class PXK12ViewHolder extends RecyclerView.ViewHolder{
        private LinearLayout layout;
        private TextView txtmaLo, txtmaSP, txttenSP, txtdonGia, txtNSX, txtHSD, txtthanhTien;
        private EditText txtsoLuong;

        public PXK12ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtmaLo = itemView.findViewById(R.id.maLo);
            txtmaSP = itemView.findViewById(R.id.customPXK1_txtmaSP);
            txttenSP = itemView.findViewById(R.id.customPXK1_txttenSP);
            txtdonGia = itemView.findViewById(R.id.customPXK1_txtdonGia);
            txtsoLuong = itemView.findViewById(R.id.customPXK1_txtsoLuong);
            txtNSX = itemView.findViewById(R.id.customPXK1_txtNSX);
            txtHSD = itemView.findViewById(R.id.customPXK1_txtHSD);
            txtthanhTien = itemView.findViewById(R.id.customPXK1_txtThanhTien);
            layout = itemView.findViewById(R.id.customPXK_ln);
        }
    }
    public void setFilteredList(ArrayList<CTPXK> filteredList) {
        this.sanPhams = filteredList;
        notifyDataSetChanged();
    }
}