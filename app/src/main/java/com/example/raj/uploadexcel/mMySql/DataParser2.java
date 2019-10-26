package com.example.raj.uploadexcel.mMySql;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DataParser2 extends AsyncTask<Void,Void,Integer> {

    Context c;
    Spinner sp2;
    String jsonData;
    ProgressDialog pd;
    ArrayList<String> spacecrafts = new ArrayList<>();

    public DataParser2(Context c, Spinner sp2, String jsonData) {
        this.c=c;
        this.sp2=sp2;
        this.jsonData=jsonData;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd=new ProgressDialog(c);
        pd.setTitle("Parse");
        pd.setMessage("Parsing..please wait");
        pd.show();
    }

    @Override
    protected Integer doInBackground(Void... params) {
        return this.parseData();
    }

    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);
        pd.dismiss();
        if (result==0){
            Toast.makeText(c,"Unable to parse",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(c,"Parsed successfully",Toast.LENGTH_SHORT).show();

            //bind to adapter
            ArrayAdapter adapter=new ArrayAdapter(c,android.R.layout.simple_list_item_1,spacecrafts);
            sp2.setAdapter(adapter);

            sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    Toast.makeText(c, spacecrafts.get(position), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

        }

    }

    private int parseData(){
        try {
            JSONArray ja = new JSONArray(jsonData);

            spacecrafts.clear();


            for (int i=0;i<ja.length();i++){
                JSONObject jo=ja.getJSONObject(i);
                String name =jo.getString("subname");

                spacecrafts.add(name);
            }
            return 1;

        }catch (JSONException e){
            e.printStackTrace();
        }
        return 0;

    }
}
