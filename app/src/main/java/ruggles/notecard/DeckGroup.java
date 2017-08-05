package ruggles.notecard;

/**
 * Created by ruggles on 8/5/17.
 */

// This object stores deck information, lowering the amount of
// raw db calls required

public class DeckGroup {
    private DeckInfo[] deckArray;

    public void update(int[] idArray, String[] nameArray) {
        if (BuildConfig.DEBUG && !(idArray.length == nameArray.length))
            throw new AssertionError("idArray is not the same length as nameArray");

        deckArray = new DeckInfo[idArray.length];

        for (int i=0; i < idArray.length; i++) {
            deckArray[i] = new DeckInfo(idArray[i], nameArray[i]);
        }

    }

    // Pulls the deckID based on position of selected deck
    public long getID(int deckPos)
    {
        return deckArray[deckPos].get_id();
    }

    public DeckInfo getInfoObject(int deckPos) {
        return deckArray[deckPos];
    }

    // Pulls array of names
    public String[] getDeckList()
    {
        String[] deckList = new String[deckArray.length];

        for (int i = 0; i < deckArray.length; i++)
        {
            deckList[i] = deckArray[i].getName();
        }

        return deckList;
    }

    // Checks for duplicate names
    public boolean isNameUsed(String name)
    {
        for (DeckInfo deck: deckArray)
        {
            if (deck.getName().equals(name))
            {
                return true;
            }
        }
        return false;
    }


}

class DeckInfo {
    final private int _id;
    final private String name;

    DeckInfo (int _id, String name)
    {
        this._id = _id;
        this.name = name;
    }

    public int get_id() {
        return _id;
    }

    public String getName() {
        return name;
    }
}