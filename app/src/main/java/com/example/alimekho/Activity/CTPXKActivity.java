package com.example.alimekho.Activity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import com.example.alimekho.Model.phieuXuatKho;
import com.example.alimekho.Model.sanPham;
import com.example.alimekho.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CTPXKActivity extends AppCompatActivity {
    private String maPhieu;
    private List<String> listSP = new ArrayList<>();
    private SQLServerConnection db = new SQLServerConnection();
    private Connection conn = db.getConnection();
    private RecyclerView rcv;
    private List<CTPXK> ListCTPXK;
    private CTPXKAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ctpxk);
        maPhieu = getIntent().getExtras().getString("maPhieuXuat");
        phieuXuatKho PXK = null;


        //back btn
        Button backBtn = findViewById(R.id.btn_back);
        backBtn.setOnClickListener(view -> onBackPressed());

        // panel
        try {
            Statement stm = conn.createStatement();
            String Query = "select output_form.id, output_form.output_day, store.name as s_name, employee.name, output_form.total\n" +
                    "from output_form, store, employee\n" +
                    "where output_form.emp_id = employee.id and output_form.store_id = store.id and output_form.id = " + maPhieu;
            ResultSet rs = stm.executeQuery(Query);
            if (rs.next()) {
                PXK = new phieuXuatKho(
                        rs.getString("id"),
                        new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("output_day")),
                        rs.getString("s_name"),
                        rs.getString("name"),
                        rs.getDouble(5));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (PXK != null) {
            TextView tvMaPhieu = findViewById(R.id.gdcreatePNK3_txtmaPhieu);
            tvMaPhieu.setText("Mã phiếu nhập: " + PXK.getMaPhieu());
            TextView tvNgayNK = findViewById(R.id.gdcreatePNK3_txtngayNhap);
            tvNgayNK.setText("Ngày xuất kho: " + PXK.getNgayXuatKho());
            TextView tvCHN = findViewById(R.id.gdcreatePNK3_txtCHX);
            tvCHN.setText("Cửa hàng xuất: " + PXK.getTenCuaHangXuat());
            TextView tvNPT = findViewById(R.id.gdcreatePNK3_txtnguoiPT);
            tvNPT.setText("Người phụ trách: " + PXK.getTenNV());
            TextView tvTT = findViewById(R.id.gdcreatePNK3_txtthanhTien);
            tvTT.setText("Thành tiền: " + String.valueOf(PXK.getTotalMoney()));
        }

        //list SP
        rcv = findViewById(R.id.rcv);
        ListCTPXK = getListCTPXK();
        adapter = new CTPXKAdapter(this, ListCTPXK);
        rcv.setAdapter(adapter);
        rcv.setLayoutManager(new LinearLayoutManager(this));

        //del btn
        Button delbtn = findViewById(R.id.btn_xoa);
        delbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.deleteCheckedItems();
                recreate();
            }
        });

        //edit btn
        Button editbtn = findViewById(R.id.btn_sua);
        editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(CTPXKActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.edit_ctpnk);
                Window window = dialog.getWindow();
                if (window == null) return;
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                Spinner spnMaSP = dialog.findViewById(R.id.maSP);
                spnMaSP.setAdapter(new ArrayAdapter<>(CTPXKActivity.this, R.layout.style_spinner_form, listSP));
                EditText sl = dialog.findViewById(R.id.sl);

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
                        SQLServerConnection db = new SQLServerConnection();
                        Connection conn = db.getConnection();
                        try {
                            Statement stm = conn.createStatement();
                            String Query = "update [detail_output]" +
                                    "\nset quantity = " + sl.getText()
                                    +"\nwhere form_id = " + maPhieu
                                    +"and product_id = " + spnMaSP.getSelectedItem();
                            stm.executeUpdate(Query);
                            stm.close();
                            conn.close();
                            Toast.makeText(CTPXKActivity.this, "Thanh cong", Toast.LENGTH_SHORT).show();
                        } catch (SQLException e) {
                            e.printStackTrace();
                            Toast.makeText(CTPXKActivity.this, "That bai", Toast.LENGTH_SHORT).show();
                        }
                        recreate();
                    }
                });
                dialog.show();
            }
        });
    }

    private List<CTPXK> getListCTPXK() {
        List<CTPXK> list = new ArrayList<>();
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Statement stm = conn.createStatement();
            String Query = "select detail_output.product_id, product.name, product.unit_price, detail_output.quantity, detail_output.NSX, detail_output.HSD\n" +
                    "from detail_output, product\n" +
                    "where detail_output.product_id = product.id and form_id = " + maPhieu;
            ResultSet rs = stm.executeQuery(Query);
            while (rs.next()) {
                list.add(new CTPXK(maPhieu,
                        new sanPham(rs.getString("product_id"),
                                rs.getString("name"),
                                rs.getDouble("unit_price")
                                ),
                        rs.getInt("quantity"),
                        dateFormat.format(rs.getDate("NSX")),
                        dateFormat.format(rs.getDate("HSD"))));
                listSP.add(rs.getString("product_id"));
            }
            rs.close();
            stm.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}