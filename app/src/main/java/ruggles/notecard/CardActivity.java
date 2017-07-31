package ruggles.notecard;

import android.app.Dialog;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        myDBHelper = new MySQLiteHelper(this);
        myDB = myDBHelper.getWritableDatabase();
        //Log.d(TAG, myDB.toString());

        long deckID = getIntent().getExtras().getLong(MySQLiteHelper.DECK_COLNAME_ID);
        Log.d(TAG, Long.toString(deckID));

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

                editCardMenu();
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
    }

    //

    private void addCardMenu() {

        LayoutInflater myInflater = LayoutInflater.from(this);
        View dialogView = myInflater.inflate(R.layout.menu_card_edit, null);

        new AlertDialog.Builder(this)
                .setTitle("Add Card")
                .setView(dialogView)
                .setPositiveButton("Add", new Dialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addCard();
                    }
                })
                .setNeutralButton("Cancel", new Dialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //
                    }
                })
                .show();

        // TODO Create dialog including menu_card_add.xml
    }

    private void editCardMenu() {

        LayoutInflater myInflater = LayoutInflater.from(this);
        View dialogView = myInflater.inflate(R.layout.menu_card_edit, null);

        new AlertDialog.Builder(this)
                .setTitle("Edit Card")
                .setView(dialogView)
                .setPositiveButton("Delete", new Dialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteCard();
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
                        editCard();
                    }
                })
                .show();
    }

    private void updateCards() {
        cardList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                cardDeck.getCards()));
    }

    private Deck buildDeck(long deckID) {
        ArrayList<String> cardFronts = new ArrayList<>();
        ArrayList<String> cardBacks = new ArrayList<>();

        Cursor cursor = myDB.query(MySQLiteHelper.CARD_TABLE_NAME,
                MySQLiteHelper.CARD_COLUMNS,
                MySQLiteHelper.CARD_COLNAME_DECK_ID + "=?", new String[]{Long.toString(deckID)},
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

    private void addCard() {
        // TODO Fill in add card after data is complete
    }

    private void editCard() {
        //TODO Finish editCard once data is complete
    }

    private void deleteCard() {
        //TODO Finish deleteCard once data model is complete
    }

    private void flipCard(long cardPos) {

        Toast.makeText(getApplicationContext(), "flipCard() has been triggered",
                Toast.LENGTH_SHORT).show();
        cardDeck.flip((int) cardPos);
        updateCards();
    }

}
