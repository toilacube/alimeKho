package com.example.alimekho.Activity;

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

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alimekho.Adapter.CTPXKAdapter;
import com.example.alimekho.DataBase.SQLServerConnection;
import com.example.alimekho.Model.CTPXK;
import com.example.alimekho.Model.ExcelExporter;
import com.example.alimekho.Model.loSanPham;
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

public class CTPXKActivity extends AppCompatActivity {
    private String maPhieu;
    phieuXuatKho PXK;
    private double total = 0;
    TextView tvTT;
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
        HashMap<String, String> pxkMap = (HashMap<String, String>) getIntent().getSerializableExtra("PXK");
        maPhieu = pxkMap.get("maPhieu");
        PXK = new phieuXuatKho(
                maPhieu,
                pxkMap.get("ngXK"),
                pxkMap.get("CHX"),
                pxkMap.get("NV"),
                Double.parseDouble(pxkMap.get("thanhTien")));

        //check all
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
        TextView tvMaPhieu = findViewById(R.id.gdcreatePNK3_txtmaPhieu);
        tvMaPhieu.setText("Mã phiếu nhập: " + PXK.getMaPhieu());
        TextView tvNgayNK = findViewById(R.id.gdcreatePNK3_txtngayNhap);
        tvNgayNK.setText("Ngày xuất kho: " + PXK.getNgayXuatKho());
        TextView tvCHN = findViewById(R.id.gdcreatePNK3_txtCHX);
        tvCHN.setText("Cửa hàng xuất: " + PXK.getTenCuaHangXuat());
        TextView tvNPT = findViewById(R.id.gdcreatePNK3_txtnguoiPT);
        tvNPT.setText("Người phụ trách: " + PXK.getTenNV());
        tvTT = findViewById(R.id.gdcreatePNK3_txtthanhTien);


        //list SP
        rcv = findViewById(R.id.rcv);
        ListCTPXK = getListCTPXK();
        adapter = new CTPXKAdapter(this, ListCTPXK);
        rcv.setAdapter(adapter);
        rcv.setLayoutManager(new LinearLayoutManager(this));

        //export btn
        Button exportBtn = findViewById(R.id.btn_xuat);
        exportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                export();
            }
        });

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

                Spinner spnMaSP = dialog.findViewById(R.id.spinnersp);
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
                            String spnText = spnMaSP.getSelectedItem().toString();
                            String maLo = spnText.split(": ", 2)[1].split(",")[0];
                            Statement stm = conn.createStatement();
                            String Query = "update [detail_output]" +
                                    "\nset quantity = " + sl.getText()
                                    +"\nwhere form_id = " + maPhieu
                                    +"and batch_id = " + maLo;
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
    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(CTPXKActivity.this, permission)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    CTPXKActivity.this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(CTPXKActivity.this,
                        new String[]{permission}, requestCode);

            } else {
                ActivityCompat.requestPermissions(CTPXKActivity.this,
                        new String[]{permission}, requestCode);
            }
        }
    }

    private void export() {
        askForPermission(Manifest.permission.READ_EXTERNAL_STORAGE, 100);
        askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, 200);
        askForPermission(Manifest.permission.MANAGE_EXTERNAL_STORAGE, 300);
        ArrayList<Object[]> data =  new ArrayList<>();
        for (CTPXK ct : ListCTPXK) {
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
                data.add(new Object[]{
                        ct.getLo().getMaLo(),
                        ct.getLo().getSanPham().getMaSP(),
                        ct.getLo().getSanPham().getTenSP(),
                        ct.getLo().getNSX(),
                        ct.getLo().getHSD(),
                        ct.getLo().getDonGia(),
                        ct.getSoLuong(),
                        ct.getThanhTien(),
                        location
                });
                ExcelExporter excelExporter = new ExcelExporter(this);
                excelExporter.setFileName("phieuxuatkho" + PXK.getMaPhieu() + ".xlsx");
                excelExporter.setHeader(Arrays.asList("Mã lô", "Mã sản phẩm", "Tên sản phẩm", "Ngày sản xuất", "Hạn sử dụng", "Đơn giá", "Số lượng", "Thành tiền", "Vị trí trong kho"));
                excelExporter.exportToExcel(data);
                stm.close();
                conn.close();
            } catch (SQLException e) {
                Toast.makeText(this, "Loi: " + e.toString(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    private List<CTPXK> getListCTPXK() {
        List<CTPXK> list = new ArrayList<>();
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Statement stm = conn.createStatement();
            String Query = "select d.batch_id, b.product_id, p.name, d.quantity, b.unit_price, b.NSX, b.HSD\n" +
                    "from detail_output d, batch b, product p\n" +
                    "where d.batch_id = b.id and p.id = b.product_id and d.form_id = " + maPhieu;
            ResultSet rs = stm.executeQuery(Query);
            while (rs.next()) {
                list.add(new CTPXK(
                            new loSanPham(
                                    rs.getString("batch_id"),
                                    dateFormat.format(rs.getDate("NSX")),
                                    dateFormat.format(rs.getDate("HSD")),
                                    new sanPham(
                                            rs.getString("product_id"),
                                            rs.getString("name")),
                                    rs.getDouble("unit_price")),
                            maPhieu,
                            rs.getInt("quantity")
                        ));
                total += (rs.getInt("quantity")*rs.getDouble("unit_price"));
                listSP.add("Mã lô: " + rs.getString("batch_id") + ", SP: " + rs.getString("name"));
            }
            String numString = String.valueOf((int)total);
            String thanhTien ="";
            for(int i = 0; i < numString.length() ; i++){
                if((numString.length() - i - 1) % 3 == 0 && i < numString.length()-1){
                    thanhTien += Character.toString(numString.charAt(i)) + ".";
                }else{
                    thanhTien += Character.toString(numString.charAt(i));
                }
            }
            tvTT.setText("Thành tiền: " + thanhTien + " đ");
            rs.close();
            stm.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

}