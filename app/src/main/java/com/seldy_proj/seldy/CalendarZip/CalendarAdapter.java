package com.seldy_proj.seldy.CalendarZip;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.seldy_proj.seldy.R;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;

@RequiresApi(api = Build.VERSION_CODES.O)
public class CalendarAdapter extends RecyclerView.Adapter<CalendarViewHolder> {
    private final ArrayList<String> days;
    private final OnItemListener onItemListener;
    public YearMonth yearMonth;
    public CalendarViewHolder calendarViewHolder;
    public View parentView;
    String[] months = {"January","February","March","April","May","June","July","August",
            "September","October","November","December"};
    private int selectedPosition = -1;
    LocalDate localDate = LocalDate.now();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    FirebaseFirestore mFirestore;

    public CalendarAdapter(ArrayList<String> days, OnItemListener onItemListener){
        this.days = days;
        this.onItemListener = onItemListener;
    }

    @SuppressLint("ResourceAsColor")
    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.calendar_cell,parent,false);
        mFirestore = FirebaseFirestore.getInstance();
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if(days.size()>15)
            layoutParams.height = (int)(parent.getHeight()*0.15);//homeFragment
        else{
            layoutParams.height = 300;//AddTodoFragement
            layoutParams.width = 250;
        }
        return new CalendarViewHolder(view,onItemListener,days);
    }

    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull  CalendarViewHolder holder, int position) {
        String date = days.get(position).trim();

        if(days.get(position).equals("")){
            holder.dayOfMonth.setText("");
            holder.YearMonthDay.setText("");
        }
        else{
            holder.dayOfMonth.setText(date.substring(8,10));
            holder.YearMonthDay.setText(date);
            if(date.equals(CalendarUtils.selectedDate.toString())){
                holder.calenderCircle1.setVisibility(View.VISIBLE);

            }
            if(date.equals(localDate.toString())){
                holder.dayOfMonth.setTextColor(Color.rgb(241,190,66));
            }
        }
        if(days.size()<=15){
            holder.calenderCircle2.setVisibility(View.VISIBLE);//색
            holder.dayOfMonth.setTextColor(Color.rgb(255,255,255));
        }//데이터 베이스 정보 가져오기
        Log.e("date",holder.YearMonthDay.getText().toString());

        DocumentReference docRef = mFirestore.collection("checkDay").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        int a = document.getData().get("day").toString().length()-1;
                        String[] Saveday = document.getData().get("day").toString().substring(1,a).trim().split(", ");
                        Arrays.sort(Saveday);
                        for(int i =0;i<Saveday.length;i++) {
                            Log.d("log",Saveday[i]);
                            if (Saveday[i].equals(holder.YearMonthDay.getText().toString())) {
                                holder.calenderCircle.setVisibility(View.VISIBLE);//노란색 색칠
                            }
                        }
                    }
                    else{
                        Log.d("log","찾지 못함");
                    }
                }else{
                    Log.d("log","실패",task.getException());
                }
            }
        });
        //일요일
        if(position%7==0){
            holder.dayOfMonth.setTextColor(Color.rgb(241,95,95));
        }
        //토요일
        if(position%7==6){
            holder.dayOfMonth.setTextColor(Color.rgb(67,116,217));
        }


    }

    @Override
    public int getItemCount() {
        return days.size();
    }

    public interface  OnItemListener{
        void onItemClick(int position, String date);
    }
}
