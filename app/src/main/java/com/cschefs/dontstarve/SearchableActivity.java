package com.cschefs.dontstarve;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.HashMap;

public class SearchableActivity extends AppCompatActivity {

    /** Listview listSearch links to list_ingredients_search
     *  EditText inputSearch links to input_search  */
    private ListView listSearch;
    EditText inputSearch;

    ArrayAdapter<String> adapter;
    ArrayList<HashMap<String, String>> ingredientList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);

        String ingredients[] = getResources().getStringArray(R.array.string_ingredients);

        //Listview and EditText set up
        listSearch = (ListView) findViewById(R.id.list_ingredients_search);
        inputSearch = (EditText) findViewById(R.id.input_search);

        // Adding items to ListView
        adapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.ingredient_name, ingredients);
        listSearch.setAdapter(adapter);

        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
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

        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            if (doMySearch(query)){
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the options menu from XML
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }
    protected boolean doMySearch(String query) {
        boolean found = false;
        ArrayList al = new ArrayList();
        al.add("potato");
        al.add("onion");
        al.add("eggs");
        al.add("milk");

        for(int i = 0; i < al.size(); i++) {
            if (al.get(i).equals(query)) {
                found = true;
            }
        }
        return found;
    }
    public void mainPage(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        setContentView(R.layout.activity_main);
    }
}
