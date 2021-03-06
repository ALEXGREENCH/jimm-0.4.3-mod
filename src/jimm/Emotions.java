//#sijapp cond.if modules_SMILES is "true" #
package jimm;

import DrawControls.*;
import jimm.util.GreenchUtils;
import jimm.util.ResourceBundle;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

public class Emotions implements VirtualListCommands, CommandListener {

    static private final Command cmdOk = new Command(
            ResourceBundle.getString("select"),
            Command.OK,
            1
    );
    static private final Command cmdCancel = new Command(
            ResourceBundle.getString("cancel"),
            Command.BACK,
            2
    );
    final private ImageList images = new ImageList();
    final private Vector findedEmotions = new Vector();
    final private Vector textCorr = new Vector();
    final private Vector selEmotions = new Vector();
    boolean used;
    private Displayable lastDisplay;
    private CommandListener selectionListener;
    private String emotionText;


    ///////////////////////////////////
    //                               //
    //   UI for emotion selection    //
    //                               //
    ///////////////////////////////////
    private Selector selector;

    public Emotions() {
        int iconsSize;
        used = false;

        // Load file "smiles.txt"
        InputStream stream = this.getClass().getResourceAsStream("/smiles.txt");
        if (stream == null) return;

        DataInputStream dos = new DataInputStream(stream);

        try {
            StringBuffer strBuffer = new StringBuffer();
            boolean eof = false, clrf = false;

            // Read icon size
            readStringFromStream(strBuffer, dos);
            iconsSize = Integer.parseInt(strBuffer.toString());

            do {
                // Read smile index
                readStringFromStream(strBuffer, dos);
                Integer currIndex = Integer.valueOf(strBuffer.toString());

                // Read smile name
                readStringFromStream(strBuffer, dos);
                String smileName = strBuffer.toString();

                // Read smile strings
                for (int i = 0; ; i++) {
                    try {
                        clrf = readStringFromStream(strBuffer, dos);
                    } catch (EOFException eofExcept) {
                        eof = true;
                    }

                    String word = new String(strBuffer).trim();

                    // Add pair (word, integer) to textCorr
                    if (word.length() != 0) insertTextCorr(word, currIndex);

                    // Add triple (index, word, name) to selEmotions
                    if (i == 0) selEmotions.addElement(new Object[]{currIndex, word, smileName});

                    if (clrf || eof) break;
                }
            } while (!eof);

            // Read images
            images.load("/smiles.png", iconsSize, iconsSize);
        } catch (Exception e) {
            return;
        }

        used = true;
    }

    // Reads simple word from stream. Used in Emotions().
    // Returns "true" if break was found after word
    static boolean readStringFromStream(StringBuffer buffer, DataInputStream stream) throws IOException {
        byte chr;
        buffer.setLength(0);
        for (; ; ) {
            chr = stream.readByte();
            if ((chr == ' ') || (chr == '\n') || (chr == '\t')) break;
            if (chr == '_') chr = ' ';
            if (chr >= ' ') buffer.append((char) chr);
        }
        return (chr == '\n');
    }

    // Add smile text and index to textCorr in decreasing order of text length
    void insertTextCorr(String word, Integer index) {
        Object[] data = new Object[]{word, index};
        int wordLen = word.length();
        int size = textCorr.size();
        int insIndex = 0;
        for (; insIndex < size; insIndex++) {
            Object[] cvtData = (Object[]) textCorr.elementAt(insIndex);
            int cvlDataWordLen = ((String) cvtData[0]).length();
            if (cvlDataWordLen <= wordLen) {
                textCorr.insertElementAt(data, insIndex);
                return;
            }
        }
        textCorr.addElement(data);
    }

    private void findEmotionInText(String text, String emotion, Integer index, int startIndex) {
        int findedIndex, len = emotion.length();

        findedIndex = text.indexOf(emotion, startIndex);
        if (findedIndex == -1) return;
        findedEmotions.addElement(new int[]{findedIndex, len, index.intValue()});
    }

    public void addTextWithEmotions(TextList textList, String text, int fontStyle, int textColor, int bigTextIndex) {
        if (!used || !Jimm.jimm.getOptionsRef().getBooleanOption(Options.OPTION_USE_SMILES)) {
            textList.addBigText(text, textColor, fontStyle, bigTextIndex);
            return;
        }

        int startIndex = 0;
        for (; ; ) {
            findedEmotions.removeAllElements();

            int size = textCorr.size();
            for (int i = 0; i < size; i++) {
                Object[] array = (Object[]) textCorr.elementAt(i);
                findEmotionInText
                        (
                                text,
                                (String) array[0],
                                (Integer) array[1],
                                startIndex
                        );
            }

            if (findedEmotions.isEmpty()) break;
            int count = findedEmotions.size();
            int minIndex = 100000;
            int[] data = null;
            int[] minArray = null;
            for (int i = 0; i < count; i++) {
                data = (int[]) findedEmotions.elementAt(i);
                if (data[0] < minIndex) {
                    minIndex = data[0];
                    minArray = data;
                }
            }

            if (startIndex != minIndex)
                textList.addBigText(text.substring(startIndex, minIndex), textColor, fontStyle, bigTextIndex);

            textList.addImage
                    (
                            images.elementAt(minArray[2]),
                            text.substring(minIndex, minIndex + minArray[1]),
                            images.getWidth(),
                            images.getHeight(),
                            bigTextIndex
                    );

            startIndex = minIndex + minArray[1];
        }

        int lastIndex = text.length();

        if (lastIndex != startIndex)
            textList.addBigText(text.substring(startIndex, lastIndex), textColor, fontStyle, bigTextIndex);
    }

    public void selectEmotion(CommandListener selectionListener, Displayable lastDisplay) {
        this.selectionListener = selectionListener;
        this.lastDisplay = lastDisplay;
        //selList = new TextList(null);
        selector = new Selector();
        JimmUI.setColorScheme(selector);

        // #sijapp cond.if target is "MIDP2" | target is "MOTOROLA" | target is "SIEMENS2"#
        selector.setFullScreenMode(false);
        // #sijapp cond.end#

        selector.addCommand(cmdOk);
        selector.addCommand(cmdCancel);
        selector.setCommandListener(this);

        Jimm.display.setCurrent(selector);
    }

    public void commandAction(Command c, Displayable d) {
        if (c == cmdOk) select();
        else if (c == cmdCancel) {
            Jimm.display.setCurrent(lastDisplay);
            selector = null;
            System.gc();
        }
    }

    public void onKeyPress(VirtualList sender, int keyCode) {
    }

    public void onCursorMove(VirtualList sender) {
    }

    public void onItemSelected(VirtualList sender) {
        select();
    }

    private void select() {
        Jimm.display.setCurrent(lastDisplay);
        selector = null;
        System.gc();
        selectionListener.commandAction(cmdOk, selector);
    }

    public String getSelectedEmotion() {
        return emotionText;
    }

    public boolean isMyOkCommand(Command command) {
        return (command == cmdOk);
    }


    /////////////////////////
    //                     //
    //    class Selector   //
    //                     //
    /////////////////////////

    private class Selector extends VirtualList implements VirtualListCommands {
        private final int cols;
        private final int rows;
        private final int itemHeight;
        private int curCol;

        Selector() {
            super(null);
            setVLCommands(this);

            int drawWidth = getWidth() - scrollerWidth - 2;

            setCursorMode(SEL_NONE);

            int imgHeight = images.getHeight();

            itemHeight = imgHeight + 5;

            cols = drawWidth / itemHeight;
            rows = (selEmotions.size() + cols - 1) / cols;
            curCol = 0;

            showCurrSmileName();
        }

        protected void drawItemData(
                        Graphics g,
                        boolean isSelected,
                        int index,
                        int x1, int y1, int x2, int y2,
                        int fontHeight
        ) {
            int xa, xb;
            int startIdx = cols * index;
            int imagesCount = images.size();
            xa = x1;
            for (int i = 0; i < cols; i++, startIdx++) {
                if (startIdx >= selEmotions.size()) break;
                Object[] data = (Object[]) selEmotions.elementAt(startIdx);
                int smileIdx = ((Integer) data[0]).intValue();
                xb = xa + itemHeight;

                if (isSelected && (i == curCol)) {
                    GreenchUtils.drawGradientAndFrameBG(g,
                            xa,
                            y1,
                            xb,
                            y2,
                            0x95cc5e); // TODO: OPTION_COLOR_CURSOR
                }

                if (smileIdx < imagesCount)
                    g.drawImage(images.elementAt(smileIdx), xa + 3, y1 + 3, Graphics.TOP | Graphics.LEFT);


                xa = xb;
            }
        }

        private void showCurrSmileName() {
            int selIdx = getCurrIndex() * cols + curCol;
            if (selIdx >= selEmotions.size()) return;
            Object[] data = (Object[]) selEmotions.elementAt(selIdx);
            emotionText = (String) data[1];
            // #sijapp cond.if target is "MIDP2" | target is "MOTOROLA" | target is "SIEMENS2"#
            setTitle((String) data[2]);
            // #sijapp cond.else#
            setCaption((String) data[2]);
            // #sijapp cond.end#
        }

        public int getItemHeight(int itemIndex) {
            return itemHeight;
        }

        protected int getSize() {
            return rows;
        }

        protected void get(int index, ListItem item) {

        }

        public void onKeyPress(VirtualList sender, int keyCode) {
            int lastCol = curCol;
            switch (getGameAction(keyCode)) {
                case LEFT:
                    if (curCol != 0) curCol--;
                    break;
                case RIGHT:
                    if (curCol < (cols - 1)) curCol++;
                    break;
            }

            int index = curCol + getCurrIndex() * cols;
            if (index >= selEmotions.size()) curCol = (selEmotions.size() - 1) % cols;

            if (lastCol != curCol) {
                repaint();
                showCurrSmileName();
            }
        }

        public void onCursorMove(VirtualList sender) {
            showCurrSmileName();
        }

        public void onItemSelected(VirtualList sender) {
            select();
        }
    }
}

//#sijapp cond.end#
