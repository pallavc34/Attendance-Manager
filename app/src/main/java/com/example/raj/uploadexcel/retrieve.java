package com.example.raj.uploadexcel;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.raj.uploadexcel.mMySql.retrieve_sub;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.example.raj.uploadexcel.mMySql.dataparser4.selected_sub;
import static com.example.raj.uploadexcel.select_sub.urlAddress;


public class retrieve extends Activity {

    public int sem = 1;
    public String dept = "Information Technology";
    String type, message;
    private static String TAG = "ReadActivity";
    Button btn_submit;
    Spinner sp;
    TextView currentDate;
    TextView initialDate;
    public static int currentdatemonth;
    public static int getCurrentdateyear;
    public static int getCurrentday;
Uri uri;
public  String fileName  = "Monthly_Report_"+currentdatemonth+"_"+getCurrentdateyear+"_"+selected_sub+".xls";
//raj
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.read);
        setTitle("Subject Allocation");

        sp = findViewById(R.id.sub);
        currentDate = findViewById(R.id.currentdateTxt);
        initialDate = findViewById(R.id.initialDateTxt);

        Calendar c = Calendar.getInstance();
        currentdatemonth = c.get(Calendar.MONTH);
        getCurrentdateyear = c.get(Calendar.YEAR);
        getCurrentday = c.get(Calendar.DAY_OF_MONTH);

        initialDate.setText("1/" + currentdatemonth + "/" + getCurrentdateyear);
        currentDate.setText(getCurrentday + "/" + currentdatemonth + "/" + getCurrentdateyear);


        new retrieve_sub(retrieve.this, urlAddress, sp).execute();

        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (selected_sub){
                    case "I.P":
                         uri = Uri.parse("http://androidattend.000webhostapp.com/create_excel_ip.php");
                         Log.v(TAG, "hii"+uri.toString());
                        break;

                    case "Android":
                         uri = Uri.parse("http://androidattend.000webhostapp.com/create_excel_android.php");
                        Log.v(TAG, uri.toString());
                        break;
                    case "CGVR":
                         uri = Uri.parse("http://androidattend.000webhostapp.com/create_excel_cgvr.php");
                        Log.v(TAG, uri.toString());
                        break;
                    default:
                         uri = Uri.parse("http://androidattend.000webhostapp.com/create_excel.php");
                        Log.v(TAG, uri.toString());
                        break;

            }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
                        requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                        File direct  = new File(Environment.getExternalStorageDirectory()+"/Monthly_report");
                        if(!direct.exists()){
                            direct.mkdirs();

                        }
                        DownloadManager dm = (DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);
                        DownloadManager.Request request = new DownloadManager.Request(uri);
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        request.setDestinationInExternalFilesDir(retrieve.this,"/Monthly_report" , fileName);
                        dm.enqueue(request);
                    }else{
                        Toast.makeText(getBaseContext(), "Provide read and write permission.",Toast.LENGTH_SHORT).show();

                    }
                }


            }
        });
    }




}