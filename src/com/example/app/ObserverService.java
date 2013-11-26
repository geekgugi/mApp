/*
 *  Copyright 2013-2014 Sagar Gugwad <saagar.gugwad@gmail.com>
 *
 *  This file is part of mApp project.
 *
 *  mApp is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  mApp is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with mApp.  If not, see <http://www.gnu.org/licenses/>.
 */
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

    private final static String TAG = ObserverService.class.getName();
    // indicates how to behave if the service is killed
    int mStartMode;
    // interface for clients that bind
    IBinder mBinder; 
    // indicates whether onRebind should be used
    boolean mAllowRebind; 

    private String  message = new String("Hello ! Iam working -- Sagar");
    private int  messageId;
    private DataSource mDataSource;

    @Override
    public void onCreate() {
        Log.i(TAG, "Service created");
        // The service is being created
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i(TAG, "Service started");
        Toast.makeText(getBaseContext(), "Service has been started..",
                Toast.LENGTH_SHORT).show();
        
        final TelephonyManager mgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        // Open the database again ? Bullshit design
        mDataSource= new DataSource(getApplicationContext());
        mDataSource.open();
        messageId = mDataSource.getStatusId(message);
        

        PhoneStateListener phoneStateListener = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                super.onCallStateChanged(state, incomingNumber);

                if (state == TelephonyManager.CALL_STATE_RINGING) {
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
                        sms.sendTextMessage(incomingNumber, null, message, null, null);
                        mDataSource.createLog(messageId, incomingNumber);
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
