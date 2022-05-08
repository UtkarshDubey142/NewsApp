package com.utkarsh.newsapp.DB;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {NewsTable.class}, version = 1)
public abstract class NewsDatabase extends RoomDatabase {

    public abstract NewsDao newsDao ();
}