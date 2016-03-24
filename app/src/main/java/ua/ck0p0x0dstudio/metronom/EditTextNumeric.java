package ua.ck0p0x0dstudio.metronom;

import android.content.Context;
import android.text.InputFilter;
import android.text.InputType;
import android.widget.EditText;

/**
 * Created by CK0P0X0D on 22.03.2016.
 */
public class EditTextNumeric extends EditText {
    protected int max_value = Integer.MAX_VALUE;
    protected int min_value = Integer.MIN_VALUE;

    // constructor
    public EditTextNumeric(Context context) {
        super(context);
        this.setInputType(InputType.TYPE_CLASS_NUMBER);
    }

    // checks whether the limits are set and corrects them if not within limits
    @Override
    protected void onTextChanged(CharSequence text, int start, int before, int after) {
        if (max_value != Integer.MAX_VALUE) {
            try {
                if (Integer.parseInt(this.getText().toString()) > max_value) {
                    // change value and keep cursor position
                    int selection = this.getSelectionStart();
                    this.setText(String.valueOf(max_value));
                    if (selection >= this.getText().toString().length()) {
                        selection = this.getText().toString().length();
                    }
                    this.setSelection(selection);
                }
            } catch (NumberFormatException exception) {
                super.onTextChanged(text, start, before, after);
            }
        }
        if (min_value != Integer.MIN_VALUE) {
            try {
                if (Integer.parseInt(this.getText().toString()) < min_value) {
                    // change value and keep cursor position
                    int selection = this.getSelectionStart();
                    this.setText(String.valueOf(min_value));
                    if (selection >= this.getText().toString().length()) {
                        selection = this.getText().toString().length();
                    }
                    this.setSelection(selection);
                }
            } catch (NumberFormatException exception) {
                super.onTextChanged(text, start, before, after);
            }
        }
        super.onTextChanged(text, start, before, after);
    }

    // set the max number of digits the user can enter
    public void setMaxLength(int length) {
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(8);
        this.setFilters(FilterArray);
    }

    // set the maximum integer value the user can enter.
    // if exeeded, input value will become equal to the set limit
    public void setMaxValue(int value) {
        max_value = value;
    }
    // set the minimum integer value the user can enter.
    // if entered value is inferior, input value will become equal to the set limit
    public void setMinValue(int value) {
        min_value = value;
    }

    // returns integer value or 0 if errorous value
    public int getValue() {
        try {
            return Integer.parseInt(this.getText().toString());
        } catch (NumberFormatException exception) {
            return 0;
        }
    }
}
