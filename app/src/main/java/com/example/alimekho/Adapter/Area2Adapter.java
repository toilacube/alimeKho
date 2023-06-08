package com.example.alimekho.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alimekho.Activity.DetailAreaActivity;
import com.example.alimekho.DataBase.SQLServerConnection;
import com.example.alimekho.Model.Area;
import com.example.alimekho.Model.sanPham;
import com.example.alimekho.R;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Area2Adapter extends RecyclerView.Adapter<Area2Adapter.AreaViewHolder> {
    private Context context;
    List<Area> areaList;

    int currentSelected = 0;
    int previousSelected = 0;

    SQLServerConnection db = new SQLServerConnection();

    public Area2Adapter(Context context)
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_area_view2, parent,false);
        return new Area2Adapter.AreaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AreaViewHolder holder, int position) {
        Area area = areaList.get(position);
        if (area == null)
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

        holder.radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.radioButton.isChecked()){
                    previousSelected = currentSelected;
                    currentSelected = holder.getAdapterPosition();
                }
            notifyDataSetChanged();
            }
        });

        if(currentSelected == holder.getAdapterPosition()){
            holder.radioButton.setChecked(true);
        }
        else {
            holder.radioButton.setChecked(false);
        }
    }

    public Area getSelectedArea(){
        return areaList.get(currentSelected);
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
        private LinearLayout linearLayoutItem;
        private RadioButton radioButton;
        public AreaViewHolder(@NonNull View itemView) {

            super(itemView);
            id=itemView.findViewById(R.id.tv_id);
            area=itemView.findViewById(R.id.tv_area);
            shelf=itemView.findViewById(R.id.tv_shelf);
            type=itemView.findViewById(R.id.tv_type);
            linearLayoutItem=itemView.findViewById(R.id.linear);
            radioButton = itemView.findViewById(R.id.radioButton);
        }
    }
}
