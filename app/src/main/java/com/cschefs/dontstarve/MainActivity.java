package com.cschefs.dontstarve;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    /** Items entered by the user is stored in this ArrayList variable */
    ArrayList<String> list_ing = new ArrayList<String>();

    /** Declaring an ArrayAdapter to set items to ListView */
    ArrayAdapter<String> adapter_ing;

    /** Quick class/method for search add to list functionality */
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);


        /** This part shouldn't be in onCreate, so it doesn't reset itself*/
        setContentView(R.layout.content_main);

        Button add = (Button) findViewById(R.id.search_add);

        ListView listView_ing = (ListView)findViewById(R.id.list_ingredients);

        adapter_ing = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list_ing);

        /** Later, change this listener to react with the search page button. For now button name is add*/
        OnClickListener listener = new OnClickListener() {
            @Override
            public void onClick(View view){
                EditText edit = (EditText) findViewById(R.id.search_bar);
                list_ing.add(edit.getText().toString());
                edit.setText("");
                adapter_ing.notifyDataSetChanged();
            }
        };

        add.setOnClickListener(listener);

        listView_ing.setAdapter(adapter_ing);
    }

    /**
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void selectItem(View view){
        boolean checked = ((CheckBox) view).isChecked();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
