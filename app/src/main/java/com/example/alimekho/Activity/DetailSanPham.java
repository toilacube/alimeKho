package com.example.alimekho.Activity;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.alimekho.DataBase.SQLServerConnection;
import com.example.alimekho.Model.sanPham;
import com.example.alimekho.R;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;


public class DetailSanPham extends AppCompatActivity {
    ImageView imvSP;
    TextView id, name , donGia, phanLoai, donViTinh, nhaCC;
    Button btnBack, btnDelete, btnAddImage;
    SQLServerConnection db = new SQLServerConnection();
    Uri uri;
    sanPham sp;
    FirebaseFirestore fb;
    FirebaseStorage storage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_san_pham);

        sp = (sanPham) getIntent().getSerializableExtra("SP");
        fb = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        id = findViewById(R.id.txvMaSP);
        name = findViewById(R.id. txvTenSP);
        phanLoai = findViewById(R.id.txvLoaiSP);
        donViTinh = findViewById(R.id.txvDonViTinh);
        nhaCC = findViewById(R.id.txvNhaCC);
        btnBack = findViewById(R.id.btn_back_san_pham);
        btnAddImage = findViewById(R.id.btnAddImage);
        imvSP = findViewById(R.id.imvSP);

        id.setText(sp.getMaSP());
        name.setText(sp.getTenSP());
        donViTinh.setText(sp.getDonViTinh());
       // donGia.setText(Double.toString(sp.getDonGia()));


        fb.collection("product").document(sp.getMaSP()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            sanPham sp1 = new sanPham();
                            sp1 = task.getResult().toObject(sanPham.class);
                            if(sp1 != null)
                                if(sp1.getImg() != null) sp.setImg(sp1.getImg());
                            if(sp.getImg() != null){
                                Glide.with(DetailSanPham.this)
                                        .load(sp.getImg())
                                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                                        .into(imvSP);
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "san pham chua co hinh anh");
                    }
                });




        Statement stm1 = null;
        try {
            stm1 = db.getConnection().createStatement();
            String type = sp.getPhanLoai();
            String getTypeQuery = "SELECT name FROM product_type where id = " + type;
            ResultSet rsType = stm1.executeQuery(getTypeQuery);

            if(rsType.next())
                phanLoai.setText(rsType.getString(1));

            String supplier = sp.getSupplier_id();
            String getSupplierQuery = "select name from supplier where id = " + supplier;
            ResultSet rsSupplier = stm1.executeQuery(getSupplierQuery);
            if(rsSupplier.next())
                nhaCC.setText(rsSupplier.getString(1));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (getSharedPreferences("user info", MODE_PRIVATE).getInt("role", 0) != 0) {
            btnAddImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ImagePicker.with(DetailSanPham.this)
//                        .crop()	    			//Crop image(Optional), Check Customization for more option
//                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
//                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                            .start();
                }
            });
        } else {
            btnAddImage.setOnClickListener(view -> Toast.makeText(this, "Bạn không có quyền thực hiện thao tác này", Toast.LENGTH_SHORT).show());
        }


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SanPhamActivity.class);
                startActivity(intent);
            }
        });

//        btnDelete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                AlertDialog.Builder builder=new AlertDialog.Builder(view.getRootView().getContext());
//                View dialogView= LayoutInflater.from(view.getRootView().getContext()).inflate(R.layout.confirm_delete,null);
//                builder.setView(dialogView);
//                builder.show();
//            }
//        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uri = data.getData();
        if(uri != null){
            imvSP.setImageURI(uri);

            uploadToFirebase();
            //uploadSanPham();
        }
    }

    private void uploadToFirebase(){
        if(uri != null){
            StorageReference imgRef = storage.getReference().child("image/" + uri.getLastPathSegment());
            imgRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            if(uri != null) {
                                sp.setImg(uri.toString());
                                Log.e(TAG, uri.toString());
                                uploadSanPham();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "ko up duoc san pham len storage");
                        }
                    });
                }
            });
        }
    }

    private void uploadSanPham(){
        fb.collection("product").document(sp.getMaSP()).set(sp, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "up image san pham ");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "ko up duoc san pham");
                    }
                });
    }
}