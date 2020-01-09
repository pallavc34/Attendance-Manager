package com.example.raj.uploadexcel;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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

import static com.example.raj.uploadexcel.login.designation;
import static com.example.raj.uploadexcel.mMySql.dataparser4.selected_sub;
import static com.example.raj.uploadexcel.select_sub.urlAddress;


public class retrieve extends Activity {

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
    AlertDialog.Builder builder;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.read);
        setTitle("Subject Allocation");



        sp = findViewById(R.id.sub);
        currentDate = findViewById(R.id.currentdateTxt);
        initialDate = findViewById(R.id.initialDateTxt);

        Calendar c = Calendar.getInstance();
        currentdatemonth = c.get(Calendar.MONTH) + 1;
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

                if (ContextCompat.checkSelfPermission(retrieve.this, Manifest.permission
                        .READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(retrieve.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                } else {
                    File direct = new File(Environment.getExternalStorageDirectory() + "/Monthly_report");
                    if (!direct.exists()) {
                        direct.mkdirs();

                    }
                    DownloadManager dm = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                    DownloadManager.Request request = new DownloadManager.Request(uri);
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

                    request.setDestinationInExternalFilesDir(retrieve.this, "/Monthly_report", "Monthly_Report_"+currentdatemonth+"_"+getCurrentdateyear+"_"+selected_sub+".xls");

                    dm.enqueue(request);
                }



            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            File direct = new File(Environment.getExternalStorageDirectory() + "/Monthly_report");
            if (!direct.exists()) {
                direct.mkdirs();

            }
            DownloadManager dm = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalFilesDir(retrieve.this, "/Monthly_report", "Monthly_Report_"+currentdatemonth+"_"+getCurrentdateyear+"_"+selected_sub+".xls");

            dm.enqueue(request);
        }
        else{
            builder = new AlertDialog.Builder(this);
            builder.setMessage("Please provide storage access to store report on your device. Click yes to manually allow storage permissions.")
                    .setCancelable(false)
                    .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:"+getPackageName()));
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addCategory(Intent.CATEGORY_DEFAULT);
                            //Uri uri1 = Uri.fromParts("Package",getPackageName(),null);
                            //intent.setData(uri1);
                            startActivity(intent);

                        }
                    })
            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if(designation.equalsIgnoreCase("H.O.D")){
                        Intent in = new Intent(getApplicationContext(),mainMenu_h.class );
                        startActivity(in);
                    }else{
                        Intent in = new Intent(getApplicationContext(),mainMenu_l.class );
                        startActivity(in);
                    }
                }
            });
            AlertDialog alert = builder.create();
            alert.setTitle("Allow Permissions");
            alert.show();

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}