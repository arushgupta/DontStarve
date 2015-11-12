package com.cschefs.dontstarve;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class SearchableActivity extends AppCompatActivity {
    /** ListView listSearch links to list_ingredients_search. Displays list of ingredients */
    private ListView listSearch;
    /** EditText inputSearch links to input_search. Displays search bar */
    private EditText inputSearch;
    /** String ingredient_name parses the selected ingredients name */
    private String ingredient_name;
    /** Other variables needed */
    private ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Automatically call to set up the activity page.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);
        //Set up the list of ingredients
        String ingredients[] = getResources().getStringArray(R.array.string_ingredients);
        //Set up the UI
        listSearch = (ListView) findViewById(R.id.list_ingredients_search);
        inputSearch = (EditText) findViewById(R.id.input_search);
        Button backBtn = (Button) findViewById(R.id.back_button_search);
        Button addBtn = (Button) findViewById(R.id.add_button_search);
        //Add items and adapter to ListView
        adapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.ingredient_name, ingredients);
        listSearch.setAdapter(adapter);
        //Text watcher is used to filter the search and display as you type.
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changes text in inputSearch
                SearchableActivity.this.adapter.getFilter().filter(cs);
            }
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub
            }
            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });
        //Set the selected Ingredient to the search bar.
        listSearch.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3)
            {
                Object ingredient_obj = arg0.getItemAtPosition(arg2);
                ingredient_name = ingredient_obj.toString();
                inputSearch.setText(ingredient_name);
            }});
        //End the activity and go back.
        backBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                finish();
            }
        });
        //Add the selected item.
        addBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent search_done = getIntent();
                search_done.putExtra("received_ingredient", ingredient_name);
                setResult(100, search_done);
                finish();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the options menu from XML
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }
}