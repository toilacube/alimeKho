package com.example.alimekho.Model;

import java.io.Serializable;

public class loSanPham implements Serializable {
    private String maLo;
    private String NSX;
    private String HSD;
    private sanPham sanPham;
    private int sLTon;
    private int sLChuaXep;
    private double donGia;

    public loSanPham() {
    }

    public loSanPham(String maLo, String NSX, String HSD, com.example.alimekho.Model.sanPham sanPham, int sLTon, int sLChuaXep, double donGia) {
        this.maLo = maLo;
        this.NSX = NSX;
        this.HSD = HSD;
        this.sanPham = sanPham;
        this.sLTon = sLTon;
        this.sLChuaXep = sLChuaXep;
        this.donGia = donGia;
    }

    public loSanPham(String maLo, String NSX, String HSD, com.example.alimekho.Model.sanPham sanPham, double donGia) {
        this.maLo = maLo;
        this.NSX = NSX;
        this.HSD = HSD;
        this.sanPham = sanPham;
        this.donGia = donGia;
        this.sLTon = 0;
        this.sLChuaXep = 0;
    }

    public double getDonGia() {
        return donGia;
    }

    public void setDonGia(double donGia) {
        this.donGia = donGia;
    }

    public String getMaLo() {
        return maLo;
    }

    public void setMaLo(String maLo) {
        this.maLo = maLo;
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

    public sanPham getSanPham() {
        return sanPham;
    }

    public void setSanPham(sanPham sanPham) {
        this.sanPham = sanPham;
    }

    public int getsLTon() {
        return sLTon;
    }

    public void setsLTon(int sLTon) {
        this.sLTon = sLTon;
    }

    public int getsLChuaXep() {
        return sLChuaXep;
    }

    public void setsLChuaXep(int sLChuaXep) {
        this.sLChuaXep = sLChuaXep;
    }
}


