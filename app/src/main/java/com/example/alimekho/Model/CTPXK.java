package com.example.alimekho.Model;

public class CTPXK {
    private String maPXK;
    private sanPham sanPham;
    private int thanhTien;

    public CTPXK(String maPXK, com.example.alimekho.Model.sanPham sanPham, int thanhTien) {
        this.maPXK = maPXK;
        this.sanPham = sanPham;
        this.thanhTien = thanhTien;
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

    public int getThanhTien() {
        return thanhTien;
    }

    public void setThanhTien(int thanhTien) {
        this.thanhTien = thanhTien;
    }
}
