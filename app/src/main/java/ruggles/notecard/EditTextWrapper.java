package ruggles.notecard;

import android.view.View;
import android.widget.EditText;

/**
 * Created by ruggles on 8/16/17.
 */

// This class will take an inflated view, and the ID for the text field as input
// and hold them so you can pass them between functions.

public class EditTextWrapper {

    private EditText textField=null;
    private View base=null;
    private int id;

    EditTextWrapper(View base, int id) {
        this.base=base;
        this.id=id;
    }

    public String getText() {
        return(getTextField().getText().toString());
    }

    private EditText getTextField() {
        if (textField==null) {
            textField=(EditText)base.findViewById(id);
        }
        return(textField);
    }

}
