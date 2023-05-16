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
import com.example.alimekho.DataBase.SQLServerConnection;
import com.example.alimekho.Model.phieuNhapKho;
import com.example.alimekho.R;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PNKAdapter extends RecyclerView.Adapter<PNKAdapter.ViewHolder> {
    private Context mContext;
    private List<phieuNhapKho> listPNK;
    private List<phieuNhapKho> listChecked = new ArrayList<>();

    public PNKAdapter(Context mContext, List<phieuNhapKho> listPNK) {
        this.mContext = mContext;
        this.listPNK = listPNK;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvMaPhieu;
        private TextView tvNgNK;
        private TextView tvNCC;
        private TextView tvNPT;
        private LinearLayout linearLayout;
        private CheckBox cb;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMaPhieu = itemView.findViewById(R.id.tv_ma_phieu);
            tvNgNK = itemView.findViewById(R.id.tv_ngXK);
            tvNCC = itemView.findViewById(R.id.tv_ncc);
            tvNPT = itemView.findViewById(R.id.tv_npt);
            linearLayout = itemView.findViewById(R.id.linear);
            cb = itemView.findViewById(R.id.checkbox);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View mView = inflater.inflate(R.layout.item_pnk, parent, false);
        ViewHolder viewHolder = new ViewHolder(mView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        phieuNhapKho pnk = listPNK.get(position);
        if(position % 2 == 0) holder.linearLayout.setBackgroundColor(Color.WHITE);

        holder.tvMaPhieu.setText(pnk.getMaPhieu().toString().trim());
        holder.tvNPT.setText(pnk.getTenNV());
        holder.tvNCC.setText(pnk.getTenNCC());
        holder.tvNgNK.setText(pnk.getNgayNhapKho());
        holder.cb.setChecked(false);
        holder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()) listChecked.add(pnk);
                else listChecked.remove(pnk);
            }
        });
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CTPNKActivity.class);
                intent.putExtra("pnk", pnk);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listPNK.size();
    }

    public void deleteCheckedItems() {
        SQLServerConnection db = new SQLServerConnection();
        Connection conn = db.getConnection();
        for (phieuNhapKho pnk : listChecked) {
            listPNK.remove(pnk);
            try {
                String delete = "DELETE FROM detail_input WHERE form_id = ?";
                String delete1 = "DELETE FROM input_form WHERE id = ?";
                PreparedStatement stm = conn.prepareStatement(delete);
                PreparedStatement stm1 = conn.prepareStatement(delete1);
                stm.setInt(1, Integer.parseInt(pnk.getMaPhieu()));
                stm1.setInt(1, Integer.parseInt(pnk.getMaPhieu()));
                int rs = stm.executeUpdate();
                int rs1 = stm1.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        listChecked.clear();
        notifyDataSetChanged();
    }
}
