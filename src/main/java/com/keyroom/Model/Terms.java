package com.keyroom.Model;

import com.google.gson.annotations.Expose;

public class Terms {
    @Expose
    String name;
    @Expose
    Integer id;

    boolean isCheck;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean isCheck() {
        return isCheck;
    }
    public void setCheck(boolean check) {
        isCheck = check;
    }
}
