package jimm.comm;

import java.util.Date;

import jimm.ContactListContactItem;

public class PlainMessage extends Message {

    // Message text
    private final String text;

    // Constructs an incoming message
    public PlainMessage(String sndrUin, String rcvrUin, Date date, String text, boolean offline) {
        this.sndrUin = sndrUin;
        this.rcvrUin = rcvrUin;
        this.date = new Date(date.getTime());
        this.text = text;
        this.offline = offline;
    }

    // Constructs an outgoing message
    public PlainMessage(String sndrUin, ContactListContactItem rcvr, int _messageType, Date date, String text) {
        this.sndrUin = sndrUin;
        this.rcvr = rcvr;
        this.messageType = _messageType;
        this.date = new Date(date.getTime());
        this.text = text;
    }

    // Returns the message text
    public String getText() {
        return this.text;
    }
}
