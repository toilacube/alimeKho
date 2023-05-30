package com.example.alimekho.Model;

import java.io.Serializable;
import java.util.ArrayList;

public class Area implements Serializable {
    private String id;
    private String area;
    private String shelve;
    private  int slot;
    private int available;
    private String type_id;
    private int is_deleted;
    private ArrayList<loSanPham> listLoSP = new ArrayList<>();
    public Area() {
        this.is_deleted = 0;
    }

    public void setListLoSP(ArrayList<loSanPham> listLoSP) {
        this.listLoSP = listLoSP;
    }

    public ArrayList<loSanPham> getListLoSP() {
        return listLoSP;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public int getSlot() {
        return slot;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public void setShelve(String shelf) {
        this.shelve = shelf;
    }

    public void setAvailable(int available) {
        this.available = available;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    public String getId() {
        return id;
    }

    public String getArea() {
        return area;
    }

    public String getShelve() {
        return shelve;
    }

    public int getAvailable() {
        return available;
    }

    public String getType_id() {
        return type_id;
    }
}
