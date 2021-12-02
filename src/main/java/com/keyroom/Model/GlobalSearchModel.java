package com.keyroom.Model;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

public class GlobalSearchModel {
    @Expose
    Integer status;
    @Expose
    ArrayList<Data> data;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public ArrayList<Data> getData() {
        return data;
    }

    public void setData(ArrayList<Data> data) {
        this.data = data;
    }


}
