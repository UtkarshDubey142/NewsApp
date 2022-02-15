package com.utkarsh.newsapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.utkarsh.newsapp.Articles;
import com.utkarsh.newsapp.NewsDetailActivity;
import com.utkarsh.newsapp.R;

import java.util.ArrayList;

public class NewsRVAdapter extends RecyclerView.Adapter<NewsRVAdapter.ViewHolder> {

    private ArrayList<Articles> articlesArrayList;
    private Context context;

    // Constructor
    public NewsRVAdapter(ArrayList<Articles> articlesArrayList, Context context) {
        this.articlesArrayList = articlesArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public NewsRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_rv_item,parent,false);
        return new NewsRVAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsRVAdapter.ViewHolder holder, int position) {
        Articles articles = articlesArrayList.get(position);
        if (articles.getDescription().equals("null"))
        {
            holder.subTitleTV.setText(R.string.readFullNews);
        }
        else
        {
            holder.subTitleTV.setText(articles.getDescription());
        }
        holder.titleTV.setText(articles.getTitle());
        holder.pubDate.setText("Date: " +articles.getPubDate());
        Glide.with(context).load(articles.getUrlToImage()).error(R.drawable.newsimage).into(holder.newsIV);
        // Share Button
        holder.shareOpt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareLink = new Intent(Intent.ACTION_SEND);
                shareLink.setType("text/plain");
                shareLink.putExtra(Intent.EXTRA_TEXT, articles.getUrl()+"");
                // passing subject of the content
                Intent shareIntent = Intent.createChooser(shareLink, "Share Using");
                context.startActivity(shareIntent);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context , NewsDetailActivity.class);
                i.putExtra("title" , articles.getTitle());
                i.putExtra("desc" , articles.getDescription());
                i.putExtra("image" , articles.getUrlToImage());
                i.putExtra("url" , articles.getUrl());
                i.putExtra("date" , articles.getPubDate());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return articlesArrayList.size();
    }

    // Inner class
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTV , subTitleTV , pubDate;
        private ImageView newsIV , shareOpt;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            newsIV = itemView.findViewById(R.id.idIVNews);
            shareOpt = itemView.findViewById(R.id.shareOption);
            pubDate = itemView.findViewById(R.id.idTVPubDate);
            titleTV = itemView.findViewById(R.id.idTVNewsHeading);
            subTitleTV = itemView.findViewById(R.id.idTVSubTitle);
        }
    }
}
