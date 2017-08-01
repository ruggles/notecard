package ruggles.notecard;

import android.view.View;
import android.widget.EditText;

/**
 * Created by ruggles on 7/29/17.
 */

public class CardTextWrapper {

    private EditText frontField=null;
    private EditText backField=null;
    private View base=null;

    CardTextWrapper(View base) {
            this.base=base;
        }

    public String getBack() {
        return(getBackField().getText().toString());
    }

    private EditText getBackField() {
        if (backField==null) {
            backField=(EditText)base.findViewById(R.id.backEdit);
        }
        return(backField);

    }


    public String getFront() {
            return(getFrontField().getText().toString());
        }

    private EditText getFrontField() {
        if (frontField==null) {
            frontField=(EditText)base.findViewById(R.id.frontEdit);
        }
        return(frontField);

    }


}



