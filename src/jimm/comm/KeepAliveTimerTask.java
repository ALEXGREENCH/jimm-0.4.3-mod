package jimm.comm;

import jimm.Jimm;
import jimm.JimmException;
import jimm.Options;

import java.util.TimerTask;

public class KeepAliveTimerTask extends TimerTask {

    // ICQ object
    protected Icq icq;

    // Set ICQ object
    protected void setIcq(Icq icq) {
        this.icq = icq;
    }

    // Send an alive packet
    public void run() {

        // If STATE_CONNECTED is active, we've already got an reference to the ICQ object and the corresponding option has been set, send an alive packet
        if ((this.icq != null) && this.icq.isConnected() && Jimm.jimm.getOptionsRef().getBooleanOption(Options.OPTION_KEEP_CONN_ALIVE)) {
            // Instantiate and send an alive packet
            try {
                this.icq.c.sendPacket(new PingPacket());
            } catch (JimmException e) {
                JimmException.handleException(e);
                if (e.isCritical()) this.cancel();
            }
        }
    }
}
