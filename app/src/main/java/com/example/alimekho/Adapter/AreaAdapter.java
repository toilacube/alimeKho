package com.example.alimekho.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alimekho.Activity.DetailAreaActivity;
import com.example.alimekho.DataBase.SQLServerConnection;
import com.example.alimekho.Model.Area;
import com.example.alimekho.Model.sanPham;
import com.example.alimekho.R;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AreaAdapter extends RecyclerView.Adapter<AreaAdapter.AreaViewHolder> {
    private Context context;
    List<Area> areaList;

    SQLServerConnection db = new SQLServerConnection();

    public AreaAdapter(Context context)
    {
        this.context=context;
    }

    public void setData (List<Area> list) {
        this.areaList=list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AreaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_area_view, parent,false);
        return new AreaAdapter.AreaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AreaViewHolder holder, int position) {
        Area area = areaList.get(position);
        if(area==null)
            return;
        holder.id.setText(area.getId());
        holder.area.setText(area.getArea());
        holder.shelf.setText(area.getShelve());
        holder.type.setText(area.getType_id());

        holder.linearLayoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToDetailArea(area);
            }
        });

        if (context.getSharedPreferences("user info", Context.MODE_PRIVATE).getInt("role", 0) == 2){
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder=new AlertDialog.Builder(view.getRootView().getContext());
                    View dialogView=LayoutInflater.from(view.getRootView().getContext()).inflate(R.layout.confirm_delete,null);
                    builder.setView(dialogView);
                    builder.show();
                }
            });
            holder.update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder=new AlertDialog.Builder(view.getRootView().getContext());
                    View dialogView= LayoutInflater.from(view.getRootView().getContext())
                            .inflate(R.layout.dialog_them_vi_tri,null);
                    AlertDialog dialog = builder.create();
                    dialog.setView(dialogView);

                    TextView txvTitle = dialogView.findViewById(R.id.txvSua);
                    EditText edtZone = dialogView.findViewById(R.id.edtZone),
                            edtShelve = dialogView.findViewById(R.id.edtShelve);
                    Button btnAdd = dialogView.findViewById(R.id.btnAdd),
                            btnCancel = dialogView.findViewById(R.id.btnCancel);
                    Spinner spLoaiSP = dialogView.findViewById(R.id.spLoaiSP);

                    ArrayList<String> listLoaiSP = new ArrayList<>();
                    listLoaiSP = getLoaiSP();
                    ArrayAdapter<String> adapterLoaiSP = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, listLoaiSP);
                    adapterLoaiSP.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spLoaiSP.setAdapter(adapterLoaiSP);
                    spLoaiSP.setSelection(adapterLoaiSP.getPosition(area.getType_id()));

                    sanPham sanPham = new sanPham();

                    spLoaiSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            sanPham.setPhanLoai(getIDLoaiSP().get(i));
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                    dialog.show();
                }
            });
        } else {
            holder.delete.setOnClickListener(view -> Toast.makeText(context, "Bạn không có quyền thực hiện thao tác này", Toast.LENGTH_SHORT).show());
            holder.update.setOnClickListener(view -> Toast.makeText(context, "Bạn không có quyền thực hiện thao tác này", Toast.LENGTH_SHORT).show());
        }
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

    @Override
    public int getItemCount() {
        if(areaList!=null) {
            return areaList.size();
        }
        return 0;
    }
    public void release(){
        context=null;
    }

    private void goToDetailArea(Area area) {
        Intent intent = new Intent(context, DetailAreaActivity.class);
        intent.putExtra("object_area", area);
        context.startActivity(intent);
    }
    public class AreaViewHolder extends RecyclerView.ViewHolder{

        private TextView id, area, shelf, type;
        private ImageView delete, update;
        private LinearLayout linearLayoutItem;
        public AreaViewHolder(@NonNull View itemView) {

            super(itemView);
            id=itemView.findViewById(R.id.tv_id);
            area=itemView.findViewById(R.id.tv_area);
            shelf=itemView.findViewById(R.id.tv_shelf);
            type=itemView.findViewById(R.id.tv_type);
            delete=itemView.findViewById(R.id.img_delete);
            update=itemView.findViewById(R.id.img_update);
            linearLayoutItem=itemView.findViewById(R.id.linear);

        }
    }
}
