package com.example.alimekho.Adapter;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alimekho.Activity.DetailEmployeeActivity;
import com.example.alimekho.DataBase.SQLServerConnection;
import com.example.alimekho.Model.Employee;
import com.example.alimekho.R;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder> {
    private Context context;
    List<Employee> employeeList;
    Calendar myCalendar= Calendar.getInstance();
    SQLServerConnection db = new SQLServerConnection();

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
        if (context.getSharedPreferences("user info", Context.MODE_PRIVATE).getInt("role", -1) == 2) {
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
                                deleteQuery = "exec pro_xoa_nhan_vien @ID = " + id;
                                stm.executeUpdate(deleteQuery);
                                employeeList.remove(employee);
                                notifyDataSetChanged();
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

                    Employee emp = new Employee();

                    ArrayList<String> chucVu = new ArrayList<>();
                    chucVu.add("1. Quản lý");
                    chucVu.add("2. Kế toán");
                    chucVu.add("3. Nhân viên");

                    EditText edtTen, edtNgaySinh, edtCCCD, edtSDT, edtAddress, edtNgayVaoLam;

                    Spinner spinner;

                    edtNgayVaoLam = dialogView.findViewById(R.id.edtNgayVaoLam);
                    edtTen = dialogView.findViewById(R.id.edtTenNhanVien);
                    edtNgaySinh = dialogView.findViewById(R.id.edtNgaySinh);
                    edtCCCD = dialogView.findViewById(R.id.edtCCCD);
                    edtSDT = dialogView.findViewById(R.id.edtSDT);
                    edtAddress = dialogView.findViewById(R.id.edtAddress);
                    spinner = dialogView.findViewById(R.id.spChucVu);
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, chucVu);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);

                    edtTen.setText(employee.getName());
                    edtNgayVaoLam.setText(employee.getNgayVaoLam());
                    edtNgaySinh.setText(employee.getDayOfBirth());
                    edtCCCD.setText(employee.getIdentify());
                    edtSDT.setText(employee.getPhoneNumber());
                    edtAddress.setText(employee.getAddress());
                    spinner.setSelection(Integer.parseInt(employee.getTitle()));

                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            emp.setTitle(Integer.toString(2 - i));
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });

                    DatePickerDialog.OnDateSetListener date = (view1, year, month, day) -> {
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH,month);
                        myCalendar.set(Calendar.DAY_OF_MONTH,day);
                        edtNgaySinh.setText(new SimpleDateFormat("dd/MM/yyyy").format(myCalendar.getTime()));

                    };
                    DatePickerDialog.OnDateSetListener date2 = (view1, year, month, day) -> {
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH,month);
                        myCalendar.set(Calendar.DAY_OF_MONTH,day);
                        edtNgayVaoLam.setText(new SimpleDateFormat("dd/MM/yyyy").format(myCalendar.getTime()));

                    };

                    edtNgaySinh.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new DatePickerDialog(context, date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();

                        }
                    });

                    edtNgayVaoLam.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new DatePickerDialog(context, date2,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                        }
                    });

                    Button btnSua = dialogView.findViewById(R.id.btnUpdate );
                    Button btnThoat = dialogView.findViewById(R.id.btnCancel);

                    btnThoat.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });

                    btnSua.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            emp.setName(edtTen.getText().toString());
                            emp.setIdentify(edtCCCD.getText().toString());
                            emp.setPhoneNumber(edtSDT.getText().toString());
                            emp.setNgayVaoLam(edtNgayVaoLam.getText().toString());
                            emp.setAddress(edtAddress.getText().toString());

                            String query = "exec pro_cap_nhat_nhan_vien @id = ?, @name = ?, " +
                                    "@dateofbirth = ?, @identify = ?, @phone = ?, " +
                                    "@address = ?, @start_day = ?, @role = ?";
                            query = "update employee " +
                                    "set name = N'" + emp.getName() + "',birthday = '" + edtNgaySinh.getText() +"', indentify = '" +emp.getIdentify() +
                                    "', phone = '"+ emp.getPhoneNumber() +"',  address = '"+ emp.getAddress()+"', " +
                                    "start_day = '"+ edtNgayVaoLam.getText() +"', role = "+ emp.getTitle() +
                                    "where id = " + emp.getId();


                            try{
//                                PreparedStatement stm = db.getConnection().prepareStatement(query);
//                                stm.setString(8, employee.getId());
//                                stm.setString(1, emp.getName());
//                                stm.setString(3, emp.getIdentify());
//                                stm.setString(4, emp.getPhoneNumber());
//                                stm.setString(2, emp.getDayOfBirth());
//                                stm.setString(5, emp.getAddress());
//                                stm.setString(6, emp.getNgayVaoLam());
//                                stm.setString(7, emp.getTitle());
//                                stm.executeUpdate();

                                Statement stm =  db.getConnection().createStatement();
                                stm.executeUpdate(query);

                                notifyDataSetChanged();
                                dialog.dismiss();
                            }
                            catch (SQLException e){
                                Log.e(TAG, e.toString());
                            }
                        }
                    });

                    dialog.show();
                }
            });

        } else {
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "Bạn không có quyền thực hiện thao tác này", Toast.LENGTH_SHORT).show();
                }
            });
            holder.update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "Bạn không có quyền thực hiện thao tác này", Toast.LENGTH_SHORT).show();
                }
            });
        }



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
