package ruggles.notecard;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class CardActivity extends AppCompatActivity {

    private String[] placeholderCards = {"Alpha", "Omega", "Theta"};

    private static final String TAG = CardActivity.class.getSimpleName();

    private SQLiteDatabase myDB;
    private MySQLiteHelper myDBHelper;

    private ListView cardList;
    private Deck cardDeck;
    private long deckID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        myDBHelper = new MySQLiteHelper(this);
        myDB = myDBHelper.getWritableDatabase();
        //Log.d(TAG, myDB.toString());

        deckID = getIntent().getExtras().getLong(MySQLiteHelper.DECK_COLNAME_ID);
        //Log.d(TAG, Long.toString(deckID));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getDeckName(deckID));
        setSupportActionBar(toolbar);




        cardList = (ListView) findViewById(R.id.card_list);

        cardDeck = buildDeck(deckID);
        updateCards();

        //Log.d(TAG, cardDeck.getCards()[1]);

        cardList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                flipCard(id);
            }
        });

        cardList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                editCardMenu(id);
                return true;
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCardMenu();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Log.d(TAG, getDeckName(deckID));
        Log.d(TAG, Long.toString(deckID));
    }

    //

    private void addCardMenu() {

        LayoutInflater myInflater = LayoutInflater.from(this);
        View dialogView = myInflater.inflate(R.layout.menu_card_edit, null);
        final CardTextWrapper wrapper = new CardTextWrapper(dialogView);

        new AlertDialog.Builder(this)
                .setTitle("Add Card")
                .setView(dialogView)
                .setPositiveButton("Add", new Dialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addCard(wrapper);
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

    private void editCardMenu(long id) {

        LayoutInflater myInflater = LayoutInflater.from(this);
        View dialogView = myInflater.inflate(R.layout.menu_card_edit, null);
        final CardTextWrapper wrapper = new CardTextWrapper(dialogView);
        final String cardName = cardDeck.getcardFront(id);

        new AlertDialog.Builder(this)
                .setTitle("Edit Card")
                .setView(dialogView)
                .setPositiveButton("Delete", new Dialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteCard(cardName);
                    }
                })
                .setNeutralButton("Cancel", new Dialog.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        //
                    }
                })
                .setNegativeButton("Modify", new Dialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        editCard(cardName, wrapper);
                    }
                })
                .show();
    }

    private void updateCards() {
        cardList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                cardDeck.getCards()));
    }

    private String getDeckName(long deckID) {
        String deckName;

        Cursor myCursor = myDB.query(MySQLiteHelper.DECK_TABLE_NAME,
                new String[] {MySQLiteHelper.DECK_COLNAME_DECKNAME},
                MySQLiteHelper.DECK_COLNAME_ID + "=?",
                new String[] {Long.toString(deckID)},
                null, null, null);

        myCursor.moveToFirst();

        deckName = myCursor.getString(0);

        return deckName;
    }

    private Deck buildDeck(long deckID) {
        ArrayList<String> cardFronts = new ArrayList<>();
        ArrayList<String> cardBacks = new ArrayList<>();

        Cursor cursor = myDB.query(MySQLiteHelper.CARD_TABLE_NAME,
                MySQLiteHelper.CARD_COLUMNS,
                MySQLiteHelper.CARD_COLNAME_DECK_ID + "=?",
                new String[]{Long.toString(deckID)},
                null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String Front = cursor.getString(2);
            cardFronts.add(Front);
            String Back = cursor.getString(3);
            cardBacks.add(Back);
            cursor.moveToNext();
        }

        cursor.close();

        return new Deck(cardFronts.toArray(new String[cardFronts.size()]),
                cardBacks.toArray(new String[cardBacks.size()]));
    }

    private void addCard(CardTextWrapper wrapper) {

        String front = wrapper.getFront();
        String back = wrapper.getBack();

        if (cardDeck.doesExist(front)) {
            Toast.makeText(getApplicationContext(), "Card already exists", Toast.LENGTH_SHORT)
                        .show();
            return;
        }

        ContentValues myValues = new ContentValues(3);

        myValues.put(MySQLiteHelper.CARD_COLNAME_DECK_ID, Long.toString(deckID));
        myValues.put(MySQLiteHelper.CARD_COLNAME_FRONT, front);
        myValues.put(MySQLiteHelper.CARD_COLNAME_BACK, back);

        myDB.insert(MySQLiteHelper.CARD_TABLE_NAME, MySQLiteHelper.CARD_COLNAME_ID, myValues);

        cardDeck = buildDeck(deckID);
        updateCards();
    }

    private void deleteCard(String card) {

        myDB.delete(MySQLiteHelper.CARD_TABLE_NAME, MySQLiteHelper.CARD_COLNAME_FRONT + "=?",
                new String[]{card});

        cardDeck = buildDeck(deckID);
        updateCards();
    }

    private void editCard(String card, CardTextWrapper wrapper) {

        String front = wrapper.getFront();
        String back = wrapper.getBack();

        if ((!card.equals(front)) && cardDeck.doesExist(front)) {
            Toast.makeText(getApplicationContext(), "Card already exists", Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        ContentValues myValues = new ContentValues(2);
        myValues.put(MySQLiteHelper.CARD_COLNAME_FRONT, front);
        myValues.put(MySQLiteHelper.CARD_COLNAME_BACK, back);

        myDB.update(MySQLiteHelper.CARD_TABLE_NAME, myValues,
                MySQLiteHelper.CARD_COLNAME_FRONT + "=?",
                new String[] {card});

        cardDeck = buildDeck(deckID);
        updateCards();
    }

    private void flipCard(long cardPos) {

        cardDeck.flip((int) cardPos);
        updateCards();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_card, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_shuffle) {
            cardDeck.shuffle();
            updateCards();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
