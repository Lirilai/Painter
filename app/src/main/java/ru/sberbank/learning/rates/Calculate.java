package ru.sberbank.learning.rates;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import static android.R.attr.y;
import static android.content.ContentValues.TAG;
import static java.lang.String.format;

/**
 * Created by Lirilai on 01.05.2017.
 */

public class Calculate extends Activity {
    private double valueSet;
    private double nominalSet;
    private String codeSet;

    private double changedNominal;

    TextView valueView;
    TextView codeView;
    EditText nominalView;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate);

        Intent intent = getIntent();

        valueSet = intent.getDoubleExtra(RatesActivity.EXTRA_VALUE, valueSet);
        nominalSet = intent.getDoubleExtra(RatesActivity.EXTRA_NOMINAL, nominalSet);
        codeSet = intent.getStringExtra(RatesActivity.EXTRA_CODE);

        valueView = (TextView) findViewById(R.id.get_value);
        codeView = (TextView) findViewById(R.id.get_code);
        nominalView = (EditText) findViewById(R.id.get_nominal);

        valueView.setText(String.valueOf(valueSet));
        codeView.setText(codeSet);
        nominalView.setText(String.format(Locale.US, "%.0f", nominalSet));

        nominalView.setSelection(nominalView.getText().length());

        nominalView.addTextChangedListener(getNewNominal);

    }


    private final TextWatcher getNewNominal = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            Log.v(TAG, "before");

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            Log.v(TAG, "on");

        }

        @Override
        public void afterTextChanged(Editable s) {
            Log.v(TAG, "after");

            if (s.length() != 0) {
                changedNominal = Double.parseDouble(s.toString());
                double valueChanged = (valueSet/nominalSet) * changedNominal;

                valueView.setText(String.format(Locale.US, "%.4f", valueChanged));

            } else {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Unsuitable nominal!", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    };
}
