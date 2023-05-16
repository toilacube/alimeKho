package com.example.alimekho.Model;

import java.io.Serializable;

public class sanPham implements Serializable {
    private String maSP;
    private String tenSP;
    private double donGia;
    private String phanLoai;
    private String donViTinh;
    private String supplier_id;

    public sanPham(String maSP, String tenSP, double donGia) {
        this.maSP = maSP;
        this.tenSP = tenSP;
        this.donGia = donGia;
    }

    public sanPham(String maSP, String tenSP, double donGia, String phanLoai, String donViTinh, String supplier_id) {
        this.maSP = maSP;
        this.tenSP = tenSP;
        this.donGia = donGia;//
        this.phanLoai = phanLoai;
        this.donViTinh = donViTinh;
        this.supplier_id = supplier_id;
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

    public double getDonGia() {
        return donGia;
    }

    public void setDonGia(double donGia) {
        this.donGia = donGia;
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