package com.example.alimekho.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alimekho.Model.cuaHangXuat;
import com.example.alimekho.Model.nhaCungCap;
import com.example.alimekho.R;

import java.util.ArrayList;

public class PXK2Adapter extends RecyclerView.Adapter<PXK2Adapter.PXK2ViewHolder>{
    private Context context;
    private ArrayList<cuaHangXuat> cuaHangXuats;
    private cuaHangXuat cuaHangXuatVV;

    public PXK2Adapter(Context context, ArrayList<cuaHangXuat> cuaHangXuats) {
        this.context = context;
        this.cuaHangXuats = cuaHangXuats;
    }

    @NonNull
    @Override
    public PXK2ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_pxk2, parent, false);
        return new PXK2ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PXK2ViewHolder holder, int position) {
        cuaHangXuat cuaHangXuat = cuaHangXuats.get(position);
        holder.txtmaCHX.setText(cuaHangXuat.getMaCHX().toString().trim());
        holder.txttenCHX.setText(cuaHangXuat.getTenCHX().toString().trim());
        holder.txtdiaChi.setText(cuaHangXuat.getDiaChi().toString().trim());
        holder.txtSDT.setText(cuaHangXuat.getSDT().toString().trim());
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cuaHangXuatVV = (new cuaHangXuat(cuaHangXuat.getMaCHX().toString().trim(), cuaHangXuat.getTenCHX().toString().trim(),
                        cuaHangXuat.getDiaChi().toString().trim(), cuaHangXuat.getSDT().toString()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return cuaHangXuats == null ? 0 : cuaHangXuats.size();
    }

    public class PXK2ViewHolder extends RecyclerView.ViewHolder{
        private TextView txtmaCHX, txttenCHX, txtdiaChi, txtSDT;
        private LinearLayout linearLayout;
        public PXK2ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtmaCHX = itemView.findViewById(R.id.custompxk2_txtmaCHX);
            txttenCHX = itemView.findViewById(R.id.custompxk2_txttenCHX);
            txtdiaChi = itemView.findViewById(R.id.custompxk2_txtdiaChi);
            txtSDT = itemView.findViewById(R.id.custompxk2_txtSDT);
            linearLayout = itemView.findViewById(R.id.custompxk_ln);
        }
    }
    public void setFilteredList(ArrayList<cuaHangXuat> filteredList) {
        this.cuaHangXuats = filteredList;
        notifyDataSetChanged();
    }
}
