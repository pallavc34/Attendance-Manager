package com.example.raj.uploadexcel;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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


public class retrieve extends Activity{

    public int sem = 1;
    public String dept = "Information Technology";
    String type,message;
    private static String TAG = "ReadActivity";
    Button btn_submit;


    @Override
    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.read);
        setTitle("Subject Allocation");

        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    new retrieve.PostDataAsyncTask().execute();

                if (type.equalsIgnoreCase("false")) {
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    Intent myIntent = new Intent(getBaseContext(),attendence.class);
                    myIntent.putExtra("message",message);
                    startActivity(myIntent);
                } else {
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            }
        });
    }

    public class PostDataAsyncTask extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();
            // do stuff before posting data
        }

        @Override
        protected String doInBackground(String... strings) {
            try {

                postText();

            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute (String response){
            String result = response;

        }


    }
    // this will post our text data
    private void postText(){
        try{

            // url where the data will be posted
            URL url = new URL("http://androidattend.000webhostapp.com/retrieve.php");
            Log.v(TAG, "postURL: " + url);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");

            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "text/plain");

            // Send the post body
            if (this.dept != null) {
                OutputStream writer = urlConnection.getOutputStream();

                JSONObject jObject = new JSONObject();
                jObject.put("sem", sem);
                jObject.put("dept", dept);


                String data = jObject.toString();

                //byte[] data = email.getBytes();
                writer.write(data.getBytes());
                Log.v(TAG, "hhh"+data);
                writer.flush();
            }
            int statusCode = urlConnection.getResponseCode();

            if (statusCode ==  200) {

                InputStream inputStream = urlConnection.getErrorStream();

                if(inputStream == null) //If inputStream is null here, no error has occured.
                    inputStream = urlConnection.getInputStream();
                //InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());

                String response = convertInputStreamToString(inputStream);
                Log.v(TAG, "hhh"+response);
                JSONObject jObject1 = new JSONObject(response);
                type = (jObject1.getString("error"));
                message = (jObject1.getString("message"));
                Log.v(TAG, "type_value"+type);
                Log.v(TAG, "message"+message);

                retrieve.PostDataAsyncTask post = new retrieve.PostDataAsyncTask();
                post.onPostExecute(response);


            } else {
                // Status code is not 200
                // Do something to handle the error
            }




        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
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
