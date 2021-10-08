package jimm.comm;

import jimm.JimmException;

public abstract class Action {

    // ICQ object
    protected Icq icq;

    // Set ICQ object
    protected void setIcq(Icq icq) {
        this.icq = icq;
    }

    // Returns true if the action can be performed
    public abstract boolean isExecutable();

    // Returns true if this is an exclusive command
    public abstract boolean isExclusive();

    // Init action
    protected abstract void init() throws JimmException;

    // Forwards received packet, returns true if packet was consumed
    protected abstract boolean forward(Packet packet) throws JimmException;

    // Returns true if the action is completed
    public abstract boolean isCompleted();

    // Returns ture if an error has occured
    public abstract boolean isError();

    // Returns a number between 0 and 100 (inclusive) which indicates the progress
    public int getProgress() {
        if (this.isCompleted())
            return (100);
        else
            return (0);
    }


}
