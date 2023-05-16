package com.example.alimekho.Model;

public class CTPNK {
    private String maPNK;
    private sanPham sanPham;
    private double thanhTien;
    private int soLuong;
    private String NSX;
    private String HSD;
    private int status;

    public CTPNK(String maPNK, com.example.alimekho.Model.sanPham sanPham, int soLuong, String NSX, String HSD) {
        this.maPNK = maPNK;
        this.sanPham = sanPham;
        this.thanhTien = soLuong * sanPham.getDonGia();
        this.soLuong = soLuong;
        this.NSX = NSX;
        this.HSD = HSD;
        status = 0; //tinhtrang
    }

    public String getMaPNK() {
        return maPNK;
    }

    public void setMaPNK(String maPNK) {
        this.maPNK = maPNK;
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

    public void setThanhTien(double thanhTien) {
        this.thanhTien = thanhTien;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
