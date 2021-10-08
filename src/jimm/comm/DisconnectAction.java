package jimm.comm;

import jimm.JimmException;

public class DisconnectAction extends Action {

    // Action states
    public static final int STATE_INIT_DONE = 1;

    // Current action state
    private int state;


    // Returns true if STATE_CONNECTED is active
    public boolean isExecutable() {
        return (this.icq.isConnected());
    }


    // This is an exclusive command, so this returns true
    public boolean isExclusive() {
        return (true);
    }


    // Init action
    protected void init() throws JimmException {
        this.icq.resetServerCon();
        // #sijapp cond.if target is "MIDP2" | target is "MOTOROLA" | target is "SIEMENS2"#
        // #sijapp cond.if modules_FILES is "true"#
        this.icq.resetPeerCon();
        // #sijapp cond.end#
        // #sijapp cond.end#
        this.state = DisconnectAction.STATE_INIT_DONE;
    }


    // Forwards received packet, returns true if packet was consumed
    protected boolean forward(Packet packet) throws JimmException {
        return (false);
    }


    // Returns true if the action is completed
    public boolean isCompleted() {
        return (this.state == DisconnectAction.STATE_INIT_DONE);
    }


    // Returns true if an error has occured
    public boolean isError() {
        return (false);
    }
}
