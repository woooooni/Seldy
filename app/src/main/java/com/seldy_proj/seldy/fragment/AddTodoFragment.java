package com.seldy_proj.seldy.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

import static com.seldy_proj.seldy.CalendarZip.CalendarUtils.daysInWeekArray;

@RequiresApi(api = Build.VERSION_CODES.O)
public class AddTodoFragment extends Fragment implements CalendarAdapter.OnItemListener{
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    Context mContext;
    FirebaseFirestore mFirestore;
    EditText userName;
    TextView monthYearText;
    TextView selectday;
    public YearMonth yearMonth;
    private DrawerLayout drawerLayout;
    private View drawView;
    private ViewGroup viewGroup;
    private RelativeLayout mainrelativeLayout;
    private FragmentManager fm;
    private FragmentTransaction transaction;
    private HomeFragment homeFragment;
    private RecyclerView calendarRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        viewGroup = (ViewGroup)inflater.inflate(R.layout.fragment_add_todo, container, false);

        mainrelativeLayout = (RelativeLayout)viewGroup.findViewById(R.id.MainRelativeLayout);
        drawerLayout = (DrawerLayout) viewGroup.findViewById(R.id.drawer_layout);
        drawView = (View) viewGroup.findViewById(R.id.drawer);
        calendarRecyclerView = viewGroup.findViewById(R.id.calendarRecyclerView1);
        fm = getActivity().getSupportFragmentManager();

        transaction = fm.beginTransaction();

        userName = viewGroup.findViewById(R.id.user_name_et);

        Bundle bundle = getArguments();
        selectday = viewGroup.findViewById(R.id.select_day_tx);
        selectday.setText(bundle.getString("selectday"));
        mContext = getContext();
        initSetting();
        initrecycle();

        setWeekView();
        DataBase();
        return viewGroup;
    }
    //버튼 정리
    private void initSetting() {
        //뒤로가기
        ImageButton back = (ImageButton)viewGroup.findViewById(R.id.back1);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fm = getActivity().getSupportFragmentManager();
                transaction = fm.beginTransaction();
                homeFragment = new HomeFragment();
                transaction.replace(R.id.mainFrame, homeFragment).commit();
            }
        });
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

        //소개글
        TextView intro_tx = (TextView)viewGroup.findViewById(R.id.intro_tx);
        intro_tx.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final EditText edit_intro = new EditText(getContext());
                edit_intro.setMaxLines(3);
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                alertDialog.setTitle("소개글");
                alertDialog.setView(edit_intro);
                alertDialog.setPositiveButton("입력", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        intro_tx.setText(edit_intro.getText().toString());
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                alertDialog.show();
                return true;
            }
        });
        //메모추가
        ImageButton add_todolist = (ImageButton) viewGroup.findViewById(R.id.addTodolist_Imgbtn);
        add_todolist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //이전
        ImageView calendar_previous = (ImageView)viewGroup.findViewById(R.id.iv_calendar_previous);
        calendar_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previousWeekAction(viewGroup);
            }
        });
        //다음
        ImageView calendar_next = (ImageView)viewGroup.findViewById(R.id.iv_calendar_next);
        calendar_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextWeekAction(viewGroup);
            }
        });
    }
    //데이터 베이스
    private void DataBase(){
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
    }

    //리사이클 뷰들 정리
    private void initrecycle(){
        //캘린더 리사이클 뷰 정리

        monthYearText = viewGroup.findViewById(R.id.tv_date_month);

    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setWeekView() {
        monthYearText.setText(CalendarUtils.monthYearFromDate(CalendarUtils.selectedDate));
        String context = monthYearText.getText().toString();

        int start = 0;
        int end = start + context.length();

        SpannableString spannableString = new SpannableString(context);
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#374042")), 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new RelativeSizeSpan(1.3f), start, 5, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new RelativeSizeSpan(0.9f), 5, end, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        monthYearText.setText(spannableString);
        ArrayList<String> days = daysInWeekArray(CalendarUtils.selectedDate);
        CalendarAdapter calendarAdapter = new CalendarAdapter(days, this);
        //list 값 전달

//        RecyclerView.LayoutManager manager = new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.HORIZONTAL);
////        RecyclerView.LayoutManager manager = new GridLayoutManager(getContext(),7);
//        calendarRecyclerView.setLayoutManager(manager);
        calendarRecyclerView.setAdapter(calendarAdapter);
        calendarRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        calendarAdapter.notifyDataSetChanged();
    }
    @SuppressLint("NewApi")
    public void previousWeekAction(View view){
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusWeeks(1);
        setWeekView();
    }
    //다음 버튼 next
    @SuppressLint("NewApi")
    public void nextWeekAction(View view){
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusWeeks(1);
        setWeekView();
    }
    @Override
    public void onItemClick(int position, String days) {
        Log.d("days",LocalDate.parse(days).toString());
        CalendarUtils.selectedDate = LocalDate.parse(days);
        Toast.makeText(getContext(),CalendarUtils.selectedDate.toString(),Toast.LENGTH_SHORT).show();
        selectday.setText(days.substring(8,10));
        setWeekView();
    }
}