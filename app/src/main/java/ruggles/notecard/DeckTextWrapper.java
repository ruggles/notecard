package ruggles.notecard;

import android.view.View;
import android.widget.EditText;

/**
 * Created by ruggles on 7/29/17.
 */

public class DeckTextWrapper {

    EditText nameField=null;
    View base=null;

    DeckTextWrapper(View base) {
            this.base=base;
        }

    public String getName() {
            return(getNameField().getText().toString());
        }

    private EditText getNameField() {
        if (nameField==null) {
            nameField=(EditText)base.findViewById(R.id.deckName);
        }
        return(nameField);

    }


}



