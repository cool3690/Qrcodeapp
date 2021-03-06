package com.cs.qrcode;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.graphics.Color;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cs.qrcode.db.dbproperty;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import android.hardware.Camera;

public class Qrcode extends AppCompatActivity {
    private Camera camera = Camera.open();
    private Camera.Parameters parameters;
    //ImageView toback;
    Mp mp=new Mp();
    Toolbar toolbar;
    SurfaceView surfaceView;
    TextView show;
    CameraSource cameraSource;
    BarcodeDetector barcodeDetector;
    Button light;
    //private Camera camera;
    boolean tflight=false;
    private static final int REQUEST = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qrcode);
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
         toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitleTextColor(Color.BLACK);

      //  toback=(ImageView)findViewById(R.id.toback);
       // toback.setOnClickListener(backbtn);
        surfaceView=(SurfaceView)findViewById(R.id.surfaceView);
        show=(TextView)findViewById(R.id.show);
         light=(Button)findViewById(R.id.light);
        show.setOnClickListener(showclick);
        getPermission();
        getnavbottom();
         light.setOnClickListener(lightbtn);
        light.setVisibility(View.GONE);
         /////////
        int readPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if(readPermission!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, REQUEST);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, REQUEST);
        }

        ///////////
        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.QR_CODE).build();
        cameraSource=new CameraSource.Builder(this,barcodeDetector)
                .setRequestedPreviewSize(1000,1000).build();
        cameraSource = new CameraSource.Builder(this,barcodeDetector).setAutoFocusEnabled(true).build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback(){
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED){

                    return;
                }
                if(ActivityCompat.checkSelfPermission(Qrcode.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){

                    new AlertDialog.Builder(Qrcode.this)
                            .setCancelable(false)
                            .setTitle("??????????????????")
                            .setMessage("?????????????????????????????????")
                            .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    ActivityCompat.requestPermissions(Qrcode.this,new String[]{Manifest.permission.CAMERA},1);
                                }
                            })
                            .show();
                }
                getPermission();
                try{
                    cameraSource.start(surfaceHolder);
                }catch (IOException e){
                    // e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>(){

            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(@NonNull Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> qrCodes=detections.getDetectedItems();
                if(qrCodes.size()!=0){
                    show.post(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                if(!TextUtils.isEmpty(show.getText().toString())){
                                    if(show.getText().toString().equals("??????:"+qrCodes.valueAt(0).displayValue)){

                                    }
                                    else{
                                        show.setText("??????:"+qrCodes.valueAt(0).displayValue);
                                        pre(qrCodes.valueAt(0).displayValue);

                                    }
                                }
                                else{
                                    show.setText("??????:"+qrCodes.valueAt(0).displayValue);
                                    pre(qrCodes.valueAt(0).displayValue);
                                    /*

                                    String word[]=qrCodes.valueAt(0).displayValue.split("_");
                                    Intent intent=new Intent();
                                    intent.setClass(Qrcode.this,Class.forName(word[0]));
                                    Bundle bundle=new Bundle();
                                    bundle.putString("ANS", word[1]);
                                    bundle.putString("L", word[2]+"");
                                    bundle.putString("T", word[3]+"");
                                    intent.putExtras(bundle);
                                    startActivity(intent);

                                     */
                                }
                                /////////////

                            } catch (Exception e) { }
                        }


                    });
                    // show.postDelayed(Runnable, 1000);

                }
            }
        });

    }
    public void pre(String s){

        String result = dbproperty.executeQuery(s+"");

        try{
            JSONArray jsonArray = new JSONArray(result);

            for(int i = 0; i < jsonArray.length(); i++) //?????????????????????????????????
            {
                JSONObject jsonData = jsonArray.getJSONObject(i);

                String sdate=jsonData.getString("sdate");
                String property=jsonData.getString("property");
                String property_id=jsonData.getString("property_id");
                String content=jsonData.getString("content");
                String year=jsonData.getString("year");
                String num=jsonData.getString("num");
                String unit=jsonData.getString("unit");
                String position=jsonData.getString("position");
                Intent intent=new Intent();
                intent.setClass(Qrcode.this, Notes.class);
                Bundle bundle=new Bundle();
                String strs="??????: "+property_id+" \n????????????: "+property+" \n??????: "+num+" "+unit+" \n????????????:"+year+" \n?????????:"+mp.search(property_id.substring(1,3))+" \n??????: "+content+" \n??????: "+position;
                //String str="????????????:"+sdate+" \n????????????: "+property+" \n??????: "+content+" \n??????: "+year+" \n??????: "+num+" "+unit+" \n??????: "+position;
                bundle.putString("ANS",strs);
                bundle.putString("PRO",property_id);
                intent.putExtras(bundle);
                startActivity(intent);
             //   showdb.setText("????????????:"+sdate+" \n????????????: "+property+" \n??????: "+content+" \n??????: "+year+" \n??????: "+num+" "+unit+" \n??????: "+position);
            }

        }

        catch(Exception e){}

    }

    private  Button.OnClickListener lightbtn=new Button.OnClickListener(){
        @Override
        public void onClick(View view) {
/*
            mCamera = Camera.open();
            parameters = mCamera.getParameters(); //??????Camera?????????
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            mCamera.setParameters(parameters);

 */
            camera.startPreview();
            parameters = camera.getParameters();
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            camera.setParameters(parameters);
        }
    };
    private TextView.OnClickListener showclick=new TextView.OnClickListener(){
        @Override
        public void onClick(View view) {
            if(!TextUtils.isEmpty(show.getText().toString())){

                    if(show.getText().toString().contains("http://") ||show.getText().toString().contains("https://")
                            ||show.getText().toString().contains("www.")  ){

                        try {  Intent intent=new Intent();
                            intent.setClass(Qrcode.this,Class.forName("com.nihon.aki2.Menushow"));
                            startActivity(intent);
                        } catch (ClassNotFoundException e) { }




                    }

            }
        }
    };
    private ImageView.OnClickListener backbtn=new ImageView.OnClickListener(){
        @Override
        public void onClick(View view) {
            onBackPressed();
          //  overridePendingTransition(R.anim.slide_in_left,
                //    R.anim.slide_out_right);

        }
    };

    public void onBackPressed() {


        super.onBackPressed();
    }
    public void getPermission(){
        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},1);

        }
    }
    public void getnavbottom(){
        BottomNavigationView nav_view=(BottomNavigationView)findViewById(R.id.nav_view);
        nav_view.setSelectedItemId(R.id.location);
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
    private void mytoast(String str)
    {
        Toast toast=Toast.makeText(this, str, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}