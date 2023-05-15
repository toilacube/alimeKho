package com.example.alimekho.Model;

public class phieuXuatKho {
    private String maPhieu;
    private String ngayXuatKho;
    private String tenCuaHangXuat;
    private String TenNV;
    private Double totalMoney;

    public phieuXuatKho(String maPhieu, String ngayXuatKho, String tenCuaHangXuat, String TenNV, Double totalMoney) {
        this.maPhieu = maPhieu;
        this.ngayXuatKho = ngayXuatKho;
        this.tenCuaHangXuat = tenCuaHangXuat;
        this.TenNV = TenNV;
        this.totalMoney = totalMoney;
    }

    public String getMaPhieu() {
        return maPhieu;
    }

    public void setMaPhieu(String maPhieu) {
        this.maPhieu = maPhieu;
    }

    public String getNgayXuatKho() {
        return ngayXuatKho;
    }

    public void setNgayXuatKho(String ngayXuatKho) {
        this.ngayXuatKho = ngayXuatKho;
    }

    public String getTenCuaHangXuat() {
        return tenCuaHangXuat;
    }

    public void setTenCuaHangXuat(String tenCuaHangXuat) {
        this.tenCuaHangXuat = tenCuaHangXuat;
    }

    public String getTenNV() {
        return TenNV;
    }

    public void setTenNV(String TenNV) {
        this.TenNV = TenNV;
    }

    public Double getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(Double totalMoney) {
        this.totalMoney = totalMoney;
    }
}
