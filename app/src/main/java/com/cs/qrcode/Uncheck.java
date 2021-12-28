package com.cs.qrcode;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import com.cs.qrcode.controls.Job;
import com.cs.qrcode.controls.JobAdapter;
import com.cs.qrcode.controls.Unjob;
import com.cs.qrcode.controls.UnjobAdapter;
import com.cs.qrcode.db.dbcheck;
import com.cs.qrcode.db.dbuncheck;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.StrictMode;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Uncheck extends AppCompatActivity {
    ListView show;
    private Mp mp=new Mp();
    ArrayList<Unjob> jobs = new ArrayList<Unjob>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.uncheck);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.BLACK);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()
                .penaltyLog()
                .build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .penaltyLog()
                .penaltyDeath()
                .build());
        getnavbottom();
        show = (ListView) findViewById(R.id.show);

        new DownloadFileAsync().execute();
    }
    class DownloadFileAsync extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... aurl) {

            return null;
        }

        protected void onProgressUpdate(String... progress) {

        }

        @Override
        protected void onPostExecute(String unused) {
            String result = dbuncheck.executeQuery();


            try{
                JSONArray jsonArray = new JSONArray(result);

                for(int i = 0; i < jsonArray.length(); i++) //代理或主管有工號者顯示
                {	 JSONObject jsonData = jsonArray.getJSONObject(i);


                    String property=jsonData.getString("property");

                    String property_id=jsonData.getString("property_id");
                    String unit=jsonData.getString("unit");
                    String num=jsonData.getString("num");
                    String inventory_date=jsonData.getString("inventory_date");
                    String str=mp.search(property_id.substring(1,3));
                    // mytoast(str);
                    Unjob job=new Unjob(property,property_id, num+" "+unit,str);
                    jobs.add(job);


                }
                final UnjobAdapter adapter = new UnjobAdapter(getApplicationContext(), R.layout.list_uncheck, jobs);
                show.setAdapter(adapter);
                show.setTextFilterEnabled(true);

            }

            catch(Exception e){}

        }
    }
    public void getnavbottom(){
        BottomNavigationView nav_view=(BottomNavigationView)findViewById(R.id.nav_view);
        nav_view.setSelectedItemId(R.id.path);
        nav_view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.location:
                        startActivity(new Intent(getApplicationContext(),Qrcode.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.path:
                        startActivity(new Intent(getApplicationContext(),Uncheck.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.video:
                        startActivity(new Intent(getApplicationContext(),Checked.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.stamp:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                }
                return false;
            }
        });
    }
}