package jimm;

import jimm.comm.Util;

public class ContactListGroupItem extends ContactListItem {
    // Persistent variables
    private int id;
    private String name;

    private int
            // Counter for online users
            onlineCount,

    // counter for total users in group
    totalCount;

    // Constructor for an existing group item
    public ContactListGroupItem(int id, String name) {
        this.id = id;
        this.name = new String(name);
        onlineCount = totalCount = 0;
    }

    // Constructor for a new group item
    public ContactListGroupItem(String name) {
        this.id = Util.createRandomId();
        this.name = new String(name);
        onlineCount = totalCount = 0;
    }

    public void setCounters(int online, int total) {
        onlineCount = online;
        totalCount = total;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public String getText() {
        String result;

        if ((onlineCount != 0) &&
                !Jimm.jimm.getOptionsRef().getBooleanOption(Options.OPTION_CL_HIDE_OFFLINE))
            result = name + " (" + Integer.toString(onlineCount) + "/"
                    + Integer.toString(totalCount) + ")";
        else result = name;
        return result;
    }

    public int getImageIndex() {
        return -1;
    }

    public int getTextColor() {
        return Jimm.jimm.getOptionsRef().getSchemeColor(Options.CLRSCHHEME_TEXT);
    }

    // Returns the group item id
    public int getId() {
        return (this.id);
    }


    // Sets the group item id
    public void setId(int id) {
        this.id = id;
    }


    // Returns the group item name
    public String getName() {
        return (new String(this.name));
    }


    // Sets the group item name
    public void setName(String name) {
        this.name = new String(name);
    }


    // Checks whether some other object is equal to this one
    public boolean equals(Object obj) {
        if (!(obj instanceof ContactListGroupItem)) return (false);
        ContactListGroupItem gi = (ContactListGroupItem) obj;
        return (this.id == gi.getId());
    }


}
