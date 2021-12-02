/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.keyroom.DateHelper.Date;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.StateListDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


import com.keyroom.R;
import com.keyroom.Utility.Content;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * Displays a selectable list of years.
 */
public class MonthPickerView extends ListView implements OnItemClickListener, DatePickerDialog.OnDateChangedListener {
    private static final String TAG = "YearPickerView";
    private final DatePickerController mController;
    public Context ctx;
    ArrayList<String> years = new ArrayList<>();
    private YearAdapter mAdapter;
    private final int mViewSize;
    private final int mChildSize;
    private TextViewWithCircularIndicator mSelectedView;

    /**
     * @param context
     */
    public MonthPickerView(Context context, DatePickerController controller) {
        super(context);
        ctx = context;
        mController = controller;
        mController.registerOnDateChangedListener(this);
        ViewGroup.LayoutParams frame = new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        setLayoutParams(frame);
        Resources res = context.getResources();
        mViewSize = res.getDimensionPixelOffset(R.dimen.mdtp_date_picker_view_animator_height);
        mChildSize = res.getDimensionPixelOffset(R.dimen.mdtp_year_label_height);
        setVerticalFadingEdgeEnabled(true);
        setFadingEdgeLength(mChildSize / 3);
        init(context);
        setOnItemClickListener(this);
        setSelector(new StateListDrawable());
        setDividerHeight(0);
        onDateChanged();


    }

    private static int getYearFromTextView(TextView view) {
        return function(view.getText().toString());
    }

    public static int function(String mon) {

        int x = 0;
        switch (mon) {
            case "Jan":
                return 0;
            case "Feb":
                return 1;
            case "Mar":
                return 2;
            case "Apr":
                return 3;
            case "May":
                return 4;
            case "Jun":
                return 5;
            case "Jul":
                return 6;
            case "Aug":
                return 7;
            case "Sep":
                return 8;
            case "Oct":
                return 9;
            case "Nov":
                return 10;
            case "Dec":
                return 11;
        }
        return x;
    }

    private void init(final Context context) {

        int start = 0, end = 12;

        /*if (Content.IsToMonth) {
            if (Content.calendar.get(Calendar.YEAR) == DatePickerDialog.TempCalendar.get(Calendar.YEAR)) {
                start = Content.calendar.get(Calendar.MONTH);
                end = 12;
            } else if (Content.calendar.get(Calendar.YEAR) < DatePickerDialog.TempCalendar.get(Calendar.YEAR)) {
                start = 0;
                end = 12;
            } else if (Content.calendar.get(Calendar.YEAR) > DatePickerDialog.TempCalendar.get(Calendar.YEAR)) {
                start = 0;
                end = 0;
            }
        }*/

        years.clear();
        for (int year = start; year <= end; year++) {

            switch (year) {
                case 0:
                    years.add("Jan");
                    break;
                case 1:
                    years.add("Feb");
                    break;
                case 2:
                    years.add("Mar");
                    break;
                case 3:
                    years.add("Apr");
                    break;
                case 4:
                    years.add("May");
                    break;
                case 5:
                    years.add("Jun");
                    break;
                case 6:
                    years.add("Jul");
                    break;
                case 7:
                    years.add("Aug");
                    break;
                case 8:
                    years.add("Sep");
                    break;
                case 9:
                    years.add("Oct");
                    break;
                case 10:
                    years.add("Nov");
                    break;
                case 11:
                    years.add("Dec");
                    break;
            }
        }

        mAdapter = new YearAdapter(context, R.layout.mdtp_year_label_text_view, years);
        setAdapter(mAdapter);

        /*if(Content.IsToMonth){
            if (Content.calendar.get(Calendar.YEAR) == DatePickerDialog.TempCalendar.get(Calendar.YEAR)) {
                for(int i=0;i<mAdapter.getCount();i++){
                    if(Content.calendar.get(Calendar.MONTH)<i){
                        mAdapter.getItem(i).setAlpha(0.5f);
                        ////Log.e("i", "--" + i);
                    }
                }
            }
        }*/

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mController.tryVibrate();
        if (Content.IsComplicatedFlagMonth) {
            if (Content.calendar.get(Calendar.YEAR) > DatePickerDialog.TempCalendar.get(Calendar.YEAR)) {
                TextViewWithCircularIndicator clickedView = (TextViewWithCircularIndicator) view;
                if (clickedView != null) {
                    if (clickedView != mSelectedView) {
                        if (mSelectedView != null) {
                            mSelectedView.drawIndicator(false);
                            mSelectedView.requestLayout();
                        }
                        clickedView.drawIndicator(true);
                        clickedView.requestLayout();
                        mSelectedView = clickedView;
                    }
                    mController.onMonthSelected(getYearFromTextView(clickedView));
                    mAdapter.notifyDataSetChanged();
                }
                return;
            }
            if (Content.calendar.get(Calendar.YEAR) == DatePickerDialog.TempCalendar.get(Calendar.YEAR)) {
                if (Content.calendar.get(Calendar.MONTH) >= position) {
                    TextViewWithCircularIndicator clickedView = (TextViewWithCircularIndicator) view;
                    if (clickedView != null) {
                        if (clickedView != mSelectedView) {
                            if (mSelectedView != null) {
                                mSelectedView.drawIndicator(false);
                                mSelectedView.requestLayout();
                            }
                            clickedView.drawIndicator(true);
                            clickedView.requestLayout();
                            mSelectedView = clickedView;
                        }
                        mController.onMonthSelected(getYearFromTextView(clickedView));
                        mAdapter.notifyDataSetChanged();
                    }
                }
                return;
            }
        }
        if (Content.IsToMonth) {
            if (Content.calendar.get(Calendar.YEAR) < DatePickerDialog.TempCalendar.get(Calendar.YEAR)) {
                TextViewWithCircularIndicator clickedView = (TextViewWithCircularIndicator) view;
                if (clickedView != null) {
                    if (clickedView != mSelectedView) {
                        if (mSelectedView != null) {
                            mSelectedView.drawIndicator(false);
                            mSelectedView.requestLayout();
                        }
                        clickedView.drawIndicator(true);
                        clickedView.requestLayout();
                        mSelectedView = clickedView;
                    }
                    mController.onMonthSelected(getYearFromTextView(clickedView));
                    mAdapter.notifyDataSetChanged();
                }
                return;
            }

            if (Content.calendar.get(Calendar.YEAR) == DatePickerDialog.TempCalendar.get(Calendar.YEAR)) {
                if (Content.calendar.get(Calendar.MONTH) <= position) {
                    TextViewWithCircularIndicator clickedView = (TextViewWithCircularIndicator) view;
                    if (clickedView != null) {
                        if (clickedView != mSelectedView) {
                            if (mSelectedView != null) {
                                mSelectedView.drawIndicator(false);
                                mSelectedView.requestLayout();
                            }
                            clickedView.drawIndicator(true);
                            clickedView.requestLayout();
                            mSelectedView = clickedView;
                        }
                        mController.onMonthSelected(getYearFromTextView(clickedView));
                        mAdapter.notifyDataSetChanged();
                    }
                }
                return;
            }
        } else {
            TextViewWithCircularIndicator clickedView = (TextViewWithCircularIndicator) view;
            if (clickedView != null) {
                if (clickedView != mSelectedView) {
                    if (mSelectedView != null) {
                        mSelectedView.drawIndicator(false);
                        mSelectedView.requestLayout();
                    }
                    clickedView.drawIndicator(true);
                    clickedView.requestLayout();
                    mSelectedView = clickedView;
                }
                mController.onMonthSelected(getYearFromTextView(clickedView));
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    public void postSetSelectionCentered(final int position) {
        postSetSelectionFromTop(position, mViewSize / 2 - mChildSize / 2);
    }

    public void postSetSelectionFromTop(final int position, final int offset) {
        post(new Runnable() {

            @Override
            public void run() {
                setSelectionFromTop(position, offset);
                requestLayout();
            }
        });
    }

    public int getFirstPositionOffset() {
        final View firstChild = getChildAt(0);
        if (firstChild == null) {
            return 0;
        }
        return firstChild.getTop();
    }

    @Override
    public void onDateChanged() {
        mAdapter.notifyDataSetChanged();
        postSetSelectionCentered(mController.getSelectedDay().month);
    }

    @Override
    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        super.onInitializeAccessibilityEvent(event);
        if (event.getEventType() == AccessibilityEvent.TYPE_VIEW_SCROLLED) {
            event.setFromIndex(0);
            event.setToIndex(0);
        }
    }

    public class YearAdapter extends ArrayAdapter<String> {


        public YearAdapter(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            TextViewWithCircularIndicator v = (TextViewWithCircularIndicator)
                    super.getView(position, convertView, parent);
            v.setAccentColor(mController.getAccentColor(), mController.isThemeDark());
            v.requestLayout();
            int year = getYearFromTextView(v);
            boolean selected = mController.getSelectedDay().month == year;
            v.drawIndicator(selected);
            if (selected) {
                mSelectedView = v;
            }
            if (Content.IsComplicatedFlagMonth) {
                if (Content.calendar.get(Calendar.YEAR) == DatePickerDialog.TempCalendar.get(Calendar.YEAR)) {
                    if (Content.calendar.get(Calendar.MONTH) < function(v.getText().toString())) {
                        v.setAlpha(0.5f);
                    } else {
                        v.setAlpha(1.0f);
                    }
                } else {
                    v.setAlpha(1.0f);
                }
            } else if (Content.IsToMonth) {
                if (Content.calendar.get(Calendar.YEAR) == DatePickerDialog.TempCalendar.get(Calendar.YEAR)) {
                    if (Content.calendar.get(Calendar.MONTH) <= function(v.getText().toString())) {
                        v.setAlpha(1.0f);
                        //Log.e("position", "--" + position);
                        //Log.e("Content.calendar.get(Calendar.MONTH)", "--" + Content.calendar.get(Calendar.MONTH));
                    } else {
                        v.setAlpha(0.5f);
                    }
                } else {
                    v.setAlpha(1.0f);
                }
            } else {
                v.setAlpha(1.0f);
            }


            return v;
        }
    }

    /*public class YearAdapter extends BaseAdapter{

        List<String> objects;

        public YearAdapter(Context context, int resource, List<String> objects) {

            //super(context, resource, objects);
            this.objects = objects;
            //Log.e("objects.size()", "--" + objects.size());
        }

        @Override
        public int getCount() {
            return objects.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //Log.e("position", "--" + position);
            *//*TextViewWithCircularIndicator v = (TextViewWithCircularIndicator)
                    super.getView(position, convertView, parent);*//*
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = vi.inflate(R.layout.mdtp_year_label_text_view, null);
            TextViewWithCircularIndicator vt =(TextViewWithCircularIndicator) v.findViewById(R.id.month_text_view);
            vt.setText(objects.get(position));
            vt.setAccentColor(mController.getAccentColor(), mController.isThemeDark());
            vt.requestLayout();
            int year = getYearFromTextView(vt);
            boolean selected = mController.getSelectedDay().month == year;
            vt.drawIndicator(selected);
            if (selected) {
                mSelectedView = vt;
            }

            return vt;
        }
    }*/
}
