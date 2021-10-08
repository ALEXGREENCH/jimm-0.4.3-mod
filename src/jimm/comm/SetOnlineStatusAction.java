package jimm.comm;


import jimm.Jimm;
import jimm.JimmException;
import jimm.Options;


public class SetOnlineStatusAction extends Action {


    // CLI_SETSTATUS packet data
    public static final byte[] CLI_SETSTATUS_DATA = ConnectAction.CLI_SETSTATUS_DATA;


    /****************************************************************************/


    // Requested online status
    private long onlineStatus;


    // Constructor
    public SetOnlineStatusAction(long onlineStatus) {
        this.onlineStatus = onlineStatus;
    }


    // Returns true if the action can be performed
    public boolean isExecutable() {
        return (this.icq.isConnected());
    }


    // Returns true if this is an exclusive command
    public boolean isExclusive() {
        return (false);
    }


    // Init action
    protected void init() throws JimmException {

        // Convert online status
        int onlineStatus = Util.translateStatusSend(this.onlineStatus);

        // Send a CLI_SETSTATUS packet
        Util.putWord(SetOnlineStatusAction.CLI_SETSTATUS_DATA, 4, 0x0000);
        Util.putWord(SetOnlineStatusAction.CLI_SETSTATUS_DATA, 6, onlineStatus);
        SnacPacket packet = new SnacPacket(SnacPacket.CLI_SETSTATUS_FAMILY,
                SnacPacket.CLI_SETSTATUS_COMMAND,
                0x00000000,
                new byte[0],
                SetOnlineStatusAction.CLI_SETSTATUS_DATA);
        this.icq.c.sendPacket(packet);

        // Save new online status
        Jimm.jimm.getOptionsRef().setLongOption(Options.OPTION_ONLINE_STATUS, this.onlineStatus);
        try {
            Jimm.jimm.getOptionsRef().save();
        } catch (Exception e) {
            JimmException.handleException(new JimmException(172, 0, true));
        }

    }


    // Forwards received packet, returns true if packet was consumed
    protected boolean forward(Packet packet) throws JimmException {
        return (false);
    }


    // Returns true if the action is completed
    public boolean isCompleted() {
        return (true);
    }


    // Returns true if an error has occured
    public boolean isError() {
        return (false);
    }


}
