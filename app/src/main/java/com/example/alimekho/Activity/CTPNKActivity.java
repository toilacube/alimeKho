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
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alimekho.Adapter.CTPNKAdapter;
import com.example.alimekho.Adapter.CTPXKAdapter;
import com.example.alimekho.DataBase.SQLServerConnection;
import com.example.alimekho.Model.CTPNK;
import com.example.alimekho.Model.CTPXK;
import com.example.alimekho.Model.phieuNhapKho;
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
    private Connection conn = db.getConnection();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ctpnk);
        phieuNhapKho phieuNhapKho = (phieuNhapKho) getIntent().getSerializableExtra("pnk");
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
        txtmaPhieu.setText("Mã phiếu: " + phieuNhapKho.getMaPhieu().toString().trim());
        txtnhanVien.setText("Người phụ trách: " + phieuNhapKho.getTenNV().toString().trim());
        txttenNCC.setText("Nhà cung cấp: " + phieuNhapKho.getTenNCC().toString().trim());
        txttoTal.setText("Thành tiền: " + String.valueOf(phieuNhapKho.getTotalMoney()));
        txtngayNhap.setText("Ngày nhập kho: " + phieuNhapKho.getNgayNhapKho().toString().trim());
    }

    private List<CTPNK> getListCTPNK(phieuNhapKho phieuNhapKho) {
        List<CTPNK> l = new ArrayList<>();
        try {
            Statement stm = conn.createStatement();
            String getDTInput = "select product_id, name, detail_input.quantity, unit_price, unit, type_id, supplier_id from detail_input \n" +
                    "JOIN product ON detail_input.product_id = product.id\n" +
                    "WHERE detail_input.form_id = " + phieuNhapKho.getMaPhieu();
            ResultSet rs = stm.executeQuery(getDTInput);
            while (rs.next()) {
                String pattern = "MM/dd/yyyy";
                DateFormat df = new SimpleDateFormat(pattern);
                String NSX = df.format(rs.getDate("NSX"));
                String HSD = df.format(rs.getDate("HSD"));
                sanPham sanPham = new sanPham(String.valueOf(rs.getInt(1)), rs.getString(2),
                        Double.parseDouble(rs.getString(4)), rs.getString("type_id"), rs.getString("type_id"),
                        String.valueOf(rs.getInt("supplier_id")));
                CTPNK ctpnk = new CTPNK(phieuNhapKho.getMaPhieu(), sanPham, rs.getInt("quantity"), NSX, HSD);
                l.add(ctpnk);
             }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return l;
    }
}