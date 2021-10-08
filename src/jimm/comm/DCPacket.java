// #sijapp cond.if target is "MIDP2" | target is "MOTOROLA" | target is "SIEMENS2"#
// #sijapp cond.if modules_FILES is "true"#

package jimm.comm;

import jimm.JimmException;

public class DCPacket extends Packet {

    // The packet data
    byte[] data;

    // Constructor
    public DCPacket(byte[] _data) {
        data = _data;
    }

    // getDCContent(byte[] buf) => byte[]
    public byte[] getDCContent() {
        return data;
    }

    // Returns the package as byte array
    public byte[] toByteArray() {

        // Allocate memory
        byte buf[] = new byte[data.length + 2];

        // Assemble DC header
        Util.putWord(buf, 0, data.length, false); // length
        System.arraycopy(data, 0, buf, 2, data.length);

        // Return
        return (buf);

    }

    // Parses given byte array and returns a Packet object
    public static Packet parse(byte[] buf, int off, int len) throws JimmException {

        return (new DCPacket(buf));

    }

    // Parses given byte array and returns a Packet object
    public static Packet parse(byte[] buf) throws JimmException {
        return (DCPacket.parse(buf, 0, buf.length));
    }

}
// #sijapp cond.end#
// #sijapp cond.end#