package com.example.app;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class HomeActivity extends Activity 
implements OnClickListener {
    private final static String TAG = "In this method: ";
    private Button startSerivce = null;
    private Button stopSerivce = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        startSerivce = (Button) findViewById(R.id.buttonStart);
        startSerivce.setOnClickListener(this);
        stopSerivce = (Button) findViewById(R.id.buttonStop);
        stopSerivce.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (startSerivce == v) {
            Log.i(TAG, "Activity starting service..");
            Intent serviceIntent = new Intent(this, ObserverService.class);
            startService(serviceIntent);
        } else {
            Intent in = new Intent(this, ObserverService.class);
            in.setAction("stop");
            stopService(in);
        }
    }
}