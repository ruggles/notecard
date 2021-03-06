package ruggles.notecard;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

/**
 * Created by ruggles on 7/30/17.
 *
 * This data structure stores 2 arrays, the front & back of each note card
 * When you flip a card, it swaps between arrays.
 */



public class Deck {

    private String[] cardFront, cardBack, frontArchive, backArchive;

    Deck(String[] cardFront, String[] cardBack)
    {
        this.cardFront = cardFront;
        this.frontArchive = cardFront.clone();

        //Be wary, if two variables copy the same parameter, it will be the same reference!

        this.cardBack = cardBack;
        this.backArchive = cardBack.clone();
    }

    void flip(int cardPos)
    {
        String placeholder = cardFront[cardPos];
        cardFront[cardPos] = cardBack[cardPos];
        cardBack[cardPos] = placeholder;
    }

    // Check to see if a card front exists to avoid dupes
    boolean doesExist(String cardFront) {
        for (String existingFront: frontArchive)
            if (cardFront.equals(existingFront))
                return true;
        return false;
    }

    void shuffle()
    {
        long seed = System.nanoTime();
        ArrayList<String> frontList = new ArrayList<>(Arrays.asList(cardFront));
        Collections.shuffle(frontList, new Random(seed));
        ArrayList<String> backList = new ArrayList<>(Arrays.asList(cardBack));
        Collections.shuffle(backList, new Random(seed));

        this.cardFront = frontList.toArray(new String[frontList.size()]);
        this.cardBack = backList.toArray(new String[backList.size()]);
    }

    String[] getCards() {
        return cardFront;
    }

    String getCardFront(long id) {
        Log.d("Deck", Arrays.toString(frontArchive));
        return frontArchive[(int) id];
    }

    String getCardBack(long id) {
        return backArchive[(int) id];
    }
}
