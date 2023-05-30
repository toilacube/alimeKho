package com.example.alimekho.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alimekho.Adapter.CTPKKAdapter;
import com.example.alimekho.Adapter.CTPXKAdapter;
import com.example.alimekho.DataBase.SQLServerConnection;
import com.example.alimekho.Model.CTPKK;
import com.example.alimekho.Model.CTPXK;
import com.example.alimekho.Model.ExcelExporter;
import com.example.alimekho.Model.loSanPham;
import com.example.alimekho.Model.phieuKiemKho;
import com.example.alimekho.Model.phieuXuatKho;
import com.example.alimekho.Model.sanPham;
import com.example.alimekho.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class CTPKKActivity extends AppCompatActivity {
    private phieuKiemKho PKK;
    private String maPhieu;
    private CTPKKAdapter adapter;
    private RecyclerView rcv;
    private List<CTPKK> listCTPKK;
    private List<String> listLo = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ctpkk);
        HashMap<String, String> pxkMap = (HashMap<String, String>) getIntent().getSerializableExtra("PXK");
        maPhieu = pxkMap.get("ma");
        PKK = new phieuKiemKho(
                maPhieu,
                pxkMap.get("ngay"),
                pxkMap.get("nv"),
                getIntent().getIntExtra("tinhtrangInt", 0));
        //select all
        CheckBox cb = findViewById(R.id.checkall);
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()) adapter.selectAll();
                else adapter.unSelectAll();
            }
        });

        //back btn
        Button backBtn = findViewById(R.id.btn_back);
        backBtn.setOnClickListener(view -> onBackPressed());

        // panel
        TextView tvMaPhieu = findViewById(R.id.ma);
        tvMaPhieu.setText("Mã phiếu kiểm kho: " + PKK.getMaPhieu());
        TextView tvNgayKK = findViewById(R.id.ngay);
        tvNgayKK.setText("Ngày xuất kho: " + PKK.getNgayKK());
        TextView tvNPT = findViewById(R.id.tenNV);
        tvNPT.setText("Người phụ trách: " + PKK.getTenNV());
        TextView tvTingTrang = findViewById(R.id.tinhtrang);
        tvTingTrang.setText("Tình trạng: " + pxkMap.get("tinhtrang"));

        //list
        rcv = findViewById(R.id.rcv);
        listCTPKK = new ArrayList<>();
        adapter = new CTPKKAdapter(this, listCTPKK);
        rcv.setAdapter(adapter);
        rcv.setLayoutManager(new LinearLayoutManager(this));
        getListCTPKK();

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
                Dialog dialog = new Dialog(CTPKKActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.edit_ctpkk);
                Window window = dialog.getWindow();
                if (window == null) return;
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                Spinner spnMaSP = dialog.findViewById(R.id.spinnersp);
                spnMaSP.setAdapter(new ArrayAdapter<>(CTPKKActivity.this, R.layout.style_spinner_form, listLo));
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
                            String spnText = spnMaSP.getSelectedItem().toString();
                            String maLo = spnText.split(": ", 2)[1].split(",")[0];
                            Statement stm = conn.createStatement();
                            String Query = "update [detail_check]" +
                                    "\nset actual_quantity = " + sl.getText()
                                    +"\nwhere form_id = " + maPhieu
                                    +"and batch_id = " + maLo;
                            stm.executeUpdate(Query);
                            stm.close();
                            conn.close();
                            Toast.makeText(CTPKKActivity.this, "Thanh cong", Toast.LENGTH_SHORT).show();
                        } catch (SQLException e) {
                            e.printStackTrace();
                            Toast.makeText(CTPKKActivity.this, "That bai", Toast.LENGTH_SHORT).show();
                        }
                        recreate();
                    }
                });
                dialog.show();
            }
        });

        //export btn
        Button exportBtn = findViewById(R.id.btn_xuat);
        exportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                export();
            }
        });

        //update is_solve
        Button xlBtn = findViewById(R.id.btn_solve);
        xlBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (PKK.getTinhTrang() == 1) {
                    xlBtn.setText("Đánh dấu chưa xử lý");
                    try {
                        SQLServerConnection db = new SQLServerConnection();
                        Connection conn = db.getConnection();
                        Statement stm = conn.createStatement();
                        String Query = "update [check_form] set [is_solved] = 0 where [id] = " + maPhieu;
                        stm.executeUpdate(Query);
                        Toast.makeText(CTPKKActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                        tvTingTrang.setText("Tình trạng: " + "Chưa xử lý");
                        xlBtn.setText("Đánh dấu đã xử lý");
                        PKK.setTinhTrang(0);
                        stm.close();
                        conn.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                        Toast.makeText(CTPKKActivity.this, "Lỗi: " + e.toString(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    xlBtn.setText("Đánh dấu đã xử lý");
                    try {
                        SQLServerConnection db = new SQLServerConnection();
                        Connection conn = db.getConnection();
                        Statement stm = conn.createStatement();
                        String Query = "update [check_form] set [is_solved] = 1 where [id] = " + maPhieu;
                        stm.executeUpdate(Query);
                        Toast.makeText(CTPKKActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                        tvTingTrang.setText("Tình trạng: " + "Đã xử lý");
                        xlBtn.setText("Đánh dấu chưa xử lý");
                        PKK.setTinhTrang(1);
                        stm.close();
                        conn.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                        Toast.makeText(CTPKKActivity.this, "Lỗi: " + e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    private void getListCTPKK() {
        try {
            SQLServerConnection db = new SQLServerConnection();
            Connection conn = db.getConnection();
            Statement stm = conn.createStatement();
            String Query = "select d.batch_id, b.product_id, p.name, d.db_quantity, d.actual_quantity\n" +
                    "from detail_check d, batch b, product p\n" +
                    "where d.batch_id = b.id and p.id = b.product_id and d.form_id = " + maPhieu;
            ResultSet rs = stm.executeQuery(Query);
            while (rs.next()) {
                int slTT = 0;
                slTT = rs.getInt("actual_quantity");
                if (rs.wasNull()) slTT = -1;

                listCTPKK.add(new CTPKK(
                        maPhieu,
                        new loSanPham(
                                rs.getString("batch_id"),
                                new sanPham(
                                        rs.getString("product_id"),
                                        rs.getString("name"))
                        ),
                        rs.getInt("db_quantity"),
                        slTT
                ));
                listLo.add("Mã lô: " + rs.getString("batch_id") + ", SP: " + rs.getString("name"));
            }
            adapter.notifyDataSetChanged();
            rs.close();
            stm.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(CTPKKActivity.this, permission)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    CTPKKActivity.this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(CTPKKActivity.this,
                        new String[]{permission}, requestCode);

            } else {
                ActivityCompat.requestPermissions(CTPKKActivity.this,
                        new String[]{permission}, requestCode);
            }
        }
    }

    private void export() {
        askForPermission(Manifest.permission.READ_EXTERNAL_STORAGE, 100);
        askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, 200);
        askForPermission(Manifest.permission.MANAGE_EXTERNAL_STORAGE, 300);
        ArrayList<Object[]> data =  new ArrayList<>();
        for (CTPKK ct : listCTPKK) {
            SQLServerConnection db = new SQLServerConnection();
            Connection conn = db.getConnection();
            try {
                String location = "";
                Statement stm = conn.createStatement();
                String Query =
                        "select location_id from [distribute] where batch_id = " + ct.getLo().getMaLo();
                ResultSet rs = stm.executeQuery(Query);
                while (rs.next()) {
                    if (location != "")
                        location += (", " + rs.getString(1));
                    else
                        location += rs.getString(1);
                }
                if (PKK.getTinhTrang() == 0)
                    data.add(new Object[]{
                            ct.getLo().getMaLo(),
                            ct.getLo().getSanPham().getMaSP(),
                            ct.getLo().getSanPham().getTenSP(),
                            location,
                            ct.getSlDB()
                    });
                else
                    data.add(new Object[]{
                            ct.getLo().getMaLo(),
                            ct.getLo().getSanPham().getMaSP(),
                            ct.getLo().getSanPham().getTenSP(),
                            location,
                            ct.getSlDB(),
                            ct.getSlTT()
                    });
                ExcelExporter excelExporter = new ExcelExporter(this);
                excelExporter.setFileName("phieukiemkho" + PKK.getMaPhieu() + ".xlsx");
                excelExporter.setHeader(Arrays.asList("Mã lô", "Mã sản phẩm", "Tên sản phẩm", "Vị trí trong kho", "Số lượng trong hệ thống", "Số lượng thực tế"));
                excelExporter.exportToExcel(data);
                stm.close();
                conn.close();
            } catch (SQLException e) {
                Toast.makeText(this, "Lỗi: " + e.toString(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }
}