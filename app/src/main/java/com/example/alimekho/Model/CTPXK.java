package com.example.alimekho.Model;

public class CTPXK {
    private String maPXK;
    private sanPham sanPham;
    private double thanhTien;
    private int soLuong;
    private String NSX;
    private String HSD;

    public CTPXK(String maPXK, com.example.alimekho.Model.sanPham sanPham, int soLuong, String NSX, String HSD) {
        this.maPXK = maPXK;
        this.sanPham = sanPham;
        this.soLuong = soLuong;
//        this.thanhTien = sanPham.getDonGia()*soLuong;
        this.HSD = HSD;
        this.NSX = NSX;
    }
    public CTPXK(com.example.alimekho.Model.sanPham sanPham, int soLuong, String NSX, String HSD) {
        this.sanPham = sanPham;
        this.soLuong = soLuong;
   //     this.thanhTien = sanPham.getDonGia()*soLuong;
        this.HSD = HSD;
        this.NSX = NSX;
    }

    public CTPXK(com.example.alimekho.Model.sanPham sanPham, int soLuong) {
        this.sanPham = sanPham;
        this.soLuong = soLuong;
       // this.thanhTien = sanPham.getDonGia()*soLuong;
    }

    public String getNSX() {
        return NSX;
    }

    public String getHSD() {
        return HSD;
    }

    public String getMaPXK() {
        return maPXK;
    }

    public void setThanhTien(double thanhTien) {
        this.thanhTien = thanhTien;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
       // this.thanhTien = this.soLuong * this.sanPham.getDonGia();
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
