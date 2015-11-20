package com.cschefs.dontstarve;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

public class RecipeActivity extends AppCompatActivity {
    /** Displays list of ingredients */
    private ListView listOfRecipes;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Automatically call to set up the activity page.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        //Set up the UI
        listOfRecipes = (ListView) findViewById(R.id.list_recipes);

        //Set the toolbar
        Toolbar mainToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mainToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mainToolbar.setNavigationIcon(R.drawable.ic_logo);

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
//                searchFunction();
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

//    public void searchFunction(){
//        Intent searchForIngredient = new Intent(this, SearchableActivity.class);
//        startActivityForResult(searchForIngredient, 100);
//    }

}
