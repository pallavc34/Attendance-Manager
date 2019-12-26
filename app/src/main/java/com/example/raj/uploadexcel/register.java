package com.example.raj.uploadexcel;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import static android.content.ContentValues.TAG;


public class register extends AppCompatActivity {

    String[] department = { "I.T", "Comps", "Mech", "Electrical","Extc", "Civil", "Auto"};
    String[] designat = {"H.O.D", "Lecturer"};
    Button btn_signup;
    EditText txt_name;
    EditText txt_age;
    EditText txt_email;
    EditText txt_pass;
    String email;
    String pass;
    String name;
    String age;
    String type;
    String message;
    String select_depar;
    String select_design;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle("Register");

        //Getting the instance of Spinner and applying OnItemSelectedListener on it
        Spinner spin = (Spinner) findViewById(R.id.department);

        Spinner designation = (Spinner) findViewById(R.id.designation);
        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,department);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

       ArrayAdapter aa1 = new ArrayAdapter(this,android.R.layout.simple_spinner_item,designat);
        aa1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);
        spin.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                select_depar = department[position];
               // Toast.makeText(MainActivity.this, spinnerDropDownView.getSelectedItem().toString(), Toast.LENGTH_LONG).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

        designation.setAdapter(aa1);
        designation.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                select_design = designat[position];
                // Toast.makeText(MainActivity.this, spinnerDropDownView.getSelectedItem().toString(), Toast.LENGTH_LONG).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

        btn_signup = (Button) findViewById(R.id.btn_signup);
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    new register.PostDataAsyncTask().execute();


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        Button btn_link_login =  findViewById(R.id.btn_link_login);
        btn_link_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myintent = new Intent(getBaseContext(), login.class);
                startActivity(myintent);
            }
        });
    }

    //Performing action onItemSelected and onNothing selected




    public class PostDataAsyncTask extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(register.this);
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
                txt_email = findViewById(R.id.signup_input_email);
                email = txt_email.getText().toString();
                txt_pass = findViewById(R.id.signup_input_password);
                pass = txt_pass.getText().toString();
                txt_name = findViewById(R.id.signup_input_name);
                name = txt_name.getText().toString();

                // url where the data will be posted
                URL url = new URL("http://androidattend.000webhostapp.com/register.php");
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
                    jObject.put("name", name.toString());

                    jObject.put("depart", select_depar);
                    jObject.put("design", select_design);

                    Log.v(TAG, "depart" + select_design);
                    Log.v(TAG, "design" + select_depar);
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
                    Log.v(TAG, "type" + type);
                    Log.v(TAG, "message" + message);




                } else {
                    // Status code is not 200
                    // Do something to handle the error
                }


            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return response;
        }


        @Override
        protected void onPostExecute (String response){
            pdLoading.dismiss();

            if (type.equalsIgnoreCase("false")) {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                Intent myIntent = new Intent(getBaseContext(),upload_image.class);
                myIntent.putExtra("email", email);
                startActivity(myIntent);
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


}





