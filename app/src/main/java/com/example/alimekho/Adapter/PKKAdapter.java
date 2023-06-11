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

import com.example.alimekho.Activity.CTPKKActivity;
import com.example.alimekho.Activity.CTPXKActivity;
import com.example.alimekho.Activity.QLPKKActivity;
import com.example.alimekho.DataBase.SQLServerConnection;
import com.example.alimekho.Model.CTPKK;
import com.example.alimekho.Model.cuaHangXuat;
import com.example.alimekho.Model.phieuKiemKho;
import com.example.alimekho.R;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PKKAdapter extends RecyclerView.Adapter<PKKAdapter.ViewHolder>{
    private Context mContext;
    private List<phieuKiemKho> listPKK;
    private List<phieuKiemKho> listChecked = new ArrayList<>();
    private boolean isSelectedAll = false;

    public PKKAdapter(Context mContext, List<phieuKiemKho> listPKK) {
        this.mContext = mContext;
        this.listPKK = listPKK;
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
        for (phieuKiemKho pkk : listChecked) {
            listPKK.remove(pkk);
            SQLServerConnection db = new SQLServerConnection();
            Connection conn = db.getConnection();
            try {
                Statement stm = conn.createStatement();
                stm.executeUpdate("exec pro_xoa_pkk @formId = " + pkk.getMaPhieu());
                stm.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        listChecked.clear();
        notifyDataSetChanged();
    }
    public void setFilteredList(ArrayList<phieuKiemKho> filteredList) {
        this.listPKK = filteredList;
        notifyDataSetChanged();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView ma;
        private TextView ngay;
        private TextView nv;
        private TextView tinhtrang;
        private CheckBox cb;
        private LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ma = itemView.findViewById(R.id.ma);
            ngay = itemView.findViewById(R.id.ngay);
            nv = itemView.findViewById(R.id.nv);
            tinhtrang = itemView.findViewById(R.id.tinhtrang);
            cb = itemView.findViewById(R.id.checkbox);
            linearLayout = itemView.findViewById(R.id.linear);
        }
    }

    @NonNull
    @Override
    public PKKAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View mView = inflater.inflate(R.layout.item_pkk, parent, false);
        PKKAdapter.ViewHolder viewHolder = new ViewHolder(mView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PKKAdapter.ViewHolder holder, int position) {
        phieuKiemKho item = listPKK.get(position);
        if(position % 2 == 0) holder.linearLayout.setBackgroundColor(Color.WHITE);
        holder.linearLayout.setOnClickListener(view -> {
            Intent intent = new Intent(mContext, CTPKKActivity.class);
            HashMap<String, String> pkkMap = new HashMap<>();
            pkkMap.put("ma", item.getMaPhieu());
            pkkMap.put("ngay",item.getNgayKK());
            pkkMap.put("nv",item.getTenNV());
            pkkMap.put("tinhtrang",item.getTinhTrang() == 0 ? "Chưa xử lý" : "Đã xử lý");
            intent.putExtra("tinhtrangInt", item.getTinhTrang());
            intent.putExtra("PXK", pkkMap);
            mContext.startActivity(intent);
        });
        holder.ma.setText(item.getMaPhieu());
        holder.ngay.setText(item.getNgayKK());
        holder.nv.setText(item.getTenNV());
        holder.tinhtrang.setText(item.getTinhTrang() == 0 ? "Chưa xử lý" : "Đã xử lý");
        holder.cb.setOnCheckedChangeListener((compoundButton, b) -> {
            if (compoundButton.isChecked()) listChecked.add(item);
            else listChecked.remove(item);
        });
        if (!isSelectedAll) holder.cb.setChecked(false);
        else holder.cb.setChecked(true);
    }

    @Override
    public int getItemCount() {
        return listPKK.size();
    }
}
