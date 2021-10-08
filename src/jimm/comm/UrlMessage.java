package jimm.comm;

import java.util.Date;

import jimm.ContactListContactItem;

public class UrlMessage extends Message {

    // URL
    private final String url;

    // Message text
    private final String text;

    // Constructs an incoming message
    public UrlMessage(String sndrUin, String rcvrUin, Date date, String url, String text) {
        this.sndrUin = sndrUin;
        this.rcvrUin = rcvrUin;
        this.date = new Date(date.getTime());
        this.url = url;
        this.text = text;
    }

    // Constructs an outgoing message
    public UrlMessage(String sndrUin, ContactListContactItem rcvr, int _messageType, Date date, String url, String text) {
        this.sndrUin = sndrUin;
        this.rcvr = rcvr;
        this.messageType = _messageType;
        this.date = new Date(date.getTime());
        this.url = url;
        this.text = text;
    }

    // Returns the URL
    public String getUrl() {
        return this.url;
    }

    // Returns the message text
    public String getText() {
        return this.text;
    }
}
