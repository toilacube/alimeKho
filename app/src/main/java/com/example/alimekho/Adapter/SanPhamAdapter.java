package com.example.alimekho.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alimekho.Activity.DetailSanPham;
import com.example.alimekho.Model.sanPham;
import com.example.alimekho.R;

import java.util.List;

public class SanPhamAdapter extends RecyclerView.Adapter<SanPhamAdapter.SanPhamViewHolder>{
    private List<sanPham> sanPhamList;
    private Context context;

    public SanPhamAdapter(Context context, List<sanPham> sanPhamList) {
        this.sanPhamList = sanPhamList;
        this.context = context;
    }

    public class SanPhamViewHolder extends RecyclerView.ViewHolder {
        private TextView id, name, soLuong, ngayNhap, NSX, HSD, donGia;
        private ImageView delete, update;
        private LinearLayout linearLayout;
        public SanPhamViewHolder(@NonNull View view){
            super(view);
            id = view.findViewById(R.id.txvID);
            name = view.findViewById(R.id.txvName);
            soLuong = view.findViewById(R.id.txvSoLuong);
            donGia = view.findViewById(R.id.txvDonGia);
            NSX = view.findViewById(R.id.txvNSX);
            HSD = view.findViewById(R.id.txvHSD);
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

        holder.id.setText(sanPham.getMaSP());
        holder.name.setText(sanPham.getTenSP());
//        holder.soLuong.setText(Integer.toString(sanPham.getSoLuong()));
//        holder.donGia.setText(Double.toString(sanPham.getDonGia()));
//        holder.NSX.setText(sanPham.getNSX());
//        holder.HSD.setText(sanPham.getHSD());

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
                        edtSoLuong = dialogView.findViewById(R.id.edtSoLuong),
                        edtNSX = dialogView.findViewById(R.id.edtNSX),
                        edtHSD = dialogView.findViewById(R.id.edtHSD),
                        edtLoaiSP = dialogView.findViewById(R.id.edtLoaiSP),
                        edtDonViTinh = dialogView.findViewById(R.id.edtDonVi);

                edtTenSP.setText(sanPham.getTenSP());
                edtDonGia.setText(Double.toString(sanPham.getDonGia()));
//                edtHSD.setText(sanPham.getHSD());
//                edtNSX.setText(sanPham.getNSX());
 //               edtSoLuong.setText(Integer.toString(sanPham.getSoLuong()));
                edtLoaiSP.setText(sanPham.getPhanLoai());
                edtDonViTinh.setText(sanPham.getDonViTinh());

                Button btnUpdate = dialogView.findViewById(R.id.btnUpdate),
                        btnCancel = dialogView.findViewById(R.id.btnCancel);



                btnUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sanPham.setTenSP(edtTenSP.getText().toString().trim());
                        sanPham.setDonGia(Double.valueOf(edtDonGia.getText().toString().trim()));
//                        sanPham.setHSD(edtHSD.getText().toString().trim());
//                        sanPham.setNSX(edtNSX.getText().toString().trim());
//                        sanPham.setSoLuong(Integer.valueOf(edtSoLuong.getText().toString().trim()));
//                        sanPham.setPhanLoai(edtLoaiSP.getText().toString().trim());
                        sanPham.setDonViTinh(edtDonViTinh.getText().toString().trim());

                        sanPhamList.set(holder.getAdapterPosition(), sanPham);
                        setList(sanPhamList);
                        notifyDataSetChanged();

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
                View dialogView= LayoutInflater.from(view.getRootView().getContext()).inflate(R.layout.confirm_delete,null);
                builder.setView(dialogView);
                builder.show();
            }
        });
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
