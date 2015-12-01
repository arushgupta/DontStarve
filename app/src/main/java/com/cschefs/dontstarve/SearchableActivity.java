package com.cschefs.dontstarve;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchableActivity extends AppCompatActivity {
    /** ListView listSearch links to list_ingredients_search. Displays list of ingredients */
    private ListView listSearch;
    /** EditText inputSearch links to input_search. Displays search bar */
    private EditText inputSearch;
    /** String ingredient_name parses the selected ingredients name to be passed to MainActivity*/
    private String ingredient_name;
    /** ArrayList to organize objects */
    private static ArrayList<String> ingredients = new ArrayList<String>();
    /** Other variables needed */
    private ArrayAdapter<String> searchAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Automatically call to set up the activity page.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);
        //Set up the UI
        listSearch = (ListView) findViewById(R.id.list_ingredients_search);
        inputSearch = (EditText) findViewById(R.id.input_search);
        Button newBtn = (Button) findViewById(R.id.new_button_search);
        Button addBtn = (Button) findViewById(R.id.add_button_search);
        Toolbar mainToolbar = (Toolbar) findViewById(R.id.toolbar);
        //Set the toolBar
        setSupportActionBar(mainToolbar);
        getSupportActionBar().setTitle(R.string.search_menu_text);
        mainToolbar.setNavigationIcon(R.drawable.ic_logo);
        //Set up the list of ingredients
        if (ingredients.isEmpty()) {
            String ingredients_array[] = getResources().getStringArray(R.array.string_ingredients);
            List<String> ingredients_list = Arrays.asList(ingredients_array);
            ingredients.addAll(ingredients_list);
        }
        //Add items and adapter to ListView
        searchAdapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.ingredient_name, ingredients);
        listSearch.setAdapter(searchAdapter);
        //Set the selected Ingredient to the search bar.
        listSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Object ingredient_obj = arg0.getItemAtPosition(arg2);
                ingredient_name = ingredient_obj.toString();
                inputSearch.setText(ingredient_name);
            }
        });
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
            }

            @Override
            public void afterTextChanged(Editable arg0) {
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
                Intent a = new Intent(this, MainActivity.class);
                startActivity(a);
                return true;
            }
            case R.id.search_menu: {
                //Nothing.
                return true;
            }
            case R.id.recipe_menu:{
                Intent search_done = getIntent();
                setResult(200, search_done);
                finish();
                return true;
            }
            //Else is selected.
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    /** Adds ingredient to the main page ingredients list. */
    public void addIngredient(View view){ addFunction(); }
    /** */
    private void addFunction(){
        Intent search_done = getIntent();
        ingredient_name = inputSearch.getText().toString();
        //If Blank, then do nothing.
        if (ingredient_name.matches("")) {
            Toast.makeText(getApplicationContext(), R.string.item_is_empty, Toast.LENGTH_SHORT).show();
        }
        //If it does not Exist, do not add it.
        else if(!ingredients.contains(ingredient_name)) {
            Toast.makeText(getApplicationContext(), R.string.item_not_found, Toast.LENGTH_SHORT).show();
        }
        //If Exists, then add it.
        else{
            search_done.putExtra("received_ingredient", ingredient_name);
            setResult(100, search_done);
            finish();
        }
    }
    /** */
    public void newItem(View view){ newItemDialog(); }
    /** Creates an AlertDialog that prompts the user for input in an EditText*/
    private void newItemDialog(){
        // Set up the AlertDialog onto the current context. Also set up the UI.
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SearchableActivity.this);
        LayoutInflater dialogInflater = SearchableActivity.this.getLayoutInflater();
        final View dialogView = dialogInflater.inflate(R.layout.new_item_search,null);
        dialogBuilder.setView(dialogView);
        final EditText dialogInput = (EditText) dialogView.findViewById(R.id.new_item_edit);
        // Set up the alertDialog.
        dialogBuilder.setTitle(R.string.new_item_detail_text);
        // Define the positive and negative buttons.
        dialogBuilder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String addedIngredient = dialogInput.getText().toString();
                addNewItem(addedIngredient);
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        // Display the alertDialog.
        final AlertDialog newItemDialog = dialogBuilder.create();
        newItemDialog.show();
        // Disable the "Add" button.
        newItemDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
        // Sets "Add" button to disabled until something is typed in.
        dialogInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                if (dialogInput.getText().length() > 0) {
                    newItemDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                } else {
                    newItemDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                }
            }
        });
    }
    /** Adds a new item to ingredients list and updates or repopulates the listView to match.*/
    private void addNewItem(String item){
        // If the new item is blank, then nothing happens. This should never be reached. Precaution only.
        if(item.matches(""));
            // If the new item already exists, then help the user by showing it in the EditText field.
        else if(ingredients.contains(item)){
            inputSearch.setText(item);
            Toast.makeText(getApplicationContext(), R.string.item_exists, Toast.LENGTH_SHORT).show();
        }
        // Else, the item does not exist and should be added to the ingredients list.
        else{/*
            if(inputSearch.getText().toString().matches("")){//Encountered glitch. TEMP FIX!!!
                ingredients.add(item);
            }
            else {
                ingredients.add(item);
                searchAdapter.clear();
                searchAdapter.addAll(ingredients);
            }*/
            searchAdapter.insert(item,0);
            searchAdapter.notifyDataSetChanged();
            inputSearch.setText("");
        }
    }

}