package com.example.alimekho.Model;

public class phieuNhapKho {
    private String maPhieu;
    private String ngayNhapKho;
    private String tenNCC;
    private String maNV;
    private int totalMoney;

    public phieuNhapKho(String maPhieu, String ngayNhapKho, String tenNCC, String maNV, int totalMoney) {
        this.maPhieu = maPhieu;
        this.ngayNhapKho = ngayNhapKho;
        this.tenNCC = tenNCC;
        this.maNV = maNV;
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

    public String getMaNV() {
        return maNV;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }

    public int getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(int totalMoney) {
        this.totalMoney = totalMoney;
    }
}
