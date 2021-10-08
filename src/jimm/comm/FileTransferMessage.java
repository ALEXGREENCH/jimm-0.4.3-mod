// #sijapp cond.if target is "MIDP2" | target is "MOTOROLA" | target is "SIEMENS2"#
// #sijapp cond.if modules_FILES is "true"#
package jimm.comm;

import jimm.ContactListContactItem;

public class FileTransferMessage extends Message {

    // Filename
    private final String filename;

    // Description
    private final String description;

    // File to transfer
    byte[] file;

    // Constructs an outgoing message
    public FileTransferMessage(String sndrUin, ContactListContactItem _rcvr, int _messageType, String _filename, String _description, byte[] _file) {
        this.sndrUin = sndrUin;
        this.rcvr = _rcvr;
        this.messageType = _messageType;
        this.filename = _filename;
        this.description = _description;
        this.file = _file;
        this.rcvr.setFTM(this);
    }

    // Returns the description
    public String getDescription() {
        return description;
    }

    // Returns the filename
    public String getFilename() {
        return filename;
    }

    // Returns the size of the file
    public int getSize() {
        return file.length;
    }

    // Is another segment available?
    public boolean segmentAvail(int i) {
        return (i <= (file.length / 2048));
    }

    public byte[] getFileSegmentPacket(int segment) {
        byte[] buf;
        if (segment < (file.length / 2048))
            buf = new byte[2049];
        else
            buf = new byte[(file.length % 2048) + 1];
        // System.out.println("buf length: " + buf.length);
        Util.putByte(buf, 0, 0x06);
        // System.out.println("p");
        try {
            System.arraycopy(file, (segment * 2048), buf, 1, buf.length - 1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return (buf);
    }
}

//#sijapp cond.end#
//#sijapp cond.end#
