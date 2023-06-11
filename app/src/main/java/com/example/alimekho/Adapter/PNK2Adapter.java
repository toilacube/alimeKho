package com.example.alimekho.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import com.example.alimekho.Activity.CreatePNK2Activity;
import com.example.alimekho.DataBase.SQLServerConnection;
import com.example.alimekho.Model.nhaCungCap;
import com.example.alimekho.Model.sanPham;
import com.example.alimekho.R;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class PNK2Adapter extends Adapter<PNK2Adapter.PNK2ViewHolder>{
    private Context context;
    private ArrayList<nhaCungCap> nhaCungCaps;
    private setData setData;
    private nhaCungCap nhaCungCapVV;
    public PNK2Adapter(Context context, ArrayList<nhaCungCap> nhaCungCaps) {
        this.context = context;
        this.nhaCungCaps = nhaCungCaps;
        this.setData = (PNK2Adapter.setData) context;
    }

    public interface setData {
        void setNhaCungCap(nhaCungCap nhaCungCap);
    }

    @NonNull
    @Override
    public PNK2ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_pnk2, parent, false);
        return new PNK2ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PNK2ViewHolder holder, int position) {
        nhaCungCap nhaCungCap = nhaCungCaps.get(position);
        holder.txtmaNCC.setText(nhaCungCap.getMaNCC().toString().trim());
        holder.txttenNCC.setText(nhaCungCap.getTenCC().toString().trim());
        holder.txtdiaChi.setText(nhaCungCap.getDiaChi().toString().trim());
        holder.txtSDT.setText(nhaCungCap.getSDT().toString().trim());
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nhaCungCapVV = (new nhaCungCap(nhaCungCap.getMaNCC().toString().trim(), nhaCungCap.getTenCC().toString().trim(),
                        nhaCungCap.getDiaChi().toString().trim(), nhaCungCap.getSDT().toString()));
                setData.setNhaCungCap(nhaCungCapVV);
            }
        });
        holder.linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Xóa nhà cung cấp");
                builder.setMessage("Bạn có đồng ý xóa không?");
                builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        nhaCungCaps.remove(nhaCungCap);
                        setData.setNhaCungCap(new nhaCungCap("", "", "", ""));
                        notifyDataSetChanged();
                        SQLServerConnection db = new SQLServerConnection();
                        Connection conn = db.getConnection();
                        try {
                            String delete = "UPDATE supplier\n" +
                                    "SET is_deleted = 1\n" +
                                    " WHERE id = ?";
                            delete = "exec pro_xoa_nha_cung_cap @maNCC = ?";
                            PreparedStatement stm = conn.prepareStatement(delete);
                            stm.setInt(1, Integer.parseInt(nhaCungCap.getMaNCC()));
                            int rs = stm.executeUpdate();

                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        dialog.cancel();
                    }
                });
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return true;
            }
        });
    }
    @Override
    public int getItemCount() {
        return nhaCungCaps == null ? 0 : nhaCungCaps.size();
    }

    public class PNK2ViewHolder extends RecyclerView.ViewHolder{
        private TextView txtmaNCC, txttenNCC, txtdiaChi, txtSDT;
        private LinearLayout linearLayout;
        public PNK2ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtmaNCC = itemView.findViewById(R.id.custompnk2_txtmaNCC);
            txttenNCC = itemView.findViewById(R.id.custompnk2_txttenNCC);
            txtdiaChi = itemView.findViewById(R.id.custompnk2_txtdiaChi);
            txtSDT = itemView.findViewById(R.id.custompnk2_txtSDT);
            linearLayout = itemView.findViewById(R.id.custompnk_ln);
        }
    }
    public void setFilteredList(ArrayList<nhaCungCap> filteredList) {
        this.nhaCungCaps = filteredList;
        notifyDataSetChanged();
    }
}