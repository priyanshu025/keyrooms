package com.keyroom.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.keyroom.Adapter.BookingAdapter;
import com.keyroom.Database.BookingDatabase;
import com.keyroom.Model.BookingMod;
import com.keyroom.Model.BookingModel;
import com.keyroom.Network.API;
import com.keyroom.Network.ConfigHader;
import com.keyroom.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllBookingFragment extends Fragment {
    RecyclerView rcyBooking;
   // ProgressBar progressBar;
    API api;
    ArrayList<BookingModel.Row> bookingModelArrayList = new ArrayList<>();
    BookingAdapter bookingAdapter;
    String data = "";
    boolean isBooking = false;
    Integer page_no = 1, total_record = 0;
    Handler handler;
    TextView textmsg;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        api = ConfigHader.getClient().create(API.class);
        View rootView= inflater.inflate(R.layout.fragment_all_booking, container, false);
        initView(rootView);
        return rootView;
    }
    private void initView(View view) {
        bookingModelArrayList.clear();
        handler = new Handler();
        rcyBooking = view.findViewById(R.id.rcybooking);
       // progressBar = view.findViewById(R.id.progressBar);
        textmsg=view.findViewById(R.id.textmsg);


        rcyBooking.setHasFixedSize(true);
        rcyBooking.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rcyBooking.setItemAnimator(new DefaultItemAnimator());
        bookingAdapter = new BookingAdapter(getActivity(), bookingModelArrayList, (code, id) -> callCancelApi(code, id) ,rcyBooking, () -> {
            if (total_record > bookingModelArrayList.size()) {
                final Runnable r = () -> {
                    ++page_no;
                    callBookingApi();
                };
                handler.post(r);
            }
        });
        rcyBooking.setAdapter(bookingAdapter);
        callBookingApi();
    }
    private void cancelStatusChange(String status, Integer id) {
        List<BookingModel.Row> models = new ArrayList<>();

        for (BookingModel.Row model : bookingModelArrayList) {
            models.add(model.clone());
        }
        for (int i = 0; i < models.size(); i++) {
            if (models.get(i).getId() == id) {
                models.get(i).setStatus(status);
            }
        }
        bookingAdapter.updateList(models);
    }

    private void callBookingApi() {
        //progressBar.setVisibility(View.VISIBLE);
        if (page_no == 1)
            bookingModelArrayList.clear();
        Call<JsonObject> call = api.bookingHistory(data, page_no);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    Log.e("URL==>", "@@" + response.raw().request().url());
                   /* if (progressBar != null && progressBar.isShown()) {
                        progressBar.setVisibility(View.GONE);
                    }*/
                    bookingAdapter.setLoaded();
                    BookingModel bookingModel = new Gson().fromJson(response.body(), BookingModel.class);
                    bookingModelArrayList.addAll(bookingModel.getRow());
                    total_record = bookingModel.getTotal_records();
                    if (bookingModelArrayList.size() != 0) {
                        /** Add status to database*/
                        BookingDatabase database = BookingDatabase.getDatabase(getActivity());
                        for (int i = 0; i < bookingModelArrayList.size(); i++) {
                            BookingMod bookingMod = new BookingMod(bookingModelArrayList.get(i).getId(), bookingModelArrayList.get(i).getStatus());
                            database.bookingDAO().insertAllList(bookingMod);
                        }
                        bookingAdapter.notifyDataSetChanged();
                    } else {
                        bookingAdapter.notifyDataSetChanged();
                        rcyBooking.setVisibility(View.GONE);
                        textmsg.setVisibility(View.VISIBLE);
                       // Toast.makeText(getContext(), "No Data Found", Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
           /*     if (progressBar != null && progressBar.isShown()) {
                    progressBar.setVisibility(View.GONE);
                }*/
                t.printStackTrace();
            }
        });
    }

    private void callCancelApi(String code, Integer id) {
       // progressBar.setVisibility(View.VISIBLE);
        Call<JsonObject> call = api.cancelBooking(code);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                   /* if (progressBar != null && progressBar.isShown()) {
                        progressBar.setVisibility(View.GONE);
                    }*/
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    if (jsonObject.getBoolean("status")) {
                        Toast.makeText(getContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                        BookingDatabase database = BookingDatabase.getDatabase(getActivity());
                        database.bookingDAO().updateStatus("cancelled", id);
                        String statusSelect = database.bookingDAO().getStatusInfo(id);
                        cancelStatusChange(statusSelect, id);
                    } else {
                        rcyBooking.setVisibility(View.GONE);

                        //Toast.makeText(getContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
               /* if (progressBar != null && progressBar.isShown()) {
                    progressBar.setVisibility(View.GONE);
                }*/
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        deleteDataRepo();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        deleteDataRepo();
    }

    private void deleteDataRepo() {
        BookingDatabase database = BookingDatabase.getDatabase(getActivity());
        database.bookingDAO().deleteAll();
    }

}