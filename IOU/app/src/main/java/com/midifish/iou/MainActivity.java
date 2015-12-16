package com.midifish.iou;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends Activity {

    private DataIO database;
    private TextView yourPointsText;
    private int points;
    private Button addFavorBtn;
    private ListView favorChoices;
    private ListView favorsYouRequested;

    private ArrayList<String> favorChoicesArray;
    private ArrayList<String> favorsYouRequestedArray;
    private ArrayAdapter<String> favorChoicesAdapter;
    private ArrayAdapter<String> favorsYouRequestedAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize data objects
        database = new DataIO(this);
        points = 1000;
        favorChoicesArray = new ArrayList<>();
        favorsYouRequestedArray = new ArrayList<>();

        //get data from the database
        ArrayList<Favor> choices = database.getFavorChoices();
        ArrayList<Favor> youRequested = database.getFavorsYouRequested();

        //build string list for favor choices
        String current = "";
        for (Favor favor : choices) {
            current = favor.getDescription();
            current += " " + Integer.toString(favor.getValue());
            favorChoicesArray.add(current);
        }
        for (Favor favor : youRequested) {
            current = favor.getDescription();
            current += " " + Integer.toString(favor.getValue());
            favorsYouRequestedArray.add(current);
        }

        //set the string lists into the adapters
        favorChoicesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, favorChoicesArray);
        favorsYouRequestedAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, favorsYouRequestedArray);

        //initialize UI stuff
        yourPointsText = (TextView) findViewById(R.id.yourPointsText);
        addFavorBtn = (Button) findViewById(R.id.addBtn);
        favorChoices = (ListView) findViewById(R.id.favorChoicesList);
        favorsYouRequested = (ListView) findViewById(R.id.favorsYouRequestedList);

        //set data into the UI objects
        yourPointsText.setText("Your Points: " + Integer.toString(points));
        favorChoices.setAdapter(favorChoicesAdapter);
        favorsYouRequested.setAdapter(favorsYouRequestedAdapter);

        //create events for clicks
        addFavorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        favorChoices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) favorChoices.getItemAtPosition(position);
                points -= database.getFavorChoices().get(position).getValue();
                yourPointsText.setText("Your Points: " + Integer.toString(points));
                favorsYouRequestedArray.add(selection);
                favorsYouRequestedAdapter.notifyDataSetChanged();
            }
        });

        favorsYouRequested.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) favorsYouRequested.getItemAtPosition(position);
                favorsYouRequestedArray.remove(selection);
                favorsYouRequestedAdapter.notifyDataSetChanged();
            }
        });


    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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
