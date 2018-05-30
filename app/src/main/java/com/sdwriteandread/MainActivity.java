package com.sdwriteandread;

import android.content.Context;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {
    private Button save,read,delete;
    private EditText content;
    private TextView show;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        save = (Button) this.findViewById(R.id.save);
        read = (Button) this.findViewById(R.id.read);
        delete = (Button) this.findViewById(R.id.del);
        show = (TextView) this.findViewById(R.id.show);
        content = (EditText) this.findViewById(R.id.content);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveFile();
            }
        });

        read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show.setText(readFile());
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeFile();
            }
        });
    }

    //保存文件到SD卡
    public void saveFile(){
        FileOutputStream fos = null;
        //获取SD卡状态
        String state = Environment.getExternalStorageState();
        //判断SD卡是否就绪
        if (!state.equals(Environment.MEDIA_MOUNTED)){
            Toast.makeText(this,"请检查SD卡",Toast.LENGTH_SHORT).show();
            return;
        }
        //取得SD卡根目录
        File file = Environment.getExternalStorageDirectory();
        try {
            Log.d("====SD卡根目录:",""+file.getCanonicalPath().toString());
            //输出流的构造参数1:可以是 File对象 也可以是文件路径
            //输出流的构造参数2:默认为false=>覆盖内容；true=>追加内容
            File myfile = new File(file.getCanonicalPath()+"/SD.txt");
            fos = new FileOutputStream(myfile,true);
            String str = content.getText().toString();
            fos.write(str.getBytes());
            Toast.makeText(this,"保存成功",Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos!=null){
                try {
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    void createExternalStoragePrivateFile() {
        File file = new File(getExternalFilesDir(null), "");
        try {
            InputStream is = getResources()
                    .openRawResource((Integer) null);
            OutputStream os = new FileOutputStream(file);
            byte[] data = new byte[is.available()];
            is.read(data);
            os.write(data);
            is.close();
            os.close();
        } catch (IOException e) {
            Log.w("ExternalStorage", "Error writing " + file, e);
        }
    }

    //从SD卡 读取文件
    public String readFile(){
        BufferedReader reader = null;
        FileInputStream fis = null;
        StringBuilder sbd = new StringBuilder();
        String statu = Environment.getExternalStorageState();
        if(!statu.equals(Environment.MEDIA_MOUNTED)){
            Toast.makeText(this,"SD卡未就绪",Toast.LENGTH_SHORT).show();
            return "";
        }
        File root = Environment.getExternalStorageDirectory();
        try {
            fis = new FileInputStream(root+"/SD.txt");
            reader = new BufferedReader(new InputStreamReader(fis));
            String row = "";
            while ((row = reader.readLine())!=null){
                sbd.append(row);
            }
        } catch (FileNotFoundException e) {
            Toast.makeText(this,"文件不存在",Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(fis!=null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sbd.toString();
    }

    //从SD卡中删除文件
    public void removeFile(){

        String[] files = fileList();
        File myfile = null;
        String statu = Environment.getExternalStorageState();
        if(!statu.equals(Environment.MEDIA_MOUNTED)){
            Toast.makeText(this,"SD卡未就绪",Toast.LENGTH_SHORT).show();
        }
        File root = Environment.getExternalStorageDirectory();
        try {
            myfile = new File(root.getCanonicalPath()+"/SD.txt");
            myfile.delete();
        } catch (IOException e) {
            Toast.makeText(this,"文件不存在",Toast.LENGTH_SHORT).show();
        }
    }
}
