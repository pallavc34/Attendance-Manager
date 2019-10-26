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

import java.util.ArrayList;

import static com.example.raj.uploadexcel.mMySql.DataParser.Position;


public class DataParser3 extends AsyncTask<Void,Void,Integer> {

    Context c;
    String jsonData;
    ProgressDialog pd;
    ArrayList<String> spacecrafts = new ArrayList<>();
    public static int selected_emp_id;

    public static int[] emp_id;

    public DataParser3(Context c, String jsonData) {
        this.c = c;
        this.jsonData=jsonData;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected Integer doInBackground(Void... params) {
        return this.parseData();
    }

    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);

    }

    private int parseData(){
        try {
            JSONArray ja = new JSONArray(jsonData);



            emp_id = new int[jsonData.length()];
            for (int i=0;i<ja.length();i++){
                JSONObject jo=ja.getJSONObject(i);
                 emp_id[i]=jo.getInt("emp_id");
               Log.v("get_Values",""+emp_id[i]);

            }
            return 1;

        }catch (JSONException e){
            e.printStackTrace();
        }
        return 0;

    }
}
