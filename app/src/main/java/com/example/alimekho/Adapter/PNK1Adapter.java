package com.example.alimekho.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alimekho.DataBase.SQLServerConnection;
import com.example.alimekho.Model.CTPNK;
import com.example.alimekho.R;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class PNK1Adapter extends RecyclerView.Adapter<PNK1Adapter.PNK1ViewHolder>{
    private Context context;
    private ArrayList<CTPNK> ctpnks;

    public PNK1Adapter(Context context, ArrayList<CTPNK> ctpnks) {
        this.context = context;
        this.ctpnks = ctpnks;
    }

    @NonNull
    @Override
    public PNK1ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_pnk1, parent, false);
        return new PNK1ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull PNK1ViewHolder holder, int position) {
        CTPNK ctpnk = ctpnks.get(position);
        holder.txtmaSP.setText(ctpnk.getSanPham().getMaSP().toString().trim());
        holder.txttenSP.setText(ctpnk.getSanPham().getTenSP().toString().trim());
        holder.txtdonGia.setText(Double.toString(ctpnk.getSanPham().getDonGia()));
        holder.txtsoLuong.setText(Integer.toString(ctpnk.getSoLuong()));
        holder.txtthanhTien.setText(Double.toString(ctpnk.getThanhTien()));
        holder.txtNSX.setText(ctpnk.getNSX().toString().trim());
        holder.txtHSD.setText(ctpnk.getHSD().toString().trim());
        holder.linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Xóa sản phẩm");
                builder.setMessage("Bạn có đồng ý xóa không?");
                builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ctpnks.remove(ctpnk);
                        notifyDataSetChanged();
                        SQLServerConnection db = new SQLServerConnection();
                        Connection conn = db.getConnection();
                        try {
                            String delete = "DELETE FROM detail_input WHERE form_id = ? AND product_id = ?";
                            PreparedStatement stm = conn.prepareStatement(delete);
                            stm.setInt(1, Integer.parseInt(ctpnk.getMaPNK()));
                            stm.setInt(2, Integer.parseInt(ctpnk.getSanPham().getMaSP()));
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
        return ctpnks == null ? 0 : ctpnks.size();
    }

    public class PNK1ViewHolder extends RecyclerView.ViewHolder{
        private TextView txtmaSP, txttenSP, txtsoLuong, txtdonGia, txtNSX, txtHSD, txtthanhTien;
        private LinearLayout linearLayout;

        public PNK1ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtmaSP = itemView.findViewById(R.id.custompnk1_txtmaSP);
            txttenSP = itemView.findViewById(R.id.custompnk1_txttenSP);
            txtdonGia = itemView.findViewById(R.id.custompnk1_txtdonGia);
            txtsoLuong = itemView.findViewById(R.id.custompnk1_txtsoLuong);
            txtNSX = itemView.findViewById(R.id.custompnk1_txtNSX);
            txtHSD = itemView.findViewById(R.id.custompnk1_txtHSD);
            txtthanhTien = itemView.findViewById(R.id.custompnk1_txtThanhTien);
            linearLayout = itemView.findViewById(R.id.custompnk_ln);
        }
    }
}
