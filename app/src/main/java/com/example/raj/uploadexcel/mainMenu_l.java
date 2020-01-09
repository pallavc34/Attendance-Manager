package com.example.raj.uploadexcel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class mainMenu_l extends AppCompatActivity {

    Button  attend;
    Button  allot;
    Button  report;
    Button  logout;

    @Override
    public void onBackPressed() {
        //Kill in app
        moveTaskToBack(true);
        finish();
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_menu_l);
        setTitle("Attendance Menu");

        attend = findViewById(R.id.btn_attend);

        report = findViewById(R.id.btn_report);
        logout = findViewById(R.id.btn_logout);

        attend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent myintent  = new Intent(getBaseContext(), select_sub.class);
                startActivity(myintent);


            }
        });


        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent myintent  = new Intent(getBaseContext(), retrieve.class);
                startActivity(myintent);

            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PreferenceManager.getDefaultSharedPreferences(mainMenu_l.this).edit().putString("pref","").apply();
                Intent int123 = new Intent(mainMenu_l.this,login.class);
                startActivity(int123);
                finish();
            }
        });



    }


}
