package com.example.app;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;

import android.util.Log;
import android.widget.Toast;

public class ObserverService extends Service {

    private final static String TAG = "ObserverService";
    // indicates how to behave if the service is killed
    int mStartMode;
    // interface for clients that bind
    IBinder mBinder; 
    // indicates whether onRebind should be used
    boolean mAllowRebind; 

    public static String  MESSAGE = "I am busy ! Call me later, Thank you.";

    @Override
    public void onCreate() {
        Log.i(TAG, "Service created");
        // The service is being created
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final TelephonyManager mgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        Log.i(TAG, "Service started");

        Toast.makeText(getBaseContext(), "Service has been started..",
                Toast.LENGTH_SHORT).show();

        PhoneStateListener phoneStateListener = new PhoneStateListener() 
        {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) 
            {
                super.onCallStateChanged(state, incomingNumber);

                if (state == TelephonyManager.CALL_STATE_RINGING)
                {
                    Toast.makeText(getBaseContext(), "Ready To send message",
                            Toast.LENGTH_LONG).show();
                    //Java Reflections
                    try {
                        Class<?> c  = Class.forName(mgr.getClass().getName());
                        Method m = c.getDeclaredMethod("getITelephony");
                        m.setAccessible(true);
                        // Get the internal ITelephony object
                        Object telephonyService = m.invoke(mgr); 
                        // Get its class
                        c = Class.forName(telephonyService.getClass().getName());
                        // Get the "endCall()" method
                        m = c.getDeclaredMethod("endCall");
                        // Make it accessible
                        m.setAccessible(true);
                        // invoke endCall()
                        m.invoke(telephonyService); 
                        // Send message
                        SmsManager sms = SmsManager.getDefault();
                        sms.sendTextMessage(incomingNumber, null, MESSAGE, null, null);
                    } catch (NoSuchMethodException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IllegalArgumentException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } 
            }
        };

        if(mgr != null)
        {
            mgr.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        } 
        return mStartMode;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // A client is binding to the service with bindService()
        Log.i(TAG, "Service binded");
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        // All clients have unbound with unbindService()
        Log.i(TAG, "Service un-binded");
        return mAllowRebind;
    }

    @Override
    public void onRebind(Intent intent) {
        // A client is binding to the service with bindService(),
        // after onUnbind() has already been called
        Log.i(TAG, "Service re-binded");
    }

    @Override
    public void onDestroy() {
        // The service is no longer used and is being destroyed
        Log.i(TAG, "Service destroyed");

    }

}
