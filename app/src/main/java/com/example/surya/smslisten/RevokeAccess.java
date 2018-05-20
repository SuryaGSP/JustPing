package com.example.surya.smslisten;

import android.content.Intent;
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
import android.widget.Toast;

import java.util.ArrayList;

public class RevokeAccess extends AppCompatActivity {
    ListView lv6;
    ArrayList list6=new ArrayList();
    SQLiteDatabase sql;
    EditText e36;
    Button b56;
    GPSTracker gps;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revoke_access);
        sql=openOrCreateDatabase("Details",MODE_PRIVATE,null);
        e36=(EditText) findViewById(R.id.editText84);
        b56=(Button) findViewById(R.id.button84);
        Cursor c=sql.rawQuery("select * from student1;",null);
        lv6=(ListView)findViewById(R.id.list_item84);
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
                String ss=e36.getText().toString();
                if(ss.length()>0)
                {
                    Cursor s66=sql.rawQuery("select * from student1 where id='"+ss+"';",null);
                    if(!s66.moveToFirst())
                    {
                        Toast.makeText(getApplicationContext(),"Enter Valid id",Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(s66.getString(2),null,"REVOKEACCESS",null,null);
                        gps = new GPSTracker(RevokeAccess.this);
                        if(gps.canGetLocation()){
                            double latitude = gps.getLatitude();
                            double longitude = gps.getLongitude();
                            String otp="http://maps.google.com/?q="+latitude+","+longitude;
                            // \n is for new line
                            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: "
                                    + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                            smsManager.sendTextMessage(s66.getString(2),null,"Access Revoked at "+otp,null,null);
                        }else {
                            // can't get location
                            // GPS or Network is not enabled
                            // Ask user to enable GPS/network in settings
                            gps.showSettingsAlert();
                        }
                        Toast.makeText(getApplicationContext(),"ACCESS REVOKED",Toast.LENGTH_LONG).show();
                        sql.execSQL("delete from student1 where id='"+ss+"';");

                        if(gps.canGetLocation()){

                            double latitude = gps.getLatitude();
                            double longitude = gps.getLongitude();
                            String otp="latitude="+latitude+"\n"+"longitude"+longitude;
                            // \n is for new line
                            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: "
                                    + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                        }else {
                            // can't get location
                            // GPS or Network is not enabled
                            // Ask user to enable GPS/network in settings
                            gps.showSettingsAlert();
                        }
                        Intent i1=new Intent(RevokeAccess.this,MainActivity.class);
                        startActivity(i1);
                    }
                }
            }
        });
    }
}
