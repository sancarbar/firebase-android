package com.gdg.bogota.firebaseandroid.model;

/**
 * @author Santiago Carrillo
 */
public class Message
{

    private final String sender;

    private final String text;

    public Message( String sender, String text )
    {
        this.sender = sender;
        this.text = text;
    }


    public String getSender()
    {
        return sender;
    }

    public String getText()
    {
        return text;
    }
}
