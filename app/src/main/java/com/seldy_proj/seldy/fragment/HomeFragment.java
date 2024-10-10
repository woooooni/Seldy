package com.seldy_proj.seldy.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.seldy_proj.seldy.CalendarZip.CalendarAdapter;
import com.seldy_proj.seldy.CalendarZip.CalendarUtils;
import com.seldy_proj.seldy.R;
import com.seldy_proj.seldy.acitiviy.ActivityLogin;
import com.seldy_proj.seldy.acitiviy.AppVersionActivity;
import com.seldy_proj.seldy.util.PreferenceManager;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;

public class HomeFragment extends Fragment implements CalendarAdapter.OnItemListener{

    //파이어베이스
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    Context mContext;
    private DatabaseReference mDatabase;
    FirebaseFirestore mFirestore;
    //설정버튼
    private DrawerLayout drawerLayout;
    private View drawView;
    private ViewGroup viewGroup;
    private RelativeLayout mainrelativeLayout;
    //캘린더
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    //이동 프레그먼트
    private FragmentManager fm;
    private FragmentTransaction transaction;
    private AddTodoFragment addTodoFragement;
    private CalendarAdapter calendarAdapter;

    TextView userName;


    //
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);

        mainrelativeLayout = (RelativeLayout)viewGroup.findViewById(R.id.MainRelativeLayout);
        drawerLayout = (DrawerLayout) viewGroup.findViewById(R.id.drawer_layout);
        drawView = (View) viewGroup.findViewById(R.id.drawer);

        fm = getActivity().getSupportFragmentManager();
        transaction = fm.beginTransaction();
        userName = viewGroup.findViewById(R.id.user_name);
        mFirestore = FirebaseFirestore.getInstance();
        DocumentReference docRef = mFirestore.collection("member").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        Log.d("log",document.getData().get("nickname").toString());
                        userName.setText(document.getData().get("nickname").toString());
                    }
                    else{
                        Log.d("log","찾지 못함");
                    }
                }else{
                    Log.d("log","실패",task.getException());
                }
            }
        });
        mContext = getContext();

        //String nickname = mDatabase.child(user.getUid()).get;
        //설정 기능 완료
        initSetting();
        setMonthView();
        return viewGroup;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setMonthView() {
        monthYearText.setText(CalendarUtils.monthYearFromDate(CalendarUtils.selectedDate));
        String context = monthYearText.getText().toString();
        ArrayList<String> dayInMonth = CalendarUtils.daysInMonthArray(CalendarUtils.selectedDate);

        int start = 0;
        int end = start + context.length();

        SpannableString spannableString = new SpannableString(context);
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#374042")), 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new RelativeSizeSpan(1.3f), start, 5, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new RelativeSizeSpan(0.9f), 5, end, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        monthYearText.setText(spannableString);

        calendarAdapter = new CalendarAdapter(dayInMonth, this);
        //list 값 전달
        RecyclerView.LayoutManager manager = new GridLayoutManager(getContext(),7);
        calendarRecyclerView.setAdapter(calendarAdapter);
        calendarRecyclerView.setLayoutManager(manager);
    }
    //이전 버튼 back
    @SuppressLint("NewApi")
    public void previousMonthAction(View view){
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusMonths(1);
        setMonthView();
    }

    //다음 버튼 next
    @SuppressLint("NewApi")
    public void nextMonthAction(View view){
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusMonths(1);
        setMonthView();
    }

    //설정화면에 버튼
    private void initSetting() {
        // 설정 열기
        ImageButton setting_open = (ImageButton) viewGroup.findViewById(R.id.setting_open);
        setting_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(drawView);
                mainrelativeLayout.setClickable(false);
                mainrelativeLayout.setEnabled(false);
            }
        });

        // 설정 닫기
        TextView setting_close = (TextView) viewGroup.findViewById(R.id.setting_close);
        setting_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawers();
                mainrelativeLayout.setVisibility(View.VISIBLE);
            }
        });

        // 프로필 설정
        Button profile_setting = (Button) viewGroup.findViewById(R.id.profile_setting_btn);
        profile_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //프로필 설정 페이지
                Toast.makeText(getActivity(), "프로필 페이지", Toast.LENGTH_SHORT).show();
            }
        });

        //테마 폰트
        Button tema_font = (Button) viewGroup.findViewById(R.id.tema_font_btn);
        tema_font.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //테마 폰트 페이지
                Toast.makeText(getActivity(), "테마 폰트 페이지", Toast.LENGTH_SHORT).show();
            }
        });

        //회원 탈퇴
        Button withdrawal = (Button) viewGroup.findViewById(R.id.withdrawal_btn);
        withdrawal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //회원 탈퇴 페이지
                Toast.makeText(getActivity(), "회원 탈퇴 페이지", Toast.LENGTH_SHORT).show();

            }
        });
        //친구 관리
        Button friend_management = (Button) viewGroup.findViewById(R.id.friend_management_btn);
        friend_management.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //친구 관리 페이지
                Toast.makeText(getActivity(), "친구 관리 페이지", Toast.LENGTH_SHORT).show();
            }
        });
        //계정 관리
        Button account_management = (Button) viewGroup.findViewById(R.id.account_management_btn);
        account_management.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //계정 관리 페이지
                Toast.makeText(getActivity(), "계정 관리", Toast.LENGTH_SHORT).show();
            }
        });
        //로그 아웃
        Button logout = (Button) viewGroup.findViewById(R.id.logout_btn);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //로그 아웃후 로그인 페이지로 이동
                Toast.makeText(getActivity(), "로그아웃", Toast.LENGTH_SHORT).show();
                mAuth.signOut();
                PreferenceManager.removeKey(mContext, "AutoLogin");
                PreferenceManager.removeKey(mContext, "Id");
                PreferenceManager.removeKey(mContext,"Pw");
                Intent intent = new Intent(getActivity(), ActivityLogin.class);
                startActivity(intent);
            }
        });
        //앱 버전
        Button app_version = (Button) viewGroup.findViewById(R.id.app_version_btn);
        app_version.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AppVersionActivity.class);
                startActivity(intent);
                Toast.makeText(getActivity(), "앱 버전 페이지", Toast.LENGTH_SHORT).show();
            }
        });

        //문의
        Button question_btn = (Button) viewGroup.findViewById(R.id.question_btn);
        question_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //문의 페이지
                Toast.makeText(getActivity(), "문의 페이지", Toast.LENGTH_SHORT).show();
            }
        });
        //프로필 다시쓰기1 가장 왼쪽 위
        ImageButton rewrite1 = (ImageButton) viewGroup.findViewById(R.id.Rewrite_ibtn1);
        rewrite1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //프로필 다시쓰기 페이지 이동
                Toast.makeText(getContext(), "이벤트 발생", Toast.LENGTH_SHORT).show();
            }
        });
        //캘린더 뷰
        calendarRecyclerView = (RecyclerView) viewGroup.findViewById(R.id.calendarRecyclerView);
        monthYearText = (TextView) viewGroup.findViewById(R.id.monthYearTv);

        ImageButton back = (ImageButton)viewGroup.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previousMonthAction(viewGroup);
            }
        });
        ImageButton next = (ImageButton)viewGroup.findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextMonthAction(viewGroup);
            }
        });
    }
    //달력 클릭 이벤트
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onItemClick(int position, String days) {
        //여기에 intent 연결하면 됨
        if(!days.equals("")){
            String message = CalendarUtils.monthYearFromDate(CalendarUtils.selectedDate)+" "+days;
            String[]aa= message.split(" ");
            String[] months = {"January","February","March","April","May","June","July","August",
                    "September","October","November","December"};
            int a = 0;
            for(int i =0;i<months.length;i++){
                if(aa[1].equals(months[i])){
                    a=i+1;
                    break;
                }
            }
            addTodoFragement = new AddTodoFragment();
            fm = getActivity().getSupportFragmentManager();
            transaction = fm.beginTransaction();
            transaction.replace(R.id.mainFrame, addTodoFragement).commit();
            Log.e("Data",aa[2]);
            Intent intent = new Intent();
            intent.putExtra("selectday",aa[2].substring(8,10));
            Bundle bundle = new Bundle(); bundle.putString("selectday",aa[2].substring(8,10));
            addTodoFragement.setArguments(bundle);
            CalendarUtils.selectedDate = LocalDate.parse(aa[2]);
            Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
            setMonthView();
        }
    }
}

