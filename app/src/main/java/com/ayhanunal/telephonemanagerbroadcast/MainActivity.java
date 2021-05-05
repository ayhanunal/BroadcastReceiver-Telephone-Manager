package com.ayhanunal.telephonemanagerbroadcast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    TextView callerText;

    LocalBroadcastManager manager;

    private String caller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        callerText = findViewById(R.id.callerText);

        manager = LocalBroadcastManager.getInstance(this);

        caller = "";



    }


    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter intentFilter = new IntentFilter("my.result.receiver");

        manager.registerReceiver(receiver,intentFilter);

    }


    @Override
    protected void onPause() {
        super.onPause();

        manager.unregisterReceiver(receiver);
    }


    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String incomingNumber = intent.getStringExtra("incomingNumber");
            System.out.println("incoming number :"+incomingNumber);
            matchContact(incomingNumber);

        }
    };

    private void matchContact(String number){

        ArrayList<String> namesFromDB = new ArrayList<String>();
        ArrayList<String> numbersFromDB = new ArrayList<String>();

        HashMap<String,String> contactInfo = new HashMap<String, String>();



        SQLiteDatabase sqLiteDatabase = this.openOrCreateDatabase("com.ayhanunal.telephonemanagerbroadcast", MODE_PRIVATE,null);

        sqLiteDatabase.execSQL("create table if not exists phonebook (name varchar, number varchar)");
        sqLiteDatabase.execSQL("insert into phonebook(name,number) values ('ayhan','05373443749')");
        sqLiteDatabase.execSQL("insert into phonebook(name,number) values ('jessica','05373443774')");
        sqLiteDatabase.execSQL("insert into phonebook(name,number) values ('jane','05373471749')");

        Cursor cursor = sqLiteDatabase.rawQuery("select * from phonebook",null);

        int nameIX = cursor.getColumnIndex("name");
        int numberIX = cursor.getColumnIndex("number");

        while (cursor.moveToNext()){

            String nameFromDB = cursor.getString(nameIX);
            String numberFromDB = cursor.getString(numberIX);

            namesFromDB.add(nameFromDB);
            numbersFromDB.add(numberFromDB);

            contactInfo.put(numberFromDB,nameFromDB);

        }

        cursor.close();

        for(String s : numbersFromDB){

            if(s.matches(number)){
                //eslesme var.
                caller = contactInfo.get(number);
                callerText.setText("caller: "+caller);
            }else {
                caller = "";
            }

        }





    }

}