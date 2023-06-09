package com.example.alimekho.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.alimekho.DataBase.SQLServerConnection;
import com.example.alimekho.Model.CTPXK;
import com.example.alimekho.Model.cuaHangXuat;
import com.example.alimekho.Model.phieuXuatKho;
import com.example.alimekho.R;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SuperMarketAdapter extends RecyclerView.Adapter<SuperMarketAdapter.ViewHolder>{
    private Context mContext;
    private List<cuaHangXuat> listCHX;
    private List<cuaHangXuat> listChecked = new ArrayList<>();

    private boolean isSelectedAll = false;

    public SuperMarketAdapter(Context mContext, List<cuaHangXuat> listCHX) {
        this.mContext = mContext;
        this.listCHX = listCHX;
    }

    public void selectAll() {
        isSelectedAll=true;
        notifyDataSetChanged();
    }

    public void unSelectAll() {
        isSelectedAll=false;
        notifyDataSetChanged();
    }

    public void deleteCheckedItems() {
        for (cuaHangXuat chx : listChecked) {
            listCHX.remove(chx);
            SQLServerConnection db = new SQLServerConnection();
            Connection conn = db.getConnection();
            try {
                Statement stm = conn.createStatement();
                stm.executeUpdate("update supermarket\n" +
                        "set is_deleted = 1\n" +
                        "where id = " + chx.getMaCHX());
                stm.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        listChecked.clear();
        notifyDataSetChanged();
    }

    public void setFilteredList(ArrayList<cuaHangXuat> filteredList) {
        this.listCHX = filteredList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView ma;
        private TextView ten;
        private TextView diachi;
        private TextView sdt;
        private CheckBox cb;
        private LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ma = itemView.findViewById(R.id.ma);
            ten = itemView.findViewById(R.id.ten);
            diachi = itemView.findViewById(R.id.diachi);
            sdt = itemView.findViewById(R.id.sdt);
            cb = itemView.findViewById(R.id.checkbox);
            linearLayout = itemView.findViewById(R.id.linear);
        }
    }
    @NonNull
    @Override
    public SuperMarketAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View mView = inflater.inflate(R.layout.item_ddx, parent, false);
        SuperMarketAdapter.ViewHolder viewHolder = new ViewHolder(mView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SuperMarketAdapter.ViewHolder holder, int position) {
        cuaHangXuat item = listCHX.get(position);
        holder.ma.setText(item.getMaCHX());
        holder.ten.setText(item.getTenCHX());
        holder.diachi.setText(item.getDiaChi());
        holder.sdt.setText(item.getSDT());
        holder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()) listChecked.add(item);
                else listChecked.remove(item);
            }
        });
        if (!isSelectedAll) holder.cb.setChecked(false);
        else holder.cb.setChecked(true);
    }

    @Override
    public int getItemCount() {
        return listCHX.size();
    }
}
