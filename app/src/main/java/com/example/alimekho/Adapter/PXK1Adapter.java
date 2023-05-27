package com.example.alimekho.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alimekho.Model.CTPNK;
import com.example.alimekho.Model.CTPXK;
import com.example.alimekho.Model.loSanPham;
import com.example.alimekho.Model.nhaCungCap;
import com.example.alimekho.Model.sanPham;
import com.example.alimekho.R;

import java.util.ArrayList;

public class PXK1Adapter extends RecyclerView.Adapter<PXK1Adapter.PXK1ViewHolder>{
    private Context context;
    private ArrayList<CTPXK> sanPhams;
    private ArrayList<CTPXK> sanPhamVV;
    private boolean isSelectedAll = false;

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
        holder.txtmaLo.setText(sanPham.getLo().getMaLo());
        holder.txtmaSP.setText(sanPham.getLo().getSanPham().getMaSP().toString().trim());
        holder.txttenSP.setText(sanPham.getLo().getSanPham().getTenSP().toString().trim());
        holder.txtdonGia.setText(Integer.toString((int)sanPham.getLo().getDonGia()));
        holder.txtsoLuong.setText(Integer.toString(sanPham.getSoLuong()));
        holder.txtNSX.setText(sanPham.getLo().getNSX().toString().trim());
        holder.txtHSD.setText(sanPham.getLo().getHSD().toString().trim());
        if (!isSelectedAll) holder.checkBox.setChecked(false);
        else holder.checkBox.setChecked(true);
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                boolean isChecked = compoundButton.isChecked();
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

    public void selectAll() {
        isSelectedAll=true;
        notifyDataSetChanged();
    }

    public void unSelectAll() {
        isSelectedAll=false;
        notifyDataSetChanged();
    }

    public class PXK1ViewHolder extends RecyclerView.ViewHolder{
        private LinearLayout layout;
        private TextView txtsoLuong, txtmaLo, txtmaSP, txttenSP, txtdonGia, txtNSX, txtHSD;
        private CheckBox checkBox;

        public PXK1ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtmaLo = itemView.findViewById(R.id.maLo);
            txtmaSP = itemView.findViewById(R.id.customPXK1_txtmaSP);
            txttenSP = itemView.findViewById(R.id.customPXK1_txttenSP);
            txtdonGia = itemView.findViewById(R.id.customPXK1_txtdonGia);
            txtsoLuong = itemView.findViewById(R.id.customPXK1_txtsoLuong);
            txtNSX = itemView.findViewById(R.id.customPXK1_txtNSX);
            txtHSD = itemView.findViewById(R.id.customPXK1_txtHSD);
            checkBox = itemView.findViewById(R.id.customPXK1_checkbox);
            layout = itemView.findViewById(R.id.customPXK_ln);
        }
    }
    public void setFilteredList(ArrayList<CTPXK> filteredList) {
        this.sanPhams = filteredList;
        notifyDataSetChanged();
    }
}