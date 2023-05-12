package com.example.alimekho.Model;

public class nhaCungCap {
    private String maNCC;
    private String tenCC;
    private String SDT;
    private String diaChi;

    public nhaCungCap(String maNCC, String tenCC, String diaChi, String SDT) {
        this.maNCC = maNCC;
        this.tenCC = tenCC;
        this.SDT = SDT;
        this.diaChi = diaChi;
    }

    public String getMaNCC() {
        return maNCC;
    }

    public void setMaNCC(String maNCC) {
        this.maNCC = maNCC;
    }

    public String getTenCC() {
        return tenCC;
    }

    public void setTenCC(String tenCC) {
        this.tenCC = tenCC;
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
