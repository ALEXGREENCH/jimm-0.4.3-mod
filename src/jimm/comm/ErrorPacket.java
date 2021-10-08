package jimm.comm;

import jimm.JimmException;

public class ErrorPacket extends Packet {

    // Returns the package as byte array
    public byte[] toByteArray() {
        return (null);
    }

    // Parses given byte array and returns a Packet object
    public static Packet parse(byte[] buf, int off, int len) throws JimmException {
        return (null);
    }

    // Parses given byte array and returns a Packet object
    public static Packet parse(byte[] buf) throws JimmException {
        return (ErrorPacket.parse(buf, 0, buf.length));
    }
}
