package com.cs.qrcode;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText acc,pwd;
    ImageView login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        acc=(EditText)findViewById(R.id.acc);
        pwd=(EditText)findViewById(R.id.pwd);
        login=(ImageView)findViewById(R.id.login);
        login.setOnClickListener(login_btn);
    }
    private ImageView.OnClickListener login_btn=new ImageView.OnClickListener(){
        @Override
        public void onClick(View view) {
            if(!TextUtils.isEmpty(acc.getText().toString()) && !TextUtils.isEmpty(pwd.getText().toString())){
                String account=acc.getText().toString();
                String password=pwd.getText().toString();
                mytoast("登入中...");
                if(account.equals("admin") && password.equals("12345")){
                    Intent intent=new Intent();
                    intent.setClass(MainActivity.this, Qrcode.class);
                    startActivity(intent);
                }
                else{
                    mytoast("密碼錯誤");
                }
            }
        }
    };
    private void mytoast(String str)
    {
        Toast toast=Toast.makeText(this, str, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}