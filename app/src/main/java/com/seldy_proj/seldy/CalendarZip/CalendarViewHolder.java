package com.seldy_proj.seldy.CalendarZip;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.TintableBackgroundView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.seldy_proj.seldy.R;

import java.util.ArrayList;


public class CalendarViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    FirebaseAuth mAuth;
    Context mContext;
    private final ArrayList<String> days;

    public View parentView;
    public final TextView dayOfMonth;
    public final TextView YearMonth;
    public final TextView YearMonthDay;
    public final View calenderCircle;
    public final View calenderCircle1;
    public final View calenderCircle2;
    public TextView textView;
    public String ym;

    //database 설정하기

    private final CalendarAdapter.OnItemListener onItemListener;
    String[] months = {"January","February","March","April","May","June","July","August",
                        "September","October","November","December"};
    @SuppressLint("ResourceType")
    public CalendarViewHolder(@NonNull View itemView, CalendarAdapter.OnItemListener onItemListener, ArrayList<String> days) {
        super(itemView);
        parentView = itemView.findViewById(R.id.parentView);
        dayOfMonth = itemView.findViewById(R.id.cellDayText);
        YearMonth = itemView.findViewById(R.id.monthYearTv);
        YearMonthDay = itemView.findViewById(R.id.cellYearMonthDay_tx);
        calenderCircle2 = itemView.findViewById(R.id.calendarcircle2);
        calenderCircle = itemView.findViewById(R.id.calendarcircle);
        calenderCircle1 = itemView.findViewById(R.id.calendarcircle1);
        this.onItemListener = onItemListener;
        this.days = days;
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
//        if (!dayOfMonth.getText().equals("")) {
//            Intent intent = new Intent();
//            int a = intent.getIntExtra("initWrite", 1);
//            if (a == 0) {
//                calenderCircle.setVisibility(View.GONE);
//
//            } else {
//                calenderCircle.setVisibility(View.VISIBLE);
//            }
//        }
        onItemListener.onItemClick(getAdapterPosition(), days.get(getAdapterPosition()));
    }

}

