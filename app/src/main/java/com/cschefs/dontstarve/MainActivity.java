package com.cschefs.dontstarve;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;

public class MainActivity extends AppCompatActivity {
    /** ListView listIngredients links to list_ingredients. Displays list of ingredients chosen */
    private ListView listIngredients;
    /** ArrayList arrayIngredients allows for storage of ingredients chosen */
    ArrayList<String> arrayIngredients;
    /** Other variables needed */
    ArrayAdapter<String> adapter;

    private AlertDialog.Builder dialogBuilder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Automatically call to set up the activity page.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Set up the UI
        listIngredients = (ListView) findViewById(R.id.list_ingredients);
        //Button searchBtn = (Button) findViewById(R.id.search_button);
        Button findBtn = (Button) findViewById(R.id.find_button);
        Button clearBtn = (Button) findViewById(R.id.clear_button);


       // clearBtn.setOnLongClickListener();
        TextView emptyText = (TextView)findViewById(R.id.empty_list);
        Toolbar mainToolbar = (Toolbar) findViewById(R.id.toolbar);
        //Set the toolBar
        setSupportActionBar(mainToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mainToolbar.setNavigationIcon(R.drawable.ic_logo);
        //Set up the ArrayList
        if (savedInstanceState == null) { arrayIngredients = new ArrayList<String>();}
        else { arrayIngredients = savedInstanceState.getStringArrayList("savedList");}
        //Add items and adapter to ListView
        adapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.ingredient_name, arrayIngredients);
        listIngredients.setAdapter(adapter);
        //Register the listView for context menu functionality.
        registerForContextMenu(listIngredients);
        //Set the textView to display when ListView is empty.
        listIngredients.setEmptyView(emptyText);

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
                return true;
            }
            case R.id.search_menu: {
                searchFunction();
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
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save the user's current state
        outState.putStringArrayList("savedList", arrayIngredients);
    }
    /** Function is called when the user clicks the Search Ingredients Button
     *  Opens Search Activity with search functionality embedded */
    public void searchPage(View view) {
        // Do something in response to button
        searchFunction();
    }
    public void searchFunction(){
        Intent searchForIngredient = new Intent(this, SearchableActivity.class);
        startActivityForResult(searchForIngredient, 100);
    }

    private void clearAllDialog(){
        dialogBuilder = new AlertDialog.Builder(this);
        String s1 = "Are you sure you want to clear all?";
        dialogBuilder.setTitle(s1);
        dialogBuilder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                arrayIngredients.clear();
                adapter.notifyDataSetChanged();
            }
        });
        dialogBuilder.setPositiveButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Does nothing.
            }
        });
        AlertDialog dialogConfirm = dialogBuilder.create();
        dialogConfirm.show();

    }

    public void clearAll(View view) {
        clearAllDialog();
    }
    /** Function to read the result from newly created activities */
    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //If the code is from "Search Ingredients" page
        if(resultCode == 100){
            String ingredient_to_add = data.getStringExtra("received_ingredient");
            if (ingredient_to_add != null) {
                arrayIngredients.add(ingredient_to_add);
                adapter.notifyDataSetChanged();
            }
        }
    }
    /** Function to open a context menu*/
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_context, menu);
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = info.position;
        switch (item.getItemId()) {
            //If delete is selected.
            case R.id.context_delete: {
                arrayIngredients.remove(position);
                adapter.notifyDataSetChanged();
                return true;
            }
            //Else is selected.
            default:
                return super.onContextItemSelected(item);
        }
    }

}