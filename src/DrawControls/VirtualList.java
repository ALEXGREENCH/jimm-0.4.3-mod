/*******************************************************************************
 Jimm - Mobile Messaging - J2ME ICQ clone
 Copyright (C) 2003-05  Jimm Project

 This program is free software; you can redistribute it and/or
 modify it under the terms of the GNU General Public License
 as published by the Free Software Foundation; either version 2
 of the License, or (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 ********************************************************************************
 File: src/DrawControls/VirtualList.java
 Version: 0.4.3  Date: 2005/11/18
 Author(s): Artyomov Denis
 *******************************************************************************/

package DrawControls;

import jimm.util.GreenchUtils;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;


//! This class is base class of owner draw list controls
/*!
    It allows you to create list with different colors and images. 
    Base class of VirtualDrawList if Canvas, so it draw itself when
    paint event is heppen. VirtualList have cursor controlled of
    user
*/

public abstract class VirtualList extends Canvas {
    /*! Use dotted mode of cursor. If item of list
     is selected, dotted rectangle drawn around  it*/
    public final static int SEL_DOTTED = 2;

    /*! Does't show cursor at selected item. */
    public final static int SEL_NONE = 3;

    /*! Constant for medium sized font of caption and item text */
    public final static int MEDIUM_FONT = Font.SIZE_MEDIUM;

    /*! Constant for large sized font of caption and item text */
    public final static int LARGE_FONT = Font.SIZE_LARGE;

    /*! Constant for small sized font of caption and item text */
    public final static int SMALL_FONT = Font.SIZE_SMALL;

    // Set of fonts for quick selecting
    private Font normalFont, boldFont, italicFont;

    // Width of scroller line
    protected final static int scrollerWidth;

    // Font for drawing caption
    private final static Font capFont;

    // Commands to react to VL events
    private VirtualListCommands vlCommands;

    // Caption of VL
    private String caption;

    // Used by "Invalidate" method to prevent invalidate when locked
    private boolean dontRepaint = false;

    // Images for VL
    private ImageList imageList = null;

    // Index for current item of VL
    protected int currItem = 0;

    // Used for passing params of items whan painting
    final static protected ListItem paintedItem;

    // Used to catch changes to repaint data
    private int lastCurrItem = 0, lastTopItem = 0;

    private int
            topItem = 0,            // Index of top visilbe item
            fontSize = MEDIUM_FONT,  // Current font size of VL
            bkgrndColor = 0xFFFFFF,     // bk color of VL
            cursorColor = 0x808080,     // Used when drawing focus rect.
            textColor = 0x000000,     // Default text color.
            capTxtColor = 0xFFFFFF,     // Color of caprion text
            cursorMode = SEL_DOTTED;   // Cursor mode

    static {
        capFont = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_SMALL);
        int width = capFont.getHeight() / 4;
        scrollerWidth = width > 4 ? width : 4;
        paintedItem = new ListItem();
    }

    //! Create new virtual list with default values
    public VirtualList(
            String capt //!< Caption text of new virtual list
    ) {
        super();
        setCaption(capt);
        this.capTxtColor = 0xFFFFFF;
        this.bkgrndColor = 0xFFFFFF;
        this.fontSize = Font.SIZE_MEDIUM;
        createSetOfFonts(this.fontSize);
        this.cursorMode = SEL_DOTTED;
    }

    // public VirtualList
    public VirtualList(
            String capt,      //!< Caption text of new virtual list
            int capTextColor, //!< Caption text color
            int backColor,    //!< Control back color
            int fontSize,     /*!< Control font size. This font size if used both for caption and text in tree nodes */
            int cursorMode    /*!< Cursor mode. Can be VirtualList.SEL_DOTTED or VirtualList.SEL_INVERTED */
    ) {
        super();
        setCaption(capt);
        this.capTxtColor = capTextColor;
        this.bkgrndColor = backColor;

        this.fontSize = fontSize;
        createSetOfFonts(this.fontSize);
        this.cursorMode = cursorMode;
    }

    //! Request number of list elements to be shown in list
	/*! You must return number of list elements in successtor of
	    VirtualList. Class calls method "getSize" each time before it drawn */
    abstract protected int getSize();

    //! Request of data of one list item
	/*! You have to reload this method. With help of method "get" class finds out
	    data of each item. Method "get" is called each time when list item 
	    is drawn */
    abstract protected void get
    (
            int index,    //!< Number of requested list item
            ListItem item //!< Data of list item. Fill this object with item data.
    );

    Font getQuickFont(int style) {
        switch (style) {
            case Font.STYLE_BOLD:
                return boldFont;
            case Font.STYLE_PLAIN:
                return normalFont;
            case Font.STYLE_ITALIC:
                return italicFont;
        }
        return Font.getFont(Font.FACE_SYSTEM, style, fontSize);
    }

    // returns height of draw area in pixels
    protected int getDrawHeight() {
        return getHeight() - getCapHeight();
    }

    //! Sets new font size and invalidates items
    public void setFontSize(int value) {
        if (fontSize == value) return;
        fontSize = value;
        createSetOfFonts(fontSize);
        checkTopItem();
        invalidate();
    }

    public void setVLCommands(VirtualListCommands vlCommands) {
        this.vlCommands = vlCommands;
    }

    public void setColors(int capTxt, int bkgrnd, int cursor, int text) {
        capTxtColor = capTxt;
        bkgrndColor = bkgrnd;
        cursorColor = cursor;
        textColor = text;
        repaint();
    }

    private void createSetOfFonts(int size) {
        normalFont = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, fontSize);
        boldFont = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, fontSize);
        italicFont = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_ITALIC, fontSize);
    }

    public int getFontSize() {
        return fontSize;
    }

    public int getTextColor() {
        return textColor;
    }

    //! Returns number of visibled lines of text which fits in screen
    public int getVisCount() {
        int size = getSize();
        int y = 0;
        int counter = 0, i;
        int height = getDrawHeight();
        int topItem = this.topItem;

        if (size == 0) return 0;

        if (topItem < 0) topItem = 0;
        if (topItem >= size) topItem = size - 1;

        for (i = topItem; i < (size - 1); i++) {
            y += getItemHeight(i);
            if (y > height) return counter;
            counter++;
        }

        y = height;
        counter = 0;
        for (i = size - 1; i >= 0; i--) {
            y -= getItemHeight(i);
            if (y < 0) break;
            counter++;
        }

        return counter;
    }

    //TODO: brief text
    public void setCursorMode(int value) {
        if (cursorMode == value) return;
        cursorMode = value;
        invalidate();
    }

    public int getCursorMode() {
        return cursorMode;
    }

    //! Returns height of each item in list
    public int getItemHeight(int itemIndex) {
        int imgHeight, fontHeight = getFontHeight();
        if (imageList != null) imgHeight = imageList.getHeight() + 1;
        else imgHeight = 0;
        return (fontHeight > imgHeight) ? fontHeight : imgHeight;
    }

    // protected void invalidate()
    protected void invalidate() {
        if (dontRepaint) return;
        repaint();
    }

    // Setting image list for items
    public void setImageList(ImageList list) {
        imageList = list;
        invalidate();
    }

    //! Return current image list, used for tree node icons
    /*! If no image list stored, null is returned */
    public ImageList getImageList() {
        return imageList;
    }

    // protected void checkCurrItem()
    protected void checkCurrItem() {
        if (currItem < 0) currItem = 0;
        if (currItem >= getSize() - 1) currItem = getSize() - 1;
    }

    // protected void checkTopItem() - internal
    // check for position of top element of list and change it, if nesessary
    protected void checkTopItem() {
        int size = getSize();
        int visCount = getVisCount();

        if (size == 0) {
            topItem = 0;
            return;
        }

        if (currItem >= (topItem + visCount - 1)) topItem = currItem - visCount + 1;
        if (currItem < topItem) topItem = currItem;

        if ((size - topItem) <= visCount) topItem = (size > visCount) ? (size - visCount) : 0;
        if (topItem < 0) topItem = 0;
    }

    // Check does item with index visible
    protected boolean visibleItem(int index) {
        return (index >= topItem) && (index <= (topItem + getVisCount()));
    }

    // private void storelastItemIndexes()
    protected void storelastItemIndexes() {
        lastCurrItem = currItem;
        lastTopItem = topItem;
    }

    // private void repaintIfLastIndexesChanged()
    protected void repaintIfLastIndexesChanged() {
        if ((lastCurrItem != currItem) || (lastTopItem != topItem)) {
            invalidate();
            if (vlCommands != null) vlCommands.onCursorMove(this);
        }
    }

    // protected void moveCursor(int step)
    protected void moveCursor(int step, boolean moveTop) {
        storelastItemIndexes();
        if (moveTop && (cursorMode == SEL_NONE)) topItem += step;
        currItem += step;
        checkCurrItem();
        checkTopItem();
        repaintIfLastIndexesChanged();
    }

    protected void itemSelected() {
    }

    // private keyReaction(int keyCode)
    private void keyReaction(int keyCode) {
        switch (getGameAction(keyCode)) {
            case Canvas.DOWN:
                moveCursor(1, false);
                break;
            case Canvas.UP:
                moveCursor(-1, false);
                break;
            case Canvas.FIRE:
                itemSelected();
                if (vlCommands != null) vlCommands.onItemSelected(this);
                break;
        }

        switch (keyCode) {
            case KEY_NUM1:
                storelastItemIndexes();
                currItem = topItem = 0;
                repaintIfLastIndexesChanged();
                break;

            case KEY_NUM7:
                storelastItemIndexes();
                int endIndex = getSize() - 1;
                currItem = endIndex;
                checkTopItem();
                repaintIfLastIndexesChanged();
                break;

            case KEY_NUM3:
                moveCursor(-getVisCount(), false);
                break;

            case KEY_NUM9:
                moveCursor(getVisCount(), false);
                break;

            // #sijapp cond.if target is "MOTOROLA"#
            case KEY_STAR:
                LightControl.changeState();
                break;
            // #sijapp cond.end#
        }

    }

    // protected void keyPressed(int keyCode)
    protected void keyPressed(int keyCode) {
        //#sijapp cond.if target is "MOTOROLA"#
        LightControl.flash(false);
        //#sijapp cond.end#

        keyReaction(keyCode);
        if (vlCommands != null) vlCommands.onKeyPress(this, keyCode);
    }

    // protected void keyRepeated(int keyCode)
    protected void keyRepeated(int keyCode) {
        keyReaction(keyCode);
    }

    //! Set caption text for list
    public void setCaption(String capt) {
        if (caption != null) if (caption.equals(capt)) return;
        caption = (capt == null) ? null : new String(capt);
        repaint();
    }

    public String getCaption() {
        return new String(caption);
    }

    public void setTopItem(int index) {
        storelastItemIndexes();
        currItem = topItem = index;
        checkTopItem();
        repaintIfLastIndexesChanged();
    }

    // public void setCurrentItem(int index)
    public void setCurrentItem(int index) {
        storelastItemIndexes();
        currItem = index;
        checkTopItem();
        repaintIfLastIndexesChanged();
    }

    // public int getCurrIndex()
    public int getCurrIndex() {
        return currItem;
    }

    // Return height of caption in pixels
    private int getCapHeight() {
        if (caption == null) return 0;
        return capFont.getHeight() + 3;
    }

    // private int drawCaption(Graphics g)
    private int drawCaption(Graphics g) {
        if (this.caption == null) return 0;
        int width = getWidth();
        g.setFont(capFont);
        int th = capFont.getHeight();
        g.setColor(bkgrndColor);
        g.fillRect(0, 0, width, th + 3);
        g.setColor(capTxtColor);
        g.drawString(caption, 2, 1, Graphics.TOP | Graphics.LEFT);
        int lineY = th + 1;
        g.drawLine(0, lineY, width, lineY);
        return lineY + 2;
    }

    protected boolean isItemSelected(int index) {
        return ((currItem == index) && (cursorMode != SEL_NONE));
    }

    // private int drawItem(int index, Graphics g, int top_y, int th, ListItem item)
    private int drawItem(
            int index,
            Graphics g,
            int yCrd,
            int itemWidth,
            int itemHeight,
            int fontHeight) {
        drawItemData(g, (currItem == index), index, 2, yCrd, itemWidth - itemHeight / 3, yCrd + itemHeight, fontHeight);
        return yCrd + itemHeight;
    }

    // Draw scroller is items doesn't fit in VL area
    private void drawScroller(Graphics g, int topY, int visCount) {
        int width = getWidth() - scrollerWidth;
        int height = getHeight();
        int itemCount = getSize();
        boolean haveToShowScroller = ((itemCount > visCount) && (itemCount > 0));
        int color = GreenchUtils.transformColorLight(
                GreenchUtils.transformColorLight(
                        bkgrndColor, 32
                ), -32
        );
        if (color == 0) color = 0x797aa8;
        g.setStrokeStyle(Graphics.SOLID);
        g.setColor(color);
        g.fillRect(width + 1, topY, scrollerWidth - 1, height - topY);
        g.setColor(GreenchUtils.transformColorLight(color, -64));
        g.drawLine(width, topY, width, height);
        if (haveToShowScroller) {
            int sliderSize = (height - topY) * visCount / itemCount;
            if (sliderSize < 7) sliderSize = 7;
            int y1 = topItem * (height - sliderSize - topY) / (itemCount - visCount) + topY;
            int y2 = y1 + sliderSize;
            g.setColor(color);
            g.fillRect(width + 2, y1 + 2, scrollerWidth - 3, y2 - y1 - 3);
            g.setColor(GreenchUtils.transformColorLight(color, -192));
            g.drawRect(width, y1, scrollerWidth - 1, y2 - y1 - 1);
            g.setColor(GreenchUtils.transformColorLight(color, 96));
            g.drawLine(width + 1, y1 + 1, width + 1, y2 - 2);
            g.drawLine(width + 1, y1 + 1, width + scrollerWidth - 2, y1 + 1);
        }
    }

    //! returns font height
    public int getFontHeight() {
        return getQuickFont(Font.STYLE_PLAIN).getHeight();
    }

    // private int drawItems(Graphics g, int top_y)
    private int drawItems(Graphics g, int top_y, int fontHeight) {
        int grCursorY1 = -1, grCursorY2 = -1;
        int height = getHeight();
        int size = getSize();
        int i, y;
        int itemWidth = getWidth() - scrollerWidth;

        // Fill background
        g.setColor(bkgrndColor);
        g.fillRect(0, top_y, itemWidth, height - top_y);

        // Draw cursor
        y = top_y;
        for (i = topItem; i < size; i++) {
            int itemHeight = getItemHeight(i);
            if (isItemSelected(i)) {
                if (grCursorY1 == -1) grCursorY1 = y;
                grCursorY2 = y + itemHeight - 1;
            }
            y += itemHeight;
            if (y >= height) break;
        }

        if (grCursorY1 != -1) {
            GreenchUtils.drawGradientAndFrameBG(
                    g,
                    0,
                    grCursorY1,
                    itemWidth,
                    grCursorY2,
                    0x95cc5e); // TODO: OPTION_COLOR_CURSOR
        }

        // Draw items
        paintedItem.clear();
        y = top_y;
        for (i = topItem; i < size; i++) {
            int itemHeight = getItemHeight(i);
            g.setStrokeStyle(Graphics.SOLID);
            y = drawItem(i, g, y, itemWidth, itemHeight, fontHeight);
            if (y >= height) break;
        }

        return y;
    }

    // private void paintAllOnGraphics(Graphics graphics)
    private void paintAllOnGraphics(Graphics graphics) {
        int visCount = getVisCount();
        int y = drawCaption(graphics);
        drawItems(graphics, y, getFontHeight());
        drawScroller(graphics, y, visCount);
    }

    static Image bDIimage = null;

    // protected void paint(Graphics g)
    protected void paint(Graphics g) {
        if (dontRepaint) return;

        if (isDoubleBuffered()) {
            paintAllOnGraphics(g);
        } else {
            try {
                if (bDIimage == null) bDIimage = Image.createImage(getWidth(), getHeight());
                paintAllOnGraphics(bDIimage.getGraphics());
                g.drawImage(bDIimage, 0, 0, Graphics.TOP | Graphics.LEFT);
            } catch (Exception e) {
                paintAllOnGraphics(g);
            }
        }
    }

    // protected void drawItemData
    protected void drawItemData(
            Graphics g,
            boolean isSelected,
            int index,
            int x1,
            int y1,
            int x2,
            int y2,
            int fontHeight) {
        int imgWidth;

        get(index, paintedItem);

        g.setFont(getQuickFont(paintedItem.fontStyle));
        g.setColor(paintedItem.color);

        ImageList imageList = getImageList();
        if ((imageList != null)
                && (paintedItem.imageIndex >= 0)
                && (paintedItem.imageIndex < imageList.size())) {
            imgWidth = imageList.getWidth() + 3;
            g.drawImage
                    (
                            imageList.elementAt(paintedItem.imageIndex),
                            x1 + 1,
                            (y1 + y2 - imageList.getHeight()) / 2,
                            Graphics.TOP | Graphics.LEFT
                    );
        } else imgWidth = 0;

        if (paintedItem.text != null)
            g.drawString
                    (
                            paintedItem.text,
                            x1 + imgWidth,
                            (y1 + y2 - fontHeight) / 2,
                            Graphics.TOP | Graphics.LEFT
                    );
    }

    public void lock() {
        dontRepaint = true;
    }

    protected void afterUnlock() {
    }

    public void unlock() {
        dontRepaint = false;
        afterUnlock();
        invalidate();
    }

    protected boolean getLocked() {
        return dontRepaint;
    }

    static public int getInverseColor(int color)
    {
        int r = (color & 0xFF);
        int g = ((color & 0xFF00) >> 8);
        int b = ((color & 0xFF0000) >> 16);
        return ((r + g + b) > 3 * 127) ? 0 : 0xFFFFFF;
    }
}