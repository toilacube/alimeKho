package com.example.alimekho.Model;

public class CTPXK {
    private String maPXK;
    private sanPham sanPham;
    private double thanhTien;
    private int soLuong;

    public CTPXK(String maPXK, com.example.alimekho.Model.sanPham sanPham, int soLuong) {
        this.maPXK = maPXK;
        this.sanPham = sanPham;
        this.soLuong = soLuong;
        this.thanhTien = sanPham.getDonGia()*soLuong;
    }

    public CTPXK(com.example.alimekho.Model.sanPham sanPham, int soLuong) {
        this.sanPham = sanPham;
        this.soLuong = soLuong;
        this.thanhTien = sanPham.getDonGia()*soLuong;
    }

    public String getMaPXK() {
        return maPXK;
    }

    public void setThanhTien(double thanhTien) {
        this.thanhTien = thanhTien;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public String getMaPNX() {
        return maPXK;
    }

    public void setMaPNX(String maPXK) {
        this.maPXK = maPXK;
    }

    public com.example.alimekho.Model.sanPham getSanPham() {
        return sanPham;
    }

    public void setSanPham(com.example.alimekho.Model.sanPham sanPham) {
        this.sanPham = sanPham;
    }

    public double getThanhTien() {
        return thanhTien;
    }

    public void setThanhTien(int thanhTien) {
        this.thanhTien = thanhTien;
    }
}
