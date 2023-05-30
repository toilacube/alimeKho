package com.example.alimekho.Model;

public class CTPKK {
    private String maPhieu;
    private loSanPham lo;
    private int slDB;
    private int slTT;

    public CTPKK(String maPhieu, loSanPham lo, int slDB, int slTT) {
        this.maPhieu = maPhieu;
        this.lo = lo;
        this.slDB = slDB;
        this.slTT = slTT;
    }

    public void setMaPhieu(String maPhieu) {
        this.maPhieu = maPhieu;
    }

    public void setLo(loSanPham lo) {
        this.lo = lo;
    }

    public void setSlDB(int slDB) {
        this.slDB = slDB;
    }

    public void setSlTT(int slTT) {
        this.slTT = slTT;
    }

    public String getMaPhieu() {
        return maPhieu;
    }

    public loSanPham getLo() {
        return lo;
    }

    public int getSlDB() {
        return slDB;
    }

    public int getSlTT() {
        return slTT;
    }
}
