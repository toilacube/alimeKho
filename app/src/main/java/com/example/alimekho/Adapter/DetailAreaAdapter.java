package com.example.alimekho.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alimekho.Model.loSanPham;
import com.example.alimekho.Model.sanPham;
import com.example.alimekho.R;

import java.util.ArrayList;

public class DetailAreaAdapter extends RecyclerView.Adapter<DetailAreaAdapter.DetailAreaViewHolder> {

    Context context;
    ArrayList <loSanPham> listLoSP = new ArrayList<>();
    boolean isSelectedAll = false;
    ArrayList<loSanPham> listChecked = new ArrayList<>();

    public void setListLoSP(ArrayList<loSanPham> listLoSP) {
        this.listLoSP = listLoSP;
        notifyDataSetChanged();
    }

    public DetailAreaAdapter(Context context, ArrayList<loSanPham> listLoSP) {
        this.context = context;
        this.listLoSP = listLoSP;
    }

    @NonNull
    @Override
    public DetailAreaAdapter.DetailAreaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_area_batch, parent,false);
        return new DetailAreaAdapter.DetailAreaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailAreaAdapter.DetailAreaViewHolder holder, int position) {
        loSanPham loSP = listLoSP.get(position);
        sanPham sp = loSP.getSanPham();

        holder.txvName.setText(sp.getTenSP());
        holder.txvID.setText(sp.getMaSP());
        holder.txvSupplier.setText(sp.getSupplier_id());
        holder.txvQuantity.setText(Integer.toString(loSP.getsLTon()));
        holder.checkBox.setChecked(isSelectedAll);

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()) listChecked.add(loSP);
                else listChecked.remove(loSP);
            }
        });
    }
    public void setCheckAll(boolean is_checked){
        isSelectedAll = is_checked;
        notifyDataSetChanged();
    }
    public ArrayList<loSanPham> getSelectedLo(){
        return listChecked;
    }
    @Override
    public int getItemCount() {
        if(listLoSP != null) return listLoSP.size();
        return 0;
    }

    public class DetailAreaViewHolder extends RecyclerView.ViewHolder {
        TextView txvID, txvName, txvSupplier, txvQuantity;
        CheckBox checkBox;
        public DetailAreaViewHolder(@NonNull View itemView) {
            super(itemView);
            txvID = itemView.findViewById(R.id.txvID);
            txvName = itemView.findViewById(R.id.txvName);
            txvSupplier = itemView.findViewById(R.id.txvNhaCC);
            txvQuantity = itemView.findViewById(R.id.txvQuantity);
            checkBox = itemView.findViewById(R.id.cbAll);
        }
    }
}
