package com.example.raj.uploadexcel;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import static android.content.ContentValues.TAG;

public class login extends Activity {
    Button btn_login;
    Button btn_link_signup;
    EditText txt_email;
    EditText txt_pass;
    String email="";
    String pass="";
    String type;
    String message="";
    public static String designation;
    public static String department;
    public static String emp_id;

    //Checking network state
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Login");
        //Code for Auto Login
        message = PreferenceManager.getDefaultSharedPreferences(this).getString("email", "");
        String pref = PreferenceManager.getDefaultSharedPreferences(this).getString("pref", "");
        if (!message.equals("")) {
            if (pref.equals("hod")) {
                hod();
            } else if (pref.equals("lect")) {
                lect();
            }
        }

        btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt_email = (EditText) findViewById(R.id.login_input_email);
                String email = txt_email.getText().toString();
                txt_pass = findViewById(R.id.login_input_password);
                String pass = txt_pass.getText().toString();
                if (!isNetworkAvailable()) {
                    Toast.makeText(login.this, "Please enable mobile data", Toast.LENGTH_SHORT).show();
                } else if (email.equals("") || pass.equals("")) {
                    Toast.makeText(login.this, "Please fill email and password", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        new PostDataAsyncTask().execute();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        btn_link_signup =  findViewById(R.id.btn_link_signup);
        btn_link_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent myintent = new Intent(getBaseContext(), register.class);
            startActivity(myintent);
            }
        });
    }

    public class PostDataAsyncTask extends AsyncTask<String, String, String> {

        ProgressDialog pdLoading = new ProgressDialog(login.this);



        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }

        @Override
        protected String doInBackground(String... strings) {
            String response = null;
            try {
                txt_email = (EditText) findViewById(R.id.login_input_email);
                String email = txt_email.getText().toString();
                txt_pass = findViewById(R.id.login_input_password);
                String pass = txt_pass.getText().toString();
                // url where the data will be posted
                URL url = new URL("http://androidattend.000webhostapp.com/login.php");
                Log.v(TAG, "postURL: " + url);

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");

                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                urlConnection.setRequestProperty("Content-Type", "text/plain");

                // Send the post body
                if (email != null && pass != null) {
                    OutputStream writer = urlConnection.getOutputStream();

                    JSONObject jObject = new JSONObject();
                    jObject.put("email", email.toString());
                    jObject.put("pass", pass.toString());

                    String data = jObject.toString();

                    //byte[] data = email.getBytes();
                    writer.write(data.getBytes());
                    Log.v(TAG, "hhh" + data);

                    writer.flush();
                }
                int statusCode = urlConnection.getResponseCode();

                if (statusCode == 200) {

                    InputStream inputStream = urlConnection.getErrorStream();

                    if (inputStream == null) //If inputStream is null here, no error has occured.
                        inputStream = urlConnection.getInputStream();
                    //InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());

                    response = convertInputStreamToString(inputStream);
                    Log.v(TAG, "hhh" + response);
                    JSONObject jObject1 = new JSONObject(response);
                    type = (jObject1.getString("error"));
                    message = (jObject1.getString("message"));
                    designation = (jObject1.getString("designation"));
                    department = (jObject1.getString("department"));
                    emp_id = (jObject1.getString("emp_id"));
                    Log.v(TAG, "type" + type);
                    Log.v(TAG, "message" + message);
                    Log.v(TAG, "designation" + designation);
                    Log.v(TAG, "designation" + department);
                    Log.v(TAG, "emp_id" + emp_id);




                } else {
                    // Status code is not 200
                    // Do something to handle the error
                }


            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return response.toString();
        }



        @Override
        protected void onPostExecute (String response){
            pdLoading.dismiss();

            if (type.equalsIgnoreCase("false")) {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                if(designation.equalsIgnoreCase("H.O.D"))
                {
                    PreferenceManager.getDefaultSharedPreferences(login.this).edit().putString("email", email).apply();
                    PreferenceManager.getDefaultSharedPreferences(login.this).edit().putString("pref", "hod").apply();
                    Intent myIntent = new Intent(getBaseContext(),mainMenu_h.class);
                    startActivity(myIntent);
                    finish();
                }else if(designation.equalsIgnoreCase("Lecturer")){
                    PreferenceManager.getDefaultSharedPreferences(login.this).edit().putString("email", email).apply();
                    PreferenceManager.getDefaultSharedPreferences(login.this).edit().putString("pref", "lect").apply();
                    Intent myIntent = new Intent(getBaseContext(),mainMenu_l.class);
                    startActivity(myIntent);
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                }





            } else {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }

        }


    }
    // this will post our text data
    private void postText(){



    }
    private String convertInputStreamToString(InputStream inputStream) {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
    public void hod(){
        Intent myIntent = new Intent(getBaseContext(),mainMenu_h.class);
        startActivity(myIntent);
    }
    public void lect(){
        Intent myIntent = new Intent(getBaseContext(),mainMenu_l.class);
        startActivity(myIntent);
    }


}





