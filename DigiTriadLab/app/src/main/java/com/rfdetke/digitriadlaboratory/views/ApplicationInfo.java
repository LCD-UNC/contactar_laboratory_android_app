package com.rfdetke.digitriadlaboratory.views;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.rfdetke.digitriadlaboratory.BuildConfig;
import com.rfdetke.digitriadlaboratory.R;

public class ApplicationInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_info);

        TextView version = findViewById(R.id.app_version);
        version.setText(BuildConfig.VERSION_NAME);
    }
}