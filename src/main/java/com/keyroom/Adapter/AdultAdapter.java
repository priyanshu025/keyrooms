package com.keyroom.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.keyroom.Activity.ChekInCheckOutActivity;
import com.keyroom.Model.RoomModel;
import com.keyroom.R;

import java.util.ArrayList;

public class AdultAdapter extends RecyclerView.Adapter<AdultAdapter.ViewHolder> {

    Context mContext;
    ArrayList<RoomModel> roomModelArrayList;
    int[] count;
    int[] childCount;
    TotalAdult iTotalAdult;
    int roomCount;
    boolean isRemove=false;
    public AdultAdapter(Context context, ArrayList<RoomModel> roomModelArrayList) {
        this.mContext = context;
        this.roomModelArrayList = roomModelArrayList;
        this.iTotalAdult = (TotalAdult) context;
        roomCount=ChekInCheckOutActivity.maxAdult;
    }

    @NonNull
    @Override
    public AdultAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_adult, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdultAdapter.ViewHolder holder, int position) {
        final RoomModel roomModel=roomModelArrayList.get(position);
        count = new int[roomModelArrayList.size()];
        childCount = new int[roomModelArrayList.size()];


        holder.txtRoom.setText("Room " + (position + 1));
        if (position == 0) {
            roomModel.setVisibility(true);
            holder.btnRemove.setVisibility(View.GONE);
        }else {
            holder.btnRemove.setVisibility(View.VISIBLE);
        }
        if(roomModel.isVisibility())
            holder.llfooter.setVisibility(View.VISIBLE);
        else
            holder.llfooter.setVisibility(View.GONE);

        if(position==(roomModelArrayList.size()-1)){
            holder.llfooter.setVisibility(View.VISIBLE);

            holder.chkChildren.setChecked(roomModel.isHaveChildren());
            if(!isRemove) {
                count[holder.getAdapterPosition()] = 1;
                childCount[holder.getAdapterPosition()] = 1;
                roomModel.setAdult(count[holder.getAdapterPosition()]);
                holder.textAdultNumber.setText(String.valueOf(roomModel.getAdult()));
                holder.txtGuest.setText(roomModel.getAdult() + " Guest");
                holder.textChildrenNumber.setText(String.valueOf(roomModel.getChildren()));
            }
            isRemove=false;

        }else {
            holder.llfooter.setVisibility(View.GONE);
            count[holder.getAdapterPosition()]=Integer.parseInt(holder.textAdultNumber.getText().toString());
            childCount[holder.getAdapterPosition()]=Integer.parseInt(holder.textChildrenNumber.getText().toString());
            roomModel.setAdult(count[holder.getAdapterPosition()]);
            holder.textAdultNumber.setText(String.valueOf(roomModel.getAdult()));
            holder.txtGuest.setText(roomModel.getAdult() + " Guest");
            holder.textChildrenNumber.setText(String.valueOf(roomModel.getChildren()));
        }


        holder.imgLeft.setOnClickListener(v -> {

            if (Integer.parseInt(holder.textAdultNumber.getText().toString()) > 1 /*&& count[holder.getAdapterPosition()] != 1*/) {
                count[holder.getAdapterPosition()]=Integer.parseInt(holder.textAdultNumber.getText().toString());
                roomModelArrayList.get(holder.getAdapterPosition()).setAdult(--count[holder.getAdapterPosition()]);
                holder.textAdultNumber.setText(String.valueOf(roomModelArrayList.get(holder.getAdapterPosition()).getAdult()));
                holder.txtGuest.setText(roomModelArrayList.get(holder.getAdapterPosition()).getAdult() + " Guest");
            }
            iTotalAdult.getTotalAdult(roomModelArrayList);
        });
        holder.imgRight.setOnClickListener(v -> {
            if(ChekInCheckOutActivity.totalAll<roomCount) {
                if (Integer.parseInt(holder.textAdultNumber.getText().toString()) < ChekInCheckOutActivity.maxAdult /*&& count[holder.getAdapterPosition()]!=ChekInCheckOutActivity.maxAdult*/) {
                    count[holder.getAdapterPosition()]=Integer.parseInt(holder.textAdultNumber.getText().toString());
                    roomModelArrayList.get(holder.getAdapterPosition()).setAdult(++count[holder.getAdapterPosition()]);
                    holder.textAdultNumber.setText(String.valueOf(roomModelArrayList.get(holder.getAdapterPosition()).getAdult()));
                    holder.txtGuest.setText(roomModelArrayList.get(holder.getAdapterPosition()).getAdult() + " Guest");
                }
                iTotalAdult.getTotalAdult(roomModelArrayList);
            }
            else
                Toast.makeText(mContext,"Please add room for more people ",Toast.LENGTH_LONG).show();
        });
        holder.btnAdd.setOnClickListener(v -> {
            RoomModel model = new RoomModel(1, false,0);
            roomModelArrayList.add(holder.getAdapterPosition() + 1, model);
            notifyItemInserted(holder.getAdapterPosition()+ 1);
            roomModel.setVisibility(true);
            ChekInCheckOutActivity.room = roomModelArrayList.size();
            iTotalAdult.getTotalAdult(roomModelArrayList);
            roomCount=roomCount+ChekInCheckOutActivity.maxAdult;
            isRemove=false;
            notifyDataSetChanged();
        });
        holder.btnRemove.setOnClickListener(v -> {
            if(roomModel.isVisibility()) {
                holder.llChildren.setVisibility(View.VISIBLE);
                roomModel.setHaveChildren(true);
            }
            else {
                holder.llChildren.setVisibility(View.GONE);
                roomModel.setVisibility(false);
                childCount[holder.getAdapterPosition()]=1;
                roomModelArrayList.get(holder.getAdapterPosition()).setChildren(0);
                holder.textChildrenNumber.setText(String.valueOf(roomModel.getChildren()));
            }
            roomModelArrayList.remove(roomModel);
            notifyItemRemoved(holder.getAdapterPosition());
            roomCount=roomCount-ChekInCheckOutActivity.maxAdult;
            ChekInCheckOutActivity.room = roomModelArrayList.size();
            iTotalAdult.getTotalAdult(roomModelArrayList);
            isRemove=true;
            notifyDataSetChanged();

        });
        holder.chkChildren.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (ChekInCheckOutActivity.totalAll < roomCount) {
                if (isChecked) {
                    roomModel.setHaveChildren(true);
                    holder.llChildren.setVisibility(View.VISIBLE);
                    childCount[holder.getAdapterPosition()] = 1;
                    roomModelArrayList.get(holder.getAdapterPosition()).setChildren(1);
                    holder.textChildrenNumber.setText(String.valueOf(roomModel.getChildren()));
                } else {
                    roomModel.setHaveChildren(false);
                    holder.llChildren.setVisibility(View.GONE);
                    childCount[holder.getAdapterPosition()] = 1;
                    roomModelArrayList.get(holder.getAdapterPosition()).setChildren(0);
                }
                iTotalAdult.getTotalChild(roomModelArrayList);
            } else {
                Toast.makeText(mContext, "Please add room for more people ", Toast.LENGTH_LONG).show();

            }
        });
        holder.imgLeftChildren.setOnClickListener(v -> {
            roomModel.setHaveChildren(true);
             if (Integer.parseInt(holder.textChildrenNumber.getText().toString()) > 1) {
                childCount[holder.getAdapterPosition()]=Integer.parseInt(holder.textChildrenNumber.getText().toString());
                roomModelArrayList.get(holder.getAdapterPosition()).setChildren(--childCount[holder.getAdapterPosition()]);
                holder.textChildrenNumber.setText(String.valueOf(roomModelArrayList.get(holder.getAdapterPosition()).getChildren()));
            }
            iTotalAdult.getTotalChild(roomModelArrayList);
        });
        holder.imgRightChildren.setOnClickListener(v -> {
            if(ChekInCheckOutActivity.totalAll<roomCount) {
                roomModel.setHaveChildren(true);
                if (Integer.parseInt(holder.textChildrenNumber.getText().toString())< ChekInCheckOutActivity.maxAdult ) {
                    childCount[holder.getAdapterPosition()]=Integer.parseInt(holder.textChildrenNumber.getText().toString());
                    roomModelArrayList.get(holder.getAdapterPosition()).setChildren(++childCount[holder.getAdapterPosition()]);
                    holder.textChildrenNumber.setText(String.valueOf(roomModelArrayList.get(holder.getAdapterPosition()).getChildren()));
                }
                iTotalAdult.getTotalChild(roomModelArrayList);
            }
            else
                Toast.makeText(mContext,"Please add room for more people ",Toast.LENGTH_LONG).show();
        });
    }

    @Override
    public int getItemCount() {
        return roomModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView btnAdd, btnRemove, imgLeft, imgRight, textAdultNumber, txtRoom,txtGuest;
        TextView imgLeftChildren,textChildrenNumber,imgRightChildren;
        CheckBox chkChildren;
        LinearLayout llChildren,llfooter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            btnAdd = itemView.findViewById(R.id.btnAdd);
            btnRemove = itemView.findViewById(R.id.btnRemove);
            textAdultNumber = itemView.findViewById(R.id.textAdultNumber);
            imgRight = itemView.findViewById(R.id.imgRight);
            imgLeft = itemView.findViewById(R.id.imgLeft);
            txtRoom = itemView.findViewById(R.id.txtRoom);
            chkChildren = itemView.findViewById(R.id.chkChildren);
            txtGuest = itemView.findViewById(R.id.txtGuest);
            llChildren = itemView.findViewById(R.id.llChildren);
            imgLeftChildren = itemView.findViewById(R.id.imgLeftChildren);
            textChildrenNumber = itemView.findViewById(R.id.textChildrenNumber);
            imgRightChildren = itemView.findViewById(R.id.imgRightChildren);
            llfooter = itemView.findViewById(R.id.llfooter);

        }
    }
    public interface TotalAdult {
        void getTotalAdult(ArrayList<RoomModel> roomModelArrayList);
        void getTotalChild(ArrayList<RoomModel> roomModelArrayList);
    }
}
