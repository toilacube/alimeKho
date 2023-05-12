package com.example.alimekho.Model;

public class phieuXuatKho {
    private String maPhieu;
    private String ngayXuatKho;
    private String tenCuaHangXuat;
    private String maNV;
    private int totalMoney;

    public phieuXuatKho(String maPhieu, String ngayXuatKho, String tenCuaHangXuat, String maNV, int totalMoney) {
        this.maPhieu = maPhieu;
        this.ngayXuatKho = ngayXuatKho;
        this.tenCuaHangXuat = tenCuaHangXuat;
        this.maNV = maNV;
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
