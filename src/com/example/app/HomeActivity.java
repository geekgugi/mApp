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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
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