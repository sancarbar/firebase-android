package com.gdg.bogota.firebaseandroid.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import butterknife.BindView;
import com.gdg.bogota.firebaseandroid.R;
import com.gdg.bogota.firebaseandroid.model.Message;
import com.gdg.bogota.firebaseandroid.ui.adapter.MessagesAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity
    extends AppCompatActivity
{

    static final int REQUEST_IMAGE_CAPTURE = 1;

    FirebaseDatabase database = FirebaseDatabase.getInstance();

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    DatabaseReference databaseReference = database.getReference( "messages" );

    FirebaseStorage storage = FirebaseStorage.getInstance();

    StorageReference storageRef = storage.getReferenceFromUrl( "gs://funchat-ef3ed.appspot.com" );

    @BindView( R.id.messages_layout )
    View messagesLayout;

    @BindView( R.id.message )
    EditText message;

    @BindView( R.id.sender )
    EditText sender;

    @BindView( R.id.recycler_view )
    RecyclerView recyclerView;

    private MessagesAdapter messagesAdapter;

    private final ChildEventListener messagesListener = new ChildEventListener()
    {

        @Override
        public void onChildAdded( DataSnapshot dataSnapshot, String s )
        {
            updateMessage( dataSnapshot );
        }

        @Override
        public void onChildChanged( DataSnapshot dataSnapshot, String s )
        {
            updateMessage( dataSnapshot );
        }

        @Override
        public void onChildRemoved( DataSnapshot dataSnapshot )
        {
            Message message = dataSnapshot.getValue( Message.class );
            if ( message != null )
            {
                messagesAdapter.removeMessage( message );
            }
        }

        @Override
        public void onChildMoved( DataSnapshot dataSnapshot, String s )
        {
        }

        @Override
        public void onCancelled( DatabaseError databaseError )
        {
        }
    };

    private void updateMessage( DataSnapshot dataSnapshot )
    {
        final Message message = dataSnapshot.getValue( Message.class );
        if ( message != null )
        {
            runOnUiThread( new Runnable()
            {
                @Override
                public void run()
                {
                    messagesAdapter.addMessage( message );
                }
            } );
        }
    }

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        ButterKnife.bind( this );
        Toolbar toolbar = (Toolbar) findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );
        messagesAdapter = new MessagesAdapter( this );
        configureRecyclerView();
        databaseReference.addChildEventListener( messagesListener );
    }

    private void configureRecyclerView()
    {
        recyclerView.setHasFixedSize( true );
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( this );
        linearLayoutManager.setReverseLayout( true );
        recyclerView.setLayoutManager( linearLayoutManager );
        recyclerView.setAdapter( messagesAdapter );
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
        if ( id == R.id.action_logout )
        {
            firebaseAuth.signOut();
            startActivity( new Intent( this, LoginActivity.class ) );
            finish();
            return true;
        }

        return super.onOptionsItemSelected( item );
    }

    public void onSendClicked( View view )
    {
        String text = message.getText().toString();
        message.setText( null );
        String messageSender = sender.getText().toString();
        Message message = new Message( messageSender, text );
        databaseReference.push().setValue( message );
    }

    @OnClick( R.id.add_picture )
    public void onAddImageClicked()
    {
        dispatchTakePictureIntent();
    }

    private void dispatchTakePictureIntent()
    {
        Intent takePictureIntent = new Intent( MediaStore.ACTION_IMAGE_CAPTURE );
        if ( takePictureIntent.resolveActivity( getPackageManager() ) != null )
        {
            startActivityForResult( takePictureIntent, REQUEST_IMAGE_CAPTURE );
        }
    }

    @Override
    protected void onActivityResult( int requestCode, int resultCode, Intent data )
    {
        if ( requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK )
        {

            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get( "data" );
            UploadPostTask uploadPostTask = new UploadPostTask();
            uploadPostTask.execute( imageBitmap );
        }
    }

    @SuppressWarnings( "VisibleForTests" )
    private class UploadPostTask
        extends AsyncTask<Bitmap, Void, Void>
    {

        @Override
        protected Void doInBackground( Bitmap... params )
        {
            Bitmap bitmap = params[0];
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress( Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream );
            storageRef.child( UUID.randomUUID().toString() + "jpg" ).putBytes(
                byteArrayOutputStream.toByteArray() ).addOnSuccessListener(
                new OnSuccessListener<UploadTask.TaskSnapshot>()
                {
                    @Override
                    public void onSuccess( UploadTask.TaskSnapshot taskSnapshot )
                    {
                        if ( taskSnapshot.getDownloadUrl() != null )
                        {
                            String imageUrl = taskSnapshot.getDownloadUrl().toString();
                            final Message message = new Message( imageUrl );
                            databaseReference.push().setValue( message );
                        }
                    }
                } );

            return null;
        }
    }


}
