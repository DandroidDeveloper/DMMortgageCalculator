package com.chaos.dmmortgagecalculator;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

// main Activity class for the DMMortgageCalculator
public class MainActivity extends Activity
{
    // constants used when saving/restoring state
    private static final String PURCHASE_PRICE="PURCHASE_PRICE";
    private static final String DOWN_PAYMENT="DOWN_PAYMENT";
    private static final String INTEREST_RATE="INTEREST_RATE";
    private static final String CUSTOM_YEARS="CUSTOM_YEARS";
    private double currentPurchasePrice;
    private double currentDownPayment;
    private int currentInterestRate;
    private int currentCustomYears;
    private EditText PurchasePriceEditText;
    private EditText DownPaymentEditText;
    private EditText tenYearMonthlyPaymentEditText;
    private EditText twentyYearMonthlyPaymentEditText;
    private EditText thirtyYearMonthlyPaymentEditText;
    private TextView InterestRateTextView;
    private TextView YearsTextView;
    private EditText LoanAmountEditText;
    private EditText CustomMonthlyPaymentEditText;

    // Called when the activity is first created.
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState); // call superclass's version
        setContentView(R.layout.activity_main); // inflate the GUI

        // check if app just started or is being restored from memory
        if ( savedInstanceState == null ) // the app just started running
        {
            currentPurchasePrice=0.0;//initialize the purchase price
            currentDownPayment=0.0;//initialize the down payment
        } // end if
        else // app is being restored from memory, not executed from scratch
        {
            // initialize the purchase price and down payment amounts to saved amount
            currentPurchasePrice=savedInstanceState.getDouble(PURCHASE_PRICE);
            currentDownPayment=savedInstanceState.getDouble(DOWN_PAYMENT);
            // initialize the custom Interest Rate and Years to saved amounts
            currentInterestRate =
                    savedInstanceState.getInt(INTEREST_RATE);
            currentCustomYears =
                    savedInstanceState.getInt(CUSTOM_YEARS);
        } // end else

        // get references to the ten, twenty, and thirty year monthly payment EditTexts

        tenYearMonthlyPaymentEditText=(EditText) findViewById(R.id.tenYearMonthlyPaymentEditText);
        twentyYearMonthlyPaymentEditText=(EditText) findViewById(R.id.twentyYearMonthlyPaymentEditText);
        thirtyYearMonthlyPaymentEditText=(EditText) findViewById(R.id.thirtyYearMonthlyPaymentEditText);
        // get the TextView displaying the custom Interest Rates and years

        InterestRateTextView=(TextView) findViewById(R.id.InterestRateTextView);
        YearsTextView=(TextView) findViewById(R.id.YearsTextView);
        // get the custom monthly payment and loan amount edit texts

        CustomMonthlyPaymentEditText=(EditText) findViewById(R.id.CustomMonthlyPaymentEditText);
        LoanAmountEditText=(EditText) findViewById(R.id.LoanAmountEditText);
        // get the purchase price and down payment edit texts

        PurchasePriceEditText=(EditText) findViewById(R.id.PurchasePriceEditText);
        DownPaymentEditText=(EditText) findViewById(R.id.DownPaymentEditText);
        // purchase price and down paymentEditTextWatcher handles purchase price and down paymentEditText's onTextChanged event
        PurchasePriceEditText.addTextChangedListener(PurchasePriceEditTextTextWatcher);
        DownPaymentEditText.addTextChangedListener(DownPaymentEditTextTextWatcher);
        // get the SeekBar used to set the interest rate and custom years
        SeekBar customInterestRateSeekBar = (SeekBar) findViewById(R.id.customInterestRateSeekBar);
        customInterestRateSeekBar.setOnSeekBarChangeListener(customInterestRateSeekBarListener);
        SeekBar customYearsSeekBar = (SeekBar) findViewById(R.id.customYearsSeekBar);
        customYearsSeekBar.setOnSeekBarChangeListener(customYearsSeekBarListener);
    } // end method onCreate

    // updates 10, 20, and 30 year amounts
    private void updateStandard()
    {
        double currentLoanAmount=(currentPurchasePrice-currentDownPayment);
        // calculate bill total with a ten percent tip

        double tenYearMonthlyPaymentTotal=((currentLoanAmount*(currentInterestRate*.01)*10)+currentLoanAmount)/(10*12);
        // set tipTenEditText's text to tenPercentTip

        tenYearMonthlyPaymentEditText.setText(String.format("%.02f", tenYearMonthlyPaymentTotal));
        // set totalTenEditText's text to tenPercentTotal

        // calculate bill total with a fifteen percent tip

        double twentyYearMonthlyPaymentTotal=((currentLoanAmount*(currentInterestRate*.01)*20)+currentLoanAmount)/(20*12);

        // set tipFifteenEditText's text to fifteenPercentTip

        // set totalFifteenEditText's text to fifteenPercentTotal
        twentyYearMonthlyPaymentEditText.setText(String.format("%.02f", twentyYearMonthlyPaymentTotal));

        // calculate bill total with a twenty percent tip

        double thirtyYearMonthlyPaymentTotal=((currentLoanAmount*(currentInterestRate*.01)*30)+currentLoanAmount)/(30*12);

        // set tipTwentyEditText's text to twentyPercentTip

        // set totalTwentyEditText's text to twentyPercentTotal
        thirtyYearMonthlyPaymentEditText.setText(String.format("%.02f", thirtyYearMonthlyPaymentTotal));

        LoanAmountEditText.setText(String.format("%.02f", currentLoanAmount));
    } // end method updateStandard

    // updates the custom interest rates, loan amount, and monthly payment
    private void updateCustom()
    {
        double currentLoanAmount=(currentPurchasePrice-currentDownPayment);
        // set Interest Rate and years TextView's text to match the position of the SeekBar

        InterestRateTextView.setText(currentInterestRate+"%");
        YearsTextView.setText(currentCustomYears+"%");
        // calculate the custom tip amount

        // calculate the customMonthlyPayment amount

        double CustomMonthlyPaymentAmount=((currentLoanAmount*(currentInterestRate*.01)*currentCustomYears)+currentLoanAmount)/(currentCustomYears*12);
        // display the custom monthly payment
        CustomMonthlyPaymentEditText.setText(String.format("%.02f", CustomMonthlyPaymentAmount));
    } // end method updateCustom

    // save values of purchase price, down payment, interest rate, and customYearsSeekBar
    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);

        outState.putDouble(PURCHASE_PRICE, currentPurchasePrice);
        outState.putDouble(DOWN_PAYMENT, currentDownPayment);

        outState.putInt(INTEREST_RATE, currentInterestRate);
        outState.putInt(CUSTOM_YEARS, currentCustomYears);

    } // end method onSaveInstanceState


    // called when the user changes the position of SeekBar
    private OnSeekBarChangeListener customInterestRateSeekBarListener =
            new OnSeekBarChangeListener()
            {
                // update currentInterestRate, then call updateCustom
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress,
                                              boolean fromUser)
                {
                    // sets currentCustomInterestRate to position of the SeekBar's thumb
                    currentInterestRate = seekBar.getProgress();
                    updateCustom(); // update EditTexts for custom tip and total
                    updateStandard();
                } // end method onProgressChanged

                @Override
                public void onStartTrackingTouch(SeekBar seekBar)
                {
                } // end method onStartTrackingTouch

                @Override
                public void onStopTrackingTouch(SeekBar seekBar)
                {
                } // end method onStopTrackingTouch
            }; // end OnSeekBarChangeListener
    // called when the user changes the position of SeekBar
    private OnSeekBarChangeListener customYearsSeekBarListener =
            new OnSeekBarChangeListener()
            {
                // update currentYears, then call updateCustom
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress,
                                              boolean fromUser)
                {
                    // sets currentCustomYears to position of the SeekBar's thumb
                    currentCustomYears = seekBar.getProgress();
                    updateCustom(); // update EditTexts for custom tip and total
                } // end method onProgressChanged

                @Override
                public void onStartTrackingTouch(SeekBar seekBar)
                {
                } // end method onStartTrackingTouch

                @Override
                public void onStopTrackingTouch(SeekBar seekBar)
                {
                } // end method onStopTrackingTouch
            }; // end OnSeekBarChangeListener
    // event-handling object that responds to purchasepriceeditText's events
    private TextWatcher PurchasePriceEditTextTextWatcher = new TextWatcher()
    {
        // called when the user enters a number
        @Override
        public void onTextChanged(CharSequence s, int start,
                                  int before, int count)
        {
            // convert purchasePriceEditText's text to a double
            try
            {
                currentPurchasePrice = Double.parseDouble(s.toString());
            } // end try
            catch (NumberFormatException e)
            {
                currentPurchasePrice = 0.0; // default if an exception occurs
            } // end catch

            // update the standard and custom monthlypayment EditTexts
            updateStandard(); // update the 10, 20 and 30 yr amounts
            updateCustom(); // update the custom amounts
        } // end method onTextChanged

        @Override
        public void afterTextChanged(Editable s)
        {
        } // end method afterTextChanged

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after)
        {
        } // end method beforeTextChanged
    }; // end EditTextWatcher
    private TextWatcher DownPaymentEditTextTextWatcher = new TextWatcher()
    {
        // called when the user enters a number
        @Override
        public void onTextChanged(CharSequence s, int start,
                                  int before, int count)
        {
            // convert downpaymentEditText's text to a double
            try
            {
                currentDownPayment = Double.parseDouble(s.toString());
            } // end try
            catch (NumberFormatException e)
            {
                currentDownPayment = 0.0; // default if an exception occurs
            } // end catch

            // update the standard and custom downpayment EditTexts
            updateStandard(); // update the 10, 20, 30 yr edittexts
            updateCustom(); // update the custom  EditTexts
        } // end method onTextChanged

        @Override
        public void afterTextChanged(Editable s)
        {
        } // end method afterTextChanged

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after)
        {
        } // end method beforeTextChanged
    }; // end downpaymentEditTextWatcher
} // end class DMMortgageCalculator
