package com.gdg.bogota.firebaseandroid.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.gdg.bogota.firebaseandroid.R;
import com.gdg.bogota.firebaseandroid.model.Message;
import com.gdg.bogota.firebaseandroid.ui.adapter.MessagesAdapter;

import java.util.List;

/**
 * @author Santiago Carrillo
 * 5/2/18.
 */
public class MessagesListFragment
    extends Fragment
{


    @BindView( R.id.recycler_view )
    RecyclerView recyclerView;

    private MessagesAdapter messagesAdapter;


    @Nullable
    @Override
    public View onCreateView( @NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                              @Nullable Bundle savedInstanceState )
    {
        View view = inflater.inflate( R.layout.fragment_messages_list, container, false );
        ButterKnife.bind( this, view );
        return view;
    }

    @Override
    public void onViewCreated( @NonNull View view, @Nullable Bundle savedInstanceState )
    {
        super.onViewCreated( view, savedInstanceState );
        messagesAdapter = new MessagesAdapter();
        configureRecyclerView();
    }

    public void removeMessage( Message message )
    {
        messagesAdapter.removeMessage( message );
    }

    private void configureRecyclerView()
    {
        recyclerView.setHasFixedSize( true );
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( getActivity() );
        linearLayoutManager.setReverseLayout( true );
        recyclerView.setLayoutManager( linearLayoutManager );
        recyclerView.setAdapter( messagesAdapter );
    }

    public void addMessage( Message message )
    {
        messagesAdapter.addMessage( message );
    }
}
