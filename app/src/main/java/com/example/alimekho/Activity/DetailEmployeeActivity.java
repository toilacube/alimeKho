package com.example.alimekho.Activity;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.alimekho.Model.Employee;
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

public class DetailEmployeeActivity extends AppCompatActivity {

    TextView id, name, title, birth, identify, phone;
    Button btnBackDetail, btnUpdate, btnDelete, btnAddImage;
    ImageView imvSP;
    Uri uri;
    Employee employee;
    FirebaseFirestore fb;
    FirebaseStorage storage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_employee);
        Bundle bundle = getIntent().getExtras();
        if (bundle==null)
            return;
        storage = FirebaseStorage.getInstance();
        employee= (Employee) bundle.get("object_employee");
        id=findViewById(R.id.tv_id);
        name=findViewById(R.id.tv_name);
        title=findViewById(R.id.tv_title);
        birth=findViewById(R.id.tv_birth);
        identify=findViewById(R.id.tv_identify);
        phone=findViewById(R.id.tv_phone);
        btnBackDetail=findViewById(R.id.btn_back_employee);
        btnUpdate=findViewById(R.id.btn_update);
        btnDelete=findViewById(R.id.btn_delete);
        btnAddImage = findViewById(R.id.btnAddImage);
        imvSP = findViewById(R.id.imvSP);

        id.setText(employee.getId());
        name.setText(employee.getName());
        title.setText(employee.getTitle());
        birth.setText(employee.getDayOfBirth());
        identify.setText(employee.getIdentify());
        phone.setText(employee.getPhoneNumber());

        fb = FirebaseFirestore.getInstance();

        fb.collection("employee").document(employee.getId()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            sanPham sp1 = new sanPham();
                            sp1 = task.getResult().toObject(sanPham.class);
                            if(sp1 != null)
                                if(sp1.getImg() != null) employee.setImg(sp1.getImg());
                            if(employee.getImg() != null){
                                Glide.with(DetailEmployeeActivity.this)
                                        .load(employee.getImg())
                                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                                        .into(imvSP);
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "nhan vien chua co hinh anh");
                    }
                });

        if (getSharedPreferences("user info", MODE_PRIVATE).getInt("role", 0) != 0) {
            btnAddImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ImagePicker.with(DetailEmployeeActivity.this)
//                        .crop()	    			//Crop image(Optional), Check Customization for more option
//                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
//                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                            .start();
                }
            });
        } else {
            btnAddImage.setOnClickListener(view -> Toast.makeText(this, "Bạn không có quyền thực hiện thao tác này", Toast.LENGTH_SHORT).show());
        }

        if (getSharedPreferences("user info", Context.MODE_PRIVATE).getInt("role", -1) == 2){
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder=new AlertDialog.Builder(view.getRootView().getContext());
                    View dialogView= LayoutInflater.from(view.getRootView().getContext()).inflate(R.layout.confirm_delete,null);
                    builder.setView(dialogView);
                    builder.show();
                }
            });
        } else {
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(DetailEmployeeActivity.this, "Bạn không có quyền thực hiện thao tác này", Toast.LENGTH_SHORT).show();
                }
            });
        }

        btnBackDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EmployeeActivity.class);
                startActivity(intent);
                finish();
            }
        });


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
            StorageReference imgRef = storage.getReference().child("image_emp/" + uri.getLastPathSegment());
            imgRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            if(uri != null) {
                                employee.setImg(uri.toString());
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
        fb.collection("employee").document(employee.getId()).set(employee, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "up image nhan vien ");
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