package com.rfdetke.digitriadlaboratory;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButtonToggleGroup;

import java.util.Objects;

public class NewExperimentActivity extends AppCompatActivity {
    public static final String EXTRA_CODENAME = "com.rfdetke.digitriadlaboratory.CODENAME";
    public static final String EXTRA_DESCRIPTION = "com.rfdetke.digitriadlaboratory.DESCRIPTION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_experiment);

        Objects.requireNonNull(getSupportActionBar()).setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.toolbar);

        TextView toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        toolbarTitle.setText(getString(R.string.new_experiment));

        findViewById(R.id.toolbar_button).setVisibility(View.INVISIBLE);

        EditText codenameEditText = (EditText) findViewById(R.id.experiment_codename);
        EditText descriptionEditText = (EditText) findViewById(R.id.experiment_description);

        Button saveButton = (Button) findViewById(R.id.save_button);
        saveButton.setOnClickListener(v -> {
            Intent replyIntent = new Intent();
            if(TextUtils.isEmpty(codenameEditText.getText())) {
                Toast.makeText(this, R.string.required_field_empty, Toast.LENGTH_SHORT).show();
            } else {
                String codename = codenameEditText.getText().toString();
                String description = descriptionEditText.getText().toString();

                replyIntent.putExtra(EXTRA_CODENAME, codename);
                replyIntent.putExtra(EXTRA_DESCRIPTION, description);
                setResult(RESULT_OK, replyIntent);
                finish();
            }

        });
    }
}