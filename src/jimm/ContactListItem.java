package jimm;

import javax.microedition.lcdui.Font;


public abstract class ContactListItem {
    // Checks whether some other object is equal to this one
    public abstract boolean equals(Object obj);

    // returns text of list item. Is used for visual tree
    public abstract String getText();

    // returns image index of tree node. Is used for visual tree
    public abstract int getImageIndex();

    public abstract int getTextColor();

    public int getFontStyle() {
        return Font.STYLE_PLAIN;
    }
}
