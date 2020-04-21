package com.example.tipcalculator.classes;

import android.content.Context;
import android.text.Editable;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.common.primitives.Doubles;
import com.google.gson.Gson;
import com.example.tipcalculator.R;

import java.time.Duration;


@SuppressWarnings ( {"FieldCanBeLocal", "unused"})
public class KeyPadController
{
    private Context mContext;
    private String mCurrentButtonTextString;
    private EditText mCurrentET;
    private Editable mCurrentETEditable;
    private int mCurrentPosition;

    private int mCurrentTextLength, mCurrentTextSelectionPosStart, mCurrentTextSelectionPosEnd;

    private final String BACKSPACE = "⌫", PERIOD = ".", UP = "+", DOWN = "-";

    public KeyPadController (Context context, String currentButtonText,
                             EditText currentET, int currentPosition)
    {
        if (currentButtonText.isEmpty () || currentET == null) {
            throw new IllegalArgumentException
                    ("Button text String must not be empty and current Field must not be null");
        }
        else {
            mContext = context;
            mCurrentButtonTextString = currentButtonText;
            mCurrentET = currentET;
            mCurrentPosition = currentPosition;
        }
    }

    public String getCurrentETString ()
    {
        return mCurrentET.toString ();
    }

    public void setCurrentButtonTextString (String currentButtonTextString)
    {
        mCurrentButtonTextString = currentButtonTextString;
    }

    public void setCurrentET (EditText currentET)
    {
        mCurrentET = currentET;
    }

    public void setCurrentPosition (int currentPosition)
    {
        mCurrentPosition = currentPosition;
    }

    public void process ()
    {
        updateFields ();

        if (mCurrentButtonTextString.equals (BACKSPACE)) {
            handleBackspacePress ();
        }
        else {
            handleNumberPeriodOrArrowPress ();
        }
    }

    private void updateFields ()
    {
        mCurrentETEditable = mCurrentET.getText ();
        mCurrentTextLength = mCurrentETEditable.toString ().length ();
        mCurrentTextSelectionPosStart = mCurrentET.getSelectionStart ();
        mCurrentTextSelectionPosEnd = mCurrentET.getSelectionEnd ();
    }

    private void handleBackspacePress ()
    {
        if (hasTextSelected ()) {
            clearSelectedText ();
        }
        else {
            clearEditTextOrBackspaceCurrentCharacter ();
        }
    }

    private boolean hasTextSelected ()
    {
        String currentSelectedText = mCurrentET.getText ().toString ().substring
                (mCurrentTextSelectionPosStart, mCurrentTextSelectionPosEnd);

        return !currentSelectedText.isEmpty ();
    }

    private void clearSelectedText ()
    {
        int posStart = mCurrentTextSelectionPosStart;
        int posEnd = mCurrentTextSelectionPosEnd;
        int len = mCurrentTextLength;
        CharSequence beforeCurrentPos, afterCurrentPos;

        beforeCurrentPos = posStart > 0 ? mCurrentETEditable.subSequence (0, posStart) : "";
        afterCurrentPos = posEnd < len ? mCurrentETEditable.subSequence (posEnd, len) : "";

        mCurrentET.setText (beforeCurrentPos.toString ().concat (afterCurrentPos.toString ()));

        mCurrentET.setSelection (mCurrentTextSelectionPosStart);

    }

    private void clearEditTextOrBackspaceCurrentCharacter ()
    {
        // backspace works only if the cursor is currently past the start of the EditText field
        if (mCurrentTextSelectionPosStart > 0) {
            if (mCurrentTextLength == 1) {
                mCurrentET.setText ("");
            }
            else if (mCurrentTextLength > 1) {
                removePreviousCharacterFromCurrentField ();
            }
        }
        else {
            Toast.makeText (mContext, "Cannot remove text here", Toast.LENGTH_SHORT).show ();
        }
    }

    private void handleNumberPeriodOrArrowPress ()
    {
        // prevent entry of a second decimal point
        if (mCurrentButtonTextString.equals (PERIOD) &&
                (mCurrentETEditable.toString ().contains (PERIOD))) {
            outputErrorTwoPeriods ();
        }
        else if (mCurrentButtonTextString.equals (UP) || mCurrentButtonTextString.equals (DOWN)) {
            handleUpOrDownPress ();
        }
        else {
            clearAnySelectedTextThenUpdateFields ();
            handleNumberPress ();
        }
    }

    private void removePreviousCharacterFromCurrentField ()
    {
        int positionStart = mCurrentTextSelectionPosStart;
        int len = mCurrentTextLength;
        CharSequence beforeCurrentPos, afterCurrentPos;

        beforeCurrentPos = mCurrentETEditable.subSequence (0, positionStart - 1);
        afterCurrentPos = mCurrentETEditable.subSequence (positionStart, len);
        mCurrentET.setText (beforeCurrentPos.toString ().concat (afterCurrentPos.toString ()));

        mCurrentET.setSelection (mCurrentTextSelectionPosStart - 1);
    }

    private void outputErrorTwoPeriods ()
    {
        Toast.makeText (mContext,
                        mContext.getString (R.string.error_keypad_decimal_point_not_allowed_here),
                        Toast.LENGTH_SHORT).show ();
    }

    @SuppressWarnings ("UnstableApiUsage") private void handleUpOrDownPress ()
    {
        String currentString = mCurrentETEditable.toString ();
        boolean upButtonPressed = mCurrentButtonTextString.equals (UP);
        boolean currentFieldIsPlayers = mCurrentPosition == 3;
        int valueToAdd = upButtonPressed ? 1 : -1;

        if (Doubles.tryParse (currentString) == null) {
            Log.d ("CUR", "Can't parse; current is Payer: " + currentFieldIsPlayers);
            mCurrentET.setText (currentFieldIsPlayers ? "1" : upButtonPressed ? "1.0" : "0.0");
        }
        else {
            Log.d ("CUR", "CAN parse; current is Payer: " + currentFieldIsPlayers +
                    "; str is " + currentString);

            String newText = (currentFieldIsPlayers
                              ? Integer.toString (Integer.parseInt (currentString) + valueToAdd)
                              : Double.toString (Double.parseDouble (currentString) + valueToAdd));

            newText = newText.charAt (0) != '-' ? newText : currentFieldIsPlayers ? "0" : "0.0";
            mCurrentET.setText (newText);
        }
    }

    private void clearAnySelectedTextThenUpdateFields ()
    {
        // prepare the text
        if (hasTextSelected ()) {
            // clear any selected text
            clearSelectedText ();
            // update the member variables after running the prior method
            updateFields ();
        }
    }

    private void handleNumberPress ()
    {
        // put the number in wherever it should go
        insertNumberIntoEditText ();

        // advance cursor to reflect addition of character to the currentButton text
        mCurrentET.setSelection (
                mCurrentTextSelectionPosStart + mCurrentButtonTextString.length ());
        // currently, length() will always return a 1.
    }

    private void insertNumberIntoEditText ()
    {
        // if the field is empty, then simply set the text to the current button's text
        if (mCurrentTextLength == 0) {
            mCurrentET.setText (mCurrentButtonTextString);
        }
        // else if the field is not empty and the "cursor" is at the end of the field
        // then append the text to the current text
        else if (mCurrentTextSelectionPosStart == mCurrentTextLength) {
            mCurrentET.setText (mCurrentETEditable.append (mCurrentButtonTextString));
        }
        // otherwise, if the cursor is in the middle of the text,
        // then insert the button text
        else {
            insertTextInMiddleOfEditText ();
        }
    }

    private void insertTextInMiddleOfEditText ()
    {
        // text to be set before current insertion
        CharSequence beforeCurrentPosition =
                mCurrentETEditable.subSequence (0, mCurrentTextSelectionPosStart);

        // text to be set after current insertion
        CharSequence afterCurrentPosition =
                mCurrentETEditable.subSequence (mCurrentTextSelectionPosStart,
                                                mCurrentTextLength);

        // text of current insertion is already referred to in currentButton

        // put it all together (before + currentButton + after)
        mCurrentET.setText (beforeCurrentPosition.toString ()
                                    .concat (mCurrentButtonTextString)
                                    .concat (afterCurrentPosition.toString ()));
    }

    /**
     * Serializes the current object so it can be stored in the Bundle during rotation
     *
     * @param obj the Object to be serialized
     * @return Serialized (String) of the current object
     */
    public static String getJSONStringFromObject (KeyPadController obj)
    {
        Gson gson = new Gson ();
        return gson.toJson (obj);
    }

    /**
     * Reverses the serialization of the object String back to an object
     *
     * @param json The serialized String of the object
     * @return The object, de-serialized from the String
     */
    public static KeyPadController restoreObjectFromJSONString (String json)
    {
        Gson gson = new Gson ();
        return gson.fromJson (json, KeyPadController.class);
    }

}
