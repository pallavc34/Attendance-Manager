package com.example.raj.uploadexcel.mMySql;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static com.example.raj.uploadexcel.login.emp_id;
import static com.example.raj.uploadexcel.select_sub.TAG;

import static com.example.raj.uploadexcel.allot.urlAddress3;
import static com.example.raj.uploadexcel.login.department;
public class Connector {



    public static HttpURLConnection connect(String urlAddress){
        String url1 = "http://androidattend.000webhostapp.com/subjectt.php";
         String url2 = "http://androidattend.000webhostapp.com/connect.php";
         String url3 ="http://androidattend.000webhostapp.com/retrieve_id.php";

        try {
           URL url=new URL(urlAddress);
           HttpURLConnection con= (HttpURLConnection) url.openConnection();

           //set properties
           con.setRequestMethod("POST");
           con.setConnectTimeout(20000);
           con.setReadTimeout(20000);
           con.setDoInput(true);
           con.setRequestProperty("Content-Type", "text/plain");

           // Send the post body
           if(urlAddress.equalsIgnoreCase(url1) || urlAddress.equalsIgnoreCase(url2) || urlAddress.equalsIgnoreCase(urlAddress3)) {
               if (department != null) {
                   OutputStream writer = con.getOutputStream();

                   JSONObject jObject = new JSONObject();
                   try {
                       jObject.put("department", department);
                   } catch (JSONException e) {
                       e.printStackTrace();
                   }

                   String data = jObject.toString();

                   //byte[] data = email.getBytes();
                   writer.write(data.getBytes());
                   Log.v(TAG, "hhh" + data);

                   writer.flush();
               }
           }else{

                   OutputStream writer = con.getOutputStream();

                   JSONObject jObject = new JSONObject();
                   try {
                       jObject.put("emp_id", emp_id);
                   } catch (JSONException e) {
                       e.printStackTrace();
                   }

                   String data = jObject.toString();

                   //byte[] data = email.getBytes();
                   writer.write(data.getBytes());
                   Log.v(TAG, "hhh" + data);

                   writer.flush();

           }

           return con;

       }catch (MalformedURLException e){
           e.printStackTrace();
       } catch (IOException e) {
           e.printStackTrace();
       }
       return null;
    }
}
