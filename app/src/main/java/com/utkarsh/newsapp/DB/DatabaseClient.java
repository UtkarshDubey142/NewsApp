package com.utkarsh.newsapp.DB;

import android.content.Context;
import androidx.room.Room;

public class DatabaseClient {

    Context context;
    private static DatabaseClient client;
    NewsDatabase newsDatabase;

    // Constructor
    public DatabaseClient(Context context) {
        this.context = context;

        newsDatabase = Room.databaseBuilder(context , NewsDatabase.class , "newsDatabase").build();
    }

    public static synchronized  DatabaseClient getInstance(Context context)
    {
        if (client == null)
        {
            client = new DatabaseClient(context);
        }
        return client;
    }

    public NewsDatabase getNewsDatabase()
    {
        return newsDatabase;
    }





}