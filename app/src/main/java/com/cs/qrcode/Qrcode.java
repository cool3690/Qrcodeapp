package com.cs.qrcode;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;


public class Qrcode extends AppCompatActivity {
    Toolbar toolbar;
    ImageView toback;
    SurfaceView surfaceView;
    TextView show,showdb;
    CameraSource cameraSource;
    BarcodeDetector barcodeDetector;
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

      //  toback=(ImageView)findViewById(R.id.toback);
       // toback.setOnClickListener(backbtn);
        surfaceView=(SurfaceView)findViewById(R.id.surfaceView);
        show=(TextView)findViewById(R.id.show);
        showdb=(TextView)findViewById(R.id.showdb);
        show.setOnClickListener(showclick);
        getPermission();
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
                            .setTitle("需要相機權限")
                            .setMessage("有相機權限才有辦法掃描")
                            .setPositiveButton("同意", new DialogInterface.OnClickListener() {
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
                                    if(show.getText().toString().equals("產編:"+qrCodes.valueAt(0).displayValue)){
                                        //mytoast( qrCodes.size()+"");

                                        //show.setText("");
                                    }
                                    else{
                                        show.setText("產編:"+qrCodes.valueAt(0).displayValue);
                                        pre(qrCodes.valueAt(0).displayValue);
                                        /*

                                        String word[]=qrCodes.valueAt(0).displayValue.split("_");
                                        Intent intent=new Intent();//com.nihon.aki2.Listening
                                        intent.setClass(Qrcode.this,Class.forName(word[0]));
                                        Bundle bundle=new Bundle();
                                        bundle.putString("ANS", word[1]);
                                        bundle.putString("L", word[2]+"");
                                        bundle.putString("T", word[3]+"");
                                        intent.putExtras(bundle);
                                        startActivity(intent);

                                         */
                                    }
                                }
                                else{
                                    show.setText("產編:"+qrCodes.valueAt(0).displayValue);
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

            for(int i = 0; i < jsonArray.length(); i++) //代理或主管有工號者顯示
            {
                JSONObject jsonData = jsonArray.getJSONObject(i);

                String sdate=jsonData.getString("sdate");
                String name=jsonData.getString("name");
                String content=jsonData.getString("content");
                String year=jsonData.getString("year");
                String num=jsonData.getString("num");
                String unit=jsonData.getString("unit");
                showdb.setText("建立日期:"+sdate+" \n財產名稱: "+name+" \n內容: "+content+" \n年度: "+year+" \n數量: "+num+" "+unit);
            }

        }

        catch(Exception e){}

    }
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
    private void mytoast(String str)
    {
        Toast toast=Toast.makeText(this, str, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}