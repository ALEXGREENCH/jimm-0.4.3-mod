package jimm.comm;


import jimm.JimmException;

public class PingPacket extends Packet {

    // Constructor
    public PingPacket(int sequence) {
        this.sequence = sequence;
    }


    // Constructor
    public PingPacket() {
        this(-1);
    }


    // Returns the package as byte array
    public byte[] toByteArray() {

        // Allocate memory
        byte buf[] = new byte[6];

        // Assemble FLAP header
        Util.putByte(buf, 0, 0x2A);   // FLAP.ID
        Util.putByte(buf, 1, 0x05);   // FLAP.CHANNEL
        Util.putWord(buf, 2, this.sequence);   // FLAP.SEQUENCE
        Util.putWord(buf, 4, 0x0000);   // FLAP.LENGTH

        // Return
        return (buf);

    }


    // Parses given byte array and returns a Packet object
    public static Packet parse(byte[] buf, int off, int len) throws JimmException {

        // Get FLAP sequence number
        int flapSequence = Util.getWord(buf, off + 2);

        // Get length of FLAP data
        int flapLength = Util.getWord(buf, off + 4);

        // Validate length of FLAP data
        if (flapLength != 0) {
            // throw (new JimmException(136, 0));
            // Ignore invalide PING packet
        }

        // Instantiate LoginPacket
        return (new PingPacket(flapSequence));

    }


    // Parses given byte array and returns a Packet object
    public static Packet parse(byte[] buf) throws JimmException {
        return (PingPacket.parse(buf, 0, buf.length));
    }


}
