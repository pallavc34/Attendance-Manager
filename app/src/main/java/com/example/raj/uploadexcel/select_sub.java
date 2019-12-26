package com.example.raj.uploadexcel;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Spinner;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.raj.uploadexcel.mMySql.Downloader;
import com.example.raj.uploadexcel.mMySql.retrieve_sub;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static android.content.ContentValues.TAG;
import static com.example.raj.uploadexcel.mMySql.dataparser4.selected_sub;

public class select_sub  extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

        String type,message;
        public static String sem,dept;
        Spinner sp,sp1;
        String type1,message1;
        Context context;
        public static String TAG = "Content";

         String department;
         public static String currentDateString;
         public static String time;

    final static String urlAddress ="http://androidattend.000webhostapp.com/retrieve_teacher_sub.php";
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.select_sub);
            setTitle("Select Subject");
           sp = findViewById(R.id.spinner2);
           sp1 = findViewById(R.id.spinner1);
           time = sp1.getSelectedItem().toString();
           Log.v(TAG, "time"+time);

            Button submit_btn = findViewById(R.id.btnSubmit);
            submit_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        new select_sub.PostDataAsyncTask().execute();


                    }catch(Exception e){
                        e.printStackTrace();

                    }

                }
            });



          new retrieve_sub(select_sub.this, urlAddress, sp).execute();



            Button button = (Button) findViewById(R.id.button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogFragment datePicker = new DatePickerFragment();
                    datePicker.show(getSupportFragmentManager(), "date picker");
                }
            });
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            Calendar c = Calendar.getInstance();
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, month);
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            SimpleDateFormat sdf = new SimpleDateFormat("YYYY/MM/dd");
            currentDateString = sdf.format(c.getTime());
            Log.v("Date",currentDateString);
            TextView textView = (TextView) findViewById(R.id.textView);
            textView.setText(currentDateString);
        }


    public class PostDataAsyncTask extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(select_sub.this);

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
                URL url = new URL("http://androidattend.000webhostapp.com/retrieve_roll.php");
                Log.v(TAG, "postURL: " + url);

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");


                Log.v(TAG, "sss" + selected_sub);

                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                urlConnection.setRequestProperty("Content-Type", "text/plain");

                // Send the post body
                if (selected_sub != null) {
                    OutputStream writer = urlConnection.getOutputStream();

                    JSONObject jObject = new JSONObject();
                    jObject.put("subject", selected_sub);


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
                    dept = (jObject1.getString("dept"));
                    sem = (jObject1.getString("sem"));
                    //designation = (jObject1.getString("designation"));
                    Log.v(TAG, "type" + type);
                    Log.v(TAG, "message" + message);
                    Log.v(TAG, "message" + sem);
                    Log.v(TAG, "message" + dept);
                    //Log.v(TAG, "designation"+designation);


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
            if (type.equalsIgnoreCase("false")) {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                Intent myIntent = new Intent(getBaseContext(),attendence.class);
                myIntent.putExtra("message",message);
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
