package ruggles.notecard;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ruggles on 5/29/17.
 */

public class MySQLiteHelper extends SQLiteOpenHelper {

    public static final String DBFILENAME = "deck.db";
    private static final int DBVERSION = 1;

    public static final String DECK_TABLE_NAME = "deck_table";
    public static final String DECK_COLNAME_ID = "_id";
    public static final String DECK_COLNAME_DECKNAME = "deck_name";
    public static final String[] DECK_COLUMNS =
            new String[]{DECK_COLNAME_ID, DECK_COLNAME_DECKNAME};

    public static final String CARD_TABLE_NAME = "card_table";
    public static final String CARD_COLNAME_ID = "_id";
    public static final String CARD_COLNAME_DECK_ID = "deck_id";
    public static final String CARD_COLNAME_FRONT = "front";
    public static final String CARD_COLNAME_BACK = "back";
    public static final String[] CARD_COLUMNS =
            new String[]{CARD_COLNAME_ID, CARD_COLNAME_DECK_ID, CARD_COLNAME_FRONT, CARD_COLNAME_BACK};

    private static final String CREATE_DECK_TABLE =
            "create table " + DECK_TABLE_NAME +
                    " (" + DECK_COLNAME_ID + " integer primary key autoincrement, " +
                    DECK_COLNAME_DECKNAME + " varchar(30) not null" +
                    ")";

    private static final String CREATE_CARD_TABLE =
            "create table " + CARD_TABLE_NAME +
                    " (" + CARD_COLNAME_ID + " integer primary key autoincrement, " +
                    CARD_COLNAME_DECK_ID + " integer references "
                        + DECK_TABLE_NAME + "(" + DECK_COLNAME_ID + ") " +
                        "on delete cascade, " +
                    CARD_COLNAME_FRONT + " varchar(30), " +
                    CARD_COLNAME_BACK + " varchar(120))";

    private static final String TEST_DECK_INSERT =
            "insert into " + DECK_TABLE_NAME + " (" +
                    DECK_COLNAME_DECKNAME + ") " +
                    "values ('Deck1')";

    private static final String TEST_DECK_INSERT2 =
            "insert into " + DECK_TABLE_NAME + " (" +
                    DECK_COLNAME_DECKNAME + ") " +
                    "values ('Deck2')";

    private static final String TEST_CARD_INSERT =
            "insert into " + CARD_TABLE_NAME + " (" +
                    CARD_COLNAME_DECK_ID + ", " +
                    CARD_COLNAME_FRONT + ", " +
                    CARD_COLNAME_BACK + ") " +
                    "values ( 1, 'hello this is front', 'hello this is back')";


    public MySQLiteHelper(Context context) {
        super(context, DBFILENAME, null, DBVERSION);
        //TODO Fill in SQLite Helper Constructor
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //TODO Fill in SQLite Helper onCreate
        db.execSQL(CREATE_DECK_TABLE);
        db.execSQL(CREATE_CARD_TABLE);
        db.execSQL(TEST_DECK_INSERT);
        db.execSQL(TEST_DECK_INSERT2);
        db.execSQL(TEST_CARD_INSERT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO Fill in SQLite Helper onUpgrade
    }
}
