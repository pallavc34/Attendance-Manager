package com.example.raj.uploadexcel;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.raj.uploadexcel.mMySql.Downloader;
import com.example.raj.uploadexcel.mMySql.Downloader2;
import com.example.raj.uploadexcel.mMySql.Downloader3;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.example.raj.uploadexcel.allot.TAG;
import static com.example.raj.uploadexcel.allot.selectedDepartment;
import static com.example.raj.uploadexcel.allot.selectedSubject;
import static com.example.raj.uploadexcel.allot.urlAddress3;
import static com.example.raj.uploadexcel.login.department;
import static com.example.raj.uploadexcel.login.designation;
import static com.example.raj.uploadexcel.mMySql.DataParser.Position;
import static com.example.raj.uploadexcel.mMySql.DataParser3.emp_id;

public class allocation extends Activity {

    Spinner sp;
    String selectedTeacher, type, message;
    TextView txtSubject, txtDepartment;
    Button btnSubmit;
    public final static String urlAddress = "http://androidattend.000webhostapp.com/connect.php";


    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.allot_teacher);
        setTitle("Subject Allocation");

        txtSubject = findViewById(R.id.txt_subject);
        txtDepartment = findViewById(R.id.txt_department);
        txtSubject.setText(selectedSubject);
        txtDepartment.setText(selectedDepartment);

        sp=findViewById(R.id.sp);

        new Downloader3(allocation.this, urlAddress3).execute();
        Log.v(TAG,"uuu"+ urlAddress3);

        new Downloader(allocation.this, urlAddress, sp).execute();

        btnSubmit = findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new allocation.PostDataAsyncTask().execute();
            }
        });


    }

    public class PostDataAsyncTask extends AsyncTask<String, String, String> {

        ProgressDialog pdLoading = new ProgressDialog(allocation.this);
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
                // url where the data will be posted

                URL url = new URL("http://androidattend.000webhostapp.com/allocation.php");
                Log.v(TAG, "postURL: " + url);

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");

                selectedTeacher = sp.getSelectedItem().toString();


                int selected_emp_id = emp_id[Position];
                Log.v("Selected emp id", "" + selected_emp_id);

                Log.v(TAG, "spinn1" + selectedTeacher);

                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                urlConnection.setRequestProperty("Content-Type", "text/plain");

                // Send the post body
                if (selectedTeacher != null && selectedDepartment != null && selectedSubject!= null) {
                    OutputStream writer = urlConnection.getOutputStream();

                    JSONObject jObject = new JSONObject();
                    jObject.put("Teacher", selectedTeacher);
                    jObject.put("Subject", selectedSubject);
                    jObject.put("department", selectedDepartment);
                    jObject.put("emp_id", selected_emp_id);

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
            String result = response;
            pdLoading.dismiss();

            if (type.equalsIgnoreCase("tru")) {
                Toast.makeText(allocation.this, message, Toast.LENGTH_SHORT).show();
                Intent i = new Intent(allocation.this, allot.class);
                startActivity(i);
            } else {
                Toast.makeText(allocation.this, message, Toast.LENGTH_SHORT).show();
                Intent i = new Intent(allocation.this, Main_menu.class);
                i.putExtra("key", designation);
                startActivity(i);
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
