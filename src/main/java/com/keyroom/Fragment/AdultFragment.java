package com.keyroom.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.keyroom.Activity.ChekInCheckOutActivity;
import com.keyroom.Activity.SearchResultActivity;
import com.keyroom.Adapter.AdultAdapter;
import com.keyroom.Database.BookingDatabase;
import com.keyroom.Model.RoomModel;
import com.keyroom.Network.API;
import com.keyroom.Network.ConfigHader;
import com.keyroom.R;
import com.keyroom.Utility.SharedPrefs;

import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AdultFragment extends Fragment {
    AdultAdapter adapter;
    RecyclerView recyclerView;
    ArrayList<RoomModel> roomModelArrayList;
    RoomModel roomModel;
    AdultAdapter.TotalAdult iTotalAdult;

    API api;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        api= ConfigHader.getClient().create(API.class);
        View rootView = inflater.inflate(R.layout.fragment_adult, container, false);
        recyclerView=rootView.findViewById(R.id.rcAdult);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        roomModelArrayList=new ArrayList<>();

        callApi();
        return rootView;
    }
    private void callApi() {

        Call<JsonObject> call = api.getMax_adult();
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {

                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    if (jsonObject.getBoolean("status")) {
                        ChekInCheckOutActivity.maxAdult=Integer.parseInt(jsonObject.getString("max_guests"));
                        roomModel=new RoomModel(1,false,0);
                        roomModelArrayList.add(roomModel);
                        adapter=new AdultAdapter(getContext(),roomModelArrayList);
                        recyclerView.setAdapter(adapter);

                    } else {
                        Toast.makeText(getContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

                t.printStackTrace();
            }
        });
    }

    private void allDataClear() {
        SharedPrefs.clearOnlyFilter();
        ChekInCheckOutActivity.totalAdult = 1;
        ChekInCheckOutActivity.totalChildren = 0;
        ChekInCheckOutActivity.totalAll=1;
        ChekInCheckOutActivity.room = 1;
    }

    @Override
    public void onResume() {
        super.onResume();
        allDataClear();
    }
}