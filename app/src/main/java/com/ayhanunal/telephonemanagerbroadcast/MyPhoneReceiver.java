package com.ayhanunal.telephonemanagerbroadcast;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class MyPhoneReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(final Context context, Intent intent) {


        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

  

        telephonyManager.listen(new PhoneStateListener() {

            @Override
            public void onCallStateChanged(int state, String phoneNumber) {
                super.onCallStateChanged(state, phoneNumber);

                LocalBroadcastManager manager = LocalBroadcastManager.getInstance(context);

                Intent intentToMain = new Intent("my.result.receiver");
                intentToMain.putExtra("incomingNumber", phoneNumber);
                manager.sendBroadcast(intentToMain);

            }
        }, PhoneStateListener.LISTEN_CALL_STATE);

    }
}
