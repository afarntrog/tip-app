package com.example.tipcalculator.classes;

import com.google.gson.Gson;

import java.text.DecimalFormat;

import androidx.annotation.NonNull;

/**
 * Tip Calculator Class
 */

@SuppressWarnings ( {"unused", "WeakerAccess"})
public class TipCalculator
{
    private double subtotal, taxPercent, taxAmount, tipPercent;
    private int payers;
    private char currencySymbol;
    private transient DecimalFormat mCurrencyFormatter, mNoCurrencySignFormatter, mPctFormatter;
    private final String BASE_FORMAT = "#,##0.00";
    private boolean mTaxPercentAutoSet;


    public TipCalculator ()
    {
        this (0, 0, 0,
              0, 1, '$');
    }

    public TipCalculator (double subtotal, double taxPercent,  double taxAmount, double tipPercent,
                          int payers, char currencySymbol)
    {
        initializeFields (subtotal, taxPercent, taxAmount, tipPercent, currencySymbol, payers);
        mTaxPercentAutoSet = false;
        setTaxPercentFromAmount ();
        initializeDecimalFormatObjects ();
    }

    private void initializeFields (double subtotal, double taxPercent, double taxAmount,
                                   double tipPercent, char currencySymbol, int payers)
    {
        this.subtotal = subtotal;
        this.taxPercent = getActualPercent (taxPercent);
        this.taxAmount = taxAmount;
        this.tipPercent = getActualPercent (tipPercent);
        this.currencySymbol = currencySymbol;
        this.payers = payers;
    }

    private double getActualPercent (double percent)
    {
        return percent < 1 ? percent : percent / 100;
    }

    private void reinitializeDecimalFormatObjectsIfNull ()
    {
        if (mPctFormatter == null)
        {
            initializeDecimalFormatObjects ();
        }
    }

    private void initializeDecimalFormatObjects ()
    {
        String PERCENT_FORMAT = "#0.00#%";
        mPctFormatter = new DecimalFormat (PERCENT_FORMAT);
        mNoCurrencySignFormatter = new DecimalFormat (BASE_FORMAT);
        initializeCurrencyFormatObject ();
    }

    private void initializeCurrencyFormatObject ()
    {
        mCurrencyFormatter = new DecimalFormat (currencySymbol + BASE_FORMAT);
    }

    public double getSubtotal ()
    {
        return subtotal;
    }

    public double getTaxPercent ()
    {
        return taxPercent;
    }

    public double getTaxAmountCalculated ()
    {
        return subtotal * taxPercent;
    }

    public double getTaxAmountEntered ()
    {
        return taxAmount;
    }

    public double getTipPercent ()
    {
        return tipPercent;
    }

    public double getTipAmount ()
    {
        return subtotal * tipPercent;
    }

    public char getCurrencySymbol ()
    {
        return currencySymbol;
    }

    public int getPayers ()
    {
        return payers;
    }

    public String getPayersString () { return Integer.toString (payers); }

    public String getSubTotalFormattedWithCurrencySymbol ()
    {
        reinitializeDecimalFormatObjectsIfNull();
        return mCurrencyFormatter.format (subtotal);
    }

    public String getSubTotalFormattedWithNoCurrencySymbol ()
    {
        reinitializeDecimalFormatObjectsIfNull();
        return mNoCurrencySignFormatter.format (subtotal);
    }

    public String getSubTotalPerPersonFormattedWithCurrencySymbol ()
    {
        reinitializeDecimalFormatObjectsIfNull();
        return mCurrencyFormatter.format (subtotal/payers);
    }

    public String getSubTotalPerPersonFormattedWithNoCurrencySymbol ()
    {
        reinitializeDecimalFormatObjectsIfNull();
        return mNoCurrencySignFormatter.format (subtotal/payers);
    }

    public String getTaxPercentFormatted ()
    {
        reinitializeDecimalFormatObjectsIfNull();
        return (mPctFormatter.format (taxPercent));
    }

    public String getTipPercentFormatted ()
    {
        reinitializeDecimalFormatObjectsIfNull();
        return (mPctFormatter.format (tipPercent));
    }

    public String getTaxAmountFormattedWithCurrencySymbol ()
    {
        reinitializeDecimalFormatObjectsIfNull();
        return mCurrencyFormatter.format (subtotal * taxPercent);
    }

    public String getTaxAmountFormattedWithNoCurrencySymbol ()
    {
        reinitializeDecimalFormatObjectsIfNull();
        return mNoCurrencySignFormatter.format (subtotal * taxPercent);
    }

    public String getTaxAmountPerPersonFormattedWithCurrencySymbol ()
    {
        reinitializeDecimalFormatObjectsIfNull();
        return mCurrencyFormatter.format ((subtotal * taxPercent)/payers);
    }

    public String getTaxAmountPerPersonFormattedWithNoCurrencySymbol ()
    {
        reinitializeDecimalFormatObjectsIfNull();
        return mNoCurrencySignFormatter.format ((subtotal * taxPercent)/payers);
    }

    public String getTipAmountFormattedWithCurrencySymbol ()
    {
        reinitializeDecimalFormatObjectsIfNull();
        return mCurrencyFormatter.format (subtotal * tipPercent);
    }

    public String getTipAmountFormattedWithNoCurrencySymbol ()
    {
        reinitializeDecimalFormatObjectsIfNull();
        return mNoCurrencySignFormatter.format (subtotal * tipPercent);
    }

    public String getTipAmountPerPersonFormattedWithCurrencySymbol ()
    {
        reinitializeDecimalFormatObjectsIfNull();
        return mCurrencyFormatter.format ((subtotal * tipPercent)/payers);
    }

    public String getTipAmountPerPersonFormattedWithNoCurrencySymbol ()
    {
        reinitializeDecimalFormatObjectsIfNull();
        return mNoCurrencySignFormatter.format ((subtotal * tipPercent)/payers);
    }

    public String getTotalFormattedWithCurrencySymbol ()
    {
        reinitializeDecimalFormatObjectsIfNull();
        return mCurrencyFormatter.format (subtotal + getTipAmount () + taxAmount);
    }

    public String getTotalFormattedWithNoCurrencySymbol ()
    {
        reinitializeDecimalFormatObjectsIfNull();
        return mNoCurrencySignFormatter.format (subtotal + getTipAmount () + taxAmount);
    }

    public String getTotalPerPersonFormattedWithCurrencySymbol ()
    {
        reinitializeDecimalFormatObjectsIfNull();
        return mCurrencyFormatter.format ((subtotal + getTipAmount () + taxAmount) / payers);
    }

    public String getTotalPerPersonFormattedWithNoCurrencySymbol ()
    {
        reinitializeDecimalFormatObjectsIfNull();
        return mNoCurrencySignFormatter.format ((subtotal + getTipAmount () + taxAmount) / payers);
    }

    @NonNull @Override
    public String toString ()
    {
        return "TipCalculator{" +
                "subtotal=" + getSubTotalFormattedWithCurrencySymbol () +
                ", taxPercent=" + getTaxPercentFormatted () +
                ", taxAmount=" + getTaxAmountFormattedWithCurrencySymbol () +
                ", tipPercent=" + getTipPercentFormatted () +
                ", tipAmount=" + getTipAmountFormattedWithCurrencySymbol () +
                ", payers=" + payers +
                '}';
    }

    private void setTaxPercentFromAmount ()
    {
        if (taxAmount !=0 && subtotal !=0) {
            taxPercent = taxAmount / subtotal;
            mTaxPercentAutoSet = true;
        }
    }

    public void setSubtotal (double subtotal)
    {
        this.subtotal = subtotal;
        setTaxPercentFromAmount ();
    }

    public void setSubtotal (String strData)
    {
        setSubtotal (getDoubleFromCurrencyString (strData));
    }

    public void setTaxAmount (double taxAmount)
    {
        this.taxAmount = taxAmount;
        setTaxPercentFromAmount ();
    }


    public void setTaxAmount (String strData)
    {
        setTaxAmount (getDoubleFromCurrencyString (strData));
    }

    public void setTaxPercent (double taxPercent)
    {
        this.taxPercent = getActualPercent (taxPercent);
        mTaxPercentAutoSet = false;
    }

    public void setTaxPercent (String strData)
    {
        setTaxPercent (getDoubleFromPercentString (strData));
    }

    public void setTipPercent (double tipPercent)
    {
        this.tipPercent = getActualPercent (tipPercent);
    }


    public void setTipPercent (String strData)
    {
        setTipPercent (getDoubleFromPercentString (strData));
    }

    public void setPayers (int payers)
    {
        this.payers = payers;
    }

    public void setPayers (String strData)
    {
        setPayers (getIntFromPayersString (strData));
    }

    public void setCurrencySymbol (char currencySymbol)
    {
        this.currencySymbol = currencySymbol;
        initializeCurrencyFormatObject ();
    }

    public static int getIntFromPayersString (String strPayers)
    {
        return (Integer.parseInt (strPayers.trim ()));
    }

    public static double getDoubleFromCurrencyString (String strData)
    {
        // Trim
        strData = strData.trim ();

        // Remove currency symbol, if any
        strData = Character.isDigit (strData.charAt (0))
                  ? strData
                  : strData.substring (1); //, strData.length ());

        // Remove commas
        strData = strData.replace (",", "");

        return Double.parseDouble (strData);
    }

    public static double getDoubleFromPercentString (String strData)
    {
        // Trim
        strData = strData.trim ();

        // Remove percent symbol, if any
        strData = Character.isDigit (strData.charAt (strData.length () - 1))
                  ? strData
                  : strData.substring (0, strData.length () - 1);

        // Remove commas
        strData = strData.replace (",", "");

        return Double.parseDouble (strData);
    }

    /**
     * Serializes the current object so it can be stored in the Bundle during rotation
     *
     * @param obj the Object to be serialized
     * @return Serialized (String) of the current object
     */
    public static String getJSONStringFromObject (TipCalculator obj)
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
    public static TipCalculator restoreObjectFromJSONString (String json)
    {
        Gson gson = new Gson ();
        return gson.fromJson (json, TipCalculator.class);
    }
}
