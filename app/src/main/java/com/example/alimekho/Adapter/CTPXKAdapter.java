package com.example.alimekho.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alimekho.DataBase.SQLServerConnection;
import com.example.alimekho.Model.CTPXK;
import com.example.alimekho.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CTPXKAdapter extends RecyclerView.Adapter<CTPXKAdapter.ViewHolder> {
    private Context mContext;
    private List<CTPXK> listCTPXK;
    private List<CTPXK> listChecked = new ArrayList<>();
    private boolean isSelectedAll = false;
    public CTPXKAdapter(Context mContext, List<CTPXK> listCTPXK) {
        this.mContext = mContext;
        this.listCTPXK = listCTPXK;
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
        private TextView SL;
        private TextView DG;
        private TextView NSX;
        private TextView HSD;
        private TextView TT;
        private LinearLayout linearLayout;
        private CheckBox cb;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            maLo = itemView.findViewById(R.id.maLo);
            maSP = itemView.findViewById(R.id.masp);
            tenSP = itemView.findViewById(R.id.tensp);
            SL = itemView.findViewById(R.id.sl);
            DG = itemView.findViewById(R.id.dongia);
            NSX = itemView.findViewById(R.id.nsx);
            HSD = itemView.findViewById(R.id.hsd);
            TT = itemView.findViewById(R.id.thanhtien);
            linearLayout = itemView.findViewById(R.id.linear);
            cb = itemView.findViewById(R.id.checkbox);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View mView = inflater.inflate(R.layout.item_chitietpxk, parent, false);
        ViewHolder viewHolder = new ViewHolder(mView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CTPXK item = listCTPXK.get(position);
        if(position % 2 == 0) holder.linearLayout.setBackgroundColor(Color.WHITE);

        holder.maLo.setText(item.getLo().getMaLo());
        holder.maSP.setText(item.getLo().getSanPham().getMaSP());
        holder.tenSP.setText(item.getLo().getSanPham().getTenSP());
        holder.SL.setText(String.valueOf(item.getSoLuong()));
        holder.DG.setText(String.valueOf((int)item.getLo().getDonGia()));
        holder.NSX.setText(item.getLo().getNSX());
        holder.HSD.setText(item.getLo().getHSD());
        holder.TT.setText(String.valueOf((int)item.getThanhTien()));
        if (!isSelectedAll) holder.cb.setChecked(false);
        else holder.cb.setChecked(true);

        holder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()) listChecked.add(item);
                else listChecked.remove(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listCTPXK.size();
    }

    public void deleteCheckedItems() {
        for (CTPXK ctpxk : listChecked) {
            listCTPXK.remove(ctpxk);

            SQLServerConnection db = new SQLServerConnection();
            Connection conn = db.getConnection();
            try {
                Statement stm = conn.createStatement();
                stm.executeUpdate( "delete from detail_output where batch_id = " + ctpxk.getLo().getMaLo() +
                        "and form_id = " + ctpxk.getMaPXK());

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
