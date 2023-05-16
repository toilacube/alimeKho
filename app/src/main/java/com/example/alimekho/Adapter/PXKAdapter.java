package com.example.alimekho.Adapter;

import android.content.Context;
import android.content.Intent;
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

import com.example.alimekho.Activity.CTPNKActivity;
import com.example.alimekho.Activity.CTPXKActivity;
import com.example.alimekho.DataBase.SQLServerConnection;
import com.example.alimekho.Model.phieuXuatKho;
import com.example.alimekho.Model.sanPham;
import com.example.alimekho.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class PXKAdapter extends RecyclerView.Adapter<PXKAdapter.ViewHolder> {
    private Context mContext;
    private List<phieuXuatKho> listPXK;
    private List<phieuXuatKho> listChecked = new ArrayList<>();

    public PXKAdapter(Context mContext, List<phieuXuatKho> listPXK) {
        this.mContext = mContext;
        this.listPXK = listPXK;
    }

    public void setFilteredList(ArrayList<phieuXuatKho> filteredList) {
        this.listPXK = filteredList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvMaPhieu;
        private TextView tvNgXK;
        private TextView tvNPT;
        private TextView tvCHX;
        private LinearLayout linearLayout;
        private CheckBox cb;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMaPhieu = itemView.findViewById(R.id.tv_ma_phieu);
            tvNgXK = itemView.findViewById(R.id.tv_ngXK);
            tvNPT = itemView.findViewById(R.id.tv_npt);
            tvCHX = itemView.findViewById(R.id.tv_chx);
            linearLayout = itemView.findViewById(R.id.linear);
            cb = itemView.findViewById(R.id.checkbox);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View mView = inflater.inflate(R.layout.item_pxk, parent, false);
        ViewHolder viewHolder = new ViewHolder(mView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        phieuXuatKho pxk = listPXK.get(position);
        if(position % 2 == 0) holder.linearLayout.setBackgroundColor(Color.WHITE);
        holder.linearLayout.setOnClickListener(view -> {
            Intent intent = new Intent(mContext, CTPXKActivity.class);
            intent.putExtra("maPhieuXuat", pxk.getMaPhieu());
            mContext.startActivity(intent);
        });
        holder.tvMaPhieu.setText(pxk.getMaPhieu());
        holder.tvCHX.setText(pxk.getTenCuaHangXuat());
        holder.tvNPT.setText(pxk.getTenNV());
        holder.tvNgXK.setText(pxk.getNgayXuatKho());
        holder.cb.setChecked(false);
        holder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()) listChecked.add(pxk);
                else listChecked.remove(pxk);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listPXK.size();
    }

    public void deleteCheckedItems() {
        for (phieuXuatKho pxk : listChecked) {
            listPXK.remove(pxk);

            SQLServerConnection db = new SQLServerConnection();
            Connection conn = db.getConnection();
            try {
                Statement stm = conn.createStatement();
                stm.executeUpdate( "delete from [detail_output] where [form_id] = " + pxk.getMaPhieu() + "\n"
                        +"delete from [output_form] where id = " + pxk.getMaPhieu());
                stm.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        listChecked.clear();
        notifyDataSetChanged();
    }
}
