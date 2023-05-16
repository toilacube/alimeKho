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
import com.example.alimekho.Model.CTPNK;
import com.example.alimekho.R;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CTPNKAdapter extends RecyclerView.Adapter<CTPNKAdapter.ViewHolder> {
    private Context mContext;
    private List<CTPNK> listCTPNK;
    private List<CTPNK> listChecked = new ArrayList<>();


    public CTPNKAdapter(Context mContext, List<CTPNK> listCTPNK) {
        this.mContext = mContext;
        this.listCTPNK = listCTPNK;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
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
        View mView = inflater.inflate(R.layout.item_chitietpnk, parent, false);
        ViewHolder viewHolder = new ViewHolder(mView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CTPNK item = listCTPNK.get(position);
        if(position % 2 == 0) holder.linearLayout.setBackgroundColor(Color.WHITE);

        holder.maSP.setText(item.getSanPham().getMaSP());
        holder.tenSP.setText(item.getSanPham().getTenSP());
        holder.SL.setText(String.valueOf(item.getSoLuong()));
        holder.DG.setText(String.valueOf(item.getSanPham().getDonGia()));
        holder.NSX.setText(item.getNSX());
        holder.HSD.setText(item.getHSD());
        holder.TT.setText(String.valueOf(item.getSoLuong() * item.getSanPham().getDonGia()));
        holder.cb.setChecked(false);
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
        return listCTPNK.size();
    }

    public void deleteCheckedItems() {
        SQLServerConnection db = new SQLServerConnection();
        Connection conn = db.getConnection();
        for (CTPNK ctpnk : listChecked) {
            listCTPNK.remove(ctpnk);
            try {
                String delete = "DELETE FROM detail_input WHERE form_id = ? AND product_id = ?";
                PreparedStatement stm = conn.prepareStatement(delete);
                stm.setInt(1, Integer.parseInt(ctpnk.getMaPNK()));
                stm.setInt(2, Integer.parseInt(ctpnk.getSanPham().getMaSP()));
                int rs = stm.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        listChecked.clear();
        notifyDataSetChanged();
    }
}

