package com.example.surya.smslisten;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Changecode extends AppCompatActivity {
    TextView t1;
    Button b1;
    String s1,s2;
    EditText e1;
    SQLiteDatabase sql;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changecode);
        e1=(EditText) findViewById(R.id.editText00);
        b1=(Button) findViewById(R.id.button00);
        t1=(TextView) findViewById(R.id.textView00);
        sql=openOrCreateDatabase("Details",MODE_PRIVATE,null);
        Cursor c=sql.rawQuery("select * from number;",null);
        c.moveToFirst();
        s2=c.getString(0);
        t1.setText(s2);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s1=e1.getText().toString();
                if(s1.length()>0)
                {
                   sql.execSQL("update number set num='"+s1+"';");
                   Toast.makeText(getApplicationContext(),"Successfull",Toast.LENGTH_LONG).show();
                   t1.setText(s1);
                   Cursor s=sql.rawQuery("select * from student1;",null);
                   s.moveToFirst();
                   while (!s.isAfterLast())
                   {
                       SmsManager smsManager = SmsManager.getDefault();
                       smsManager.sendTextMessage(s.getString(2), null, "CODEREPLY:"+s1, null, null);
                       s.moveToNext();
                   }


                }
                else
                    Toast.makeText(getApplicationContext(),"Enter new code",Toast.LENGTH_LONG).show();
            }
        });


    }
}
