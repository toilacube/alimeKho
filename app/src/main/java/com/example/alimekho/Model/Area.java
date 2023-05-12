package com.example.alimekho.Model;

import java.io.Serializable;

public class Area implements Serializable {
    private String id;
    private String area;
    private String shelf;
    private String bin;
    private String idProduct;
    private int numProduct;
    private String status;

    public Area(String id, String area, String shelf, String bin, String idProduct, int numProduct, String status) {
        this.id = id;
        this.area = area;
        this.shelf = shelf;
        this.bin = bin;
        this.idProduct = idProduct;
        this.numProduct = numProduct;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getShelf() {
        return shelf;
    }

    public void setShelf(String shelf) {
        this.shelf = shelf;
    }

    public String getBin() {
        return bin;
    }

    public void setBin(String bin) {
        this.bin = bin;
    }

    public String getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(String idProduct) {
        this.idProduct = idProduct;
    }

    public int getNumProduct() {
        return numProduct;
    }

    public void setNumProduct(int numProduct) {
        this.numProduct = numProduct;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Area{" +
                "id='" + id + '\'' +
                ", area='" + area + '\'' +
                ", shelf='" + shelf + '\'' +
                ", bin='" + bin + '\'' +
                ", idProduct='" + idProduct + '\'' +
                ", numProduct=" + numProduct +
                ", status='" + status + '\'' +
                '}';
    }
}
