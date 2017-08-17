package ruggles.notecard;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.util.Log;

import java.util.ArrayList;

public class DeckActivity extends AppCompatActivity {

    // LOGGING JUNK
    private static final String TAG = DeckActivity.class.getSimpleName();

    private SQLiteDatabase myDB;
    private MySQLiteHelper myDBHelper;

    private ListView deckListView;
    private DeckGroup myDecks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // To debug, deletes database
        // getApplicationContext().deleteDatabase(MySQLiteHelper.DBFILENAME);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deck);

        myDBHelper = MySQLiteHelper.getInstance(this);
        myDB = myDBHelper.getWritableDatabase();
        if (BuildConfig.DEBUG)
            Log.d(TAG, myDB.toString());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create main adaptor
        deckListView = (ListView) findViewById(R.id.deck_list);
        myDecks = new DeckGroup();
        updateDecks();

        deckListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                toCardActivity((int) id);
            }
        });

        deckListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                deckEditMenu(id);

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

        if (BuildConfig.DEBUG)
            Log.d(TAG, "Deck onCreate finished successfully");


    }

    // SENDS APPLICATION TO CARDACTIVITY
    public void toCardActivity(int id){
        long deckID = myDecks.getID(id);
        String deckName = myDecks.getInfoObject(id).getName();
        Intent cardIntent = new Intent(this, CardActivity.class);
        cardIntent.putExtra(MySQLiteHelper.DECK_COLNAME_ID, deckID);
        cardIntent.putExtra(MySQLiteHelper.DECK_COLNAME_DECKNAME, deckName);
        startActivity(cardIntent);
    }

    // POP UP MENU FUNCTIONS

    private void deckAddMenu() {

        LayoutInflater myInflater = LayoutInflater.from(this);
        View dialogView = myInflater.inflate(R.layout.menu_deck_edit, null);
        final EditTextWrapper wrapper = new EditTextWrapper(dialogView, R.id.deckName);

        AlertDialog dialog  = new AlertDialog.Builder(this)
                .setTitle("Add Deck")
                .setView(dialogView)
                .setPositiveButton("Add", new Dialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addDeck(wrapper);
                    }
                })
                .setNeutralButton("Cancel", new Dialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //
                    }
                })
                .create();

        // This automatically creates keyboard, makes things a little faster
        if (getResources().getConfiguration().keyboard == Configuration.KEYBOARD_NOKEYS )
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        dialog.show();

    }

    private void deckEditMenu(long id) {

        LayoutInflater myInflater = LayoutInflater.from(this);
        View dialogView = myInflater.inflate(R.layout.menu_deck_edit, null);
        EditText textField = (EditText) dialogView.findViewById(R.id.deckName);

        final String deckName = myDecks.getDeckList()[(int) id];
        textField.setText(deckName);
        final EditTextWrapper wrapper = new EditTextWrapper(dialogView, R.id.deckName);

        new AlertDialog.Builder(this)
                .setTitle("Rename Deck?")
                .setView(dialogView)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteDeck(deckName);
                    }
                })
                .setNegativeButton("Rename", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        renameDeck(deckName, wrapper);
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

        return super.onOptionsItemSelected(item);

        //TODO TURN THIS INTO SEARCH MAYHAPS?
    }

    // DECK MODIFICATION FUNCTIONS

    private void addDeck(EditTextWrapper wrapper) {

        if (myDecks.isNameUsed(wrapper.getText())) {
            Toast.makeText(getApplicationContext(), "Deck has same name as previous deck",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues myValues = new ContentValues(1);

        myValues.put(MySQLiteHelper.DECK_COLNAME_DECKNAME, wrapper.getText());

        myDB.insert(MySQLiteHelper.DECK_TABLE_NAME, MySQLiteHelper.DECK_COLNAME_ID, myValues);

        updateDecks();
    }

    private void deleteDeck(String deckName) {
        //Log.d(TAG, deckName);

        myDB.delete(MySQLiteHelper.DECK_TABLE_NAME, MySQLiteHelper.DECK_COLNAME_DECKNAME + "=?",
                new String[]{deckName});

        updateDecks();
    }

    private void renameDeck(String deckName, EditTextWrapper wrapper) {

        if (myDecks.isNameUsed(wrapper.getText())) {
            Toast.makeText(getApplicationContext(), "Deck has same name as previous deck",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues myValues = new ContentValues(1);

        myValues.put(MySQLiteHelper.DECK_COLNAME_DECKNAME, wrapper.getText());

        myDB.update(MySQLiteHelper.DECK_TABLE_NAME, myValues,
                MySQLiteHelper.DECK_COLNAME_DECKNAME + "=?", new String[]{deckName});

        updateDecks();
    }

    public void updateDecks() {
        int[] idArray;
        String[] nameArray;

        if (BuildConfig.DEBUG)
            Log.d(TAG, "deckListView array declared & initialized");

        Cursor cursor = myDB.query(MySQLiteHelper.DECK_TABLE_NAME,
                MySQLiteHelper.DECK_COLUMNS
                , null, null, null, null, null);

        if (BuildConfig.DEBUG)
            Log.d(TAG, "Cursor created in getDeckList()");

        // getCount gives the amount of rows in the cursor object
        idArray = new int[cursor.getCount()];
        nameArray = new String[cursor.getCount()];

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int ID = cursor.getInt(MySQLiteHelper.DECK_COLNUM_ID);
            idArray[cursor.getPosition()] = ID;

            String name = cursor.getString(MySQLiteHelper.DECK_COLNUM_NAME);
            nameArray[cursor.getPosition()] = name;

            cursor.moveToNext();
        }

        cursor.close();

        myDecks.update(idArray, nameArray);

        deckListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                myDecks.getDeckList()));

    }


}
