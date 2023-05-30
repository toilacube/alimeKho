package com.example.alimekho.Model;

public class phieuKiemKho {
    private String maPhieu;
    private String ngayKK;
    private String tenNV;
    private int tinhTrang;

    public phieuKiemKho(String maPhieu, String ngayKK, String tenNV, int tinhTrang) {
        this.maPhieu = maPhieu;
        this.ngayKK = ngayKK;
        this.tenNV = tenNV;
        this.tinhTrang = tinhTrang;
    }

    public void setMaPhieu(String maPhieu) {
        this.maPhieu = maPhieu;
    }

    public void setNgayKK(String ngayKK) {
        this.ngayKK = ngayKK;
    }

    public void setTenNV(String tenNV) {
        this.tenNV = tenNV;
    }

    public void setTinhTrang(int tinhTrang) {
        this.tinhTrang = tinhTrang;
    }

    public String getMaPhieu() {
        return maPhieu;
    }

    public String getNgayKK() {
        return ngayKK;
    }

    public String getTenNV() {
        return tenNV;
    }

    public int getTinhTrang() {
        return tinhTrang;
    }
}
