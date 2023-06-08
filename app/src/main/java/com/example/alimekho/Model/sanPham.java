package com.example.alimekho.Model;

import java.io.Serializable;

public class sanPham implements Serializable {
    private String maSP;
    private String tenSP;
    private int isDeleted;
    private String phanLoai;
    private String donViTinh;
    private String supplier_id;
    private String img;

    public void setImg(String img) {
        this.img = img;
    }

    public String getImg() {
        return img;
    }

    public sanPham() {
    }

    public sanPham(String maSP, String tenSP) {
        this.maSP = maSP;
        this.tenSP = tenSP;
    }

    public sanPham(String maSP, String tenSP, double donGia, String phanLoai, String donViTinh, String supplier_id) {
        this.maSP = maSP;
        this.tenSP = tenSP;
        this.phanLoai = phanLoai;
        this.donViTinh = donViTinh;
        this.supplier_id = supplier_id;
        this.isDeleted = 0;
    }
    public String getMaSP() {
        return maSP;
    }

    public void setMaSP(String maSP) {
        this.maSP = maSP;
    }

    public String getTenSP() {
        return tenSP;
    }

    public void setTenSP(String tenSP) {
        this.tenSP = tenSP;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getPhanLoai() {
        return phanLoai;
    }

    public void setPhanLoai(String phanLoai) {
        this.phanLoai = phanLoai;
    }

    public String getDonViTinh() {
        return donViTinh;
    }

    public void setDonViTinh(String donViTinh) {
        this.donViTinh = donViTinh;
    }

    public String getSupplier_id() {
        return supplier_id;
    }

    public void setSupplier_id(String supplier_id) {
        this.supplier_id = supplier_id;
    }
}