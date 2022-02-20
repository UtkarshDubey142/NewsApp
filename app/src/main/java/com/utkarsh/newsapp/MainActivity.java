package com.utkarsh.newsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import com.utkarsh.newsapp.Adapters.CategoryRVAdapter;
import com.utkarsh.newsapp.Adapters.NewsRVAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements CategoryRVAdapter.CategoryClickInterface, NavigationView.OnNavigationItemSelectedListener{

    private RecyclerView newsRV , categoryRV;
    private ImageView refreshBtn;
    private ProgressBar loadingPB;
    private ArrayList<Articles> articelsArrayList;
    private ArrayList<CategoryRVModal> categoryRVModalArrayList;
    private CategoryRVAdapter categoryRVAdapter;
    private NewsRVAdapter newsRVAdapter;
    DrawerLayout mDrawerLayout;
    SharedPreferences sharedPreferences;
    public static String LANGUAGE = "lang";
    String [] category_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences("UserLanguage", MODE_PRIVATE);
        refreshBtn = findViewById(R.id.refresh_image_icon);
        categoryRV = findViewById(R.id.idRVCategory);
        newsRV = findViewById(R.id.idRVNews);
        loadingPB = findViewById(R.id.idPBLoading);
        articelsArrayList = new ArrayList<>();
        categoryRVModalArrayList = new ArrayList<>();
        newsRVAdapter = new NewsRVAdapter(articelsArrayList,this);
        categoryRVAdapter = new CategoryRVAdapter(categoryRVModalArrayList, this , this::onCategoryClick);
        newsRV.setLayoutManager(new LinearLayoutManager(this));
        newsRV.setAdapter(newsRVAdapter);
        categoryRV.setAdapter(categoryRVAdapter);

        Toolbar toolbar = findViewById(R.id.feed_toolbar);
        mDrawerLayout = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.open, R.string.close);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.yellow));
        mDrawerLayout.addDrawerListener(toggle);
        NavigationView navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);
        Menu menu = navigationView.getMenu();

        // changing navigation drawer menu title color
        MenuItem tools= menu.findItem(R.id.nav_menu_languageID);
        SpannableString s = new SpannableString(tools.getTitle());
        s.setSpan(new TextAppearanceSpan(this, R.style.TextAppearanceForMenuItem_title), 0, s.length(), 0);
        tools.setTitle(s);
        toggle.syncState();

        // Calling method for category and news
        getCategory();
        getNews("All");
        newsRVAdapter.notifyDataSetChanged();

        // Refresh Operation
        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCategory();
                getNews("All");
            }
        });

    }

    private void getCategory ()
    {
        String language_category = getLanguage();
        categoryRVModalArrayList.clear();
        Log.i("language >>" , language_category+"");
        switch (language_category) {
            case "fr":
                category_name = getResources().getStringArray(R.array.category_array_french);
                break;
            case "de":
                category_name = getResources().getStringArray(R.array.category_array_german);
                break;
            case "jp":
                category_name = getResources().getStringArray(R.array.category_array_japanese);
                break;
            case "en":
                category_name = getResources().getStringArray(R.array.category_array_english);
                break;
        }

        categoryRVModalArrayList.add(new CategoryRVModal(category_name[0] , "https://images.unsplash.com/photo-1585829365295-ab7cd400c167?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=870&q=80"));
        categoryRVModalArrayList.add(new CategoryRVModal(category_name[1] , "https://images.unsplash.com/photo-1572949645841-094f3a9c4c94?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=387&q=80"));
        categoryRVModalArrayList.add(new CategoryRVModal(category_name[2] , "https://images.unsplash.com/photo-1461749280684-dccba630e2f6?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=869&q=80"));
        categoryRVModalArrayList.add(new CategoryRVModal(category_name[3] , "https://images.unsplash.com/photo-1564325724739-bae0bd08762c?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=870&q=80"));
        categoryRVModalArrayList.add(new CategoryRVModal(category_name[4] , "https://images.unsplash.com/photo-1611251126118-b1d4f99600a1?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=870&q=80"));
        categoryRVModalArrayList.add(new CategoryRVModal(category_name[5] , "https://images.unsplash.com/photo-1444653614773-995cb1ef9efa?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=876&q=80"));
        categoryRVModalArrayList.add(new CategoryRVModal(category_name[6] , "https://images.unsplash.com/photo-1586899028174-e7098604235b?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=871&q=80"));
        categoryRVModalArrayList.add(new CategoryRVModal(category_name[7] , "https://images.unsplash.com/photo-1506126613408-eca07ce68773?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=499&q=80"));
        categoryRVAdapter.notifyDataSetChanged();
    }

    public void getNews (String category)
    {
        loadingPB.setVisibility(View.VISIBLE);
        articelsArrayList.clear();
        String selectedLang = sharedPreferences.getString(LANGUAGE,"en");
        String url = "https://newsdata.io/api/1/news?apikey=pub_1672b7063256110c681c9ef2584701cb7b4d&language="+ selectedLang;
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        if (!(category.equals("All")))
        {
            url = "https://newsdata.io/api/1/news?apikey=pub_1672b7063256110c681c9ef2584701cb7b4d&language="+selectedLang+"&category="+category;
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try
                        {
                            loadingPB.setVisibility(View.GONE);
                            JSONArray jsonArray = response.getJSONArray("results");
                            for (int i = 0 ; i < jsonArray.length() ; i++)
                            {
                                JSONObject currObj = jsonArray.getJSONObject(i);
                                articelsArrayList.add(new Articles(currObj.getString("title"),currObj.getString("description"),currObj.getString("image_url"),currObj.getString("link"),currObj.getString("content"),currObj.getString("pubDate")));
                            }

                            newsRVAdapter.notifyDataSetChanged();
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse.statusCode == 401)
                        {
                            Toast.makeText(MainActivity.this , "Check Network" , Toast.LENGTH_LONG).show();
                        }
                        Toast.makeText(MainActivity.this , "Failed To Get News" , Toast.LENGTH_LONG).show();
                    }
                });

        // Access the RequestQueue through your singleton class.
        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }

    @Override
    public void onCategoryClick(int position) {
        String category = categoryRVModalArrayList.get(position).getCategory();
        sharedPreferences.edit().putString("categories",category).apply();
        getNews(category);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        if (item.getItemId() == R.id.english)
        {
            sharedPreferences.edit().putString(LANGUAGE, "en").apply();
            getCategory();
            getNews("All");
        }
        else if(item.getItemId() == R.id.japanese)
        {
            sharedPreferences.edit().putString(LANGUAGE, "jp").apply();
            getCategory();
            getNews("All");
        }
        else if(item.getItemId() == R.id.german)
        {
            sharedPreferences.edit().putString(LANGUAGE, "de").apply();
            getCategory();
            getNews("All");
        }
        else if(item.getItemId() == R.id.french)
        {
            sharedPreferences.edit().putString(LANGUAGE, "fr").apply();
            getCategory();
            getNews("All");
        }
        //close navigation drawer
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    public String getLanguage()
    {
        return sharedPreferences.getString(LANGUAGE,"en");
    }

 }