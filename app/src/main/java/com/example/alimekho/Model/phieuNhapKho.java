package com.example.alimekho.Model;

import java.io.Serializable;

public class phieuNhapKho implements Serializable {
    private String maPhieu;
    private String ngayNhapKho;
    private String tenNCC;
    private String tenNV;
    private double totalMoney;

    public phieuNhapKho(String maPhieu, String ngayNhapKho, String tenNCC, String tenNV, double totalMoney) {
        this.maPhieu = maPhieu;
        this.ngayNhapKho = ngayNhapKho;
        this.tenNCC = tenNCC;
        this.tenNV = tenNV;
        this.totalMoney = totalMoney;
    }

    public String getMaPhieu() {
        return maPhieu;
    }

    public void setMaPhieu(String maPhieu) {
        this.maPhieu = maPhieu;
    }

    public String getNgayNhapKho() {
        return ngayNhapKho;
    }

    public void setNgayNhapKho(String ngayNhapKho) {
        this.ngayNhapKho = ngayNhapKho;
    }

    public String getTenNCC() {
        return tenNCC;
    }

    public void setTenNCC(String tenNCC) {
        this.tenNCC = tenNCC;
    }

    public String getTenNV() {
        return tenNV;
    }

    public void setTenNV(String tenNV) {
        this.tenNV = tenNV;
    }

    public double getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(double totalMoney) {
        this.totalMoney = totalMoney;
    }
}
