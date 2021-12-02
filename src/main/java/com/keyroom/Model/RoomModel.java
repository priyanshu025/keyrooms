package com.keyroom.Model;

public class RoomModel {

    int adult;
    boolean haveChildren;
    int children;
    private boolean visibility;

    public RoomModel(int adult, boolean haveChildren,int children){
        this.adult=adult;
        this.haveChildren=haveChildren;
        this.children=children;
    }
    public int getAdult() {
        return adult;
    }

    public void setAdult(int adult) {
        this.adult = adult;
    }


    public boolean isHaveChildren() {
        return haveChildren;
    }

    public void setHaveChildren(boolean haveChildren) {
        this.haveChildren = haveChildren;
    }
    public int getChildren() {
        return children;
    }

    public void setChildren(int children) {
        this.children = children;
    }

    public boolean isVisibility() {
        return visibility;
    }

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }

}
