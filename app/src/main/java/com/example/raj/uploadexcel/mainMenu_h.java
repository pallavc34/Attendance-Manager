package com.example.raj.uploadexcel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class mainMenu_h extends Activity {

   Button  attend;
   Button  allot;
    Button  report;
    Button  logout;



   @Override
    protected void onCreate(Bundle savedInstanceState)
   {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.main_menu_h);
       setTitle("Attendance Menu");


       attend = findViewById(R.id.btn_attend);
       allot = findViewById(R.id.btn_allot);
       report = findViewById(R.id.btn_report);
       logout = findViewById(R.id.btn_logout);

       attend.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               Intent myintent  = new Intent(getBaseContext(), select_sub.class);
               startActivity(myintent);


           }
       });

       allot.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               Intent myintent  = new Intent(getBaseContext(), allot.class);
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
               PreferenceManager.getDefaultSharedPreferences(mainMenu_h.this).edit().putString("email","").apply();
               Intent int123 = new Intent(mainMenu_h.this,login.class);
               startActivity(int123);
               finish();
           }
       });



   }
}
