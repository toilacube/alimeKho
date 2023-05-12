package com.example.alimekho.Model;

public class cuaHangXuat {
    private String maCHX;
    private String tenCHX;
    private String SDT;
    private String diaChi;

    public cuaHangXuat(String maCHX, String tenCHX, String SDT, String diaChi) {
        this.maCHX = maCHX;
        this.tenCHX = tenCHX;
        this.SDT = SDT;
        this.diaChi = diaChi;
    }

    public String getMaCHX() {
        return maCHX;
    }

    public void setMaCHX(String maCHX) {
        this.maCHX = maCHX;
    }

    public String getTenCHX() {
        return tenCHX;
    }

    public void setTenCHX(String tenCHX) {
        this.tenCHX = tenCHX;
    }

    public String getSDT() {
        return SDT;
    }

    public void setSDT(String SDT) {
        this.SDT = SDT;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }
}
