package com.example.alimekho.Model;

public class CTPNK {
    private String maPNK;
    private double thanhTien;
    private int soLuong;
    private String maLo;

    public CTPNK(String maPNK, double thanhTien, int soLuong, String maLo) {
        this.maPNK = maPNK;
        this.thanhTien = thanhTien;
        this.soLuong = soLuong;
        this.maLo = maLo;
    }

    public String getMaPNK() {
        return maPNK;
    }

    public void setMaPNK(String maPNK) {
        this.maPNK = maPNK;
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

    public String getMaLo() {
        return maLo;
    }

    public void setMaLo(String maLo) {
        this.maLo = maLo;
    }
}
