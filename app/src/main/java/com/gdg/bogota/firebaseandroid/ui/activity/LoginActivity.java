package com.gdg.bogota.firebaseandroid.ui.activity;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * @author Santiago Carrillo
 */

public class LoginActivity
    extends AppCompatActivity
    implements FirebaseAuth.AuthStateListener
{

    @BindView( R.id.login_button )
    Button loginButton;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate( @Nullable Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_login );
        ButterKnife.bind( this );
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

    public void onLoginClicked( View view )
    {
        loginButton.setEnabled( false );
        firebaseAuth.signInAnonymously();
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
            loginButton.setEnabled( true );
        }
    }

}
