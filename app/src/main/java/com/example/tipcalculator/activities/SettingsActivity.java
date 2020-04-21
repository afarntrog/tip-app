package com.example.tipcalculator.activities;

import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.EditTextPreference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.tipcalculator.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    // https://stackoverflow.com/a/55891144/11576212
//    @Override
//    public boolean onSupportNavigateUp() {
//        onBackPressed();
//        return true;
//    }

    // This will enable the back-arrow button in the title bar to close the Settings Activity
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {

        private void setEditTextPrefsInputTypeToDecimal() {
            String taxPrefKey = getString(R.string.defaultTaxPercentageKey);
            String tipPrefKey = getString(R.string.defaultTipPercentageKey);
            EditTextPreference taxPreference = findPreference(taxPrefKey);
            EditTextPreference tipPreference = findPreference(tipPrefKey);
            if (taxPreference != null && tipPreference != null) {
                EditTextPreference.OnBindEditTextListener listener = getNewDecimalListener();
                taxPreference.setOnBindEditTextListener(listener);
                tipPreference.setOnBindEditTextListener(listener);
            }
        }

        private EditTextPreference.OnBindEditTextListener getNewDecimalListener() {
            return new EditTextPreference.OnBindEditTextListener() {
                @Override
                public void onBindEditText(@NonNull EditText editText) {
                    editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                }
            };
        }

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            setEditTextPrefsInputTypeToDecimal(); //    This will make both the tax and tip dialogs allow only (decimal) numbers
        }
    }
}