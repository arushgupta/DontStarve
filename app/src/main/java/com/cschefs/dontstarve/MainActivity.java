package com.cschefs.dontstarve;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class MainActivity extends AppCompatActivity {
    /** ListView listIngredients links to list_ingredients. Displays list of ingredients chosen */
    private ListView listIngredients;
    /** ArrayList arrayIngredients allows for storage of ingredients chosen */
    ArrayList<String> arrayIngredients;
    /** Other variables needed */
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Automatically call to set up the activity page.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Set up the UI
        listIngredients = (ListView) findViewById(R.id.list_ingredients);
        Button searchBtn = (Button) findViewById(R.id.search_button);
        Button findBtn = (Button) findViewById(R.id.find_button);
        TextView emptyText = (TextView)findViewById(R.id.empty_list);
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
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item);
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
        Intent searchForIngredient = new Intent(this, SearchableActivity.class);
        startActivityForResult(searchForIngredient, 100);
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
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_context, menu);
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = info.position;
        switch (item.getItemId()) {
            case R.id.context_delete: {
                arrayIngredients.remove(position);
                adapter.notifyDataSetChanged();
                return true;
            }
            default:
                return super.onContextItemSelected(item);
        }
    }

}