package com.gdg.bogota.firebaseandroid.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.gdg.bogota.firebaseandroid.R;
import com.gdg.bogota.firebaseandroid.model.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Santiago Carrillo
 */
public class MessagesAdapter
    extends RecyclerView.Adapter<MessagesAdapter.ViewHolder>
{

    private List<Message> messages = new ArrayList<>();


    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType )
    {
        View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.message_row, parent, false );
        return new ViewHolder( view );
    }

    @Override
    public void onBindViewHolder( ViewHolder viewHolder, int position )
    {
        Message message = messages.get( position );
        viewHolder.sender.setText( message.getSender() );
        viewHolder.message.setText( message.getText() );
    }

    @Override
    public int getItemCount()
    {
        return messages.size();
    }

    public void addMessage( Message message )
    {
        messages.add( 0, message );
        notifyDataSetChanged();
    }

    public void removeMessage( Message message )
    {
        messages.remove( message );
        notifyDataSetChanged();
    }

    public static class ViewHolder
        extends RecyclerView.ViewHolder
    {
        @Bind( R.id.sender )
        TextView sender;

        @Bind( R.id.message )
        TextView message;


        public ViewHolder( View view )
        {
            super( view );
            ButterKnife.bind( this, view );
        }
    }
}
