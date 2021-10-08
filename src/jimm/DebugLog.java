package jimm;

import javax.microedition.lcdui.*;

import DrawControls.TextList;

//#sijapp cond.if modules_DEBUGLOG is "true" #

class Helper implements CommandListener {

    public void commandAction(Command c, Displayable d) {
        Jimm.jimm.getContactListRef().activate();
    }
}

public class DebugLog {

    private static TextList list;

    private static Command backCommand = new Command("Back", Command.BACK, 1);

    static {
        list = new TextList(null);
        list.addCommand(backCommand);
        list.setCommandListener(new Helper());
        list.setFontSize(TextList.SMALL_FONT);
        //#sijapp cond.if target is "MIDP2" | target is "MOTOROLA" | target is "SIEMENS2"#
        list.setTitle("Debug log");
        list.setFullScreenMode(false);
        //#sijapp cond.else#
        list.setCaption("Debug log");
        //#sijapp cond.end#
        list.setCursorMode(TextList.SEL_NONE);

    }

    private static boolean wasShown = false;

    public static void activate() {
        if (!wasShown) {
            JimmUI.setColorScheme(list);
            wasShown = true;
        }

        Jimm.display.setCurrent(list);
    }

    static int counter = 0;

    public static void addText(String text) {
        synchronized (list) {
            list.addBigText("[" + Integer.toString(counter + 1) + "]: ", 0xFF, Font.STYLE_PLAIN, counter);
            list.addBigText(text, 0, Font.STYLE_PLAIN, counter);
            list.doCRLF(counter);
            counter++;
        }
    }

}

//#sijapp cond.else#

public class DebugLog {

    synchronized public static void addText(String text) {
        System.out.println(text);
    }
}

//#sijapp cond.end#
