package com.example.alimekho.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.IntentCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alimekho.Adapter.PXK3Adapter;
import com.example.alimekho.DataBase.SQLServerConnection;
import com.example.alimekho.Model.CTPXK;
import com.example.alimekho.Model.loSanPham;
import com.example.alimekho.Model.phieuXuatKho;
import com.example.alimekho.Model.sanPham;
import com.example.alimekho.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CreatePXK3Activity extends AppCompatActivity {
    SQLServerConnection db = new SQLServerConnection();
    Connection conn = db.getConnection();
    private double total = 0;
    private Button btnBackHome, btnComplete, btnBack;
    private TextView txtmaPhieu, txttenCHX, txtngayNhap, txtnhanVien, txttoTal;
    private RecyclerView recyclerView;
    private ArrayList<CTPXK> ctpxks;
    private com.example.alimekho.Model.phieuXuatKho phieuXuatKho;
    private PXK3Adapter pxk3Adapter;
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    String today = dateFormat.format(Calendar.getInstance().getTime());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_pxk3);
        Init();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        pxk3Adapter = new PXK3Adapter(this, ctpxks);
        recyclerView.setAdapter(pxk3Adapter);
        setData();
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    SharedPreferences sharedPref = getSharedPreferences("user info", MODE_PRIVATE);
                    Statement stm = conn.createStatement();
                    String Query = "set dateformat dmy\n"+
                            "INSERT INTO [output_form] ([output_day], [emp_id], [supermarket_id], [is_deleted]) VALUES ( "+
                                    "'" + today +"'" +
                                    " , " + sharedPref.getString("id", "") +
                                    " , "+ getIntent().getExtras().getString("maCHX") +
                                    " , 0)";
//                    Query = "exec pro_them_pxk @empId = " + sharedPref.getString("id", "") +
//                            ", @supermarketId = " + getIntent().getExtras().getString("maCHX");
                    stm.executeUpdate(Query);

                    for (CTPXK ct : ctpxks){
                        String insertQuery = "DECLARE @formID int;\n" +
                                "SELECT @formID = IDENT_CURRENT('[output_form]');\n" +
                                "INSERT INTO [detail_output] ([batch_id], [form_id], [quantity]) "+
                                "VALUES (" +
                                ct.getLo().getMaLo() + "," +
                                "@formID, " +
                                ct.getSoLuong() +
                                ")";
                        stm.executeUpdate(insertQuery);
                    }
                    stm.close();
                    conn.close();
                    Intent intent = new Intent(CreatePXK3Activity.this, QLPXKActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } catch (SQLException e) {
                    e.printStackTrace();
                    Toast.makeText(CreatePXK3Activity.this, "Số lượng không phù hợp", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnBackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    public void setData(){
        String maPhieu = "N/A";
        try {
            Statement stm = conn.createStatement();
            String Query = "SELECT IDENT_CURRENT('output_form')";
            ResultSet rs = stm.executeQuery(Query);
            if (rs.next()) {
                maPhieu = String.valueOf(Integer.parseInt(rs.getString(1)) + 1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        txtmaPhieu.setText("Mã phiếu: " + maPhieu);
        SharedPreferences sharedPref = getSharedPreferences("user info", MODE_PRIVATE);
        txtnhanVien.setText("Người phụ trách: " + sharedPref.getString("name", "N/A"));
        txttenCHX.setText("Cửa hàng xuất: " + getIntent().getExtras().getString("tenCHX"));
        txtngayNhap.setText("Ngày xuất kho: " + today);

        String numString = String.valueOf((int)total);
        String thanhTien ="";
        for(int i = 0; i < numString.length() ; i++){
            if((numString.length() - i - 1) % 3 == 0 && i < numString.length()-1){
                thanhTien += Character.toString(numString.charAt(i)) + ".";
            }else{
                thanhTien += Character.toString(numString.charAt(i));
            }
        }
        txttoTal.setText("Thành tiền: " + thanhTien + " đ");

    }
    public void Init(){
        btnBackHome = findViewById(R.id.btn_back_createPXK3);
        btnComplete = findViewById(R.id.gdcreatePXK3_btnComplete);
        btnBack = findViewById(R.id.gdcreatePXK3_btnBack);
        recyclerView = findViewById(R.id.gdcreatePXK3_rcv);
        txtmaPhieu = findViewById(R.id.gdcreatePXK3_txtmaPhieu);
        txtngayNhap = findViewById(R.id.gdcreatePXK3_txtngayNhap);
        txttenCHX= findViewById(R.id.gdcreatePXK3_txtCHX);
        txtnhanVien = findViewById(R.id.gdcreatePXK3_txtnguoiPT);
        txttoTal = findViewById(R.id.gdcreatePXK3_txtthanhTien);
        ctpxks = new ArrayList<>();
        List<CTPXK> listSP = CreatePXK1Activity.spSelected();
        for (CTPXK i : listSP){
            ctpxks.add(i);
            total += i.getThanhTien();
        }
        phieuXuatKho = new phieuXuatKho("NH122", "23/09/2022", "PM Milk. Co", "Nguyễn Văn A", 12000000.0);
    }
}