package com.example.prodigalson7.showme.concurrency;

import android.os.AsyncTask;

import com.example.prodigalson7.showme.DataBase.DataBaseConnector;
import com.example.prodigalson7.showme.Model.Util;

/**
 * Created by ProdigaLsON7 on 20/03/2018.
 */

public class ClearDB extends AsyncTask {

    private DataBaseConnector contactsDB;
    public ClearDB( DataBaseConnector contactsDB) {
        this.contactsDB = contactsDB;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        contactsDB.delete_all_destinations();
        Util.getInstance().setConcurrencyFlag(false);
        return null;
    }
}
