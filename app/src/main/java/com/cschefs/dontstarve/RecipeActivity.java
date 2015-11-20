package com.cschefs.dontstarve;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

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
import java.util.Scanner;

public class RecipeActivity extends AppCompatActivity {
    /** Displays list of ingredients */
    private ListView listOfRecipes;
    // Request URL for the API
    private String queryString;
    static int CONNECTION_TIMEOUT = 10000;
    static int DATARETRIEVAL_TIMEOUT = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Automatically call to set up the activity page.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        //Getting the Request URL from Intent
        Bundle p = getIntent().getExtras();
        queryString = p.getString("request");

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



    public static JSONObject requestWebService(String serviceUrl) {
        disableConnectionReuseIfNecessary();

        HttpURLConnection urlConnection = null;
        try {
            // create connection
            URL urlToRequest = new URL(serviceUrl);
            urlConnection = (HttpURLConnection)
                    urlToRequest.openConnection();
            urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
            urlConnection.setReadTimeout(DATARETRIEVAL_TIMEOUT);

            // handle issues
            int statusCode = urlConnection.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                // handle unauthorized (if service requires user login)
            } else if (statusCode != HttpURLConnection.HTTP_OK) {
                // handle any other errors, like 404, 500,..
            }

            // create JSON object from content
            InputStream in = new BufferedInputStream(
                    urlConnection.getInputStream());
            return new JSONObject(getResponseText(in));

        } catch (MalformedURLException e) {
            // URL is invalid
        } catch (SocketTimeoutException e) {
            // data retrieval or connection timed out
        } catch (IOException e) {
            // could not read response body
            // (could not create input stream)
        } catch (JSONException e) {
            // response body is no valid JSON string
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return null;
    }

    /**
     * required in order to prevent issues in earlier Android version.
     */
    private static void disableConnectionReuseIfNecessary() {
        // see HttpURLConnection API doc
        if (Integer.parseInt(Build.VERSION.SDK)
                < Build.VERSION_CODES.LOLLIPOP) {
            System.setProperty("http.keepAlive", "false");
        }
    }

    private static String getResponseText(InputStream inStream) {
        // very nice trick from
        // http://weblogs.java.net/blog/pat/archive/2004/10/stupid_scanner_1.html
        return new Scanner(inStream).useDelimiter("\\A").next();
    }










    // Code to be looked at later

//    public void searchFunction(){
//        Intent searchForIngredient = new Intent(this, SearchableActivity.class);
//        startActivityForResult(searchForIngredient, 100);
//    }

//    public static JSONObject getJSONfromURL(String url){
//        //initialize
//        InputStream is = null;
//        String result = "";
//        JSONObject jArray = null;
//
//        //http post
//        try{
//            HttpClient httpclient = new DefaultHttpClient();
//            HttpPost httppost = new HttpPost(url);
//            HttpResponse response = httpclient.execute(httppost);
//            HttpEntity entity = response.getEntity();
//            is = entity.getContent();
//        }
//        catch(Exception e){
//            Log.e("log_tag", "Error in http connection " + e.toString());
//        }
//
//        //convert response to string
//        try {
//            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
//            StringBuilder sb = new StringBuilder();
//            String line = null;
//            while ((line = reader.readLine()) != null) {
//                sb.append(line + "\n");
//            }
//            is.close();
//            result=sb.toString();
//        }
//        catch(Exception e){
//            Log.e("log_tag", "Error converting result "+e.toString());
//        }
//
//        // try parse the string to a JSON object
//        try{
//            jArray = new JSONObject(result);
//        }
//        catch(JSONException e) {
//            Log.e("log_tag", "Error parsing data "+e.toString());
//        }
//
//        return jArray;
//    }
}
