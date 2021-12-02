package com.keyroom.Model;

import android.app.Notification;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

public class NotificationModel {

    @Expose
    boolean status;

    @Expose
    ArrayList<Row> row;
    @Expose
    String total_records;

    public String getTotal_records() {
        return total_records;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public ArrayList<Row> getRows() {
        return row;
    }

    public void setRows(ArrayList<Row> rows) {
        this.row = rows;
    }

    public class Row{

        @Expose
        Data data;

        @Expose
        String created_at;

        public String getCreated_at() {
            return created_at;
        }

        public Data getData() {
            return data;
        }

        public void setData(Data data) {
            this.data = data;
        }
    }

    public class Data{

        @Expose
        String id;

        @Expose
        Notification notification;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Notification getNotification() {
            return notification;
        }

        public void setNotification(Notification notification) {
            this.notification = notification;
        }
    }

    public class Notification{
        @Expose
        String message;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
