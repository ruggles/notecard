package ruggles.notecard;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class CardActivity extends AppCompatActivity {

    private String[] placeholderCards = {"Alpha", "Omega", "Theta"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ListView cardList = (ListView) findViewById(R.id.card_list);
        cardList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                placeholderCards));

        cardList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                flipCard();
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

    private void addCard() {
        // TODO Fill in add card after data is complete
    }

    private void editCard() {
        //TODO Finish editCard once data is complete
    }

    private void deleteCard() {
        //TODO Finish deleteCard once data model is complete
    }

    private void flipCard() {

        Toast.makeText(getApplicationContext(), "flipCard() has been triggered",
                Toast.LENGTH_SHORT).show();

        // TODO Add card flip function
    }

}
