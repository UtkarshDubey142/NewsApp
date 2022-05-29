package com.utkarsh.newsapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.utkarsh.newsapp.DB.DatabaseClient;
import com.utkarsh.newsapp.DB.NewsTable;
import com.utkarsh.newsapp.R;
import com.utkarsh.newsapp.SavedArticlesActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SavedArticlesAdapter extends RecyclerView.Adapter<SavedArticlesAdapter.ViewHolder>{

    private List<NewsTable> articlesArrayList = new ArrayList<>();
    private final Context context;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    Handler mainHandler = new Handler(Looper.getMainLooper());   ;

    public SavedArticlesAdapter(Context context){
        this.context = context;
    }

    public void setItems(List<NewsTable> articles){
        articlesArrayList = articles;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SavedArticlesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.saved_article_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedArticlesAdapter.ViewHolder holder, int position) {
        NewsTable article = articlesArrayList.get(position);
        holder.title.setText(article.getTitle());
        holder.title.setOnClickListener(v -> openBrowser(article.getLink()));
        holder.deleteButton.setOnClickListener(v ->{
            executorService.submit(() -> {
                NewsTable nt = DatabaseClient.getInstance(context)
                        .getNewsDatabase()
                        .newsDao().getSpecificArticle(article.getLink());
                if(nt!=null){
                    DatabaseClient.getInstance(context)
                            .getNewsDatabase()
                            .newsDao().deleteData(nt);


                    List<NewsTable> savedArticles = DatabaseClient.getInstance(context)
                            .getNewsDatabase()
                            .newsDao().selectAll();
                    articlesArrayList = savedArticles;

                    mainHandler.post(() -> notifyDataSetChanged());

                }

            });


        });
    }


    private void openBrowser(String url){
        if (!url.startsWith("https://") && !url.startsWith("http://")){
            url = "https://" + url;
        }
        Intent openUrlIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(openUrlIntent);
    }

    @Override
    public int getItemCount() {
        return articlesArrayList.size();
    }

    // Inner class
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView deleteButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.idTVPubDate);
            deleteButton = itemView.findViewById(R.id.read_aloud_iconID);
        }
    }
}
