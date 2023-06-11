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
import com.example.alimekho.Model.CTPKK;
import com.example.alimekho.Model.CTPXK;
import com.example.alimekho.R;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CTPKKAdapter extends RecyclerView.Adapter<CTPKKAdapter.ViewHolder>{
    private Context mContext;
    private List<CTPKK> listCTPKK;
    private List<CTPKK> listChecked = new ArrayList<>();
    private boolean isSelectedAll = false;

    public CTPKKAdapter(Context mContext, List<CTPKK> listCTPKK) {
        this.mContext = mContext;
        this.listCTPKK = listCTPKK;
    }
    public void selectAll() {
        isSelectedAll=true;
        notifyDataSetChanged();
    }

    public void unSelectAll() {
        isSelectedAll=false;
        notifyDataSetChanged();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView maLo;
        private TextView maSP;
        private TextView tenSP;
        private TextView SLDB;
        private TextView SLTT;
        private LinearLayout linearLayout;
        private CheckBox cb;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            maLo = itemView.findViewById(R.id.maLo);
            maSP = itemView.findViewById(R.id.masp);
            tenSP = itemView.findViewById(R.id.tensp);
            SLDB = itemView.findViewById(R.id.slDB);
            SLTT = itemView.findViewById(R.id.slTT);
            linearLayout = itemView.findViewById(R.id.linear);
            cb = itemView.findViewById(R.id.checkbox);
        }
    }
    @NonNull
    @Override
    public CTPKKAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View mView = inflater.inflate(R.layout.item_chitietpkk, parent, false);
        CTPKKAdapter.ViewHolder viewHolder = new ViewHolder(mView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CTPKKAdapter.ViewHolder holder, int position) {
        CTPKK item = listCTPKK.get(position);
        if(position % 2 == 0) holder.linearLayout.setBackgroundColor(Color.WHITE);
        holder.maLo.setText(item.getLo().getMaLo());
        holder.maSP.setText(item.getLo().getSanPham().getMaSP());
        holder.tenSP.setText(item.getLo().getSanPham().getTenSP());
        holder.SLDB.setText(String.valueOf(item.getSlDB()));
        if (item.getSlTT() == -1)
            holder.SLTT.setText("Trống");
        else
            holder.SLTT.setText(String.valueOf(item.getSlTT()));
        holder.cb.setOnCheckedChangeListener((compoundButton, b) -> {
            if (compoundButton.isChecked()) listChecked.add(item);
            else listChecked.remove(item);
        });
        if (!isSelectedAll) holder.cb.setChecked(false);
        else holder.cb.setChecked(true);
    }

    @Override
    public int getItemCount() {
        return listCTPKK.size();
    }
    public void deleteCheckedItems() {
        for (CTPKK ctpkk : listChecked) {
            listCTPKK.remove(ctpkk);

            SQLServerConnection db = new SQLServerConnection();
            Connection conn = db.getConnection();
            try {
                Statement stm = conn.createStatement();
                stm.executeUpdate( "exec pro_xoa_chi_tiet_pkk @batchId = " + ctpkk.getLo().getMaLo() +
                        "and @formId = " + ctpkk.getMaPhieu());

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
