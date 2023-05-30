package com.example.alimekho.Model;

public class CTPXK {
    private loSanPham lo;
    private String maPXK;
    private double thanhTien;
    private int soLuong;

    public CTPXK(loSanPham lo, String maPXK, double thanhTien, int soLuong) {
        this.lo = lo;
        this.maPXK = maPXK;
        this.thanhTien = thanhTien;
        this.soLuong = soLuong;
    }
    public CTPXK(loSanPham lo, String maPXK, int soLuong) {
        this.lo = lo;
        this.maPXK = maPXK;
        this.thanhTien = soLuong * lo.getDonGia();
        this.soLuong = soLuong;
    }
    public CTPXK(loSanPham lo, int soLuong) {
        this.lo = lo;
        this.thanhTien = soLuong * lo.getDonGia();
        this.soLuong = soLuong;
    }

    public void setLo(loSanPham lo) {
        this.lo = lo;
    }

    public void setMaPXK(String maPXK) {
        this.maPXK = maPXK;
    }

    public void setThanhTien(double thanhTien) {
        this.thanhTien = thanhTien;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
        this.thanhTien = soLuong * this.lo.getDonGia();
    }

    public loSanPham getLo() {
        return lo;
    }

    public String getMaPXK() {
        return maPXK;
    }

    public double getThanhTien() {
        return thanhTien;
    }

    public int getSoLuong() {
        return soLuong;
    }
}
