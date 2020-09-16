package com.rfdetke.digitriadlaboratory.gapis;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.Scope;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.rfdetke.digitriadlaboratory.R;
import com.rfdetke.digitriadlaboratory.gapis.drive.DriveServiceHelper;

import java.util.Collections;

public class GoogleServicesHelper {
    public static final int REQUEST_CODE_SIGN_IN = 5;

    private static GoogleServicesHelper INSTANCE;

    private FloatingActionButton signInButton;

    private GoogleSignInClient client;

    private DriveServiceHelper driveServiceHelper;

    private GoogleServicesHelper(Context context, FloatingActionButton signInButton) {
        this.signInButton = signInButton;

        GoogleSignInOptions signInOptions =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .requestScopes(new Scope(DriveScopes.DRIVE))
                        .build();
        client = GoogleSignIn.getClient(context, signInOptions);
    }

    public static GoogleServicesHelper getInstance(Context context, FloatingActionButton signInButton) {
        if(INSTANCE == null) {
            INSTANCE = new GoogleServicesHelper(context, signInButton);
        }
        INSTANCE.setSignInButton(signInButton);
        if(INSTANCE.isSignedIn(context)) {
            signInButton.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.green300));
        } else {
            signInButton.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.red300));
        }
        return INSTANCE;
    }

    public Intent getSignInIntent() {
        return client.getSignInIntent();
    }

    public void setSignInButton(FloatingActionButton signInButton) {
        this.signInButton = signInButton;
    }

    public boolean isSignedIn(Context context) {
        return (GoogleSignIn.getLastSignedInAccount(context) != null) && (client != null);
    }

    public void signOut(Context context) {
        signInButton.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.red300));
        client.signOut();
    }

    public void handleSignInResult(Context context, Intent result) {
        GoogleSignIn.getSignedInAccountFromIntent(result)
                .addOnSuccessListener(googleAccount -> {
                    Toast.makeText(context, "Signed in as " + googleAccount.getEmail(), Toast.LENGTH_SHORT).show();

                    // Use the authenticated account to sign in to the Drive service.
                    GoogleAccountCredential credential =
                            GoogleAccountCredential.usingOAuth2(
                                    context, Collections.singleton(DriveScopes.DRIVE));
                    credential.setSelectedAccount(googleAccount.getAccount());
                    Drive googleDriveService =
                            new Drive.Builder(
                                    AndroidHttp.newCompatibleTransport(),
                                    new GsonFactory(),
                                    credential)
                                    .setApplicationName("DigiTriad Lab")
                                    .build();

                    // The DriveServiceHelper encapsulates all REST API and SAF functionality.
                    // Its instantiation is required before handling any onClick actions.
                    signInButton.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.green300));
                    driveServiceHelper = new DriveServiceHelper(googleDriveService);
                })
                .addOnFailureListener(exception -> {
                    signInButton.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.red300));
                    Toast.makeText(context, "Remember to sign in with an unc.edu.ar account", Toast.LENGTH_LONG).show();
                });
    }

}
