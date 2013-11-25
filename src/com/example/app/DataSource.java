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

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class DataSource {

    private static final String DB_VERSION = "1.0";
    private SQLiteDatabase database;
    private DBHelper dbHelper;
    private String[] table1AllColumns = { DBHelper.TABLE1_COLUMN_ID,
            DBHelper.TABLE1_COLUMN_CALLER,
            DBHelper.TABLE1_COLUMN_STATUS_ID};
    private String[] table2AllColumns = { DBHelper.TABLE2_COLUMN_ID,
            DBHelper.TABLE2_COLUMN_STATUS_MESSAGE};

    public DataSource(Context context, String name, CursorFactory factory,
            int version) {
        dbHelper = new DBHelper(context, name, factory, version);
    }


    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public int  createStatus(String statusMessage) {
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.TABLE2_COLUMN_STATUS_MESSAGE, statusMessage);
        int insertId = (int) database.insert(DBHelper.TABLE2_NAME,null, cv);
        return insertId;
        
    }
    public CallLog createLog(int statusId, String number) {
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.TABLE1_COLUMN_STATUS_ID, statusId);
        cv.put(DBHelper.TABLE1_COLUMN_CALLER, number);
        long insertId = database.insert(DBHelper.TABLE1_NAME, null, cv);
        Cursor cursor = database.query(DBHelper.TABLE1_NAME,
                table1AllColumns, DBHelper.TABLE1_COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        CallLog newLog = cursorToLog(cursor);
        cursor.close();
        return newLog;
    }

    public int getStatusId(String statusMessage) {
        String[] columns = {DBHelper.TABLE2_COLUMN_ID};
        Cursor cursor =  database.query(DBHelper.TABLE2_NAME,
                columns, "WHERE " + DBHelper.TABLE2_COLUMN_STATUS_MESSAGE +"="+statusMessage, null,null, null, null);
        return cursor.getInt(0);
    }


    public List<CallLog> getAllLogs() {
        List<CallLog> callLogs = new ArrayList<CallLog>();

        Cursor cursor = database.query(DBHelper.TABLE1_NAME,
                table1AllColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            CallLog log = cursorToLog(cursor);
            callLogs.add(log);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return callLogs;
    }

    private CallLog cursorToLog(Cursor cursor) {
        CallLog newLog = new CallLog();
        newLog.setId(cursor.getInt(0));
        newLog.setCallerNumber(cursor.getString(1));
        newLog.setCalleeStatusId(cursor.getString(2));
        return newLog;
    }

}
