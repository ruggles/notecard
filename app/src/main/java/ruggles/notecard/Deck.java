package ruggles.notecard;

/**
 * Created by ruggles on 7/30/17.
 *
 * This data structure stores 2 arrays, the front & back of each note card
 * When you flip a card, it swaps between arrays.
 */



public class Deck {

    private String[] cardFront, cardBack, frontArchive;

    Deck(String[] cardFront, String[] cardBack)
    {
        this.cardFront = cardFront;
        this.frontArchive = cardFront;
        this.cardBack = cardBack;
        shuffle(); //unimplemented
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
        //TODO IMPLEMENT CARD SHUFFLING
    }

    String[] getCards() {
        return cardFront;
    }

    String getcardFront(long id) {
        return frontArchive[(int) id];
    }
}
