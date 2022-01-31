package com.utkarsh.newsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class NewsDetailActivity extends AppCompatActivity {

    private String title , desc , imageURL , url , date;
    private TextView titleTV , subDescTV , pubDate;
    private ImageView newsIV;
    private Button readNewsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        title = getIntent().getStringExtra("title");
        desc = getIntent().getStringExtra("desc");
        imageURL = getIntent().getStringExtra("image");
        url = getIntent().getStringExtra("url");
        date = getIntent().getStringExtra("date");

        titleTV = findViewById(R.id.idTVTitle);
        subDescTV = findViewById(R.id.idTVSubDesc);
        newsIV = findViewById(R.id.idIVNews);
        pubDate = findViewById(R.id.idTVPubDate);
        readNewsBtn = findViewById(R.id.idBtnReadNews);

        titleTV.setText(title);
        subDescTV.setText(desc);
        pubDate.setText("Date: "+date);
        Glide.with(this).load(imageURL).error(R.drawable.newsimage).into(newsIV);
        readNewsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
    }
}