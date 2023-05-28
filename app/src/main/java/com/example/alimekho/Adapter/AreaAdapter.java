package com.example.alimekho.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alimekho.Activity.DetailAreaActivity;
import com.example.alimekho.DataBase.SQLServerConnection;
import com.example.alimekho.Model.Area;
import com.example.alimekho.R;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

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
        holder.shelf.setText(area.getShelf());
        holder.available.setText(Integer.toString(area.getAvailable()));
        holder.type.setText(area.getType_id());

        holder.linearLayoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToDetailArea(area);
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(view.getRootView().getContext());
                View dialogView=LayoutInflater.from(view.getRootView().getContext()).inflate(R.layout.confirm_delete,null);
                builder.setView(dialogView);
                builder.show();
            }
        });
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
        Bundle bundle =new Bundle();
        bundle.putSerializable("object_area", (Serializable) area);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
    public class AreaViewHolder extends RecyclerView.ViewHolder{

        private TextView id, area, shelf, type, available;
        private ImageView delete, update;
        private LinearLayout linearLayoutItem;
        public AreaViewHolder(@NonNull View itemView) {

            super(itemView);
            id=itemView.findViewById(R.id.tv_id);
            area=itemView.findViewById(R.id.tv_area);
            shelf=itemView.findViewById(R.id.tv_shelf);
            type=itemView.findViewById(R.id.tv_type);
            available=itemView.findViewById(R.id.tv_status);
            delete=itemView.findViewById(R.id.img_delete);
            update=itemView.findViewById(R.id.img_update);
            linearLayoutItem=itemView.findViewById(R.id.linear);

        }
    }
}
