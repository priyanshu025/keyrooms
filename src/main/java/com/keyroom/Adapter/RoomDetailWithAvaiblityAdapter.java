package com.keyroom.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.JsonArray;
import com.keyroom.Activity.ChekInCheckOutActivity;
import com.keyroom.Model.CheckAvabilityModel;
import com.keyroom.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import uz.jamshid.library.ExactRatingBar;

public class RoomDetailWithAvaiblityAdapter extends RecyclerView.Adapter<RoomDetailWithAvaiblityAdapter.ViewHolder> {

    Context mContext;
    ArrayList<CheckAvabilityModel.Rooms> roomsArrayList = new ArrayList<>();
    RoomDetailCount roomDetailCount;
    ArrayList<String> numberOfRoom;
    List<List<String>> noOfRoom;

    JsonArray jsonArray;
    JSONObject jsonObject;
    int startRoomPos=1;
    long data[];

    public RoomDetailWithAvaiblityAdapter(Context context, ArrayList<CheckAvabilityModel.Rooms> roomsArrayList) {
        this.mContext = context;
        this.roomsArrayList = roomsArrayList;
        roomDetailCount= (RoomDetailCount) mContext;
        noOfRoom=new ArrayList<>(roomsArrayList.size());
        jsonArray=new JsonArray(roomsArrayList.size());

    }

    @NonNull
    @Override
    public RoomDetailWithAvaiblityAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_roomdetails, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomDetailWithAvaiblityAdapter.ViewHolder holder1, int position) {
        RoomDetailWithAvaiblityAdapter.ViewHolder holder = holder1;
        holder.txtRoomDetail.setVisibility(View.VISIBLE);
        CheckAvabilityModel.Rooms row = roomsArrayList.get(position);
        if (row.getTitle() != null)
            holder.txthotelName.setText(row.getTitle());
        if (row.getPrice() != null)
            holder.txtPrice.setText("₹" + row.getPrice());
        else
            holder.txtPrice.setText("₹0");

        if (row.getNumber() != null) {
            numberOfRoom = new ArrayList<>(row.getNumber());
            data = new long[row.getNumber() + 1];
            if (ChekInCheckOutActivity.totalAll > 3) {
                if (ChekInCheckOutActivity.totalAll % 3 == 0) {
                    startRoomPos = ChekInCheckOutActivity.totalAll / 3;
                } else {
                    startRoomPos = (ChekInCheckOutActivity.totalAll / 3) + 1;
                }
            } else {
                startRoomPos = 1;
            }

            for (int i = startRoomPos; i <= roomsArrayList.get(position).getNumber(); i++) {
                if (i == startRoomPos) {
                    numberOfRoom.clear();
                    data = new long[roomsArrayList.get(position).getNumber() + 1];
                   // numberOfRoom.add(0, 0 + " Room");
                    //data[0] = 0;
                }


                numberOfRoom.add(i + " Room");
                data[i] = i;
            }
        }

        noOfRoom.add(numberOfRoom);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, noOfRoom.get(position));
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
        holder.txtRoomDetail.setAdapter(spinnerArrayAdapter);
        holder.txtRoomDetail.setSelection(0);

            holder.txtRoomDetail.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (parent.getItemIdAtPosition(position) >= 0) {


                        Log.e("@@ Array spinner", " " + data[position + (roomsArrayList.get(holder.getAdapterPosition()).getNumber_selected())] + " --id=" + roomsArrayList.get(holder.getAdapterPosition()).getId());
                        try {
//                            if (holder.txtRoomDetail.getSelectedItem().equals(0 + " Room"))
//                            {
//                                holder.txtPrice.setText("₹" + 0);
//                                roomDetailCount.getRoomDetailCount(roomsArrayList.get(holder.getAdapterPosition()).getId(), 0, 0);
//                            }
//                            else
                                if (row.getExtra_prices() != null)
                            {
                                jsonObject = new JSONObject(row.getExtra_prices());
                                holder.txtPrice.setText("₹" + (((roomsArrayList.get(holder.getAdapterPosition()).getPrice()) * data[position /*- 1 */ + (roomsArrayList.get(holder.getAdapterPosition()).getNumber_selected())]) + jsonObject.getInt(String.valueOf(roomsArrayList.get(holder.getAdapterPosition()).getNumber_selected()))));
                                roomDetailCount.getRoomDetailCount(roomsArrayList.get(holder.getAdapterPosition()).getId(), Integer.valueOf((int) data[position + (roomsArrayList.get(holder.getAdapterPosition()).getNumber_selected())]), (int) (((row.getPrice()) * data[position /*- 1 */  + (roomsArrayList.get(holder.getAdapterPosition()).getNumber_selected())]) + jsonObject.getInt(String.valueOf(roomsArrayList.get(holder.getAdapterPosition()).getNumber_selected()))));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    /*else {
                        Log.d("Array", " " + data + " --id=" + roomsArrayList.get(holder.getAdapterPosition()).getId());
                        holder.txtPrice.setText("₹"+row.getPrice());
                        roomDetailCount.getRoomDetailCount(roomsArrayList.get(holder.getAdapterPosition()).getId(), Integer.valueOf((int) data[position+1]),-1);
                    }*/
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
    //}
        if (row.getImage() != null) {
            Glide.with(mContext).load(row.getImage()).placeholder(R.drawable.error_image).error(R.drawable.city).into(holder.imgHotel);
        }


    }

    @Override
    public int getItemCount() {
        return roomsArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgHotel;
        TextView txthotelName;
        Spinner txtRoomDetail;
        TextView txtPrice;
        ExactRatingBar rate;
        LinearLayout lnyHotelDetils;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgHotel = itemView.findViewById(R.id.imgHotel);
            txthotelName = itemView.findViewById(R.id.txthotelName);
            txtRoomDetail = itemView.findViewById(R.id.txtRoomDetail);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            rate = itemView.findViewById(R.id.rate);
            lnyHotelDetils = itemView.findViewById(R.id.lnyHotelDetils);

        }
    }
    public interface RoomDetailCount{
        public void getRoomDetailCount(int id, int no_content, int price);
    }
}
