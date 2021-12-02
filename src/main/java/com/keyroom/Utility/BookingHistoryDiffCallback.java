package com.keyroom.Utility;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;


import com.keyroom.Model.BookingModel;

import java.util.List;

public class BookingHistoryDiffCallback extends DiffUtil.Callback {
    private final List<BookingModel.Row> oldBookingList;
    private final List<BookingModel.Row> newBookingList;

    public BookingHistoryDiffCallback(List<BookingModel.Row> oldBookingList, List<BookingModel.Row> newBookingList) {
        this.oldBookingList = oldBookingList;
        this.newBookingList = newBookingList;
    }


    @Override
    public int getOldListSize() {
        return oldBookingList.size();
    }

    @Override
    public int getNewListSize() {
        return newBookingList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldBookingList.get(oldItemPosition).getCustomer_id() == newBookingList.get(
                newItemPosition).getCustomer_id();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        final BookingModel.Row oldEmployee = oldBookingList.get(oldItemPosition);
        final BookingModel.Row newEmployee = newBookingList.get(newItemPosition);

        //return oldEmployee.getStatus().equals(newEmployee.getStatus());
        int result = newBookingList.get(newItemPosition).compareTo(oldBookingList.get(oldItemPosition));
        return result == 0;
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        // Implement method if you're going to use ItemAnimator
        BookingModel.Row newModel = newBookingList.get(newItemPosition);
        BookingModel.Row oldModel = oldBookingList.get(oldItemPosition);

        Bundle diff = new Bundle();

        if (newModel.getStatus() != (oldModel.getStatus())) {
            diff.putString("status", newModel.getStatus());
        }if(newModel.getPayment_status()!=(oldModel.getPayment_status())){
            diff.putString("payment",newModel.getPayment_status());
        }
        if (diff.size() == 0) {
            return null;
        }
        return diff;
    }
}
