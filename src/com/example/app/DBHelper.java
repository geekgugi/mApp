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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    private final static String TAG = DBHelper.class.getName();

    public static final String DATABASE_NAME = "mApp.db";
    /*
     *   Table@mlog
     *  -----------------------------------------
     *  '_id | caller_number | callee_status_id '
     *  '---------------------------------------'
     *  ' 1  | 7829730769    |   12             '
     *  '                                       '
     *  '----------------------------------------
     *                                ^
     *                                |
     *                                |
     *   Table@mstatus            -----------------------------------
     *                            ' _id | status_message            '
     *                            ' ---------------------------------
     *                            ' 12  | Iam busy! Call me later   '
     *                            '---------------------------------'
     * 
     */
    public static final String TABLE1_NAME  = "mlog";
    public static final String TABLE1_COLUMN_ID = "_id";
    public static final String TABLE1_COLUMN_CALLER = "caller_number";
    public static final String TABLE1_COLUMN_STATUS_ID = "callee_status_id";
    public static final String TABLE2_NAME = "mstatus";
    public static final String TABLE2_COLUMN_ID = "_id";
    public static final String TABLE2_COLUMN_STATUS_MESSAGE = "status_message";

    private static final String TABLE1_CREATE = "create table "
            + TABLE1_NAME + "(" 
            + TABLE1_COLUMN_ID + " integer primary key autoincrement,"
            + TABLE1_COLUMN_CALLER + " text not null " 
            + TABLE1_COLUMN_STATUS_ID + "integer"
            + "FOREIGN KEY(TABLE1_COLUMN_STATUS_ID) REFERENCES TABLE2_NAME(TABLE2_COLUMN_ID)"
            + ");";

    private static final String TABLE2_CREATE = "create table "
            + TABLE1_NAME + "(" 
            + TABLE2_COLUMN_ID + " integer primary key autoincrement,"
            + TABLE2_COLUMN_STATUS_MESSAGE + " text not null " 
            + ");";

    public DBHelper(Context context, String name, CursorFactory factory,
            int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Note Table1 references Table2, good practice to create 
        // referenced table first ?
        db.execSQL(TABLE2_CREATE);
        db.execSQL(TABLE1_CREATE);
     }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        
        Log.i(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE1_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE2_NAME);
        onCreate(db);

    }

}
