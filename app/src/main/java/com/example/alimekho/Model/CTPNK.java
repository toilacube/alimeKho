package com.example.alimekho.Model;

public class CTPNK {
    private String maPNK;
    private sanPham sanPham;
    private double thanhTien;

    public CTPNK(String maPNK, com.example.alimekho.Model.sanPham sanPham) {
        this.maPNK = maPNK;
        this.sanPham = sanPham;
        this.thanhTien = sanPham.getDonGia()*sanPham.getSoLuong();
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

    public void setThanhTien(int thanhTien) {
        this.thanhTien = thanhTien;
    }
}
