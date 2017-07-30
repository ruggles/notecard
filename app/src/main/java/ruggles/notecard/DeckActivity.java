package ruggles.notecard;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DeckActivity extends AppCompatActivity {

    // LOGGING JUNK
    private static final String TAG = DeckActivity.class.getSimpleName();
    private final boolean debugDB = false;

    private SQLiteDatabase myDB;
    private MySQLiteHelper myDBHelper;

    private ArrayList<String> placeholderDeck;
    private ListView deckList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // To debug, deletes database
        if (debugDB)
            getApplicationContext().deleteDatabase(MySQLiteHelper.DBFILENAME);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deck);

        myDBHelper = new MySQLiteHelper(this);
        myDB = myDBHelper.getWritableDatabase();
        Log.d(TAG, myDB.toString());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create main adaptor
        deckList = (ListView) findViewById(R.id.deck_list);
        updateDecks();

        deckList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                toCardActivity();
            }
        });

        deckList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                deckEditMenu();

                //Returning true means the long click consumes the event
                //Such that a regular click wont be triggered immediately after
                return true;
            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.deck_fab);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                deckAddMenu();
            }
        });

        Log.d(TAG, "Deck onCreate finished successfully");


    }


    public void toCardActivity(){
        Intent cardIntent = new Intent(this, CardActivity.class);
        startActivity(cardIntent);
    }

    // LAYOUT FUNCTIONS DICTATING POP UP MENUS

    private void deckAddMenu() {

        LayoutInflater myInflater = LayoutInflater.from(this);
        View dialogView = myInflater.inflate(R.layout.menu_deck_edit, null);
        final DeckTextWrapper deckEditWrapper = new DeckTextWrapper(dialogView);

        new AlertDialog.Builder(this)
                .setTitle("Add Deck")
                .setView(dialogView)
                .setPositiveButton("Add", new Dialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addDeck(deckEditWrapper);
                    }
                })
                .setNeutralButton("Cancel", new Dialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //
                    }
                })
                .show();


    }

    private void deckEditMenu() {

        LayoutInflater myInflater = LayoutInflater.from(this);
        View dialogView = myInflater.inflate(R.layout.menu_deck_edit, null);

        new AlertDialog.Builder(this)
                .setTitle("Modify Deck?") // TODO ADD DECK NAME TO TITLE
                .setView(dialogView)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteDeck();
                    }
                })
                .setNegativeButton("Modify", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        modifyDeck();
                    }
                })
                .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // NOTHING HAPPENS
                    }
                })
                .show();
    }

    // DECK MODIFICATION FUNCTION - DATABASE STUFF HAPPENING HERE.

    public ArrayList getDeckList() {
        ArrayList<String> deckList = new ArrayList<>();

        if (debugDB)
            Log.d(TAG, "deckList array declared & initialized");

        Cursor cursor = myDB.query(MySQLiteHelper.DECK_TABLE_NAME,
                MySQLiteHelper.DECK_COLUMNS
                , null, null, null, null, null);

        if (debugDB)
            Log.d(TAG, "cursor created");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String model = cursor.getString(1);
            deckList.add(model);
            cursor.moveToNext();
        }

        cursor.close();
        return deckList;

    }

    private void updateDecks() {
        deckList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                getDeckList()));
    }

    private void addDeck(DeckTextWrapper wrapper) {

        //Log.d(TAG, wrapper.getName());
        ArrayList<String> deckList = getDeckList();
        for (int i=0; i< deckList.size(); i++) {
            //Log.d(TAG, deckList.get(i));
            if (wrapper.getName().equals(deckList.get(i))) {
                //Log.d(TAG, "WE HAVE A DUPE GENTLEMEN");
                Toast.makeText(getApplicationContext(), "Deck has same name as previous deck",
                        Toast.LENGTH_SHORT).show();
                return;
            }
        }


        ContentValues myValues = new ContentValues(1);

        myValues.put(MySQLiteHelper.DECK_COLNAME_DECKNAME, wrapper.getName());

        myDB.insert(MySQLiteHelper.DECK_TABLE_NAME, MySQLiteHelper.DECK_COLNAME_ID, myValues);

        updateDecks();
    }

    private void deleteDeck() {
        //TODO add deleteDeck once we have data functionality
    }

    private void modifyDeck() {
        //TODO add modifyDeck once we have data
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_deck, menu);
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
