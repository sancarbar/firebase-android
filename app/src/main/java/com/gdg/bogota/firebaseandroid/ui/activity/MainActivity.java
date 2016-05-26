package com.gdg.bogota.firebaseandroid.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.gdg.bogota.firebaseandroid.R;
import com.gdg.bogota.firebaseandroid.model.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity
    extends AppCompatActivity
    implements FirebaseAuth.AuthStateListener
{

    FirebaseDatabase database = FirebaseDatabase.getInstance();

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();


    @Bind( R.id.welcome_message )
    TextView welcomeMessage;

    @Bind( R.id.login_button )
    View loginButton;

    @Bind( R.id.logout_button )
    View logoutButton;

    @Bind( R.id.messages_layout )
    View messagesLayout;

    @Bind( R.id.message )
    EditText message;

    @Bind( R.id.sender )
    EditText sender;

    DatabaseReference databaseReference = database.getReference( "message" );

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        ButterKnife.bind( this );
        Toolbar toolbar = (Toolbar) findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );
    }


    @Override
    public boolean onCreateOptionsMenu( Menu menu )
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate( R.menu.menu_main, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item )
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if ( id == R.id.action_settings )
        {
            return true;
        }

        return super.onOptionsItemSelected( item );
    }


    @Override
    public void onStart()
    {
        super.onStart();
        firebaseAuth.addAuthStateListener( this );
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
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if ( user != null )
        {
            welcomeMessage.setVisibility( View.VISIBLE );
            logoutButton.setVisibility( View.VISIBLE );
            logoutButton.setEnabled( true );
            loginButton.setVisibility( View.GONE );
            messagesLayout.setVisibility( View.VISIBLE );
        }
        else
        {
            welcomeMessage.setVisibility( View.GONE );
            logoutButton.setVisibility( View.GONE );
            loginButton.setVisibility( View.VISIBLE );
            loginButton.setEnabled( true );
            messagesLayout.setVisibility( View.GONE );
        }
    }

    public void onLoginClicked( View view )
    {
        firebaseAuth.signInAnonymously();
        loginButton.setEnabled( false );
    }

    public void onLogoutClicked( View view )
    {
        firebaseAuth.signOut();
        loginButton.setEnabled( false );
    }


    public void onSendClicked( View view )
    {
        String text = message.getText().toString();
        message.setText( null );
        String messageSender = sender.getText().toString();
        Message message = new Message( messageSender, text );
        databaseReference.setValue( message );
    }
}
