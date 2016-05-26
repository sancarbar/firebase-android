package com.gdg.bogota.firebaseandroid.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.gdg.bogota.firebaseandroid.R;
import com.gdg.bogota.firebaseandroid.model.Message;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Santiago Carrillo
 */
public class MessagesAdapter
    extends RecyclerView.Adapter<MessagesAdapter.ViewHolder>
{

    private final Context context;

    private List<Message> messages = new ArrayList<>();

    public MessagesAdapter( Context context )
    {
        this.context = context;
    }

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
        if ( message.getImageUrl() != null )
        {
            viewHolder.imageView.setVisibility( View.VISIBLE );
            Picasso.with( context ).load( message.getImageUrl() ).into( viewHolder.imageView );
        }
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

        @Bind( R.id.image )
        ImageView imageView;

        public ViewHolder( View view )
        {
            super( view );
            ButterKnife.bind( this, view );
        }
    }
}
