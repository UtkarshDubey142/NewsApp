package com.utkarsh.newsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class UserInfoActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    String[] countries = {"India", "Russia", "Japan", "America", "China", "Germany","New-zealand"};
    String[] spinnerCategories = {"Top", "Technology", "Science", "Sports", "Business","Entertainment","Health"};
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        sharedPreferences = getSharedPreferences("UserLanguage", MODE_PRIVATE);

        // Targeting Spinner via ID
        Spinner countrySpinner = findViewById(R.id.countrySpinner_ID);
        Spinner categorySpinner = findViewById(R.id.categorySpinner_ID);
        Button userInfoButton = findViewById(R.id.save_UserInfo_ButtonID);

        countrySpinner.setOnItemSelectedListener(this);
        categorySpinner.setOnItemSelectedListener(this);

        ArrayAdapter<String> countrySpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, countries);
        ArrayAdapter<String> categorySpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerCategories);

        // Drop down layout style - list view with radio button
        countrySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        // attaching data adapter to spinner
        countrySpinner.setAdapter(countrySpinnerAdapter);
        categorySpinner.setAdapter(categorySpinnerAdapter);

        // Save Button Action
        userInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainPageIntent = new Intent(UserInfoActivity.this , MainActivity.class);
                sharedPreferences.edit().putString("country" , getCountryValue(countrySpinner.getSelectedItem().toString())).apply();
                sharedPreferences.edit().putString("preferred_category" , categorySpinner.getSelectedItem().toString()).apply();
                startActivity(mainPageIntent);
            }
        });

    }

    // to get country code
    public String getCountryValue (String countryName)
    {
        switch (countryName)
        {
            case "India":
                return "in";
            case "Russia":
                return "ru";
            case "Japan":
                return "jp";
            case "America":
                return "us";
            case "China":
                return "cn";
            case "Germany":
                return "de";
            case "New zealand":
                return "nz";
        }
        return "in";
    }



    @SuppressLint("NonConstantResourceId")
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        String item = parent.getItemAtPosition(position).toString();
        switch (parent.getId())
        {
            case R.id.countrySpinner_ID:
                // Showing selected spinner item
                break;
            case R.id.categorySpinner_ID:
                break;
        }
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Do Nothing
    }
}