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

import java.util.List;

import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;

/**
 * HomeActivity for mApp
 * It is the first activity to be executed once the app is run. It creates two 
 * buttons to start and stop the service.
 * 
 * @package com.example.app
 * @author  Sagar Gugwad
 * @see github.com/geekgugi/mApp.git
 *
 */
public class HomeActivity extends Activity implements OnClickListener {

    private final static String TAG = HomeActivity.class.getName();
    private Button startSerivce = null;
    private Button stopSerivce = null;
    private Status mStatus = null;
    private DataSource mDataSource = null;
    private int mStatusId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // Shall we add the templates to databases here ?
        // bad design though ?
        mDataSource= new DataSource(this, "mApp.db", null, 1);
        mDataSource.open();

        mStatusId = mDataSource.createStatus("Hello ! Iam working -- Sagar");
        
        List<CallLog> logs = mDataSource.getAllLogs();

        // use the SimpleCursorAdapter to show the
        // elements in a ListView
        ArrayAdapter<CallLog> adapter = new ArrayAdapter<CallLog>(this,
            android.R.layout.simple_list_item_1, logs);
        setListAdapter(adapter);
        mDataSource.close();

        startSerivce = (Button) findViewById(R.id.buttonStart);
        startSerivce.setOnClickListener(this);
        stopSerivce = (Button) findViewById(R.id.buttonStop);
        stopSerivce.setOnClickListener(this);
    }

    private void setListAdapter(ArrayAdapter<CallLog> adapter) {
        // TODO Auto-generated method stub
        
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