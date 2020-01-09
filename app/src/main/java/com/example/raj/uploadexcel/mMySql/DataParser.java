package com.example.raj.uploadexcel.mMySql;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.raj.uploadexcel.mDataObject.Spacecraft;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

import static com.example.raj.uploadexcel.allot.selectedDepartment;
import static com.example.raj.uploadexcel.allot.urlAddress3;
import static com.example.raj.uploadexcel.login.department;
import static com.example.raj.uploadexcel.mMySql.dataparser4.selected_sub;
import static com.example.raj.uploadexcel.select_sub.TAG;


public class DataParser extends AsyncTask<Void,Void,Integer> {

    Context c;
    Spinner sp;
    String jsonData;
    ProgressDialog pd;
    ArrayList<String> listItems = new ArrayList<>();
    ArrayList<String> list = new ArrayList<>();
    public static int Position;
    JSONObject jObject1;

    public DataParser(Context c, Spinner sp, String jsonData) {
        this.c = c;
        this.sp = sp;
        this.jsonData = jsonData;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd = new ProgressDialog(c);
        pd.setTitle("Parse");
        pd.setMessage("Parsing..please wait");
        pd.show();
    }

    @Override
    protected Integer doInBackground(Void... params) {
        String response = null;
        try {

            // url where the data will be posted
            URL url = new URL("http://androidattend.000webhostapp.com/connect.php");
            Log.v(TAG, "postURL: " + url);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");

            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "text/plain");


            // Send the post body

            if (selectedDepartment != null) {
                OutputStream writer = urlConnection.getOutputStream();

                JSONObject jObject = new JSONObject();
                try {
                    jObject.put("department", selectedDepartment);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

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

                //Log.v(TAG, "designation"+designation);


            } else {
                // Status code is not 200
                // Do something to handle the error
            }

            try {
                JSONArray ja = new JSONArray(response);

                listItems.clear();


                for (int i = 0; i < ja.length(); i++) {
                    JSONObject jo = ja.getJSONObject(i);
                    String name = jo.getString("name");

                    list.add(name);
                    Log.v(TAG,"name:" +name);
                }
                listItems.addAll(list);
                Log.v(TAG,"lst:" +listItems);


            } catch (JSONException e) {
                e.printStackTrace();
            }



        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    protected void onPostExecute(Integer response) {
        super.onPostExecute(response);
        pd.dismiss();

            Toast.makeText(c, "Parsed successfully", Toast.LENGTH_SHORT).show();


            //bind to adapter
            ArrayAdapter adapter = new ArrayAdapter(c, android.R.layout.simple_list_item_1, listItems);
            adapter.notifyDataSetChanged();
            sp.setAdapter(adapter);

            sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    Toast.makeText(c, listItems.get(position), Toast.LENGTH_SHORT).show();
                    Log.v("Position Selected", "Pos" + position);
                    Position = position;
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });



    }



    private String convertInputStreamToString (InputStream inputStream){
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
