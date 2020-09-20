package com.example.energyefficience;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class Base64Repository {
    private Base64BlindTextDao base64BlindTextDao;
    private LiveData<List<Base64BlindTextEntity>> completeList;

    Base64Repository(Application application){
        MyRoomDatabase db = MyRoomDatabase.getDatabase(application);
        base64BlindTextDao = db.base64BlindTextDao();
        completeList = base64BlindTextDao.getAllEntries();
    }

    public LiveData<List<Base64BlindTextEntity>> getAllEntries()
    {
        return this.completeList;
    }

    public void insert(Base64BlindTextEntity ob){
        new insertAsyncTask(base64BlindTextDao).execute(ob);
    }

    private static class insertAsyncTask extends AsyncTask<Base64BlindTextEntity, Void, Void> {

        private Base64BlindTextDao mAsyncTaskDao;

        insertAsyncTask(Base64BlindTextDao dao)
        {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Base64BlindTextEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
}
