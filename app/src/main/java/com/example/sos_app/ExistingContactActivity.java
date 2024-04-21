package com.example.sos_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ExistingContactActivity extends AppCompatActivity {

    ListView lv;
    private ArrayList<String> selectedList;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_existing_contact);

        lv = findViewById(R.id.listView);
        selectedList =  new ArrayList<>();

        Type type = new TypeToken<List<String>>() {
        }.getType();
        selectedList = new Gson().fromJson(getIntent().getStringExtra("contact_list2"), type);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, selectedList);
        lv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}