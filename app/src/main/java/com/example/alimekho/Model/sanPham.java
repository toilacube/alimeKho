package com.example.alimekho.Model;

import java.io.Serializable;

public class sanPham implements Serializable {
    private String maSP;
    private String tenSP;
    private double donGia;
    private String NSX;
    private String HSD;
    private int soLuong;
    private String phanLoai;
    private String donViTinh;


    public void setPhanLoai(String phanLoai) {
        this.phanLoai = phanLoai;
    }

    public void setDonViTinh(String donViTinh) {
        this.donViTinh = donViTinh;
    }

    public sanPham() {
    }

    public sanPham(String maSP, String tenSP, double donGia, int soLuong, String NSX, String HSD) {
        this.maSP = maSP;
        this.tenSP = tenSP;
        this.donGia = donGia;
        this.NSX = NSX;
        this.HSD = HSD;
        this.soLuong = soLuong;
    }

    public sanPham(String maSP, String tenSP, double donGia, String NSX, String HSD, int soLuong, String phanLoai, String donViTInh) {
        this.maSP = maSP;
        this.tenSP = tenSP;
        this.donGia = donGia;
        this.NSX = NSX;
        this.HSD = HSD;
        this.soLuong = soLuong;
        this.phanLoai = phanLoai;
        this.donViTinh = donViTInh;
    }
    public sanPham(String maSP, String tenSP, double donGia, String NSX, String HSD) {
        this.maSP = maSP;
        this.tenSP = tenSP;
        this.donGia = donGia;
        this.NSX = NSX;
        this.HSD = HSD;
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

    public String getNSX() {
        return NSX;
    }

    public void setNSX(String NSX) {
        this.NSX = NSX;
    }

    public String getHSD() {
        return HSD;
    }

    public void setHSD(String HSD) {
        this.HSD = HSD;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public String getDonViTinh() {
        return donViTinh;
    }

    public String getPhanLoai() {
        return phanLoai;
    }
}
