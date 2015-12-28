package com.midifish.iou;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {

    private DataIO database;
    private TextView yourPointsText;
    private int points;
    private Button addFavorBtn;
    private ListView favorChoices;
    private ListView favorsYouRequested;

    private ArrayAdapter<String> favorChoicesAdapter;
    private ArrayAdapter<String> favorsYouRequestedAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize data objects
        database = new DataIO(this);
        points = 1000;

        //set the string lists into the adapters
        favorChoicesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, database.getFavorChoicesStrings());
        favorsYouRequestedAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, database.getFavorsYouRequestedStrings());

        //initialize UI stuff
        yourPointsText = (TextView) findViewById(R.id.yourPointsText);
        addFavorBtn = (Button) findViewById(R.id.addFavorBtn);
        favorChoices = (ListView) findViewById(R.id.favorChoicesList);
        favorsYouRequested = (ListView) findViewById(R.id.favorsYouRequestedList);

        //set data into the UI objects
        yourPointsText.setText("Your Points: " + Integer.toString(points));
        favorChoices.setAdapter(favorChoicesAdapter);
        favorsYouRequested.setAdapter(favorsYouRequestedAdapter);

        favorChoices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Favor favor = database.getFavorChoices().get(position);
                requestFavor(favor);
            }
        });

        favorsYouRequested.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Favor favor = database.getFavorsYouRequested().get(position);
                clearFavorYouRequested(favor);
            }
        });
    }

    public void requestFavor(Favor favor) {
        //add the favor to the database
        database.addFavorsYouRequested(favor);
        //refresh list
        database.refreshFavorsYouRequestedStrings();
        //notify adapter so updated list will be shown
        favorsYouRequestedAdapter.notifyDataSetChanged();

        //process points transaction
        points -= favor.getValue();
        yourPointsText.setText("Your Points: " + Integer.toString(points));
    }

    public void clearFavorYouRequested(Favor favor) {
        //delete favor from the database
        database.deleteFavorYouRequested(favor);
        //refresh list
        database.refreshFavorsYouRequestedStrings();
        //notify adapter so updated list will be shown
        favorsYouRequestedAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPause() {
        super.onPause();
        database.close();
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
