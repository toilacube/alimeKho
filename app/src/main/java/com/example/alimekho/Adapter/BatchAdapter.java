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
import com.example.alimekho.Model.loSanPham;
import com.example.alimekho.Model.nhaCungCap;
import com.example.alimekho.R;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class BatchAdapter extends RecyclerView.Adapter<BatchAdapter.BatchViewHolder> {
    private Context context;
    private ArrayList<loSanPham> loSanPhams;
    private ArrayList<loSanPham> listChecked;
    private boolean isSelectedAll = false;

    public BatchAdapter(Context context, ArrayList<loSanPham> loSanPhams) {
        this.context = context;
        this.loSanPhams = loSanPhams;
        listChecked = new ArrayList<>();
    }

    @NonNull
    @Override
    public BatchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_batch, parent, false);
        return new BatchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BatchViewHolder holder, int position) {
        loSanPham loSanPham = loSanPhams.get(position);
        holder.txtmaLo.setText(loSanPham.getMaLo().toString().trim());
        holder.txtmaSP.setText(loSanPham.getSanPham().getMaSP().toString().trim());
        holder.txttenSP.setText(loSanPham.getSanPham().getTenSP().toString().trim());
        holder.txtdonGia.setText(Double.toString(loSanPham.getDonGia()));
        holder.txtNSX.setText(loSanPham.getNSX().toString().trim());
        holder.txtHSD.setText(loSanPham.getHSD().toString().trim());
        holder.txtsoLuong.setText(String.valueOf(loSanPham.getsLTon()).toString().trim());
        holder.checkBox.setChecked(isSelectedAll);
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()) listChecked.add(loSanPham);
                else listChecked.remove(loSanPham);
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
                        loSanPhams.remove(loSanPham);
                        notifyDataSetChanged();
                        SQLServerConnection db = new SQLServerConnection();
                        Connection conn = db.getConnection();
                        try {
                            String delete = "UPDATE batch\n" +
                                    "SET is_deleted = 1\n" +
                                    " WHERE id = ?";
                            delete = "exec pro_xoa_lo @maLo = ?";
                            PreparedStatement stm = conn.prepareStatement(delete);
                            stm.setInt(1, Integer.parseInt(loSanPham.getMaLo()));
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

    public ArrayList<loSanPham> getSelectedLo(){
        return listChecked;
    }
    @Override
    public int getItemCount() {
        return loSanPhams == null ? 0 : loSanPhams.size();
    }

    public class BatchViewHolder extends RecyclerView.ViewHolder{
        private TextView txtmaSP, txttenSP, txtsoLuong, txtdonGia, txtNSX, txtHSD, txtmaLo;
        private LinearLayout linearLayout;
        private CheckBox checkBox;

        public BatchViewHolder(@NonNull View itemView) {
            super(itemView);
            txtmaSP = itemView.findViewById(R.id.txtmaSP);
            txtmaLo = itemView.findViewById(R.id.txtmaLO);
            txttenSP = itemView.findViewById(R.id.txttenSP);
            txtdonGia = itemView.findViewById(R.id.txtdonGia);
            txtsoLuong = itemView.findViewById(R.id.txtsoLuong);
            txtNSX = itemView.findViewById(R.id.txtNSX);
            txtHSD = itemView.findViewById(R.id.txtHSD);
            linearLayout = itemView.findViewById(R.id.custompnk_ln);
            checkBox = itemView.findViewById(R.id.checkbox);
        }
    }
    public void setList(ArrayList<loSanPham> l){
        this.loSanPhams = l;
    }
    public void setFilteredList(ArrayList<loSanPham> filteredList) {
        this.loSanPhams = filteredList;
        notifyDataSetChanged();
    }
    public void setCheckAll(boolean is_checked){
        isSelectedAll = is_checked;
        notifyDataSetChanged();
    }
    public void deleteCheckedItems() {
        SQLServerConnection db = new SQLServerConnection();
        Connection conn = db.getConnection();
        for (loSanPham loSanPham : listChecked) {
            loSanPhams.remove(loSanPham);
            try {
                String delete = "UPDATE batch\n" +
                        "SET is_deleted = 1\n" +
                        " WHERE id = ?";
                delete = "exec pro_xoa_lo @maLo = ?";
                PreparedStatement stm = conn.prepareStatement(delete);
                stm.setInt(1, Integer.parseInt(loSanPham.getMaLo()));
                int rs = stm.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        listChecked.clear();
        notifyDataSetChanged();
    }
}
