package jimm;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.List;
import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.TextBox;
import javax.microedition.lcdui.TextField;
import javax.microedition.midlet.MIDletStateChangeException;

import jimm.comm.SearchAction;
import jimm.comm.SetOnlineStatusAction;
import jimm.comm.UpdateContactListAction;
import jimm.util.ResourceBundle;
//#sijapp cond.if target is "MOTOROLA"#
import DrawControls.LightControl;
//#sijapp cond.end#

public class MainMenu implements CommandListener {
    private static final int MSGBS_EXIT = 1;

    // Static constants for menu actios
    private static final int MENU_CONNECT = 1;
    private static final int MENU_DISCONNECT = 2;
    private static final int MENU_LIST = 3;
    private static final int MENU_OPTIONS = 4;
    // #sijapp cond.if modules_TRAFFIC is "true" #
    private static final int MENU_TRAFFIC = 5;
    // #sijapp cond.end #
    private static final int MENU_KEYLOCK = 6;
    private static final int MENU_STATUS = 7;
    private static final int MENU_SEARCH = 8;
    private static final int MENU_ADD_USER = 9;
    private static final int MENU_ADD_GROUP = 10;
    private static final int MENU_DEL_GROUP = 11;
    private static final int MENU_ABOUT = 12;
    // #sijapp cond.if target is "MIDP2" # 
    private static final int MENU_MINIMIZE = 13;
    // #sijapp cond.end #
    // Exit has to be biggest element cause it also marks the size
    private static final int MENU_EXIT = 14;


    // Abort command
    private static final Command backCommand = new Command(ResourceBundle.getString("back"), Command.BACK, 1);

    // Send command
    private static final Command sendCommand = new Command(ResourceBundle.getString("send"), Command.OK, 1);

    // Select command
    private static final Command selectCommand = new Command(ResourceBundle.getString("select"), Command.OK, 1);

    // #sijapp cond.if target is "MOTOROLA" #
    private static final Command exitCommand = new Command(ResourceBundle.getString("exit_button"), Command.EXIT, 1);
    //#sijapp cond.end# 

    // List for selecting a online status
    private static List statusList;

    // Initializer
    static {
        MainMenu.statusList = new List(ResourceBundle.getString("set_status"), List.IMPLICIT);
        MainMenu.statusList.append(ResourceBundle.getString("status_online"), ContactList.statusOnlineImg);
        MainMenu.statusList.append(ResourceBundle.getString("status_chat"), ContactList.statusChatImg);
        MainMenu.statusList.append(ResourceBundle.getString("status_away"), ContactList.statusAwayImg);
        MainMenu.statusList.append(ResourceBundle.getString("status_na"), ContactList.statusNaImg);
        MainMenu.statusList.append(ResourceBundle.getString("status_occupied"), ContactList.statusOccupiedImg);
        MainMenu.statusList.append(ResourceBundle.getString("status_dnd"), ContactList.statusDndImg);
        MainMenu.statusList.append(ResourceBundle.getString("status_invisible"), ContactList.statusInvisibleImg);
    }

    /**
     * ************************************************************************
     */

    // Visual list
    private List list;

    // Menu event list
    private int[] eventList;

    // Groups list
    private List groupList;

    // Form for the adding users dialog
    public Form addUserOrGroup;

    // Flag if we we are adding user or group
    private boolean addUserFlag;

    // Text box for adding users to the contact list
    private TextField uinTextField;
    private TextField nameTextField;

    // Textbox for  Status messages
    private TextBox statusMessage;

    // Connected
    private boolean isConnected;

    private Image getStatusImage() {
        long cursStatus = Jimm.jimm.getOptionsRef().getLongOption(Options.OPTION_ONLINE_STATUS);
        int imageIndex = ContactListContactItem.getStatusImageIndex(cursStatus);
        return ContactList.getImageList().elementAt(imageIndex);
    }

    // Builds the main menu (visual list)
    private void build() {
        if ((Jimm.jimm.getIcqRef().isConnected() != isConnected) || (this.list == null)) {
            this.eventList = new int[MENU_EXIT];
            this.list = new List(ResourceBundle.getString("menu"), List.IMPLICIT);

            if (Jimm.jimm.getIcqRef().isNotConnected()) {
                this.eventList[this.list.append(ResourceBundle.getString("connect"), ContactList.menuIcons.elementAt(17))] = MENU_CONNECT;
                this.eventList[this.list.append(ResourceBundle.getString("contact_list"), ContactList.menuIcons.elementAt(1))] = MENU_LIST;
                this.eventList[this.list.append(ResourceBundle.getString("options"), ContactList.menuIcons.elementAt(3))] = MENU_OPTIONS;

                // #sijapp cond.if target is "MOTOROLA" #
                this.list.addCommand(MainMenu.selectCommand);
                this.list.addCommand(MainMenu.exitCommand);
                // #sijapp cond.end#
            } else {
                this.eventList[this.list.append(ResourceBundle.getString("keylock_enable"), ContactList.menuIcons.elementAt(0))] = MENU_KEYLOCK;
                this.eventList[this.list.append(ResourceBundle.getString("disconnect"), ContactList.menuIcons.elementAt(18))] = MENU_DISCONNECT;
                this.eventList[this.list.append(ResourceBundle.getString("set_status"), getStatusImage())] = MENU_STATUS;
                this.eventList[this.list.append(ResourceBundle.getString("add_user"), ContactList.menuIcons.elementAt(5))] = MENU_ADD_USER;
                this.eventList[this.list.append(ResourceBundle.getString("search_user"), ContactList.menuIcons.elementAt(2))] = MENU_SEARCH;
                this.eventList[this.list.append(ResourceBundle.getString("add_group"), ContactList.menuIcons.elementAt(29))] = MENU_ADD_GROUP;
                this.eventList[this.list.append(ResourceBundle.getString("del_group"), ContactList.menuIcons.elementAt(30))] = MENU_DEL_GROUP;
                this.eventList[this.list.append(ResourceBundle.getString("options"), ContactList.menuIcons.elementAt(3))] = MENU_OPTIONS;
                this.list.addCommand(MainMenu.backCommand);
                // #sijapp cond.if target is "MOTOROLA" #
                this.list.addCommand(MainMenu.selectCommand);
                // #sijapp cond.end#
            }
            this.list.setCommandListener(this);

            // #sijapp cond.if modules_TRAFFIC is "true" #
            this.eventList[this.list.append(ResourceBundle.getString("traffic"), ContactList.menuIcons.elementAt(24))] = MENU_TRAFFIC;
            // #sijapp cond.end#
            this.eventList[this.list.append(ResourceBundle.getString("about"), ContactList.menuIcons.elementAt(4))] = MENU_ABOUT;
            // #sijapp cond.if target is "MIDP2" #
            this.eventList[this.list.append(ResourceBundle.getString("minimize"), ContactList.menuIcons.elementAt(27))] = MENU_MINIMIZE;
            // #sijapp cond.end#
            this.eventList[this.list.append(ResourceBundle.getString("exit"), ContactList.menuIcons.elementAt(28))] = MENU_EXIT;

            this.isConnected = Jimm.jimm.getIcqRef().isConnected();
        } else {
            if (!Jimm.jimm.getIcqRef().isNotConnected())
                this.list.set(2, ResourceBundle.getString("set_status"), getStatusImage());
        }
        this.list.setSelectedIndex(0, true);
    }

    // Displays the given alert and activates the main menu afterwards
    public void activate(Alert alert) {
        this.build();
        Jimm.display.setCurrent(alert, this.list);
        //#sijapp cond.if target is "MOTOROLA"#
        LightControl.flash(true);
        //#sijapp cond.end#
    }

    // Activates the main menu
    public void activate() {
        this.build();
        Jimm.display.setCurrent(this.list);
        //#sijapp cond.if target is "MOTOROLA"#
        LightControl.flash(true);
        //#sijapp cond.end#
    }

    // Show form for adding user
    public void addUserOrGroupCmd(String uin, boolean userFlag) {
        addUserFlag = userFlag;
        // Reset and display textbox for entering uin or group name to add
        if (addUserFlag) {
            addUserOrGroup = new Form(ResourceBundle.getString("add_user"));
            uinTextField = new TextField(ResourceBundle.getString("uin"), uin, 16, TextField.NUMERIC);
            nameTextField = new TextField(ResourceBundle.getString("name"), "", 32, TextField.ANY);
            if (uin == null)
                addUserOrGroup.append(uinTextField);
            else {
                StringItem si = new StringItem(ResourceBundle.getString("uin"), uin);
                addUserOrGroup.append(si);
            }
            addUserOrGroup.append(nameTextField);
        } else {
            addUserOrGroup = new Form(ResourceBundle.getString("add_group"));
            uinTextField = new TextField(ResourceBundle.getString("group_name"), uin, 16, TextField.ANY);
            addUserOrGroup.append(uinTextField);
        }
        addUserOrGroup.addCommand(sendCommand);
        addUserOrGroup.addCommand(backCommand);
        addUserOrGroup.setCommandListener(Jimm.jimm.getMainMenuRef());
        Jimm.display.setCurrent(addUserOrGroup);
    }

    private void doExit() {
        // Disconnect
        Jimm.jimm.getIcqRef().disconnect();

        // Save traffic
        //#sijapp cond.if modules_TRAFFIC is "true" #
        try {
            Jimm.jimm.getTrafficRef().save();
        } catch (Exception e) { // Do nothing
        }
        //#sijapp cond.end#


        // Exit app
        try {
            Jimm.jimm.destroyApp(true);
        } catch (MIDletStateChangeException e) { // Do nothing
        }
    }


    private void menuExit() {
        if (Jimm.jimm.getContactListRef().getUnreadMessCount() > 0) {
            JimmUI.messageBox
                    (
                            ResourceBundle.getString("attention"),
                            ResourceBundle.getString("have_unread_mess"),
                            JimmUI.MESBOX_YESNO,
                            this,
                            MSGBS_EXIT
                    );
        } else doExit();
    }

    // Command listener
    public void commandAction(Command c, Displayable d) {
        // #sijapp cond.if modules_TRAFFIC is "true" #

        //Get traffic container
        Traffic traffic = Jimm.jimm.getTrafficRef();
        // #sijapp cond.end#


        // #sijapp cond.if target is "MOTOROLA" #
        //Exit by soft button

        if (c == MainMenu.exitCommand) {
            menuExit();
        }

        // #sijapp cond.end#

        // Return to contact list
        if (c == MainMenu.backCommand) {
            if ((d == this.addUserOrGroup) || (d == statusList))
                this.activate();
            else
                Jimm.jimm.getContactListRef().activate();
        } else if ((c == sendCommand) && (d == this.addUserOrGroup)) {
            // Display splash canvas
            SplashCanvas wait2 = Jimm.jimm.getSplashCanvasRef();
            wait2.setMessage(ResourceBundle.getString("wait"));
            wait2.setProgress(0);
            Jimm.display.setCurrent(wait2);

            SearchAction act1;
            UpdateContactListAction act2;

            if (addUserFlag) // Make a search for the given UIN
            {
                Search search = new Search();
                search.setSearchRequest(uinTextField.getString(), "", "", "", "", "", "", 0, false);

                act1 = new SearchAction(search, SearchAction.CALLED_BY_ADDUSER);
                act2 = null;
            } else // Add the group
            {
                ContactListGroupItem newGroup = new ContactListGroupItem(uinTextField.getString());

                act1 = null;
                act2 = new UpdateContactListAction(newGroup, UpdateContactListAction.ACTION_ADD);
            }
            try {
                if (addUserFlag)
                    Jimm.jimm.getIcqRef().requestAction(act1);
                else
                    Jimm.jimm.getIcqRef().requestAction(act2);

            } catch (JimmException e) {
                JimmException.handleException(e);
                if (e.isCritical()) return;
            }

            // Start timer
            if (addUserFlag)
                Jimm.jimm.getTimerRef().schedule(new SplashCanvas.SearchTimerTask(act1), 1000, 1000);
            else
                Jimm.jimm.getTimerRef().schedule(new SplashCanvas.UpdateContactListTimerTask(act2), 1000, 1000);
        } else if ((c == sendCommand) && (d == this.groupList)) {
            ContactListContactItem cItems[];
            cItems = Jimm.jimm.getContactListRef().getContactItems();
            int count = 0;
            for (int i = 0; i < cItems.length; i++) {
                if (cItems[i].getGroup() == Jimm.jimm.getContactListRef().getGroupItems()[this.groupList.getSelectedIndex()].getId())
                    count++;
            }
            if (count != 0) {
                Alert errorMsg;
                errorMsg = new Alert(ResourceBundle.getString("warning"), ResourceBundle.getString("no_not_empty_gr"), null, AlertType.WARNING);
                errorMsg.setTimeout(Alert.FOREVER);
                Jimm.display.setCurrent(errorMsg, this.groupList);
            } else {
                // Display splash canvas
                SplashCanvas wait2 = Jimm.jimm.getSplashCanvasRef();
                wait2.setMessage(ResourceBundle.getString("wait"));
                wait2.setProgress(0);
                Jimm.display.setCurrent(wait2);

                // Create and request action to delete the group
                UpdateContactListAction act = new UpdateContactListAction(Jimm.jimm.getContactListRef().getGroupItems()[this.groupList.getSelectedIndex()], UpdateContactListAction.ACTION_DEL);
                try {
                    Jimm.jimm.getIcqRef().requestAction(act);
                } catch (JimmException e) {
                    JimmException.handleException(e);
                    if (e.isCritical()) return;
                }
                // Start timer
                Jimm.jimm.getTimerRef().schedule(new SplashCanvas.UpdateContactListTimerTask(act), 1000, 1000);
            }
        }

        // User select OK in exit questiom message box
        else if (JimmUI.isMsgBoxCommand(c, MSGBS_EXIT) == JimmUI.CMD_YES) {
            doExit();
        }

        // User select CANCEL in exit questiom message box
        else if (JimmUI.isMsgBoxCommand(c, MSGBS_EXIT) == JimmUI.CMD_NO) {
            Jimm.jimm.getContactListRef().activate();
        }

        // Menu item has been selected

        //#sijapp cond.if target is "MOTOROLA"#
        else if (((c == List.SELECT_COMMAND) || (c == MainMenu.selectCommand)) && (d == this.list))
        // #sijapp cond.else#
        else if ((c == List.SELECT_COMMAND) && (d == this.list))
        // #sijapp cond.end#

        {
            switch (this.eventList[this.list.getSelectedIndex()]) {
                case MENU_CONNECT:
                    // Connect
                    Jimm.jimm.getContactListRef().beforeConnect();
                    Jimm.jimm.getIcqRef().connect();
                    break;

                case MENU_DISCONNECT:
                    // Disconnect
                    Jimm.jimm.getIcqRef().disconnect();
                    break;

                case MENU_LIST:
                    // ContactList
                    Jimm.jimm.getContactListRef().activate();
                    break;

                case MENU_KEYLOCK:
                    // Enable keylock
                    Jimm.jimm.getSplashCanvasRef().lock();
                    break;

                case MENU_STATUS:
                    // Set status
                    long onlineStatus = Jimm.jimm.getOptionsRef().getLongOption(Options.OPTION_ONLINE_STATUS);
                    if (onlineStatus == ContactList.STATUS_AWAY) {
                        MainMenu.statusList.setSelectedIndex(2, true);
                    } else if (onlineStatus == ContactList.STATUS_CHAT) {
                        MainMenu.statusList.setSelectedIndex(1, true);
                    } else if (onlineStatus == ContactList.STATUS_DND) {
                        MainMenu.statusList.setSelectedIndex(5, true);
                    } else if (onlineStatus == ContactList.STATUS_INVISIBLE) {
                        MainMenu.statusList.setSelectedIndex(6, true);
                    } else if (onlineStatus == ContactList.STATUS_NA) {
                        MainMenu.statusList.setSelectedIndex(3, true);
                    } else if (onlineStatus == ContactList.STATUS_OCCUPIED) {
                        MainMenu.statusList.setSelectedIndex(4, true);
                    } else if (onlineStatus == ContactList.STATUS_ONLINE) {
                        MainMenu.statusList.setSelectedIndex(0, true);
                    }
                    MainMenu.statusList.setCommandListener(this);
                    MainMenu.statusList.addCommand(backCommand);
                    //#sijapp cond.if target is "MOTOROLA"#
                    MainMenu.statusList.addCommand(selectCommand);
                    //#sijapp cond.end#
                    Jimm.display.setCurrent(MainMenu.statusList);
                    break;

                case MENU_ADD_USER:
                    // Add user
                    addUserOrGroupCmd(null, true);
                    break;

                case MENU_SEARCH:
                    // Search for User
                    Search searchf = new Search();
                    searchf.getSearchForm().activate(false);
                    break;

                case MENU_ADD_GROUP:
                    // Add group
                    addUserOrGroupCmd(null, false);
                    break;

                case MENU_DEL_GROUP:
                    // Del group
                    // Show list of groups to select which group to delete
                    groupList = new List(ResourceBundle.getString("whichgroup"), List.EXCLUSIVE);
                    for (int i = 0; i < Jimm.jimm.getContactListRef().getGroupItems().length; i++) {
                        groupList.append(Jimm.jimm.getContactListRef().getGroupItems()[i].getName(), null);
                    }
                    groupList.addCommand(backCommand);
                    groupList.addCommand(sendCommand);
                    groupList.setCommandListener(this);
                    Jimm.display.setCurrent(groupList);
                    break;

                case MENU_OPTIONS:
                    // Options
                    Jimm.jimm.getOptionsRef().optionsForm.activate();
                    break;

                // #sijapp cond.if modules_TRAFFIC is "true" #
                case MENU_TRAFFIC:
                    // Traffic
                    traffic.setIsActive(true);
                    traffic.trafficScreen.activate();
                    break;
                // #sijapp cond.end #

                case MENU_ABOUT:
                    // Display an info
                    Jimm.jimm.getUIRef().about(list);
                    break;

                //#sijapp cond.if target is "MIDP2"#
                case MENU_MINIMIZE:
                    // Minimize Jimm (if supported)
                    Jimm.display.setCurrent(null);
                    Jimm.jimm.setMinimized(true);
                    break;
                //#sijapp cond.end#

                case MENU_EXIT:
                    // Exit
                    menuExit();
                    break;
            }
        }
        // Online status has been selected
        //#sijapp cond.if target is "MOTOROLA"#
        else if (((c == List.SELECT_COMMAND) || (c == MainMenu.selectCommand)) && (d == MainMenu.statusList))
        //#sijapp cond.else#
        else if ((c == List.SELECT_COMMAND) && (d == MainMenu.statusList))
        //#sijapp cond.end#
        {

            // Request online status change
            long onlineStatus = ContactList.STATUS_ONLINE;
            switch (MainMenu.statusList.getSelectedIndex()) {
                case 1:
                    onlineStatus = ContactList.STATUS_CHAT;
                    break;
                case 2:
                    onlineStatus = ContactList.STATUS_AWAY;
                    break;
                case 3:
                    onlineStatus = ContactList.STATUS_NA;
                    break;
                case 4:
                    onlineStatus = ContactList.STATUS_OCCUPIED;
                    break;
                case 5:
                    onlineStatus = ContactList.STATUS_DND;
                    break;
                case 6:
                    onlineStatus = ContactList.STATUS_INVISIBLE;
                    break;
            }
            try {
                SetOnlineStatusAction act = new SetOnlineStatusAction(onlineStatus);
                Jimm.jimm.getIcqRef().requestAction(act);
            } catch (JimmException e) {
                JimmException.handleException(e);
                if (e.isCritical()) return;
            }

            if (!((onlineStatus == ContactList.STATUS_INVISIBLE) || (onlineStatus == ContactList.STATUS_ONLINE))) {
                //#sijapp cond.if target is "MIDP2" | target is "MOTOROLA" | target is "SIEMENS2"#
                statusMessage = new TextBox(ResourceBundle.getString("status_message"), Jimm.jimm.getOptionsRef().getStringOption(Options.OPTION_STATUS_MESSAGE), 255, TextField.ANY | TextField.INITIAL_CAPS_SENTENCE);
                //#sijapp cond.else#
                statusMessage = new TextBox(ResourceBundle.getString("status_message"), Jimm.jimm.getOptionsRef().getStringOption(Options.OPTION_STATUS_MESSAGE), 255, TextField.ANY);
                //#sijapp cond.end#

                statusMessage.addCommand(selectCommand);
                statusMessage.setCommandListener(this);
                Jimm.display.setCurrent(statusMessage);
            } else {
                // Activate main menu
                Jimm.jimm.getContactListRef().activate();
            }
        } else if ((c == selectCommand) && (d == statusMessage)) {
            Jimm.jimm.getOptionsRef().setStringOption(Options.OPTION_STATUS_MESSAGE, statusMessage.getString());
            try {
                Jimm.jimm.getOptionsRef().save();
            } catch (Exception e) {
                JimmException.handleException(new JimmException(172, 0, true));
            }
            // Activate main menu
            Jimm.jimm.getContactListRef().activate();
        }
    }
}

