package com.cschefs.dontstarve;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchableActivity extends AppCompatActivity {
    /** ListView listSearch links to list_ingredients_search. Displays list of ingredients */
    private ListView listSearch;
    /** EditText inputSearch links to input_search. Displays search bar */
    private EditText inputSearch;
    /** String ingredient_name parses the selected ingredients name */
    private String ingredient_name;
    /** ArrayList to organize objects */
    private ArrayList<String> ingredients;
    /** Other variables needed */
    private ArrayAdapter<String> searchAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Automatically call to set up the activity page.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);
        //Set up the list of ingredients
        String ingredients_array[] = getResources().getStringArray(R.array.string_ingredients);
        //Testing as ArrayList to add dynamically.
        List<String> ingredients_list = Arrays.asList(ingredients_array);
        ingredients = new ArrayList<String>();
        ingredients.addAll(ingredients_list);
        //Set up the UI
        listSearch = (ListView) findViewById(R.id.list_ingredients_search);
        inputSearch = (EditText) findViewById(R.id.input_search);
        Button newBtn = (Button) findViewById(R.id.new_button_search);
        Button addBtn = (Button) findViewById(R.id.add_button_search);
        Toolbar mainToolbar = (Toolbar) findViewById(R.id.toolbar);
        //Set the toolBar
        setSupportActionBar(mainToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mainToolbar.setNavigationIcon(R.drawable.ic_logo);
        //Add items and adapter to ListView
        searchAdapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.ingredient_name, ingredients);
        listSearch.setAdapter(searchAdapter);
        //Text watcher is used to filter the search and display as you type.
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changes text in inputSearch
                SearchableActivity.this.searchAdapter.getFilter().filter(cs);
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
        listSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Object ingredient_obj = arg0.getItemAtPosition(arg2);
                ingredient_name = ingredient_obj.toString();
                inputSearch.setText(ingredient_name);
            }
        });
        //Add a new ingredient to the ingredients ArrayList.
        newBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                ingredient_name = inputSearch.getText().toString();
                //If Blank, then do nothing.
                if (ingredient_name.matches("")) {

                }
                //If Exists, then do nothing.
                else if (ingredients.contains(ingredient_name)){

                }
                //Else, add the new ingredient to the ArrayList.
                else{
                    ingredients.add(ingredient_name);
                    searchAdapter.clear();
                    searchAdapter.addAll(ingredients);
                    searchAdapter.notifyDataSetChanged();
                    inputSearch.setText("");
                }
            }
        });
        //Add the selected item.
        addBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent search_done = getIntent();
                ingredient_name = inputSearch.getText().toString();
                //If Blank, then do nothing.
                if (ingredient_name.matches("")) {
                }
                //If it does not Exist, do not add it.
                else if(!ingredients.contains(ingredient_name)) {
                }
                //If Exists, then add it.
                else{
                    search_done.putExtra("received_ingredient", ingredient_name);
                    setResult(100, search_done);
                    finish();
                }
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home_menu: {
                finish();
                return true;
            }
            case R.id.search_menu: {
                return true;
            }
            case R.id.recipe_menu:{
                return true;
            }
            //Else is selected.
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}