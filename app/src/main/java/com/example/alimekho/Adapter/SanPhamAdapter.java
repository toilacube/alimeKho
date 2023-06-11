package com.example.alimekho.Adapter;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alimekho.Activity.DetailSanPham;
import com.example.alimekho.DataBase.SQLServerConnection;
import com.example.alimekho.Model.sanPham;
import com.example.alimekho.R;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SanPhamAdapter extends RecyclerView.Adapter<SanPhamAdapter.SanPhamViewHolder>{
    private List<sanPham> sanPhamList;
    private Context context;

    private SQLServerConnection db = new SQLServerConnection();

    public SanPhamAdapter(Context context, List<sanPham> sanPhamList) {
        this.sanPhamList = sanPhamList;
        this.context = context;
    }

    public class SanPhamViewHolder extends RecyclerView.ViewHolder {
        private TextView id, name, donViTinh, donGia, loaiSP, nhaCC;
        private ImageView delete, update;
        private LinearLayout linearLayout;
        public SanPhamViewHolder(@NonNull View view){
            super(view);
            id = view.findViewById(R.id.txvID);
            name = view.findViewById(R.id.txvName);
            donViTinh = view.findViewById(R.id.txvDonViTinh);
            loaiSP = view.findViewById(R.id.txvLoaiSP);
            nhaCC = view.findViewById(R.id.txvNhaCC);
            linearLayout = view.findViewById(R.id.linear);
            update = view.findViewById(R.id.img_update);
            delete = view.findViewById(R.id.img_delete);
        }
    }

    @NonNull
    @Override
    public SanPhamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.item_san_pham, parent, false);
        context = parent.getContext();
        return new SanPhamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SanPhamViewHolder holder, int position) {
        sanPham sanPham = sanPhamList.get(position);
        SQLServerConnection db = new SQLServerConnection();

        holder.id.setText(sanPham.getMaSP());
        holder.name.setText(sanPham.getTenSP());
        holder.donViTinh.setText(sanPham.getDonViTinh());
       // holder.donGia.setText(Double.toString(sanPham.getDonGia()));

        try {
            Statement stm1 = db.getConnection().createStatement();

            String type = sanPham.getPhanLoai();
            String getTypeQuery = "SELECT name FROM product_type where id = " + type;
            ResultSet rsType = stm1.executeQuery(getTypeQuery);

            if(rsType.next())
                holder.loaiSP.setText(rsType.getString(1));

            String supplier = sanPham.getSupplier_id();
            String getSupplierQuery = "select name from supplier where id = " + supplier;
            ResultSet rsSupplier = stm1.executeQuery(getSupplierQuery);
            if(rsSupplier.next())
                holder.nhaCC.setText(rsSupplier.getString(1));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailSanPham.class);
                intent.putExtra("SP", sanPham);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder=new AlertDialog.Builder(view.getRootView().getContext());
                View dialogView= LayoutInflater.from(view.getRootView().getContext())
                        .inflate(R.layout.dialog_update_san_pham,null);
                AlertDialog dialog = builder.create();

                EditText edtTenSP = dialogView.findViewById(R.id.edtTenSP),
                         edtDonGia = dialogView.findViewById(R.id.edtDonGia),
                         edtDonViTinh = dialogView.findViewById(R.id.edtDonVi);
                Spinner spLoaiSP = dialogView.findViewById(R.id.spLoaiSP),
                        spNhaCC = dialogView.findViewById(R.id.spNhaCC);

                ArrayList<String> listLoaiSP = getLoaiSP();
                ArrayAdapter<String> adapterLoaiSP = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, listLoaiSP);
                adapterLoaiSP.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spLoaiSP.setAdapter(adapterLoaiSP);
                spLoaiSP.setSelection(adapterLoaiSP.getPosition(sanPham.getMaSP()));

                ArrayList<String> listNhaCC = new ArrayList<>();
                listNhaCC = getNhaCC();
                ArrayAdapter<String> adapterNhaCC = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, listNhaCC);
                adapterNhaCC.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spNhaCC.setAdapter(adapterNhaCC);
                spNhaCC.setSelection(adapterNhaCC.getPosition(sanPham.getSupplier_id()));

                spLoaiSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        sanPham.setPhanLoai(getIDLoaiSP().get(i));
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                spNhaCC.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        sanPham.setSupplier_id(getIDNhaCC().get(i));
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                edtTenSP.setText(sanPham.getTenSP());
                //edtDonGia.setText(Double.toString(sanPham.getDonGia()));
                edtDonViTinh.setText(sanPham.getDonViTinh());
                Button btnUpdate = dialogView.findViewById(R.id.btnUpdate),
                        btnCancel = dialogView.findViewById(R.id.btnCancel);

                // Thay doi thong tin san pham
                btnUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sanPham.setTenSP(edtTenSP.getText().toString().trim());
                       // sanPham.setDonGia(Double.valueOf(edtDonGia.getText().toString().trim()));
                        sanPham.setDonViTinh(edtDonViTinh.getText().toString().trim());

                        try {
                            Statement stm = db.getConnection().createStatement();
                            String updateSP = "UPDATE PRODUCT " +
                                    "SET NAME = '" +  edtTenSP.getText().toString().trim() +
                                    "', UNIT = '" + edtDonViTinh.getText().toString().trim() +
                                    "', UNIT_PRICE  = " + Double.valueOf(edtDonGia.getText().toString().trim()) +
                                    ", type_id = " + sanPham.getPhanLoai() +
                                    ", supplier_id = " + sanPham.getSupplier_id() +
                                    " WHERE ID = " + sanPham.getMaSP();

                            stm.executeUpdate(updateSP);
                        } catch (SQLException e) {
                            Log.e(TAG, e.toString());
                        }

                        sanPhamList.set(holder.getAdapterPosition(), sanPham);
                        setList(sanPhamList);
                        notifyDataSetChanged();
                        dialog.dismiss();

                    }
                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });


                int width = (int)(context.getResources().getDisplayMetrics().widthPixels*0.90);
                int height = (int)(context.getResources().getDisplayMetrics().heightPixels*0.90);

                dialog.setView(dialogView);
                dialog.getWindow().setLayout(width, height);
                dialog.show();
            }

        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder=new AlertDialog.Builder(view.getRootView().getContext());
                AlertDialog dialog = builder.create();
                View dialogDelete= LayoutInflater.from(view.getRootView().getContext()).inflate(R.layout.confirm_delete,null);
                dialog.setView(dialogDelete);

                Button btnHuy = dialogDelete.findViewById(R.id.btn_huy),
                        btnDelete = dialogDelete.findViewById(R.id.btn_delete);

                btnHuy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String id = sanPham.getMaSP();
                        try {
                            String deleteQuery = "update product set is_deleted = 1 where id = ?";
                            deleteQuery = "exec pro_xoa_san_pham @id = ?";
                            PreparedStatement stm = db.getConnection().prepareStatement(deleteQuery);
                            stm.setString(1, id);
                            stm.executeUpdate();
                            dialog.dismiss();
                            notifyDataSetChanged();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                });


                dialog.show();
            }
        });
    }

    private ArrayList<String> getLoaiSP() {
        ArrayList<String> l = new ArrayList<>();
        try {
            String select = "SELECT id, name FROM product_type";
            Statement stm = db.getConnection().createStatement();
            ResultSet rs = stm.executeQuery(select);
            while(rs.next()){
                String temp = String.valueOf(rs.getInt(1)) + " - " + rs.getString(2);
                l.add(temp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return l;
    }

    private ArrayList<String> getNhaCC() {
        ArrayList<String> l = new ArrayList<>();
        try {
            String select = "SELECT id, name FROM supplier";
            Statement stm = db.getConnection().createStatement();
            ResultSet rs = stm.executeQuery(select);
            while(rs.next()){
                String temp = String.valueOf(rs.getInt(1)) + " - " + rs.getString(2);
                l.add(temp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return l;
    }

    private ArrayList<String> getIDLoaiSP() {
        ArrayList<String> l = new ArrayList<>();
        try {
            String select = "SELECT id FROM product_type";
            Statement stm = db.getConnection().createStatement();
            ResultSet rs = stm.executeQuery(select);
            while(rs.next()){
                String temp = String.valueOf(rs.getInt(1));
                l.add(temp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return l;
    }

    private ArrayList<String> getIDNhaCC() {
        ArrayList<String> l = new ArrayList<>();
        try {
            String select = "SELECT id FROM supplier";
            Statement stm = db.getConnection().createStatement();
            ResultSet rs = stm.executeQuery(select);
            while(rs.next()){
                String temp = String.valueOf(rs.getInt(1));
                l.add(temp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return l;
    }

    public void setList(List<sanPham> sanPhamList) {
        this.sanPhamList = sanPhamList;
    }


    @Override
    public int getItemCount() {
        if(sanPhamList != null)
            return sanPhamList.size();
        return 0;
    }

}
