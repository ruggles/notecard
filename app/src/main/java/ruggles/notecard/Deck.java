package ruggles.notecard;

/**
 * Created by ruggles on 7/30/17.
 *
 * This data structure stores 2 arrays, the front & back of each note card
 * When you flip a card, it swaps between arrays.
 */



public class Deck {

    private String[] cardFront, cardBack;

    Deck(String[] cardFront, String[] cardBack)
    {
        this.cardFront = cardFront;
        this.cardBack = cardBack;
        shuffle(); //unimplemented
    }

    void flip(int cardPos)
    {
        String placeholder = cardFront[cardPos];
        cardFront[cardPos] = cardBack[cardPos];
        cardBack[cardPos] = placeholder;
    }

    void shuffle()
    {
        //TODO IMPLEMENT CARD SHUFFLING
    }

    String[] getCards() {
        return cardFront;
    }
}
