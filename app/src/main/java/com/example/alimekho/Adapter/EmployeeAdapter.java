package com.example.alimekho.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alimekho.Activity.DetailEmployeeActivity;
import com.example.alimekho.Activity.EmployeeActivity;
import com.example.alimekho.Activity.SanPhamActivity;
import com.example.alimekho.DataBase.SQLServerConnection;
import com.example.alimekho.Model.Employee;
import com.example.alimekho.R;

import java.io.Serializable;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder> {
    private Context context;
    List<Employee> employeeList;

    public EmployeeAdapter(Context context)
    {
        this.context=context;
    }

    public void setData (List<Employee> list) {
        this.employeeList=list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public EmployeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_employee_view, parent,false);
        return new EmployeeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeViewHolder holder, int position) {
        Employee employee=employeeList.get(position);
        SQLServerConnection db = new SQLServerConnection();
        if(employee==null)
            return;
        holder.id.setText(employee.getId());
        holder.name.setText(employee.getName());
        holder.title.setText(employee.getTitle());
        holder.birth.setText(employee.getDayOfBirth());
        holder.phone.setText(employee.getPhoneNumber());

        holder.linearLayoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToDetailEmployee(employee);
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
                        String id = employee.getId();
                        try {
                            Statement stm = db.getConnection().createStatement();
                            String deleteQuery = "update employee set is_deleted = 1 where id = " + id;
                            stm.executeQuery(deleteQuery);
                            dialog.dismiss();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                });

                dialog.show();
            }
        });

        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(view.getRootView().getContext());
                View dialogView= LayoutInflater.from(view.getRootView().getContext())
                        .inflate(R.layout.dialog_sua_nhanvien,null);
                AlertDialog dialog = builder.create();
                dialog.setView(dialogView);
                dialog.show();
            }
        });
    }
    private void goToDetailEmployee(Employee employee) {
        Intent intent = new Intent(context, DetailEmployeeActivity.class);
        Bundle bundle =new Bundle();
        bundle.putSerializable("object_employee", (Serializable) employee);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
    public void release(){
        context=null;
    }

    @Override
    public int getItemCount() {
        if(employeeList!=null) {
            return employeeList.size();
        }
        return 0;
    }

    public class EmployeeViewHolder extends RecyclerView.ViewHolder{
        private TextView id, name, title, birth, phone;
        private ImageView delete, update;
        private LinearLayout linearLayoutItem;
        public EmployeeViewHolder(@NonNull View itemView) {
            super(itemView);
            id=itemView.findViewById(R.id.tv_id);
            name=itemView.findViewById(R.id.tv_name);
            title=itemView.findViewById(R.id.tv_title);
            birth=itemView.findViewById(R.id.tv_birth);
            phone=itemView.findViewById(R.id.tv_phone);
            delete=itemView.findViewById(R.id.img_delete);
            update=itemView.findViewById(R.id.img_update);
            linearLayoutItem=itemView.findViewById(R.id.linear);
        }


    }

}
