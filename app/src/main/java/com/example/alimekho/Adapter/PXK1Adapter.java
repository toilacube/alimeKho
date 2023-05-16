package com.example.alimekho.Adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alimekho.Model.CTPNK;
import com.example.alimekho.Model.CTPXK;
import com.example.alimekho.Model.nhaCungCap;
import com.example.alimekho.Model.sanPham;
import com.example.alimekho.R;

import java.util.ArrayList;

public class PXK1Adapter extends RecyclerView.Adapter<PXK1Adapter.PXK1ViewHolder>{
    private Context context;
    private ArrayList<CTPXK> sanPhams;
    private ArrayList<CTPXK> sanPhamVV;

    public PXK1Adapter(Context context, ArrayList<CTPXK> sanPhams) {
        this.context = context;
        this.sanPhams = sanPhams;
        sanPhamVV = new ArrayList<>();
    }

    @NonNull
    @Override
    public PXK1ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_pxk1, parent, false);
        return new PXK1ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PXK1ViewHolder holder, int position) {

        CTPXK sanPham = sanPhams.get(position);
        holder.txtmaSP.setText(sanPham.getSanPham().getMaSP().toString().trim());
        holder.txttenSP.setText(sanPham.getSanPham().getTenSP().toString().trim());
        holder.txtdonGia.setText(Double.toString(sanPham.getSanPham().getDonGia()));
        holder.txtsoLuong.setText(Integer.toString(sanPham.getSoLuong()));
        holder.txtthanhTien.setText(Double.toString(sanPham.getSoLuong()*sanPham.getSanPham().getDonGia()));
        holder.txtsoLuong.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                if(!holder.txtsoLuong.getText().toString().trim().isEmpty()) {
                    holder.txtthanhTien.setText(Double.toString(Double.valueOf(holder.txtsoLuong.getText().toString().trim()) * sanPham.getSanPham().getDonGia()));
                    sanPham.setSoLuong(Integer.parseInt(holder.txtsoLuong.getText().toString().trim()));
                }
            }
        });
       // holder.txtNSX.setText(sanPham.getNSX().toString().trim());
        //holder.txtHSD.setText(sanPham.getHSD().toString().trim());
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = holder.checkBox.isChecked();
                String sl = holder.txtsoLuong.getText().toString().trim();
                if (sl.isEmpty()) sl = "0";
                sanPham.setSoLuong(Integer.parseInt(sl));
                if(isChecked)
                    sanPhamVV.add(sanPham);
                else
                    sanPhamVV.remove(sanPham);
            }
        });
    }

    @Override
    public int getItemCount() {
        return sanPhams == null ? 0 : sanPhams.size();
    }
    public ArrayList<CTPXK> getSanPhamVV(){
        return sanPhamVV;
    }
    public class PXK1ViewHolder extends RecyclerView.ViewHolder{
        private TextView txtmaSP, txttenSP, txtsoLuong, txtdonGia, txtNSX, txtHSD, txtthanhTien;
        private CheckBox checkBox;

        public PXK1ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtmaSP = itemView.findViewById(R.id.customPXK1_txtmaSP);
            txttenSP = itemView.findViewById(R.id.customPXK1_txttenSP);
            txtdonGia = itemView.findViewById(R.id.customPXK1_txtdonGia);
            txtsoLuong = itemView.findViewById(R.id.customPXK1_txtsoLuong);
            txtNSX = itemView.findViewById(R.id.customPXK1_txtNSX);
            txtHSD = itemView.findViewById(R.id.customPXK1_txtHSD);
            txtthanhTien = itemView.findViewById(R.id.customPXK1_txtThanhTien);
            checkBox = itemView.findViewById(R.id.customPXK1_checkbox);
        }
    }
    public void setFilteredList(ArrayList<CTPXK> filteredList) {
        this.sanPhams = filteredList;
        notifyDataSetChanged();
    }
}