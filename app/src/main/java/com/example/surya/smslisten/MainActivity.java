package com.example.surya.smslisten;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.app.PendingIntent;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;
import android.support.v4.app.ActivityCompat;
import android.test.mock.MockPackageManager;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class MainActivity extends AppCompatActivity {
    Button btnSendSMS,m89,b8,b7,b27;
    EditText txtPhoneNo;
    EditText txtMessage;
    String otp;
    int n88;
    String OTP_REGEX = "aaaaa",OTP_REGEX1;
    private static final int REQUEST_CODE_PERMISSION = 2;
    String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;

    // GPSTracker class
    GPSTracker gps;
    SQLiteDatabase sql;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            if (ActivityCompat.checkSelfPermission(this, mPermission)
                    != MockPackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{mPermission},
                        REQUEST_CODE_PERMISSION);

                // If any permission above not allowed by user, this condition will execute every time, else your else part will work
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        m89=(Button)findViewById(R.id.pendingcccess);
        m89.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent l473=new Intent(MainActivity.this,Pending_Request.class);
                startActivity(l473);
            }
        });
        b7=(Button) findViewById(R.id.deleteacccess);
        b7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i837=new Intent(MainActivity.this,RevokeAccess.class);
                startActivity(i837);
            }
        });
        btnSendSMS=(Button) findViewById(R.id.access);
        b27=(Button) findViewById(R.id.sendcode);
        b8=(Button) findViewById(R.id.change);
        b27.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1=new Intent(MainActivity.this,FetchCode.class);
                startActivity(i1);
            }
        });
        btnSendSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  Intent i=new Intent(MainActivity.this,AccessLocation.class);
                startActivity(i);
            }
        });
        sql=openOrCreateDatabase("Details",MODE_PRIVATE,null);
        sql.execSQL("create table if not exists student1(id INTEGER PRIMARY KEY AUTOINCREMENT,Name text,Number text,pword text);");
        sql.execSQL("create table if not exists student26(id INTEGER PRIMARY KEY AUTOINCREMENT,Name text,Number text);");
        String query1="create table if not exists number(num text);";
        sql.execSQL(query1);
        Cursor c1=sql.rawQuery("select * from number",null);
        if(!c1.moveToFirst())
        {
            sql.execSQL("insert into number values('aaaaa');");
        }
        else {
            Toast.makeText(getApplicationContext(),"Code set",Toast.LENGTH_LONG).show();
        }
        b8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent x=new Intent(MainActivity.this,Changecode.class);
              startActivity(x);
            }
        });
        Cursor s5=sql.rawQuery("select * from number",null);
        s5.moveToFirst();
        OTP_REGEX1=s5.getString(0);
        gps = new GPSTracker(MainActivity.this);

        // check if GPS enabled
        if(gps.canGetLocation()){

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            otp="latitude="+latitude+"\n"+"longitude"+longitude;
            // \n is for new line
            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: "
                    + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }

        SmsReceiver.bindListener(new SmsListener() {
            @Override
            public void messageReceived(String messageText,String sender) {

                //From the received text string you may do string operations to get the required OTP
                //It depends on your SMS format
                Cursor s5=sql.rawQuery("select * from number",null);
                s5.moveToFirst();
                OTP_REGEX1=s5.getString(0);
                Log.e("Message",messageText);
                if(messageText.contains("ACCESSREQUEST"))
                {
                    Uri uri=Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI,Uri.encode(sender));
                    String[] projection=new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME};
                    String contactname="";
                    Cursor cursor=getContentResolver().query(uri,projection,null,null,null);
                    if (cursor != null) {
                        if(cursor.moveToFirst()) {
                            contactname=cursor.getString(0);
                        }
                        cursor.close();
                    }
                    sql.execSQL("insert into student26 values(NULL,'" + contactname + "','" + sender + "');");

                }
                if(messageText.contains("CODE"))
                {
                    Uri uri=Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI,Uri.encode(sender));
                    String[] projection=new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME};
                    String contactname="";
                    Cursor cursor=getContentResolver().query(uri,projection,null,null,null);
                    if (cursor != null) {
                        if(cursor.moveToFirst()) {
                            contactname=cursor.getString(0);
                        }
                        cursor.close();
                    }
                    if(messageText.contains("CODEREPLY:"))
                    {
                        n88=1;
                        Cursor c=sql.rawQuery("select pword from student1 where Number='"+sender+"';",null);
                        if(!c.moveToFirst())
                        {
                            sql.execSQL("insert into student1 values(NULL,'" + contactname + "','" + sender + "','" + messageText + "');");
                        }
                        else
                        {
                            sql.execSQL("update student1 set pword='" + messageText + "' where Number='" + sender + "';");
                        }
                        messageText=messageText.replace("CODEREPLY:","");
                    }
                    else {
                        n88=0;
                        messageText = messageText.replace("CODE:", "");
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(sender, null, "CODEREPLY:"+OTP_REGEX1, null, null);
                    }
                    //Toast.makeText(getApplicationContext(),messageText+sender,Toast.LENGTH_LONG).show();

                    Toast.makeText(getApplicationContext(),contactname,Toast.LENGTH_LONG).show();
                    Cursor c=sql.rawQuery("select pword from student1 where Number='"+sender+"';",null);
                     if(!c.moveToFirst()) {
                       //  Toast.makeText(getApplicationContext(),"saved1",Toast.LENGTH_LONG).show();
                        sql.execSQL("insert into student1 values(NULL,'" + contactname + "','" + sender + "','" + messageText + "');");
                     }
                    else {
                         sql.execSQL("update student1 set pword='" + messageText + "' where Number='" + sender + "';");
                     }
                }
                if(messageText.equals("REVOKEACCESS"))
                {
                    sql.execSQL("delete from student1 where Number = '"+sender+"';" );
                    Toast.makeText(getApplicationContext(),"Access Revoked",Toast.LENGTH_LONG).show();
                }
                if(OTP_REGEX1.equals(messageText))
                {
                    gps = new GPSTracker(MainActivity.this);
                    if(gps.canGetLocation()){

                        double latitude = gps.getLatitude();
                        double longitude = gps.getLongitude();
                        otp="http://maps.google.com/?q="+latitude+","+longitude;
                        // \n is for new line
                        Toast.makeText(getApplicationContext(), "Your Location is - \nLat: "
                                + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                    }else{
                        // can't get location
                        // GPS or Network is not enabled
                        // Ask user to enable GPS/network in settings
                        gps.showSettingsAlert();
                    }
                    Cursor n98=sql.rawQuery("select * from student1 where Number='"+sender+"';",null);
                    if(n98.moveToFirst()) {
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(sender, null, otp, null, null);
                    }
                }
            }
        });

    }
}
