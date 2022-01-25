package com.cs.qrcode;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cs.qrcode.controls.Job;
import com.cs.qrcode.controls.JobAdapter;
import com.cs.qrcode.db.dbcheck;
import com.cs.qrcode.db.dbshow;
import com.cs.qrcode.db.dbupstatus;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Notes extends AppCompatActivity {
    ImageView img;
    TextView showdb;
    ImageView toback;
    LinearLayout L1;
    ImageView btG,btL,btB;
    String property_id="";
    Context context;
    Dialog dia;
    String myurl="https://property.chansing.com.tw/images/property/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notes);
        img=(ImageView)findViewById(R.id.img);
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
        showdb=(TextView)findViewById(R.id.showdb);
        img=(ImageView)findViewById(R.id.img);
         toback=(ImageView)findViewById(R.id.toback);
        L1=(LinearLayout)findViewById(R.id.L1);
        btG=(ImageView)findViewById(R.id.btG);
        btL=(ImageView)findViewById(R.id.btL);
        btB=(ImageView)findViewById(R.id.btB);
        L1.setVisibility(View.GONE);
        btG.setOnClickListener(checkbtn);
        btB.setOnClickListener(checkbtn);
        btL.setOnClickListener(checkbtn);
         toback.setOnClickListener(backbtn);
        Intent intent=this.getIntent();
        Bundle bundle=intent.getExtras();
        String str=bundle.getString("ANS");
         property_id=bundle.getString("PRO");
        showdb.setText(str);
        myurl+=property_id+".jpg";
        new AsyncTask<String, Void, Bitmap>()
        {
            @Override
            protected Bitmap doInBackground(String... params) //實作doInBackground
            {
                String url = params[0];
                return getBitmapFromURL(url);
            }

            @Override
            protected void onPostExecute(Bitmap result) //當doinbackground完成後
            {
                img.setImageBitmap (result);
                super.onPostExecute(result);
            }
        }.execute(myurl);
        new Notes.DownloadFileAsync().execute();
    }

    private ImageView.OnClickListener checkbtn=new ImageView.OnClickListener(){
        @Override
        public void onClick(View view) {
            String str="";
            switch (view.getId()){
                case R.id.btG:
                    str="1";
                    break;
                case R.id.btL:
                    str="2";
                    break;
                case R.id.btB:
                    str="3";
                    break;
                default:
                    break;

            }
            ///
             dbupstatus.executeQuery(property_id,str);
            /*
            new AlertDialog.Builder(Notes.this)
                    .setTitle("確認視窗")

                    .setMessage("已送出")
                    .setPositiveButton("返回掃描", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i)
                        {
                            Intent intent=new Intent();
                            intent.setClass(getApplicationContext(),Qrcode.class);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("取消重選", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i)
                        {

                        }
                    })
                    .show();
*/
       ///////////////
            context = Notes.this;
            dia = new Dialog(context, R.style.rightcopystyle);
            dia.setContentView(R.layout.copyright);
            Button btok=(Button)dia.findViewById(R.id.btok);
            Button ret=(Button)dia.findViewById(R.id.ret);
            dia.setCanceledOnTouchOutside(true); // Sets whether this dialog is
            Window w = dia.getWindow();
            WindowManager.LayoutParams lp = w.getAttributes();
            lp.x = 0; // 新位置X坐標
            lp.width =950; // 寬度
            dia.show();
            dia.onWindowAttributesChanged(lp);
            btok.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dia.dismiss();
                        }
                    }
            );
            ret.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent=new Intent();
                            intent.setClass(Notes.this, Qrcode.class);
                            startActivity(intent);
                        }
                    }
            );



            /////////////
        }



    };
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
            String result = dbshow.executeQuery(property_id);


            try{
                JSONArray jsonArray = new JSONArray(result);
                if(jsonArray.length()>0){
                    L1.setVisibility(View.VISIBLE);
                }
                else{
                    L1.setVisibility(View.GONE);
                }


            }

            catch(Exception e){}

        }
    }
    private static Bitmap getBitmapFromURL(String imageUrl)
    {
        try
        {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;
        }
        catch (IOException e){  return null;}
    }
    private ImageView.OnClickListener backbtn=new ImageView.OnClickListener(){
        @Override
        public void onClick(View view) {

            Intent intent=new Intent();
            intent.setClass(getApplicationContext(),Qrcode.class);
            startActivity(intent);


        }
    };

}