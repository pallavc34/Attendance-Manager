package com.example.raj.uploadexcel;

import android.app.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.example.raj.uploadexcel.mMySql.Downloader;
import com.example.raj.uploadexcel.mMySql.Downloader2;
import com.example.raj.uploadexcel.mMySql.Downloader3;

import android.util.Log;
import android.view.View;


import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.example.raj.uploadexcel.mMySql.DataParser.Position;

import static com.example.raj.uploadexcel.register.department;


public class allot extends Activity implements AdapterView.OnItemSelectedListener {



    public final static String urlAddress2 = "http://androidattend.000webhostapp.com/subjectt.php";
    public final static String urlAddress3 = "http://androidattend.000webhostapp.com/retrieve_id.php";
    public static String selectedDepartment, selectedSubject;
    String type;
    String message;
    Button btn;
    Spinner sp2, spin_depart;
    public static String TAG = "Content";
    public TextView txtTeacher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        setTitle("Subject Allocation");

        txtTeacher = findViewById(R.id.txt_teacher);

        sp2 = findViewById(R.id.sp2);
        spin_depart = findViewById(R.id.spin_dept);

        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, department);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spin_depart.setAdapter(aa);

        new Downloader2(allot.this, urlAddress2, sp2).execute();
        Log.v(TAG,"uu"+ urlAddress2);

        btn = findViewById(R.id.btnn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               selectedSubject= sp2.getSelectedItem().toString();
                Log.v(TAG,"sub"+ selectedSubject);
                Intent i = new Intent(allot.this, allocation.class);
                startActivity(i);
            }
        });

        spin_depart.setOnItemSelectedListener(this);


    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        selectedDepartment = department[i];
        Log.v(TAG,"sub"+ selectedDepartment);

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {


    }


}












