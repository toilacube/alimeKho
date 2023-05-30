package com.example.alimekho.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alimekho.DataBase.SQLServerConnection;
import com.example.alimekho.Model.loSanPham;
import com.example.alimekho.Model.sanPham;
import com.example.alimekho.R;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ViTriSPAdapter extends RecyclerView.Adapter<ViTriSPAdapter.ViTriSPViewHolder> {

    Context context;
    ArrayList <loSanPham> listLoSP = new ArrayList<>();

    public ViTriSPAdapter(Context context, ArrayList<loSanPham> listLoSP) {
        this.context = context;
        this.listLoSP = listLoSP;
    }

    @NonNull
    @Override
    public ViTriSPAdapter.ViTriSPViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vi_tri_sp, parent,false);
        return new ViTriSPAdapter.ViTriSPViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViTriSPAdapter.ViTriSPViewHolder holder, int position) {
        loSanPham loSP = listLoSP.get(position);
        sanPham sp = loSP.getSanPham();

        holder.txvID.setText(loSP.getMaLo());
        holder.txvName.setText(sp.getTenSP());
        holder.txvQuantity.setText(Integer.toString(loSP.getsLTon()));

        SQLServerConnection db = new SQLServerConnection();
        try {
            String query = "select location_id from distribute where batch_id = ?";
            PreparedStatement stm = db.getConnection().prepareStatement(query);
            stm.setInt(1, Integer.valueOf(loSP.getMaLo()));
            ResultSet rs = stm.executeQuery();
            if(rs.next()){
                System.out.println(rs.getString("location_id"));
                holder.txvLocation.setText(rs.getString("location_id"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        holder.imvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    @Override
    public int getItemCount() {
        if(listLoSP != null) return listLoSP.size();
        return 0;
    }

    public void setListLoSP(ArrayList<loSanPham> l){
        listLoSP = l;
        notifyDataSetChanged();
    }

    public class ViTriSPViewHolder extends RecyclerView.ViewHolder {
        TextView txvID, txvName, txvLocation, txvQuantity;
        ImageView imvDelete;
        public ViTriSPViewHolder(@NonNull View itemView) {
            super(itemView);
            txvID = itemView.findViewById(R.id.txvMaLoSP);
            txvName = itemView.findViewById(R.id.txvTenSP);
            txvQuantity = itemView.findViewById(R.id.txvQuantity);
            txvLocation = itemView.findViewById(R.id.txvLocation);
            imvDelete = itemView.findViewById(R.id.imvDelete);

        }
    }
}
