package com.example.alimekho.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alimekho.DataBase.SQLServerConnection;
import com.example.alimekho.Model.nhaCungCap;
import com.example.alimekho.Model.phieuNhapKho;
import com.example.alimekho.R;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SupplierAdapter extends RecyclerView.Adapter<SupplierAdapter.SupplierViewHolder> {
    private Context context;
    private ArrayList<nhaCungCap> nhaCungCaps;
    private ArrayList<nhaCungCap> listChecked;
    private boolean isSelectedAll = false;

    public SupplierAdapter(Context context, ArrayList<nhaCungCap> nhaCungCaps) {
        this.context = context;
        this.nhaCungCaps = nhaCungCaps;
        listChecked = new ArrayList<>();
    }

    @NonNull
    @Override
    public SupplierViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_supplier, parent, false);
        return new SupplierViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SupplierViewHolder holder, int position) {
        nhaCungCap nhaCungCap = nhaCungCaps.get(position);
        holder.txtmaNCC.setText(nhaCungCap.getMaNCC().toString().trim());
        holder.txttenNCC.setText(nhaCungCap.getTenCC().toString().trim());
        holder.txtdiaChi.setText(nhaCungCap.getDiaChi().toString().trim());
        holder.txtSDT.setText(nhaCungCap.getSDT().toString().trim());
        holder.cb.setChecked(isSelectedAll);
        holder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()) listChecked.add(nhaCungCap);
                else listChecked.remove(nhaCungCap);
            }
        });
        holder.linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Xóa sản phẩm");
                builder.setMessage("Bạn có đồng ý xóa không?");
                builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        nhaCungCaps.remove(nhaCungCap);
                        notifyDataSetChanged();
                        SQLServerConnection db = new SQLServerConnection();
                        Connection conn = db.getConnection();
                        try {
                            String delete = "UPDATE supplier\n" +
                                    "SET is_deleted = 1\n" +
                                    " WHERE id = ?";
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

    public class SupplierViewHolder extends RecyclerView.ViewHolder{
        private TextView txtmaNCC, txttenNCC, txtdiaChi, txtSDT;
        private LinearLayout linearLayout;
        private CheckBox cb;
        public SupplierViewHolder(@NonNull View itemView) {
            super(itemView);
            txtmaNCC = itemView.findViewById(R.id.customspl_txtmaNCC);
            txttenNCC = itemView.findViewById(R.id.customspl_txttenNCC);
            txtdiaChi = itemView.findViewById(R.id.customspl_txtdiaChi);
            txtSDT = itemView.findViewById(R.id.customspl_txtSDT);
            cb = itemView.findViewById(R.id.check_box);
            linearLayout = itemView.findViewById(R.id.customspl_ln);
        }
    }
    public void setFilteredList(ArrayList<nhaCungCap> filteredList) {
        this.nhaCungCaps = filteredList;
        notifyDataSetChanged();
    }
    public void setCheckAll(boolean is_checked){
        isSelectedAll = is_checked;
        notifyDataSetChanged();
    }
    public void deleteCheckedItems() {
        SQLServerConnection db = new SQLServerConnection();
        Connection conn = db.getConnection();
        for (nhaCungCap nhaCungCap : listChecked) {
            nhaCungCaps.remove(nhaCungCap);
            try {
                String delete = "UPDATE supplier\n" +
                        "SET is_deleted = 1\n" +
                        " WHERE id = ?";
                PreparedStatement stm = conn.prepareStatement(delete);
                stm.setInt(1, Integer.parseInt(nhaCungCap.getMaNCC()));
                int rs = stm.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        listChecked.clear();
        notifyDataSetChanged();
    }
}
