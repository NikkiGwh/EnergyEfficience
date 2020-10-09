package com.example.energyefficience;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Base64BlindTextEntity.class, MergeSortNumberEntity.class}, version=1, exportSchema = false)
public abstract class MyRoomDatabase extends RoomDatabase {
    private static MyRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);


            databaseWriteExecutor.execute(new Runnable() {
                @Override
                public void run() {


                    // Populate the database in the background.
                    // If you want to start with more words, just add them.
                    //Base64BlindTextDao dao = INSTANCE.base64BlindTextDao();
                    MergeSortNumberDao dao = INSTANCE.mergeSortNumberDao();
                    //dao.deleteAll();
                    List<MergeSortNumberEntity> li = new ArrayList<MergeSortNumberEntity>();
                    li.add(new MergeSortNumberEntity(0, 33434));
                    li.add(new MergeSortNumberEntity(1, 34534));
                   // Base64BlindTextEntity word = new Base64BlindTextEntity(1, text_1,"encoded");
                    dao.insertAll(li);
                   // word = new Base64BlindTextEntity(2, "welt","encoded");
                   // dao.insert(word);
                }
            });
        }};


        public static MyRoomDatabase getDatabase(final Context context) {
            if (INSTANCE == null) {
                synchronized (MyRoomDatabase.class) {
                    if (INSTANCE == null) {
                        //create database here
                        INSTANCE = Room.databaseBuilder(context.getApplicationContext(), MyRoomDatabase.class, "bachelor_database")./*fallbackToDestructiveMigration().addCallback(sRoomDatabaseCallback).*/build();

                    }
                }
            }
            return INSTANCE;
        }
        public abstract MergeSortNumberDao mergeSortNumberDao();
        public abstract Base64BlindTextDao base64BlindTextDao();
    }
