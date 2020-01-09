package com.example.raj.uploadexcel;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.raj.uploadexcel.mMySql.dataparser4.selected_sub;
import static com.example.raj.uploadexcel.select_sub.currentDateString;
import static com.example.raj.uploadexcel.select_sub.dept;
import static com.example.raj.uploadexcel.select_sub.sem;
import static com.example.raj.uploadexcel.select_sub.time;

public class attendence extends Activity {

    private static String TAG = "ReadActivity";
    public String message;
    public String message1,type1;
    ListView listview;
    public String[] strArr;
   ArrayList<Integer> ListViewItems = new ArrayList<Integer>();
    Button btn_submit;
    public String ValueHolder;
    public String valueholder1;
    AlertDialog.Builder builder;
    int Total_no_of_students=0;
    int Present_students;
    int Absent_students;
    SparseBooleanArray sparseBooleanArray;
    //int[] attend;
    List<Integer> attend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendence);
        setTitle("Students List");

        btn_submit = (Button) findViewById(R.id.btn_submit);
        builder = new AlertDialog.Builder(this);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ValueHolder = "";
                Present_students = 0;

                int i = 0;
                Log.v(TAG, "sb" + sparseBooleanArray.size());

                while (i < sparseBooleanArray.size()) {

                    if (sparseBooleanArray.valueAt(i)) {
                        Log.v(TAG, "qwqw" + sparseBooleanArray.valueAt(i));

                        // ValueHolder += ListViewItems.get(sparseBooleanArray.keyAt(i)) + ",";
                        Present_students += 1;
                        attend.add(ListViewItems.get(sparseBooleanArray.keyAt(i)));
                    }

                    i++;
                }

                ValueHolder = ValueHolder.replaceAll("(,)*$", "");

                valueholder1=ValueHolder;
                Log.v(TAG,"arr"+valueholder1);


                builder.setMessage(R.string.dialog_message) .setTitle(R.string.dialog_title);
                Absent_students=Total_no_of_students-Present_students;
                String messageText = "Total number of students"+ Total_no_of_students+
                        "\nPresent Students :"+ Present_students+
                        "\n\nAbsent Students :"+Absent_students+
                        "\nPress ok to continue.";

                builder.setMessage(messageText)
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                finish();
                                new attendence.PostDataAsyncTask().execute();

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'NO' Button
                                attend.clear();
                                dialog.cancel();

                            }
                        });
                //Creating dialog box
                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.setTitle("Confirm Attendence");
                alert.show();
            }
        });
        Intent intent = getIntent();
        message = intent.getStringExtra("message");
        Log.v(TAG, "post: " + message);



        JSONArray jsonArray = null;
        try {
            try {
                jsonArray = new JSONArray(message);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            strArr = new String[jsonArray.length()];

            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    strArr[i] = jsonArray.getString(i);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            Log.v(TAG, "Contains" + Arrays.toString(strArr));
            for(int i = 0;i<strArr.length;++i) {
                Total_no_of_students+=1;
                ListViewItems.add(Integer.valueOf(strArr[i]));
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }

        listview = (ListView) findViewById(R.id.listView);

        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>
                (attendence.this,
                        android.R.layout.simple_list_item_multiple_choice,
                        android.R.id.text1, ListViewItems);

        listview.setAdapter(adapter);

        //attend = new int[200];
         attend = new ArrayList<>();

        listview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub

                sparseBooleanArray = listview.getCheckedItemPositions();

            }

        });



    }



    public class PostDataAsyncTask extends AsyncTask<String, String, String> {


        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected String doInBackground(String... strings) {
            String response = null;
            try {

                // url where the data will be posted
                URL url = new URL("http://androidattend.000webhostapp.com/store.php");
                Log.v(TAG, "postURL: " + url);

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");

                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                urlConnection.setRequestProperty("Content-Type", "text/plain");



                // Send the post body
                if (attend != null) {
                    OutputStream writer = urlConnection.getOutputStream();

                    JSONArray arr = new JSONArray(attend);
Log.v(TAG,"vh"+arr);
                    JSONObject jObject = new JSONObject();
                    jObject.put("valueholder1", arr);
                    jObject.put("date", currentDateString);
                    jObject.put("time", time);
                    jObject.put("sem", sem);
                    jObject.put("dept", dept);
                    jObject.put("sub", selected_sub);
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
                    type1 = (jObject1.getString("error"));
                    message1 = (jObject1.getString("message"));
                    Log.v(TAG, "type_value" + type1);
                    Log.v(TAG, "message" + message1);


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
            Toast.makeText(getApplicationContext(),message1,
                    Toast.LENGTH_SHORT).show();

        }


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
