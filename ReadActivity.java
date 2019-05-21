package com.example.cahp9;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ReadActivity extends AppCompatActivity {
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        tv = findViewById(R.id.content2);
        String fileName = Environment.getExternalStorageDirectory().getAbsolutePath()+"/myApp/myFile.txt";
        try {
            FileReader reader = new FileReader(fileName);
            StringBuffer buffer = new StringBuffer();
            buffer.append(fileName+"\n");
            int c;
            while((c = reader.read())!=-1){
                buffer.append((char)c);
            }
            tv.setText(buffer);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Fail to Read FIle", Toast.LENGTH_SHORT).show();
        }
    }
}
