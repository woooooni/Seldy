package com.seldy_proj.seldy.acitiviy;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

//import com.example.seldy_nawon.R;
import com.seldy_proj.seldy.util.PreferenceManager;
import com.seldy_proj.seldy.R;
import com.google.firebase.auth.FirebaseAuth;

public class ActivityMain extends AppCompatActivity {
    Button signout;
    FirebaseAuth mAuth;
    Context mContext;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;
        mAuth = FirebaseAuth.getInstance();

        signout = findViewById(R.id.signout);

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                PreferenceManager.removeKey(mContext, "AutoLogin");
                PreferenceManager.removeKey(mContext, "Id");
                PreferenceManager.removeKey(mContext,"Pw");

                Intent intent = new Intent(ActivityMain.this, ActivityLogin.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
