package com.example.cahp9;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    boolean externalStorageReadable;
    boolean externalStorageWritable;
    boolean FileReadPermission, FileWritePermission;

    EditText content;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        content = findViewById(R.id.content);
        btn = findViewById(R.id.btn);
        btn.setOnClickListener(this);

        String state = Environment.getExternalStorageState();
        if(state.equals(Environment.MEDIA_MOUNTED)){
            if(state.equals(Environment.MEDIA_MOUNTED_READ_ONLY)){
                externalStorageReadable = true;
                externalStorageWritable = false;
                Log.e("externalStorageState","Readable Only");
            }else{
                externalStorageReadable = true;
                externalStorageWritable = true;
                Log.e("externalStorageState","Readable and Writable Both");
            }
        }else{
            externalStorageWritable = externalStorageReadable = false;
            Log.e("externalStorageState","None");
        }
        //펄미션 허가되었는지 체크
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
            FileReadPermission = true;
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
            FileWritePermission = true;
        }
        //펄미션 허가되어있지 않으면 요청(대화창 -> 허용, 거부)
        if(!FileReadPermission || !FileWritePermission) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.SEND_SMS, Manifest.permission.INTERNET}, 999);

        }
    }

    //펄미션 요청에 대한 결과 처리
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==999&&grantResults.length>0){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED) {
                FileReadPermission = true;
            }
            if(grantResults[1]==PackageManager.PERMISSION_GRANTED){
                FileWritePermission = true;
            }
            if(grantResults[2]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Send SMS permissions are permitted", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Send SMS permissions are denied", Toast.LENGTH_SHORT).show();
            }
            if(grantResults[3]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Internet permssions are permitted", Toast.LENGTH_LONG).show();
            }
            if(FileWritePermission&&FileReadPermission){
                //Toast.makeText(this, "ExternalStorage permissions are permitted", Toast.LENGTH_SHORT).show();
            }else{
                //Toast.makeText(this, "ExternalStorage permissions are denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btn){
            //editText  내용 로드
            String memo = content.getText().toString();
            //외부저장장치 권한 있을때만 실행
            if(FileReadPermission&&FileWritePermission) {
                //외부저장장치 폴더 만들고 파일 만들기
                FileWriter writer;
                File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/myApp");

                //디렉토리 없으면 새로 만들기;
                if(!dir.exists())
                    dir.mkdir();

                //파일에 내용 저장
                try {
                    writer = new FileWriter(dir+"/myFile.txt",true);
                    writer.write(memo);
                    writer.close();
                    Toast.makeText(this, "Save File Success", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(this,ReadActivity.class);
                    startActivity(intent);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Save File Fail", Toast.LENGTH_SHORT).show();
                }

            }else{
                Toast.makeText(this, "No Permission for External Storage", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
