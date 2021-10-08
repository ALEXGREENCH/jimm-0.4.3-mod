package jimm.comm;


import jimm.JimmException;


public class RemoveMeAction extends Action {

    private String uin;

    // Constructor
    public RemoveMeAction(String uin) {
        this.uin = new String(uin);
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

        byte[] buf;

        //	Get byte Arrys from the stuff we need the length of
        byte[] uinRaw = Util.stringToByteArray(this.uin);

        // Calculate length of use date in SNAC packet loger if denyed because of the reason
        buf = new byte[1 + uinRaw.length];

        // Assemble the packet
        int marker = 0;
        Util.putByte(buf, marker, uinRaw.length);
        System.arraycopy(uinRaw, 0, buf, marker + 1, uinRaw.length);

        // Send a CLI_AUTHORIZE packet
        SnacPacket packet = new SnacPacket(SnacPacket.CLI_REMOVEME_FAMILY, SnacPacket.CLI_REMOVEME_COMMAND, 0x00000003, new byte[0], buf);
        this.icq.c.sendPacket(packet);
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
