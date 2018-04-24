package com.gdg.bogota.firebaseandroid.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.gdg.bogota.firebaseandroid.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

/**
 * @author Santiago Carrillo
 */

public class LoginActivity
    extends AppCompatActivity
    implements FirebaseAuth.AuthStateListener
{

    @BindView( R.id.sign_in_button )
    SignInButton signInButton;

    private ProgressDialog progressDialog;

    private static final int RC_SIGN_IN = 9001;

    private static final String TAG = LoginActivity.class.getSimpleName();

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    private GoogleSignInClient googleSignInClient;

    public void showProgressDialog()
    {
        if ( progressDialog == null )
        {
            progressDialog = new ProgressDialog( this );
            progressDialog.setMessage( getString( R.string.loading ) );
            progressDialog.setIndeterminate( true );
        }

        progressDialog.show();
    }

    public void hideProgressDialog()
    {
        if ( progressDialog != null && progressDialog.isShowing() )
        {
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onCreate( @Nullable Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_login );
        ButterKnife.bind( this );
        initGoogleSignInClient();
        configureSignInButton();

    }

    private void configureSignInButton()
    {
        signInButton.setSize( SignInButton.SIZE_WIDE );
        signInButton.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick( View v )
            {
                signInButton.setEnabled( false );
                startGoogleLogin();
            }
        } );
    }


    private void initGoogleSignInClient()
    {
        GoogleSignInOptions googleSignInOptions =
            new GoogleSignInOptions.Builder( GoogleSignInOptions.DEFAULT_SIGN_IN )
                .requestIdToken(getString( R.string.web_client_id ) )
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient( this, googleSignInOptions );
    }


    @Override
    public void onStart()
    {
        super.onStart();
        firebaseAuth.addAuthStateListener( this );
        verifyCurrentUserAndStartMainActivityIfLoggedIn();
    }

    @Override
    public void onStop()
    {
        super.onStop();
        firebaseAuth.removeAuthStateListener( this );
    }

    @Override
    public void onAuthStateChanged( @NonNull FirebaseAuth firebaseAuth )
    {
        verifyCurrentUserAndStartMainActivityIfLoggedIn();
    }

    private void verifyCurrentUserAndStartMainActivityIfLoggedIn()
    {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if ( user != null )
        {
            startActivity( new Intent( this, MainActivity.class ) );
            finish();
        }
        else
        {
            signInButton.setEnabled( true );
        }
    }

    @Override
    protected void onActivityResult( int requestCode, int resultCode, Intent data )
    {
        if ( resultCode == RESULT_OK && requestCode == RC_SIGN_IN )
        {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent( data );
            try
            {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult( ApiException.class );
                firebaseAuthWithGoogle( account );
            }
            catch ( ApiException e )
            {
                reEnablesignInButton();
            }
        }
        else
        {
            super.onActivityResult( requestCode, resultCode, data );
        }
    }

    private void firebaseAuthWithGoogle( GoogleSignInAccount acct )
    {
        showProgressDialog();
        AuthCredential credential = GoogleAuthProvider.getCredential( acct.getIdToken(), null );
        firebaseAuth.signInWithCredential( credential ).addOnCompleteListener( this,
                                                                               new OnCompleteListener<AuthResult>()
                                                                               {
                                                                                   @Override
                                                                                   public void onComplete(
                                                                                       @NonNull Task<AuthResult> task )
                                                                                   {
                                                                                       if ( task.isSuccessful() )
                                                                                       {
                                                                                           verifyCurrentUserAndStartMainActivityIfLoggedIn();
                                                                                       }
                                                                                       else
                                                                                       {
                                                                                           reEnablesignInButton();
                                                                                       }
                                                                                       hideProgressDialog();
                                                                                   }
                                                                               } );
    }

    private void reEnablesignInButton()
    {
        runOnUiThread( new Runnable()
        {
            @Override
            public void run()
            {
                signInButton.setEnabled( false );
            }
        } );
    }

    private void startGoogleLogin()
    {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult( signInIntent, RC_SIGN_IN );
    }
}
