package com.example.surya.smslisten;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
public class SmsReceiver extends BroadcastReceiver {

    private static SmsListener mListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle data  = intent.getExtras();
        Object[] pdus = (Object[]) data.get("pdus");
        for(int i=0;i<pdus.length;i++){
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);
            //Check the sender to filter messages which we require to readca
            String sender=smsMessage.getDisplayOriginatingAddress();
            String messageBody = smsMessage.getMessageBody();
            //Pass the message text to interface
            mListener.messageReceived(messageBody,sender);
        }

}
    public static void bindListener(SmsListener listener) {
        mListener = listener;
    }
}
