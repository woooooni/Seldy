package com.seldy_proj.seldy.acitiviy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

//import com.example.seldy_nawon.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.seldy_proj.seldy.Listeners.OnSwipeTouchListener;
import com.seldy_proj.seldy.fragment.AddDiaryFragement;
import com.seldy_proj.seldy.fragment.DirectMsgFragment;
import com.seldy_proj.seldy.fragment.HomeFragment;
import com.seldy_proj.seldy.fragment.SearchUserFragment;
import com.seldy_proj.seldy.fragment.SnsFragment;
import com.seldy_proj.seldy.util.PreferenceManager;
import com.seldy_proj.seldy.R;
import com.google.firebase.auth.FirebaseAuth;

public class ActivityMain extends AppCompatActivity implements View.OnClickListener {
    private FragmentManager fm;

    private DirectMsgFragment directMsgFragment;
    private AddDiaryFragement addDiaryFragement;
    private HomeFragment homeFragment;
    private SearchUserFragment searchUserFragment;
    private SnsFragment snsFragment;
    private FragmentTransaction transaction;

    private LinearLayout wrap_layout;
    private BottomNavigationView bottomNavigationView;

    FirebaseAuth mAuth;
    Context mContext;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mContext = this;

        fm = getSupportFragmentManager();

        directMsgFragment = new DirectMsgFragment();
        addDiaryFragement = new AddDiaryFragement();
        homeFragment = new HomeFragment();
        searchUserFragment = new SearchUserFragment();
        snsFragment = new SnsFragment();
        initViews();
        transaction = fm.beginTransaction();
        transaction.replace(R.id.mainFrame, homeFragment).commitAllowingStateLoss();

        /////
//        mAuth = FirebaseAuth.getInstance();
        //성공
//        showId.setText(PreferenceManager.getString(mContext,"Nickname"));
//        signout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mAuth.signOut();
//                PreferenceManager.removeKey(mContext, "AutoLogin");
//                PreferenceManager.removeKey(mContext, "Id");
//                PreferenceManager.removeKey(mContext,"Pw");
//
//                Intent intent = new Intent(ActivityMain.this, ActivityLogin.class);
//                startActivity(intent);
//                finish();
//            }
//        });
    }
    private void initViews()
    {
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                transaction = fm.beginTransaction();
                switch (item.getItemId())
                {
                    case R.id.home :
                        transaction.replace(R.id.mainFrame, homeFragment).commit();
                        break;
                    case R.id.searchUser:
                        transaction.replace(R.id.mainFrame, searchUserFragment).commit();
                        break;
                    case R.id.addDiary:
                        transaction.replace(R.id.mainFrame, addDiaryFragement).commit();
                        break;
                    case R.id.directMsg:
                        transaction.replace(R.id.mainFrame, directMsgFragment).commit();
                        break;
                    case R.id.sns:
                        transaction.replace(R.id.mainFrame, snsFragment).commit();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        wrap_layout = findViewById(R.id.wrap_layout);
        wrap_layout.setOnTouchListener(new OnSwipeTouchListener(ActivityMain.this)
        {
            public void onSwipeRight(){
                Toast.makeText(ActivityMain.this, "right", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){

        }
    }
}
