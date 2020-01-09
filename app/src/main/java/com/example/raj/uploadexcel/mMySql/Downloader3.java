package com.example.raj.uploadexcel.mMySql;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.widget.Spinner;
import android.widget.Toast;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

public class Downloader3 extends AsyncTask<Void, Void,String> {


    String urlAddress;
    Context c;


    ProgressDialog pd;


    public Downloader3(Context c, String urlAddress) {
        this.c = c;
        this.urlAddress = urlAddress;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected String doInBackground(Void... params) {
        return this.downloadData();
    }


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        DataParser3 parser = new DataParser3(c,s);
        parser.execute();



    }

    private String downloadData() {
        HttpURLConnection con = Connector.connect(urlAddress);
        if (con == null) {
            return null;
        }
        InputStream is =null;

        try {
            is = new BufferedInputStream(con.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String line = null;
            StringBuffer response = new StringBuffer();
            if (br != null) {
                while ((line = br.readLine()) != null) {
                    response.append(line +"\n");
                }
                br.close();
            } else {
                return null;
            }

            return response.toString();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

}