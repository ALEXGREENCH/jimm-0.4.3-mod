//#sijapp cond.if modules_TRAFFIC is "true" #
package jimm;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;

import jimm.util.ResourceBundle;

public class Traffic {


    // Persistent variables

    // Traffic read form file
    private int all_traffic;

    // Traffic for this session
    private int session_traffic;

    // Date of last reset of all_traffic
    private final Date savedSince;

    // Date of the last use of the connection
    private final Date lastTimeUsed;

    // Number of traffic changes since last save
    private byte savedCounter;

    // Amount of money for this session
    private int sessionCost;

    // Amount of money for all
    private int savedCost;

    // Amount of money for the costs per day for this session
    private int costPerDaySum;

    // Indicates if the Traffic Screen ist showing.
    private boolean active;

    // Traffic Screen
    public TrafficScreen trafficScreen;


    // Constructor
    public Traffic() {
        session_traffic = 0;
        sessionCost = 0;
        savedCost = 0;
        lastTimeUsed = new Date(1);
        costPerDaySum = 0;
        savedSince = new Date();
        this.setIsActive(false);
        try {
            this.load();
        } catch (Exception e) {
            this.setSavedSince(new Date().getTime());
            this.setTraffic(0);
            all_traffic = 0;
        }
        // Construct traffic scrren
        this.trafficScreen = new TrafficScreen();
        savedCounter = 0;
    }

    //Loads traffic from file

    public void load() throws IOException, RecordStoreException {

        // Open record store
        RecordStore traffic = RecordStore.openRecordStore("traffic", false);

        // Temporary variables
        byte[] buf;
        ByteArrayInputStream bais;
        DataInputStream dis;

        // Get version info from record store
        buf = traffic.getRecord(1);
        bais = new ByteArrayInputStream(buf);
        dis = new DataInputStream(bais);
        if (!(dis.readUTF().equals(Jimm.VERSION))) throw (new IOException());

        // Get traffic amount and savedSince to record store
        buf = traffic.getRecord(2);
        bais = new ByteArrayInputStream(buf);
        dis = new DataInputStream(bais);
        this.setTraffic(dis.readInt());
        this.setSavedSince(dis.readLong());
        this.setLastTimeUsed(dis.readLong());
        this.setSavedCost(dis.readInt());
        // Close record store
        traffic.closeRecordStore();

    }

    // Saves traffic from file

    public void save() throws IOException, RecordStoreException {

        // Open record store
        RecordStore traffic = RecordStore.openRecordStore("traffic", true);

        // Add empty records if necessary
        while (traffic.getNumRecords() < 4) {
            traffic.addRecord(null, 0, 0);
        }

        // Temporary variables
        byte[] buf;
        ByteArrayOutputStream baos;
        DataOutputStream dos;

        // Add version info to record store
        baos = new ByteArrayOutputStream();
        dos = new DataOutputStream(baos);
        dos.writeUTF(Jimm.VERSION);
        buf = baos.toByteArray();
        traffic.setRecord(1, buf, 0, buf.length);

        // Add traffic amount and savedSince to record store
        baos = new ByteArrayOutputStream();

        dos = new DataOutputStream(baos);
        dos.writeInt(this.getAllTraffic(false));
        dos.writeLong(this.getSavedSince());
        dos.writeLong(this.getLastTimeUsed());
        dos.writeInt(this.generateCostSum(false));
        buf = baos.toByteArray();
        traffic.setRecord(2, buf, 0, buf.length);

        // Close record store
        traffic.closeRecordStore();

    }

    // Generates String for Traffic Info Screen
    protected String generateTrafficString() {
        String traffic;
        Calendar time = Calendar.getInstance();
        time.setTime(this.getSavedSinceDate());
        traffic = ResourceBundle.getString("session") + ":\n" +
                this.getSessionTraffic(false) + " " + ResourceBundle.getString("byte") + "\n" +
                this.getSessionTraffic(true) + " " + ResourceBundle.getString("kb") + "\n" +
                this.getString(this.generateCostSum(true)) + " " + Jimm.jimm.getOptionsRef().getStringOption(Options.OPTION_CURRENCY) + "\n" +
                ResourceBundle.getString("since") + ": " + this.makeTwo(time.get(Calendar.DAY_OF_MONTH)) + "." +
                this.makeTwo(time.get(Calendar.MONTH) + 1) + "." +
                time.get(Calendar.YEAR) + " " +
                this.makeTwo(time.get(Calendar.HOUR_OF_DAY)) + ":" +
                this.makeTwo(time.get(Calendar.MINUTE)) + "\n" +
                this.getAllTraffic(false) + " " + ResourceBundle.getString("byte") + "\n" +
                this.getAllTraffic(true) + " " + ResourceBundle.getString("kb") + "\n" +
                this.getString(this.generateCostSum(false)) + " " + Jimm.jimm.getOptionsRef().getStringOption(Options.OPTION_CURRENCY);
        return (traffic);

    }


    // Determins whenever we were already connected today or not
    private boolean usedToday() {
//		Date now = new Date();
        Calendar time_now = Calendar.getInstance();
        Calendar time_lastused = Calendar.getInstance();
        time_now.setTime(new Date());
        time_lastused.setTime(this.getLastTimeUsedDate());
        return (time_now.get(Calendar.DAY_OF_MONTH) == time_lastused.get(Calendar.DAY_OF_MONTH)) &&
                (time_now.get(Calendar.MONTH) == time_lastused.get(Calendar.MONTH)) &&
                (time_now.get(Calendar.YEAR) == time_lastused.get(Calendar.YEAR));
    }

    // Generates int of money amount spent on connection
    protected int generateCostSum(boolean thisSession) {

        int cost;
        int costPerPacket = Jimm.jimm.getOptionsRef().getIntOption(Options.OPTION_COST_PER_PACKET);
//		int costPerDay = Jimm.jimm.getOptionsRef().getCostPerDay();
        int costPacketLength = Jimm.jimm.getOptionsRef().getIntOption(Options.OPTION_COST_PACKET_LENGTH);

        if (thisSession) {
            if (session_traffic != 0) {
                cost = ((session_traffic / costPacketLength) + 1) * costPerPacket;
            } else {
                cost = 0;
            }
        } else {
            if (this.session_traffic != 0) {
                cost = ((session_traffic / costPacketLength) + 1) * costPerPacket + getSavedCost();
            } else {
                cost = getSavedCost();
            }
        }
        if ((!usedToday()) && (this.getSessionTraffic(false) != 0) && (costPerDaySum == 0)) {
            this.increaseCostPerDaySum();
        }
        return (cost + costPerDaySum);
    }

    //Date print utility
    protected String makeTwo(int number) {
        if (number < 10)
            return ("0" + number);
        else
            return (String.valueOf(number));
    }

    // Returns String value of cost value
    public String getString(int value) {
        String costString;
        String afterDot;
        try {
            if (value != 0) {
                costString = value / 100000 + ".";
                afterDot = Integer.toString(value % 100000);
                while (afterDot.length() != 5)
                    afterDot = "0" + afterDot;
                while ((afterDot.endsWith("0")) && (afterDot.length() > 2)) {
                    afterDot = afterDot.substring(0, afterDot.length() - 1);
                }
                costString = costString + afterDot;
                return costString;
            } else return "0.0";

        } catch (Exception e) {
            return "0.0";
        }
    }

    //Returns value of session traffic
    protected int getSessionTraffic(boolean kb) {
        if (kb) return (session_traffic / 1024);
        return (session_traffic);
    }

    //Returns value of all traffic
    public int getAllTraffic(boolean kb) {
        if (kb) return ((all_traffic + session_traffic) / 1024);
        return (all_traffic + session_traffic);
    }

    // Set value of session traffic
    public void setTraffic(int bytes) {
        all_traffic = bytes;
    }

    // Adds to session traffic
    public void addTraffic(int bytes) {
        session_traffic = session_traffic + bytes;
        if (savedCounter == 10) {
            savedCounter = 0;
            try {
                this.save();
            } catch (Exception e) { // Do nothing
            }
        }
        savedCounter++;
    }

    // Reset the saved value
    public void reset() {
        this.setTraffic(0);
        this.setSavedCost(0);
        this.setSavedSince(new Date().getTime());
        try {
            this.save();
        } catch (Exception e) { // Do nothing
        }
    }

    // Sets the value of savedSince
    public void setSavedSince(long time) {
        savedSince.setTime(time);
    }

    // Get the value of saved Since
    public long getSavedSince() {
        return (savedSince.getTime());
    }

    // Get the value of saved Since
    public Date getSavedSinceDate() {
        return (savedSince);
    }

    // Sets the value of lastTimeUsed
    public void setLastTimeUsed(long time) {
        lastTimeUsed.setTime(time);
    }

    // Get the value of lastTimeUsed
    public long getLastTimeUsed() {
        return (lastTimeUsed.getTime());
    }

    // Get the value of lastTimeUsed
    public Date getLastTimeUsedDate() {
        return (lastTimeUsed);
    }

    // Get all cost
    public int getSavedCost() {
        return (savedCost);
    }

    // Set all cost
    public void setSavedCost(int cost) {
        savedCost = cost;
    }

    // Get session cost
    public int getSessionCost() {
        return (sessionCost + costPerDaySum);
    }

    // Set all cost
    public void setSessionCost(int cost) {
        sessionCost = cost;
    }

    // Increases costPerDaySum at one unit
    public void increaseCostPerDaySum() {
        costPerDaySum = costPerDaySum + Jimm.jimm.getOptionsRef().getIntOption(Options.OPTION_COST_PER_DAY);
        setLastTimeUsed(new Date().getTime());
    }

    // Is the traffic screen active?
    public boolean isActive() {
        return (active);
    }

    // Sets the value if the Traffic Screen is active
    public void setIsActive(boolean _active) {
        active = _active;
    }

    // Screen for Traffic information
    public class TrafficScreen implements CommandListener {


        // Form elements

        private final Form trafficScreen;
        private Command resetCommand;
        private Command okCommand;

        //Number of kB defines the threshold when the screen should be update
        private final byte updateThreshold;
        //Traffic value to compare to in kB
        private byte compareTraffic;


        // Constructor
        public TrafficScreen() {

            updateThreshold = 1;
            compareTraffic = (byte) Traffic.this.getSessionTraffic(true);

            // Initialize command
            // #sijapp cond.if target is "MOTOROLA" # 
            this.resetCommand = new Command(ResourceBundle.getString("reset"), Command.BACK, 2);
            this.okCommand = new Command(ResourceBundle.getString("ok"), Command.OK, 1);
            // #sijapp cond.else #
            this.resetCommand = new Command(ResourceBundle.getString("reset"), Command.SCREEN, 2);
            this.okCommand = new Command(ResourceBundle.getString("ok"), Command.SCREEN, 1);
            // #sijapp cond.end #

            // Initialize traffic screen

            this.trafficScreen = new Form(ResourceBundle.getString("traffic"));
            this.trafficScreen.addCommand(this.resetCommand);
            this.trafficScreen.addCommand(this.okCommand);
            this.trafficScreen.setCommandListener(this);


        }

        // Activate options form
        public void activate() {
            this.update(true);
            Jimm.display.setCurrent(this.trafficScreen);
        }

        public void update(boolean doIt) {
            Jimm.jimm.getContactListRef().updateTitle(Traffic.this.getSessionTraffic(true));
            if (((Traffic.this.getSessionTraffic(true) - compareTraffic) >= updateThreshold) | doIt) {
                this.trafficScreen.append(Traffic.this.generateTrafficString());
                if (this.trafficScreen.size() > 1) {
                    this.trafficScreen.delete(0);
                }
                compareTraffic = (byte) Traffic.this.getSessionTraffic(true);
            }
        }

        // Command listener
        public void commandAction(Command c, Displayable d) {

            // Look for save command
            if (c == this.resetCommand) {
                Traffic.this.reset();
            }
            if (c == this.okCommand) {
                Traffic.this.setIsActive(false);
                if (Jimm.jimm.getIcqRef().isConnected()) {
                    Jimm.jimm.getContactListRef().activate();
                } else {
                    Jimm.jimm.getMainMenuRef().activate();
                }
            }
        }
    }
}
//#sijapp cond.end#
