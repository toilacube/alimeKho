package com.example.alimekho.Activity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alimekho.Adapter.CTPNKAdapter;
import com.example.alimekho.DataBase.SQLServerConnection;
import com.example.alimekho.Model.CTPNK;
import com.example.alimekho.Model.loSanPham;
import com.example.alimekho.Model.phieuNhapKho;
import com.example.alimekho.Model.phieuXuatKho;
import com.example.alimekho.Model.sanPham;
import com.example.alimekho.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class CTPNKActivity extends AppCompatActivity {
    private TextView txtmaPhieu, txttenNCC, txtngayNhap, txtnhanVien, txttoTal;
    private SQLServerConnection db = new SQLServerConnection();
    private String product;
    private phieuNhapKho phieuNhapKho;
    private Connection conn = db.getConnection();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ctpnk);

        String maPhieu = (String) getIntent().getSerializableExtra("mapnk");
        try {
            Statement stm = conn.createStatement();
            String getInputform = "SET DATEFORMAT DMy\n" +
                    "select input_form.id, input_form.input_day, supplier.name, employee.name, total from input_form \n" +
                    "LEFT JOIN batch ON input_form.id = batch.id\n" +
                    "LEFT JOIN product ON product.id = batch.product_id\n" +
                    "LEFT JOIN supplier ON product.supplier_id = supplier.id\n" +
                    "LEFT JOIN employee ON input_form.emp_id = employee.id\n"+
                    "WHERE input_form.id = " + String.valueOf(maPhieu);
            ResultSet rs = stm.executeQuery(getInputform);
            while (rs.next()) {
                String pattern = "dd/MM/yyyy";
                DateFormat df = new SimpleDateFormat(pattern);
                String todayAsString = df.format(rs.getDate(2));
                phieuNhapKho = new phieuNhapKho(String.valueOf(rs.getInt(1)), todayAsString, rs.getString(3), rs.getString(4), rs.getInt(5));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        txtmaPhieu = findViewById(R.id.gdCTPNK_txtmaPhieu);
        txtngayNhap = findViewById(R.id.gdCTPNK_txtngayNhap);
        txttenNCC = findViewById(R.id.gdCTPNK_txtNCC);
        txtnhanVien = findViewById(R.id.gdCTPNK_txtnguoiPT);
        txttoTal = findViewById(R.id.gdCTPNK_txtthanhTien);
        setData(phieuNhapKho);
        //test
        RecyclerView rcv = findViewById(R.id.rcv);
        CTPNKAdapter adapter = new CTPNKAdapter(this, getListCTPNK(phieuNhapKho));
        rcv.setAdapter(adapter);
        rcv.setLayoutManager(new LinearLayoutManager(this));
        //back
        Button btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //del btn
        Button delbtn = findViewById(R.id.btn_xoa);
        delbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.deleteCheckedItems();
            }
        });

        //edit btn
        Button editbtn = findViewById(R.id.btn_sua);
        editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(CTPNKActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.edit_ctpnk);
                Window window = dialog.getWindow();
                if (window == null) return;
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                Spinner spinner = dialog.findViewById(R.id.spinnersp);
                EditText sl = dialog.findViewById(R.id.sl);
                ArrayList<String> products = getListSP(phieuNhapKho);
                ArrayList<String> productss = getmaSP(phieuNhapKho);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        product = productss.get(position);
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                ArrayAdapter<String> adapter = new ArrayAdapter<>(CTPNKActivity.this, android.R.layout.simple_spinner_dropdown_item, products);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
                CardView cancel = dialog.findViewById(R.id.huy);

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                CardView confirm = dialog.findViewById(R.id.xacnhan);
                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            Statement stm = conn.createStatement();
                            String Query = "update detail_input" +
                                    "\nset quantity = " + sl.getText()
                                    +"\nwhere form_id = " + phieuNhapKho.getMaPhieu()
                                    +"and product_id = " + product;
                            stm.executeUpdate(Query);
                            Toast.makeText(CTPNKActivity.this, "Thanh cong", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            recreate();
                        } catch (SQLException e) {
                            e.printStackTrace();
                            Toast.makeText(CTPNKActivity.this, "That bai", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


                dialog.show();
            }
        });
    }
    public ArrayList<String> getListSP(phieuNhapKho phieuNhapKho){
        ArrayList<String> l = new ArrayList<>();
        try {
            String select = "select product_id, name from detail_input \n" +
                    "JOIN product ON detail_input.product_id = product.id\n" +
                    "WHERE detail_input.form_id = " + phieuNhapKho.getMaPhieu();
            Statement stm = conn.createStatement();
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
    public ArrayList<String> getmaSP(phieuNhapKho phieuNhapKho){
        ArrayList<String> l = new ArrayList<>();
        try {
            String select = "select product_id from detail_input \n" +
                     "WHERE detail_input.form_id = " + phieuNhapKho.getMaPhieu();
            Statement stm = conn.createStatement();
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
    public void setData(phieuNhapKho phieuNhapKho){
        txtmaPhieu.setText("Mã phiếu nhập: " + phieuNhapKho.getMaPhieu().toString().trim());
        txtnhanVien.setText("Người phụ trách: " + phieuNhapKho.getTenNV().toString().trim());
        txttenNCC.setText("Nhà cung cấp: " + phieuNhapKho.getTenNCC().toString().trim());
        txttoTal.setText("Thành tiền: " + String.valueOf(phieuNhapKho.getTotalMoney()));
        txtngayNhap.setText("Ngày nhập kho: " + phieuNhapKho.getNgayNhapKho().toString().trim());
    }

    private List<CTPNK> getListCTPNK(phieuNhapKho phieuNhapKho) {
        List<CTPNK> l = new ArrayList<>();
        try {
            Statement stm = conn.createStatement();
            String getDTInput = "select form_id, batch_id, quantity, total from detail_input \n" +
                    "WHERE detail_input.form_id = " + phieuNhapKho.getMaPhieu();
            ResultSet rs = stm.executeQuery(getDTInput);
            while (rs.next()) {
                CTPNK ctpnk = new CTPNK(phieuNhapKho.getMaPhieu(), rs.getDouble("total"), rs.getInt("quantity"), String.valueOf(rs.getInt("batch_id")));
                l.add(ctpnk);
             }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return l;
    }
}