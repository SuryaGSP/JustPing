package com.example.surya.smslisten;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.gsm.SmsManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class Pending_Request extends AppCompatActivity {
    ListView lv6;
    ArrayList list6=new ArrayList();
    SQLiteDatabase sql;
    EditText e36;
    Button b56;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending__request);
        sql=openOrCreateDatabase("Details",MODE_PRIVATE,null);
        e36=(EditText) findViewById(R.id.editText567);
        b56=(Button) findViewById(R.id.button567);
        Cursor c=sql.rawQuery("select * from student26;",null);
        lv6=(ListView)findViewById(R.id.list_item567);
        c.getCount();
        c.moveToFirst();
        String temp=" ";
        while(!c.isAfterLast())
        {
            temp="";
            temp=c.getString(0)+" "+c.getString(1);
            c.moveToNext();
            Toast.makeText(getApplicationContext(),temp,Toast.LENGTH_LONG).show();
            list6.add(""+temp);
        }
        ArrayAdapter array=new ArrayAdapter(this,android.R.layout.simple_list_item_1,list6);
        lv6.setAdapter(array);
        b56.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ss = e36.getText().toString();
                if (ss.length() > 0) {
                    Cursor s66 = sql.rawQuery("select * from student26 where id='" + ss + "';", null);
                    Cursor s77 = sql.rawQuery("select * from number;", null);
                    s77.moveToFirst();
                    if (!s66.moveToFirst()) {
                        Toast.makeText(getApplicationContext(), "NO such pending request", Toast.LENGTH_LONG).show();
                    } else {
                        android.telephony.SmsManager smsManager = android.telephony.SmsManager.getDefault();
                        smsManager.sendTextMessage(s66.getString(2), null, "CODE:" + s77.getString(0), null, null);
                        Toast.makeText(getApplicationContext(), "Access Granted", Toast.LENGTH_LONG).show();
                        sql.execSQL("delete from student26 where id='"+ss+"';");
                        Intent i98=new Intent(Pending_Request.this,MainActivity.class);
                        startActivity(i98);
                    }
                }
            }
        });
    }
}
