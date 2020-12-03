package com.contactar.contactarlaboratory.views;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.contactar.contactarlaboratory.R;

public class ScanQrExperiment extends AppCompatActivity {

    public static final String EXTRA_CONFIG_STRING = "com.contactar.contactarlaboratory.CONFIG_STRING";
    private CodeScanner mCodeScanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr_experiment);
        CodeScannerView scannerView = findViewById(R.id.scanner_view);

        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(result ->
                runOnUiThread(() -> {
                    Intent replyIntent = new Intent();
                    replyIntent.putExtra(EXTRA_CONFIG_STRING, result.getText());
                    setResult(Activity.RESULT_OK, replyIntent);
                    finish();
                }));

        scannerView.setOnClickListener(view -> mCodeScanner.startPreview());
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }
}