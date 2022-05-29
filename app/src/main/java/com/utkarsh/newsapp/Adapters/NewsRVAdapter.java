package com.utkarsh.newsapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.utkarsh.newsapp.Articles;
import com.utkarsh.newsapp.DB.DatabaseClient;
import com.utkarsh.newsapp.DB.NewsTable;
import com.utkarsh.newsapp.NewsDetailActivity;
import com.utkarsh.newsapp.R;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static android.content.Context.MODE_PRIVATE;
import static com.utkarsh.newsapp.MainActivity.LANGUAGE;

public class NewsRVAdapter extends RecyclerView.Adapter<NewsRVAdapter.ViewHolder>{

    private ArrayList<Articles> articlesArrayList;
    private Context context;
    private TextToSpeech textToSpeech;
    SharedPreferences sharedPreferences;
    private String message = "";
    private int speakPoint = -1;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    Handler mainHandler = new Handler(Looper.getMainLooper());   ;

    // Constructor
    public NewsRVAdapter(ArrayList<Articles> articlesArrayList, Context context) {
        this.articlesArrayList = articlesArrayList;
        this.context = context;
        this.speakPoint = -1;

        textToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS)
                {
                    String language = getLanguageVoice();
                    int result = textToSpeech.setLanguage(Locale.forLanguageTag(language));
                    Log.i("selected " , ""+result + " - " + language);

                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED)
                    {
                        Log.e("TTS", "This Language is not supported");
                    }
                }
                else {
                    Log.e("TTS", "Initilization Failed!");
                }
            }
        });
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
        holder.pubDate.setText("Date: " +articles.getPubDate().substring(0,16));
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

        /*
        textToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS)
                {
                    String language = getLanguageVoice();
                    int result = textToSpeech.setLanguage(Locale.forLanguageTag(language));
                    Log.i("selected " , ""+result + " - " + language);

                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED)
                    {
                        Log.e("TTS", "This Language is not supported");
                    }
                }
                else {
                    Log.e("TTS", "Initilization Failed!");
                }
            }
        });

        // Text-to-speech part
        holder.readAloud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textToSpeech.setSpeechRate(1.0f);
                textToSpeech.speak(message , TextToSpeech.QUEUE_FLUSH , null , null);
            }
        });
         */

        // Text-to-speech part
        holder.readAloud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((speakPoint == holder.getAdapterPosition()) && (textToSpeech.isSpeaking()))
                {
                    textToSpeech.stop();
                }
                else
                {
                    String message = "" ;
                    message = message + articles.getTitle() + " ";
                    if (articles.getDescription().equals("null"))
                    {
                        message = message + "Read Complete News";
                    }
                    else
                    {
                        message = message + articles.getDescription();
                    }
                    Log.i("selected " , ""+message);
                    textToSpeech.setSpeechRate(0.9f);
                    speakPoint = holder.getAdapterPosition();
                    textToSpeech.speak(message , TextToSpeech.QUEUE_FLUSH , null , null);
                }
            }
        });


        executorService.submit(() -> {
            NewsTable nt = DatabaseClient.getInstance(context)
                    .getNewsDatabase()
                    .newsDao().getSpecificArticle(articles.getUrl());
            if(nt!=null){
                mainHandler.post(() -> holder.savedArticle_Option.setImageResource(R.drawable.bookmark_added_icon));
            }

        });

        // BookMark Option (Saved Article)
        holder.savedArticle_Option.setOnClickListener(v -> {

            executorService.submit(() -> {
                NewsTable nt = DatabaseClient.getInstance(context)
                        .getNewsDatabase()
                        .newsDao().getSpecificArticle(articles.getUrl());

                if(nt!=null){
                   DatabaseClient.getInstance(context)
                            .getNewsDatabase()
                            .newsDao().deleteData(nt);
                    mainHandler.post(() -> holder.savedArticle_Option.setImageResource(R.drawable.blank_bookmark_icon));
                    Toast.makeText(context, "Article Removed !" , Toast.LENGTH_LONG).show();
                    return;
                }



                NewsTable article = new NewsTable();
                article.setLink(articles.getUrl());
                article.setTitle(articles.getTitle());

                DatabaseClient.getInstance(context)
                        .getNewsDatabase()
                        .newsDao()
                        .insetData(article);
            });
            holder.savedArticle_Option.setImageResource(R.drawable.bookmark_added_icon);
            Toast.makeText(context, "Article Saved !" , Toast.LENGTH_LONG).show();
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context , NewsDetailActivity.class);
                i.putExtra("title" , articles.getTitle());
                i.putExtra("desc" , articles.getDescription());
                i.putExtra("image" , articles.getUrlToImage());
                i.putExtra("url" , articles.getUrl());
                i.putExtra("date" , articles.getPubDate().substring(0,16));
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return articlesArrayList.size();
    }

    // Method to stop text-to-speech on category change
    public void stopSpeechOnChangeCategory ()
    {
        if (textToSpeech.isSpeaking())
        {
            textToSpeech.stop();
        }
    }


    // Inner class
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTV , subTitleTV , pubDate;
        private ImageView newsIV , shareOpt , readAloud , savedArticle_Option;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            newsIV = itemView.findViewById(R.id.idIVNews);
            shareOpt = itemView.findViewById(R.id.shareOption);
            pubDate = itemView.findViewById(R.id.idTVPubDate);
            titleTV = itemView.findViewById(R.id.idTVNewsHeading);
            subTitleTV = itemView.findViewById(R.id.idTVSubTitle);
            readAloud = itemView.findViewById(R.id.read_aloud_iconID);
            savedArticle_Option = itemView.findViewById(R.id.bookMark_optionID);
        }
    }

    public String getLanguageVoice ()
    {
        sharedPreferences = context.getApplicationContext().getSharedPreferences("UserLanguage", MODE_PRIVATE);
        String local_lang = sharedPreferences.getString(LANGUAGE,"en");
        switch (local_lang) {
            case "fr":
                return "fr";
            case "de":
                return "de";
            case "jp":
                return "ja";
            case "en":
                return "en";
        }
        return local_lang;
    }
}
