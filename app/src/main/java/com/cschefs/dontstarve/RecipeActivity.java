package com.cschefs.dontstarve;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;

public class RecipeActivity extends AppCompatActivity {
    /** Displays list of ingredients */
    private ListView listOfRecipes;
    /** Request URL for the API */
    private String queryString;
    /** Hashmap for ListView */
    private static ArrayList<HashMap<String, String>> recipeList;
    /** JSON Node names */
    // Counts number of recipes
    private static final String TAG_COUNT = "count";
    // JSON array of JSON recipe objects
    private static final String TAG_RECIPES = "recipes";
    // Name of recipe
    private static final String TAG_TITLE = "title";
    // URL of recipe
    private static final String TAG_URL = "source_url";
    /** Instance variables */
    static int CONNECTION_TIMEOUT = 10000;
    static int DATARETRIEVAL_TIMEOUT = 10000;
    static int MAX_RECIPES = 30;

    /** Function to set up the Activity when called. */
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
        getSupportActionBar().setTitle(R.string.recipe_menu_text);
        mainToolbar.setNavigationIcon(R.drawable.ic_logo);

        //Getting the Request URL from Intent
        Bundle p = getIntent().getExtras();
        queryString = p.getString("request");

        //Register the listView for context menu functionality.
        registerForContextMenu(listOfRecipes);

        // Call async task which returns JSON object
        new GetRecipes().execute();
    }
    /** Function to create the navigation menu. */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }
    /** Function to handle navigation menu clicks. */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home_menu: {
                Intent a = new Intent(this, MainActivity.class);
                startActivity(a);
                return true;
            }
            case R.id.search_menu: {
                Intent recipe_done = getIntent();
                setResult(300, recipe_done);
                finish();
                return true;
            }
            case R.id.recipe_menu:{
                //Nothing.
                return true;
            }
            //Else is selected.
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    /* Function to take in the original JSON object from the HTTP request
     * and parse the recipes into an array of hashmaps. Each hashmap
     * will contain the recipe name and the url for the recipe.
     * The original JSON object contains two field -
     * 1) Count - the number of recipes
     * 2) Recipes - an array of recipe JSON objects */
    private ArrayList<HashMap<String, String>> ParseJSON(String json) {
        if (json != null) {
            try {
                // Hashmap for ListView
                ArrayList<HashMap<String, String>> trecipeList = new ArrayList<HashMap<String, String>>();

                // Original json object created from string returned by HTTP request
                JSONObject jsonObj = new JSONObject(json);

                // Make sure there are recipes
                if (jsonObj.getInt(TAG_COUNT)>0) {
                    // Getting JSON Array node
                    JSONArray recipes = jsonObj.getJSONArray(TAG_RECIPES);

                    // looping through All Recipes
                    for (int i = 0; i < recipes.length(); i++) {
                        // Get the I'th recipe from JSONArray
                        JSONObject c = recipes.getJSONObject(i);

                        // Get field to store in hashmap
                        String title = c.getString(TAG_TITLE);
                        String url = c.getString(TAG_URL);

                        // tmp hashmap for single recipe
                        HashMap<String, String> recipe = new HashMap<String, String>();

                        // adding every child node to HashMap key => value
                        // Stored values are name of recipe (title) and url of recipe (url)
                        recipe.put(TAG_TITLE, title);
                        recipe.put(TAG_URL, url);

                        // adding student to students list
                        trecipeList.add(recipe);
                    }
                }
                return trecipeList;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            // In case function is called on a non-JSON object
            Log.e("ServiceHandler", "No data received from HTTP request");
            return null;
        }
    }
    /* Function to make the HTTP request at given url,
     * Fill the ArrayList with the recipes/urls, and
     * Get the ArrayAdapter to view the ArrayList*/
    private class GetRecipes extends AsyncTask<Void, Void, Void> {
        /* "Please wait" with spinning progress wheel */
        ProgressDialog proDialog;
        /* Function to start a progress dialog to run while the HTTP request works in the background */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress loading dialog
            proDialog = new ProgressDialog(RecipeActivity.this);
            proDialog.setMessage("Please wait...");
            proDialog.setCancelable(false);
            proDialog.show();
        }
        /* Function to make HTTP request at url provided and parses it into an ArrayList */
        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            WebRequest webreq = new WebRequest();

            // Making a request to url and getting response
            String jsonStr = webreq.makeWebServiceCall(queryString, WebRequest.GETRequest);

            Log.d("Response: ", "> " + jsonStr);
            recipeList = ParseJSON(jsonStr);

            return null;
        }
        /* Function to end the progress dialog and sets the adapter to list the recipe array */
        @Override
        protected void onPostExecute(Void requestresult) {
            super.onPostExecute(requestresult);
            // Dismiss the progress dialog
            if (proDialog.isShowing())
                proDialog.dismiss();
            /**
             * Updating received data from JSON into ListView
             * */

            ListAdapter adapter = new SimpleAdapter(
                    RecipeActivity.this, recipeList,
                    R.layout.list_item, new String[]{TAG_TITLE}, new int[]{R.id.ingredient_name});

            listOfRecipes.setAdapter(adapter);
        }

    }
    /** Function to open a context menu*/
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.recipe_context, menu);
    }
    /** Function to handle context menu actions */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = info.position;
        // When a recipe in the list is long clicked
        switch (item.getItemId()) {
            //If view recipe is selected.
            case R.id.context_url: {
                // Open up browser to view recipe instructions
                HashMap<String, String> tMap = recipeList.get(position);

                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(tMap.get(TAG_URL))));
                return true;
            }
            //Else is selected.
            default:
                return super.onContextItemSelected(item);
        }
    }

}
