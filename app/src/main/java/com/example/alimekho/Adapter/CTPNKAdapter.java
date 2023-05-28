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
import com.example.alimekho.Model.loSanPham;
import com.example.alimekho.Model.sanPham;
import com.example.alimekho.R;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class CTPNKAdapter extends RecyclerView.Adapter<CTPNKAdapter.ViewHolder> {
    private Context mContext;
    private List<CTPNK> listCTPNK;
    private List<CTPNK> listChecked = new ArrayList<>();
    private SQLServerConnection db = new SQLServerConnection();
    private Connection conn = db.getConnection();


    public CTPNKAdapter(Context mContext, List<CTPNK> listCTPNK) {
        this.mContext = mContext;
        this.listCTPNK = listCTPNK;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView maSP;
        private TextView maLo;
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
            maLo = itemView.findViewById(R.id.maLo);
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
        try {
            String select = "SET DATEFORMAT DMY\n" +
                    "SELECT batch.id, product_id, NSX, HSD, unit_price, name FROM batch\n" +
                    "JOIN product ON batch.product_id = product.id\n" +
                    "WHERE batch.is_deleted = 0 AND batch.id = " + item.getMaLo();
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(select);
            while(rs.next()){
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String NSX = dateFormat.format(rs.getDate("NSX"));
                String HSD = dateFormat.format(rs.getDate("HSD"));
                holder.maLo.setText(String.valueOf(rs.getInt(1)));
                holder.maSP.setText(String.valueOf(rs.getInt("product_id")));
                holder.tenSP.setText(rs.getString("name"));
                holder.SL.setText(String.valueOf(item.getSoLuong()));
                holder.DG.setText(String.valueOf(rs.getDouble("unit_price")));
                holder.NSX.setText(NSX);
                holder.HSD.setText(HSD);
                holder.TT.setText(String.valueOf(item.getThanhTien()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
        for (CTPNK ctpnk : listChecked) {
            listCTPNK.remove(ctpnk);
            try {
                String delete = "DELETE FROM detail_input WHERE form_id = ? AND batch_id = ?";
                PreparedStatement stm = conn.prepareStatement(delete);
                stm.setInt(1, Integer.parseInt(ctpnk.getMaPNK()));
                stm.setInt(2, Integer.parseInt(ctpnk.getMaLo()));
                int rs = stm.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        listChecked.clear();
        notifyDataSetChanged();
    }
}

