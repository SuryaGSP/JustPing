package com.example.surya.smslisten;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class AccessLocation extends AppCompatActivity {
    ListView lv;
    ArrayList list=new ArrayList();
    SQLiteDatabase sql;
    EditText e3;
    Button b5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access_location);
        sql=openOrCreateDatabase("Details",MODE_PRIVATE,null);
        e3=(EditText) findViewById(R.id.editText99);
        b5=(Button) findViewById(R.id.button99);
        Cursor c=sql.rawQuery("select * from student1;",null);
        lv=(ListView)findViewById(R.id.list_item);
        c.getCount();
        c.moveToFirst();
        String temp=" ";
        while(!c.isAfterLast())
        {
            temp="";
             temp=c.getString(0)+" "+c.getString(1);
             c.moveToNext();
            Toast.makeText(getApplicationContext(),temp,Toast.LENGTH_LONG).show();
             list.add(""+temp);
        }
        ArrayAdapter array=new ArrayAdapter(this,android.R.layout.simple_list_item_1,list);
        lv.setAdapter(array);
        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ss=e3.getText().toString();
                if(ss.length()>0)
                {
                    Cursor c3=sql.rawQuery("select * from student1 where id='"+ss+"';",null);
                    if(!c3.moveToFirst())
                    {
                        Toast.makeText(getApplicationContext(),"ENTER VALID ID",Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(c3.getString(2), null, c3.getString(3), null, null);
                        Toast.makeText(getApplicationContext(),"Request sent",Toast.LENGTH_LONG).show();
                        e3.setText("");
                    }
                }
            }
        });
    }
}
