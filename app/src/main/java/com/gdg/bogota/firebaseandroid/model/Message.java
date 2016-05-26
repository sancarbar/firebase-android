package com.gdg.bogota.firebaseandroid.model;

/**
 * @author Santiago Carrillo
 */
public class Message
{

    String sender;

    String text;

    String imageUrl;

    //Used by json parser
    public Message()
    {
    }

    public Message( String sender, String text )
    {
        this.sender = sender;
        this.text = text;
    }

    public Message( String imageUrl )
    {
        this.imageUrl = imageUrl;
    }

    public String getSender()
    {
        return sender;
    }

    public String getText()
    {
        return text;
    }

    public String getImageUrl()
    {
        return imageUrl;
    }

    @Override
    public boolean equals( Object o )
    {
        if ( this == o )
        {
            return true;
        }
        if ( o == null || getClass() != o.getClass() )
        {
            return false;
        }

        Message message = (Message) o;

        if ( sender != null ? !sender.equals( message.sender ) : message.sender != null )
        {
            return false;
        }
        return text != null ? text.equals( message.text ) : message.text == null;

    }

    @Override
    public int hashCode()
    {
        int result = sender != null ? sender.hashCode() : 0;
        result = 31 * result + ( text != null ? text.hashCode() : 0 );
        return result;
    }
}
