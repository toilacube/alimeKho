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

import com.example.alimekho.Activity.CreatePXK2Activity;
import com.example.alimekho.DataBase.SQLServerConnection;
import com.example.alimekho.Model.cuaHangXuat;
import com.example.alimekho.Model.nhaCungCap;
import com.example.alimekho.R;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class PXK2Adapter extends RecyclerView.Adapter<PXK2Adapter.PXK2ViewHolder>{
    private OnItemClickListener itemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }
    private Context context;
    private ArrayList<cuaHangXuat> cuaHangXuats;
    private cuaHangXuat cuaHangXuatVV;

    public PXK2Adapter(Context context, ArrayList<cuaHangXuat> cuaHangXuats) {
        this.context = context;
        this.cuaHangXuats = cuaHangXuats;
    }

    @NonNull
    @Override
    public PXK2ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_pxk2, parent, false);
        return new PXK2ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PXK2ViewHolder holder, int position) {
        cuaHangXuat cuaHangXuat = cuaHangXuats.get(position);
        holder.txtmaCHX.setText(cuaHangXuat.getMaCHX().toString().trim());
        holder.txttenCHX.setText(cuaHangXuat.getTenCHX().toString().trim());
        holder.txtdiaChi.setText(cuaHangXuat.getDiaChi().toString().trim());
        holder.txtSDT.setText(cuaHangXuat.getSDT().toString().trim());
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(cuaHangXuat);
                }
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
                        cuaHangXuats.remove(cuaHangXuat);
                        notifyDataSetChanged();
                        SQLServerConnection db = new SQLServerConnection();
                        Connection conn = db.getConnection();
                        try {
                            String delete = "DELETE FROM store WHERE id = ?";
                            delete = "exec pro_xoa_dia_diem_xuat @SupermarketId = ?";
                            PreparedStatement stm = conn.prepareStatement(delete);
                            stm.setInt(1, Integer.parseInt(cuaHangXuat.getMaCHX()));
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
        return cuaHangXuats == null ? 0 : cuaHangXuats.size();
    }

    public class PXK2ViewHolder extends RecyclerView.ViewHolder{
        private TextView txtmaCHX, txttenCHX, txtdiaChi, txtSDT;
        private LinearLayout linearLayout;
        public PXK2ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtmaCHX = itemView.findViewById(R.id.custompxk2_txtmaCHX);
            txttenCHX = itemView.findViewById(R.id.custompxk2_txttenCHX);
            txtdiaChi = itemView.findViewById(R.id.custompxk2_txtdiaChi);
            txtSDT = itemView.findViewById(R.id.custompxk2_txtSDT);
            linearLayout = itemView.findViewById(R.id.custompxk_ln);
        }
    }
    public void setFilteredList(ArrayList<cuaHangXuat> filteredList) {
        this.cuaHangXuats = filteredList;
        notifyDataSetChanged();
    }
    public interface OnItemClickListener {
        void onItemClick(cuaHangXuat data);
    }
}
