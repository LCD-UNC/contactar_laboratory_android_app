package com.rfdetke.digitriadlaboratory.gapis;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.rfdetke.digitriadlaboratory.R;

import static com.rfdetke.digitriadlaboratory.gapis.GoogleServicesHelper.REQUEST_CODE_SIGN_IN;

public abstract class GoogleSessionAppCompatActivity extends AppCompatActivity {
    protected GoogleServicesHelper googleServicesHelper;

    public void signIn(View view) {
        if(!googleServicesHelper.isSignedIn(getApplicationContext()))
            startActivityForResult(googleServicesHelper.getSignInIntent(), REQUEST_CODE_SIGN_IN);
    }

    public void signOut(){
        if(googleServicesHelper.isSignedIn(getApplicationContext())){
            googleServicesHelper.signOut(getApplicationContext());
            Toast.makeText(this, "Signed out...", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        googleServicesHelper = GoogleServicesHelper.getInstance(getApplicationContext(), findViewById(R.id.signInFab));

        BottomAppBar bottomAppBar = findViewById(R.id.bottomAppBar);
        bottomAppBar.setOnMenuItemClickListener(this::onMenuItemClickListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SIGN_IN) {
            if (data != null) {
                googleServicesHelper.handleSignInResult(getApplicationContext(), data);
            }
        }

    }

    public abstract boolean onMenuItemClickListener(MenuItem item);
}
