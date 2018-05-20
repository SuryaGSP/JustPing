package com.example.surya.smslisten;

/**
 * Created by Surya on 12/22/2017.
 */

public interface SmsListener {
    public  void messageReceived(String messageBody,String sender);
}
