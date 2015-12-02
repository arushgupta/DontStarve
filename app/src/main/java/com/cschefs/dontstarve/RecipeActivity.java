package com.cschefs.dontstarve;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class RecipeActivity extends AppCompatActivity {
    /** Displays list of ingredients */
    private ListView listOfRecipes;
    /** Request URL for the API */
    private String queryString;
    /** Hashmap for ListView */
    private static ArrayList<HashMap<String, String>> recipeList;
    /** JSON Node names */
    private static final String TAG_COUNT = "count";
    private static final String TAG_RECIPES = "recipes";
    private static final String TAG_TITLE = "title";
    private static final String TAG_URL = "source_url";
    /** Instance variables */
    static int CONNECTION_TIMEOUT = 10000;
    static int DATARETRIEVAL_TIMEOUT = 10000;
    static int MAX_RECIPES = 30;

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

    private ArrayList<HashMap<String, String>> ParseJSON(String json) {
        if (json != null) {
            try {
                // Hashmap for ListView
                ArrayList<HashMap<String, String>> trecipeList = new ArrayList<HashMap<String, String>>();

                JSONObject jsonObj = new JSONObject(json);

                // Make sure there are recipes
                if (jsonObj.getInt(TAG_COUNT)>0) {
                    // Getting JSON Array node
                    JSONArray recipes = jsonObj.getJSONArray(TAG_RECIPES);

                    // looping through All Recipes
                    for (int i = 0; i < recipes.length(); i++) {
                        JSONObject c = recipes.getJSONObject(i);

                        String title = c.getString(TAG_TITLE);
                        String url = c.getString(TAG_URL);

                        // tmp hashmap for single student
                        HashMap<String, String> recipe = new HashMap<String, String>();

                        // adding every child node to HashMap key => value
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
            Log.e("ServiceHandler", "No data received from HTTP request");
            return null;
        }
    }

    private class GetRecipes extends AsyncTask<Void, Void, Void> {

        ProgressDialog proDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress loading dialog
            proDialog = new ProgressDialog(RecipeActivity.this);
            proDialog.setMessage("Please wait...");
            proDialog.setCancelable(false);
            proDialog.show();
        }

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
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = info.position;
        switch (item.getItemId()) {
            //If delete is selected.
            case R.id.context_url: {
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
