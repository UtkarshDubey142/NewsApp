package com.utkarsh.newsapp.DB;

import androidx.room.*;

import java.util.List;

@Dao
public interface NewsDao {
    @Insert
    void insetData (NewsTable newsTable);

    // Getting All Data
    @Query("SELECT * FROM newstable")
    List<NewsTable> selectAll ();

    @Query("Select * from NewsTable where link=:link")
    NewsTable getSpecificArticle(String link);

    // Update Data
    @Update
    void updateData (NewsTable studentTable);

    // Delete Data
    @Delete
    void deleteData (NewsTable studentTable);
}
