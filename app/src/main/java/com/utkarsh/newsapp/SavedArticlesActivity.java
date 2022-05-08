package com.utkarsh.newsapp;

import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.utkarsh.newsapp.Adapters.SavedArticlesAdapter;
import com.utkarsh.newsapp.DB.DatabaseClient;
import com.utkarsh.newsapp.DB.NewsTable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SavedArticlesActivity extends AppCompatActivity {
    private ArrayList<NewsTable> newsArticles;
    private SavedArticlesAdapter savedArticlesAdapter;
    private final String TAG = "SavedArticlesActivity";
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_articles);

        Log.d(TAG, "onCreate: Activity Started");
        RecyclerView saved = findViewById(R.id.idRVNews);
        saved.setLayoutManager(new LinearLayoutManager(this));
        savedArticlesAdapter = new SavedArticlesAdapter(this);
        saved.setAdapter(savedArticlesAdapter);
        executorService.submit(() -> {
            List<NewsTable> savedArticles = DatabaseClient.getInstance(SavedArticlesActivity.this)
                    .getNewsDatabase()
                    .newsDao().selectAll();
            Log.d(TAG, "onCreate: size = " + savedArticles.size());
            runOnUiThread(() -> savedArticlesAdapter.setItems(savedArticles));
        });


    }
}