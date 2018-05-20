package com.example.surya.smslisten;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class FetchCode extends AppCompatActivity {
    Button b1;
    TextView t1;
    public static final int RQS_PICK_CONTACT = 1;
    SQLiteDatabase sql;
    String code1="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetch_code);
        t1=(TextView) findViewById(R.id.contactnumber);
        b1=(Button) findViewById(R.id.pickcontact);
        sql=openOrCreateDatabase("Details",MODE_PRIVATE,null);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
                startActivityForResult(intent, 1);
            }
        });
    }
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == RQS_PICK_CONTACT){
            if(resultCode == RESULT_OK){
                Uri contactData = data.getData();
                Cursor cursor =  managedQuery(contactData, null, null, null, null);
                cursor.moveToFirst();
                final String number= cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
                t1.setText(number);
                Button b3=(Button) findViewById(R.id.button78);
                b3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Cursor c=sql.rawQuery("select * from number;",null);
                        if(c.moveToFirst())
                        {
                            code1=c.getString(0);
                            SmsManager smsManager = SmsManager.getDefault();
                            smsManager.sendTextMessage(number, null, "ACCESSREQUEST", null, null);
                            t1.setText(number+"REQUEST SENT");
                        }
                    }
                });
            }
        }
    }
}
