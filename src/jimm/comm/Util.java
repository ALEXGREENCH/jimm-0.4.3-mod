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
 File: src/jimm/comm/Util.java
 Version: 0.4.3  Date: 2005/11/18
 Author(s): Manuel Linsmayer, Andreas Rossbacher, Sergey Chernov, Andrey B. Ivlev
 *******************************************************************************/


package jimm.comm;

import jimm.*;
import jimm.util.ResourceBundle;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.*;


public class Util {


    // Password encryption key
    public static final byte[] PASSENC_KEY = {(byte) 0xF3, (byte) 0x26, (byte) 0x81, (byte) 0xC4,
            (byte) 0x39, (byte) 0x86, (byte) 0xDB, (byte) 0x92,
            (byte) 0x71, (byte) 0xA3, (byte) 0xB9, (byte) 0xE6,
            (byte) 0x53, (byte) 0x7A, (byte) 0x95, (byte) 0x7C};


    // Online status (set values)
    public static final int SET_STATUS_AWAY = 0x0001;
    public static final int SET_STATUS_CHAT = 0x0020;
    public static final int SET_STATUS_DND = 0x0013;
    public static final int SET_STATUS_INVISIBLE = 0x0100;
    public static final int SET_STATUS_NA = 0x0005;
    public static final int SET_STATUS_OCCUPIED = 0x0011;
    public static final int SET_STATUS_ONLINE = 0x0000;

    // Counter variable
    private static int counter = 0;

    public synchronized static int getCounter() {
        counter++;
        return (counter);

    }


    // Called to get a date String
    public static String getDateString(boolean onlyTime, Date value) {
        Calendar time = Calendar.getInstance();
        String datestr = new String("failed");

        // Get time an apply time zone correction
        Date date = new Date();
        // #sijapp cond.if target is "SIEMENS2" #
        date.setTime(value.getTime() + TimeZone.getDefault().getRawOffset() + (TimeZone.getDefault().useDaylightTime() ? (60 * 60 * 1000) : 0));
        // #sijapp cond.end #
        time.setTime(date);

        // Construct the string for the display
        datestr = Util.makeTwo(time.get(Calendar.HOUR_OF_DAY)) + ":" + Util.makeTwo(time.get(Calendar.MINUTE));

        if (!onlyTime) {
            datestr = Util.makeTwo(time.get(Calendar.DAY_OF_MONTH)) + "." + Util.makeTwo(time.get(Calendar.MONTH) + 1) + "."
                    + String.valueOf(time.get(Calendar.YEAR)) + " " + datestr;
        }
        return datestr;
    }

    /*/////////////////////////////////////////////////////////////////////////
	//																	     //
	//				 METHODS FOR DATE AND TIME PROCESSING				     //
	//																	     //
	/////////////////////////////////////////////////////////////////////////*/

    private final static String error_str = "***error***";
    final public static int TIME_SECOND = 0;
    final public static int TIME_MINUTE = 1;
    final public static int TIME_HOUR   = 2;
    final public static int TIME_DAY	= 3;
    final public static int TIME_MON	= 4;
    final public static int TIME_YEAR   = 5;

    final private static byte[] dayCounts = explodeToBytes("31,28,31,30,31,30,31,31,30,31,30,31", ',', 10);

    final private static int[] monthIndexes =
            {
                    Calendar.JANUARY,
                    Calendar.FEBRUARY, Calendar.MARCH, Calendar.APRIL, Calendar.MAY,
                    Calendar.JUNE, Calendar.JULY, Calendar.AUGUST, Calendar.SEPTEMBER,
                    Calendar.OCTOBER, Calendar.NOVEMBER, Calendar.DECEMBER
            };

    static private int convertDateMonToSimpleMon(int dateMon)
    {
        for (int i = 0; i < monthIndexes.length; i++) if (monthIndexes[i] == dateMon) return i + 1;
        return -1;
    }

    /* Creates current date (GMT or local) */
    public static long createCurrentDate(boolean gmt)
    {
        return createCurrentDate(gmt, false);
    }

    public static long createCurrentDate(boolean gmt, boolean onlyDate)
    {
        long result;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        if (onlyDate)
        {
            result = createLongTime
                    (
                            calendar.get(Calendar.YEAR),
                            convertDateMonToSimpleMon(calendar.get(Calendar.MONTH)),
                            calendar.get(Calendar.DAY_OF_MONTH),
                            0,
                            0,
                            0
                    );
        }
        else
        {
            result = createLongTime
                    (
                            calendar.get(Calendar.YEAR),
                            convertDateMonToSimpleMon(calendar.get(Calendar.MONTH)),
                            calendar.get(Calendar.DAY_OF_MONTH),
                            calendar.get(Calendar.HOUR_OF_DAY),
                            calendar.get(Calendar.MINUTE),
                            calendar.get(Calendar.SECOND)
                    );
        }

        /* convert result to GMT time */
        // TODO: offset....
        //long diff = Options.getInt(Options.OPTIONS_LOCAL_OFFSET);
        //result += (diff * 3600);


        // TODO: offset....
        /* returns GMT or local time */
        //return gmt ? result : gmtTimeToLocalTime(result);
        return result;
    }

    /* Show date string */
    public static String getDateString(boolean onlyTime, boolean fullTime, long date)
    {
        if (date == 0) return error_str;

        int[] loclaDate = createDate(date);

        StringBuffer sb = new StringBuffer();

        if (!onlyTime)
        {
            sb.append(Util.makeTwo(loclaDate[TIME_DAY]))
                    .append('.')
                    .append(Util.makeTwo(loclaDate[TIME_MON]))
                    .append('.')
                    .append(loclaDate[TIME_YEAR])
                    .append(' ');
        }

        sb.append(Util.makeTwo(loclaDate[TIME_HOUR]))
                .append(':')
                .append(Util.makeTwo(loclaDate[TIME_MINUTE]));

        if (fullTime)
        {
            sb.append(':')
                    .append(Util.makeTwo(loclaDate[TIME_SECOND]));
        }

        return sb.toString();
    }

    /* Generates seconds count from 1st Jan 1970 till mentioned date */
    public static long createLongTime(int year, int mon, int day, int hour, int min, int sec)
    {
        int day_count, i, febCount;

        day_count = (year - 1970) * 365+day;
        day_count += (year - 1968) / 4;
        if (year >= 2000) day_count--;

        if ((year % 4 == 0) && (year != 2000))
        {
            day_count--;
            febCount = 29;
        }
        else febCount = 28;

        for (i = 0; i < mon - 1; i++) day_count += (i == 1) ? febCount : dayCounts[i];

        return day_count * 24L * 3600L + hour * 3600L + min * 60L + sec;
    }

    // Creates array of calendar values form value of seconds since 1st jan 1970 (GMT)
    public static int[] createDate(long value)
    {
        int total_days, last_days, i;
        int sec, min, hour, day, mon, year;

        sec = (int) (value % 60);

        min = (int) ((value / 60) % 60); // min
        value -= 60 * min;

        hour = (int) ((value / 3600) % 24); // hour
        value -= 3600 * hour;

        total_days = (int) (value / (3600 * 24));

        year = 1970;
        for (;;)
        {
            last_days = total_days - ((year % 4 == 0) && (year != 2000) ? 366 : 365);
            if (last_days <= 0) break;
            total_days = last_days;
            year++;
        } // year

        int febrDays = ((year % 4 == 0) && (year != 2000)) ? 29 : 28;

        mon = 1;
        for (i = 0; i < 12; i++)
        {
            last_days = total_days - ((i == 1) ? febrDays : dayCounts[i]);
            if (last_days <= 0) break;
            mon++;
            total_days = last_days;
        } // mon

        day = total_days; // day

        return new int[] { sec, min, hour, day, mon, year };
    }

    public static String getDateString(boolean onlyTime, boolean fullTime)
    {
        return getDateString(onlyTime, fullTime, createCurrentDate(false));
    }

    // TODO:...
    //public static long gmtTimeToLocalTime(long gmtTime)
    //{
    //    long diff = Options.getInt(Options.OPTIONS_GMT_OFFSET);
    //    return gmtTime + diff * 3600L;
    //}

    public static String longitudeToString(long seconds)
    {
        StringBuffer buf = new StringBuffer();
        int days = (int)(seconds / 86400);
        seconds %= 86400;
        int hours = (int)(seconds / 3600);
        seconds %= 3600;
        int minutes = (int)(seconds / 60);

        if (days != 0) buf.append(days).append(' ').append(ResourceBundle.getString("days")).append(' ');
        if (hours != 0) buf.append(hours).append(' ').append(ResourceBundle.getString("hours")).append(' ');
        if (minutes != 0) buf.append(minutes).append(' ').append(ResourceBundle.getString("minutes"));

        return buf.toString();
    }

    static public byte[] explodeToBytes(String text, char serparator, int radix)
    {
        String[] strings = explode(text, serparator);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        for (int i = 0; i < strings.length; i++)
        {
            String item = strings[i];
            if (item.charAt(0) == '*')
                for (int j = 1; j < item.length(); j++) bytes.write((byte)item.charAt(j));
            else
                bytes.write(Integer.parseInt(item, radix));

        }
        return bytes.toByteArray();
    }

    public static String getCurrentDay()
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        String day = "";

        switch (cal.get(Calendar.DAY_OF_WEEK))
        {
            case Calendar.MONDAY:
                day = "monday";
                break;

            case Calendar.TUESDAY:
                day = "tuesday";
                break;

            case Calendar.WEDNESDAY:
                day = "wednesday";
                break;

            case Calendar.THURSDAY:
                day = "thursday";
                break;

            case Calendar.FRIDAY:
                day = "friday";
                break;

            case Calendar.SATURDAY:
                day = "saturday";
                break;

            case Calendar.SUNDAY:
                day = "sunday";
                break;
        }
        return ResourceBundle.getString(day);
    }

    /* Divide text to array of parts using serparator charaster */
    static public String[] explode(String text, char serparator)
    {
        Vector tmp = new Vector();
        StringBuffer strBuf = new StringBuffer();
        int len = text.length();
        for (int i = 0; i < len; i++)
        {
            char chr = text.charAt(i);
            if (chr == serparator)
            {
                tmp.addElement(strBuf.toString());
                strBuf.delete(0, strBuf.length());
            }
            else strBuf.append(chr);
        }
        tmp.addElement(strBuf.toString());
        String[] result = new String[tmp.size()];
        tmp.copyInto(result);
        return result;
    }

    public static String toHexString(byte[] b) {
        StringBuffer sb = new StringBuffer(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            //	look up high nibble char
            sb.append(hexChar[(b[i] & 0xf0) >>> 4]);

            //	look up low nibble char
            sb.append(hexChar[b[i] & 0x0f]);
            sb.append(" ");
            if ((i != 0) && ((i % 15) == 0))
                sb.append("\n");
        }
        return sb.toString();
    }

    //	table to convert a nibble to a hex char.
    private static char[] hexChar = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};


    // Extracts the byte from the buffer (buf) at position off
    public static int getByte(byte[] buf, int off) {
        int val;
        val = ((int) buf[off]) & 0x000000FF;
        return (val);
    }


    // Puts the specified byte (val) into the buffer (buf) at position off
    public static void putByte(byte[] buf, int off, int val) {
        buf[off] = (byte) (val & 0x000000FF);
    }


    // Extracts the word from the buffer (buf) at position off using the specified byte ordering (bigEndian)
    public static int getWord(byte[] buf, int off, boolean bigEndian) {
        int val;
        if (bigEndian) {
            val = (((int) buf[off]) << 8) & 0x0000FF00;
            val |= (((int) buf[++off])) & 0x000000FF;
        } else   // Little endian
        {
            val = (((int) buf[off])) & 0x000000FF;
            val |= (((int) buf[++off]) << 8) & 0x0000FF00;
        }
        return (val);
    }


    // Extracts the word from the buffer (buf) at position off using big endian byte ordering
    public static int getWord(byte[] buf, int off) {
        return (Util.getWord(buf, off, true));
    }


    // Puts the specified word (val) into the buffer (buf) at position off using the specified byte ordering (bigEndian)
    public static void putWord(byte[] buf, int off, int val, boolean bigEndian) {
        if (bigEndian) {
            buf[off] = (byte) ((val >> 8) & 0x000000FF);
            buf[++off] = (byte) ((val) & 0x000000FF);
        } else   // Little endian
        {
            buf[off] = (byte) ((val) & 0x000000FF);
            buf[++off] = (byte) ((val >> 8) & 0x000000FF);
        }
    }


    // Puts the specified word (val) into the buffer (buf) at position off using big endian byte ordering
    public static void putWord(byte[] buf, int off, int val) {
        Util.putWord(buf, off, val, true);
    }


    // Extracts the double from the buffer (buf) at position off using the specified byte ordering (bigEndian)
    public static long getDWord(byte[] buf, int off, boolean bigEndian) {
        long val;
        if (bigEndian) {
            val = (((long) buf[off]) << 24) & 0xFF000000;
            val |= (((long) buf[++off]) << 16) & 0x00FF0000;
            val |= (((long) buf[++off]) << 8) & 0x0000FF00;
            val |= (((long) buf[++off])) & 0x000000FF;
        } else   // Little endian
        {
            val = (((long) buf[off])) & 0x000000FF;
            val |= (((long) buf[++off]) << 8) & 0x0000FF00;
            val |= (((long) buf[++off]) << 16) & 0x00FF0000;
            val |= (((long) buf[++off]) << 24) & 0xFF000000;
        }
        return (val);
    }


    // Extracts the double from the buffer (buf) at position off using big endian byte ordering
    public static long getDWord(byte[] buf, int off) {
        return (Util.getDWord(buf, off, true));
    }


    // Puts the specified double (val) into the buffer (buf) at position off using the specified byte ordering (bigEndian)
    public static void putDWord(byte[] buf, int off, long val, boolean bigEndian) {
        if (bigEndian) {
            buf[off] = (byte) ((val >> 24) & 0x00000000000000FF);
            buf[++off] = (byte) ((val >> 16) & 0x00000000000000FF);
            buf[++off] = (byte) ((val >> 8) & 0x00000000000000FF);
            buf[++off] = (byte) ((val) & 0x00000000000000FF);
        } else   // Little endian
        {
            buf[off] = (byte) ((val) & 0x00000000000000FF);
            buf[++off] = (byte) ((val >> 8) & 0x00000000000000FF);
            buf[++off] = (byte) ((val >> 16) & 0x00000000000000FF);
            buf[++off] = (byte) ((val >> 24) & 0x00000000000000FF);
        }
    }


    // Puts the specified double (val) into the buffer (buf) at position off using big endian byte ordering
    public static void putDWord(byte[] buf, int off, long val) {
        Util.putDWord(buf, off, val, true);
    }


    // getTlv(byte[] buf, int off) => byte[]
    public static byte[] getTlv(byte[] buf, int off) {
        if (off + 4 > buf.length) return (null);   // Length check (#1)
        int length = Util.getWord(buf, off + 2);
        if (off + 4 + length > buf.length) return (null);   // Length check (#2)
        byte[] value = new byte[length];
        System.arraycopy(buf, off + 4, value, 0, length);
        return (value);
    }


    // Extracts a string from the buffer (buf) starting at position off, ending at position off+len
    public static String byteArrayToString(byte[] buf, int off, int len, boolean utf8) {

        // Length check
        if (buf.length < off + len) {
            return (null);
        }

        // Remove \0's at the end
        while ((len > 0) && (buf[off + len - 1] == 0x00)) {
            len--;
        }

        // Read string in UTF-8 format
        if (utf8) {
            try {
                byte[] buf2 = new byte[len + 2];
                Util.putWord(buf2, 0, len);
                System.arraycopy(buf, off, buf2, 2, len);
                ByteArrayInputStream bais = new ByteArrayInputStream(buf2);
                DataInputStream dis = new DataInputStream(bais);
                return (dis.readUTF());
            } catch (Exception e) {
                // do nothing
            }
        }

        // CP1251 or default character encoding?
        if (Jimm.jimm.getOptionsRef().getBooleanOption(Options.OPTION_CP1251_HACK)) {
            return (byteArray1251ToString(buf, off, len));
        } else {
            return (new String(buf, off, len));
        }

    }


    // Extracts a string from the buffer (buf) starting at position off, ending at position off+len
    public static String byteArrayToString(byte[] buf, int off, int len) {
        return (Util.byteArrayToString(buf, off, len, false));
    }


    // Converts the specified buffer (buf) to a string
    public static String byteArrayToString(byte[] buf, boolean utf8) {
        return (Util.byteArrayToString(buf, 0, buf.length, utf8));
    }


    // Converts the specified buffer (buf) to a string
    public static String byteArrayToString(byte[] buf) {
        return (Util.byteArrayToString(buf, 0, buf.length, false));
    }


    // Converts the specified string (val) to a byte array
    public static byte[] stringToByteArray(String val, boolean utf8) {

        // Write string in UTF-8 format
        if (utf8) {
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                DataOutputStream dos = new DataOutputStream(baos);
                dos.writeUTF(val);
                return (baos.toByteArray());
            } catch (Exception e) {
                // Do nothing
            }
        }

        // CP1251 or default character encoding?
        if (Jimm.jimm.getOptionsRef().getBooleanOption(Options.OPTION_CP1251_HACK)) {
            return (stringToByteArray1251(val));
        } else {
            return (val.getBytes());
        }

    }


    // Converts the specified string (val) to a byte array
    public static byte[] stringToByteArray(String val) {
        return (Util.stringToByteArray(val, false));
    }


    // Converts the specified string to UCS-2BE
    public static byte[] stringToUcs2beByteArray(String val) {
        byte[] ucs2be = new byte[val.length() * 2];
        for (int i = 0; i < val.length(); i++) {
            Util.putWord(ucs2be, i * 2, (int) val.charAt(i));
        }
        return (ucs2be);
    }


    // Extract a UCS-2BE string from the specified buffer (buf) starting at position off, ending at position off+len
    public static String ucs2beByteArrayToString(byte[] buf, int off, int len) {

        // Length check
        if ((off + len > buf.length) || (buf.length % 2 != 0)) {
            return (null);
        }

        // Convert
        StringBuffer sb = new StringBuffer();
        for (int i = off; i < off + len; i += 2) {
            sb.append((char) Util.getWord(buf, i));
        }
        return (sb.toString());

    }


    // Extracts a UCS-2BE string from the specified buffer (buf)
    public static String ucs2beByteArrayToString(byte[] buf) {
        return (Util.ucs2beByteArrayToString(buf, 0, buf.length));
    }


    // Replaces all CRLF occurences in the string (val) with CR
    public static String crlfToCr(String val) {
        char[] dst = new char[val.length()];
        int dstLen = 0, i;
        for (i = 0; i < (val.length() - 1); i++)   // 0 to next to last
        {
            if ((val.charAt(i) == '\r') && (val.charAt(i + 1) == '\n')) {
                dst[dstLen++] = val.charAt(i++);
            } else if (val.charAt(i + 1) == '\r') {
                dst[dstLen++] = val.charAt(i);
            } else {
                dst[dstLen++] = val.charAt(i++);
                dst[dstLen++] = val.charAt(i);
            }
        }
        if (i < val.length()) {
            dst[dstLen++] = val.charAt(i);
        }
        return (new String(dst, 0, dstLen));
    }

    public static String removeClRfAndTabs(String val) {
        int len = val.length();
        char[] dst = new char[len];
        for (int i = 0; i < len; i++) {
            char chr = val.charAt(i);
            if ((chr == '\n') || (chr == '\r') || (chr == '\t')) chr = ' ';
            dst[i] = chr;
        }
        return new String(dst, 0, len);
    }


    // Compare to byte arrays (return true if equals, false otherwise)
    public static boolean byteArrayEquals(byte[] buf1, int off1, byte[] buf2, int off2, int len) {

        // Length check
        if ((off1 + len > buf1.length) || (off2 + len > buf2.length)) {
            return (false);
        }

        // Compare bytes, stop at first mismatch
        for (int i = 0; i < len; i++) {
            if (buf1[off1 + i] != buf2[off2 + i]) {
                return (false);
            }
        }

        // Return true if this point is reached
        return (true);

    }


    // DeScramble password
    public static byte[] decipherPassword(byte[] buf) {
        byte[] ret = new byte[buf.length];
        for (int i = 0; i < buf.length; i++) {
            ret[i] = (byte) (buf[i] ^ Util.PASSENC_KEY[i % 16]);
        }
        return (ret);
    }


    // translateStatus(long status) => void
    public static long translateStatusReceived(long status) {
        if (status == ContactList.STATUS_OFFLINE) return (ContactList.STATUS_OFFLINE);
        if ((status & ContactList.STATUS_DND) != 0) return (ContactList.STATUS_DND);
        if ((status & ContactList.STATUS_INVISIBLE) != 0) return (ContactList.STATUS_INVISIBLE);
        if ((status & ContactList.STATUS_OCCUPIED) != 0) return (ContactList.STATUS_OCCUPIED);
        if ((status & ContactList.STATUS_NA) != 0) return (ContactList.STATUS_NA);
        if ((status & ContactList.STATUS_AWAY) != 0) return (ContactList.STATUS_AWAY);
        if ((status & ContactList.STATUS_CHAT) != 0) return (ContactList.STATUS_CHAT);
        return (ContactList.STATUS_ONLINE);
    }


    // Get online status set value
    public static int translateStatusSend(long status) {
        if (status == ContactList.STATUS_AWAY) return (Util.SET_STATUS_AWAY);
        if (status == ContactList.STATUS_CHAT) return (Util.SET_STATUS_CHAT);
        if (status == ContactList.STATUS_DND) return (Util.SET_STATUS_DND);
        if (status == ContactList.STATUS_INVISIBLE) return (Util.SET_STATUS_INVISIBLE);
        if (status == ContactList.STATUS_NA) return (Util.SET_STATUS_NA);
        if (status == ContactList.STATUS_OCCUPIED) return (Util.SET_STATUS_OCCUPIED);
        return (Util.SET_STATUS_ONLINE);
    }


    //  If the numer has only one digit add a 0
    public static String makeTwo(int number) {
        if (number < 10) {
            return ("0" + String.valueOf(number));
        } else {
            return (String.valueOf(number));
        }
    }

    // Byte array IP to String
    public static String ipToString(byte[] ip) {
        String strIP = new String();
        int tmp;

        for (int i = 0; i < 3; i++) {
            tmp = (int) ip[i] & 0xFF;
            strIP = strIP + String.valueOf(tmp) + ".";
        }
        tmp = (int) ip[3] & 0xFF;
        strIP = strIP + String.valueOf(tmp);

        return strIP;
    }

    // String IP to byte array
    public static byte[] ipToByteArray(String ip) {
        byte[] arrIP = new byte[4];
        int i;

        for (int j = 0; j < 3; j++) {

            for (i = 0; i < 3; i++) {
                if (ip.charAt(i) == '.') break;
            }

            arrIP[j] = (byte) Integer.parseInt(ip.substring(0, i));
            ip = ip.substring(i + 1);

        }

        arrIP[3] = (byte) Integer.parseInt(ip);

        return arrIP;
    }

    // #sijapp cond.if target is "MIDP2" | target is "MOTOROLA" | target is "SIEMENS2"#
    // #sijapp cond.if modules_PROXY is "true"#
    // Try to parse string IP
    public static boolean isIP(String ip) {
        int digit = 0;
        int i;
        try {
            for (int j = 0; j < 3; j++) {

                for (i = 0; i < 3; i++) {
                    if (ip.charAt(i) == '.') break;
                }

                digit = Integer.parseInt(ip.substring(0, i));
                ip = ip.substring(i + 1);
            }

            digit = Integer.parseInt(ip);

            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // #sijapp cond.end #
    // #sijapp cond.end #

    // Create a random id which is not used yet
    public static int createRandomId() {
        // Max value is probably 0x7FFF, lowest value is unknown.
        // We use range 0x1000-0x7FFF.
        // From miranda source

        int range = 0x6FFF;

        ContactListGroupItem[] gItems = Jimm.jimm.getContactListRef().getGroupItems();
        ContactListContactItem[] cItems = Jimm.jimm.getContactListRef().getContactItems();
        int randint;
        boolean found;

        Random rand = new Random(System.currentTimeMillis());
        randint = rand.nextInt();
        if (randint < 0)
            randint = randint * (-1);
        randint = randint % range + 4096;

        //DebugLog.addText("rand: 0x"+Integer.toHexString(randint));

        do {
            found = false;
            for (int i = 0; i < gItems.length; i++) {
                if (gItems[i].getId() == randint) {
                    randint = rand.nextInt() + 4096 % range;
                    found = true;
                    break;
                }
            }
            if (!found)
                for (int j = 0; j < cItems.length; j++) {
                    if (cItems[j].getId() == randint) {
                        randint = rand.nextInt() % range + 4096;
                        found = true;
                        break;
                    }
                }
        } while (found == true);

        return randint;
    }

    // Check is data array utf-8 string
    public static boolean isDataUTF8(byte[] array, int start, int lenght) {
        if (lenght == 0) return false;
        if (array.length < (start + lenght)) return false;

        for (int i = start, len = lenght; len > 0; ) {
            int seqLen = 0;
            byte bt = array[i++];
            len--;

            if ((bt & 0xE0) == 0xC0) seqLen = 1;
            else if ((bt & 0xF0) == 0xE0) seqLen = 2;
            else if ((bt & 0xF8) == 0xF0) seqLen = 3;
            else if ((bt & 0xFC) == 0xF8) seqLen = 4;
            else if ((bt & 0xFE) == 0xFC) seqLen = 5;

            if (seqLen == 0) {
                if ((bt & 0x80) == 0x80) return false;
                else continue;
            }

            for (int j = 0; j < seqLen; j++) {
                if (len == 0) return false;
                bt = array[i++];
                if ((bt & 0xC0) != 0x80) return false;
                len--;
            }
            if (len == 0) break;
        }
        return true;
    }

    // #sijapp cond.if modules_TRAFFIC is "true" #
    // Returns String value of cost value
    public static String intToDecimal(int value) {
        String costString = "";
        String afterDot = "";
        try {
            if (value != 0) {
                costString = Integer.toString(value / 1000) + ".";
                afterDot = Integer.toString(value % 1000);
                while (afterDot.length() != 3) {
                    afterDot = "0" + afterDot;
                }
                while ((afterDot.endsWith("0")) && (afterDot.length() > 2)) {
                    afterDot = afterDot.substring(0, afterDot.length() - 1);
                }
                costString = costString + afterDot;
                return costString;
            } else {
                return new String("0.0");
            }
        } catch (Exception e) {
            return new String("0.0");
        }
    }

    // Extracts the number value form String
    public static int decimalToInt(String string) {
        int value = 0;
        byte i = 0;
        char c = new String(".").charAt(0);
        try {
            for (i = 0; i < string.length(); i++) {
                if (c != string.charAt(i)) {
                    break;
                }
            }
            if (i == string.length() - 1) {
                value = Integer.parseInt(string) * 1000;
                return (value);
            } else {
                while (c != string.charAt(i)) {
                    i++;
                }
                value = Integer.parseInt(string.substring(0, i)) * 1000;
                string = string.substring(i + 1, string.length());
                while (string.length() > 3) {
                    string = string.substring(0, string.length() - 1);
                }
                while (string.length() < 3) {
                    string = string + "0";
                }
                value = value + Integer.parseInt(string);
                return value;
            }
        } catch (Exception e) {
            return (0);
        }
    }
    // #sijapp cond.end#


    // Convert gender code to string
    static public String genderToString(int gender) {
        switch (gender) {
            case 1:
                return ResourceBundle.getString("female");
            case 2:
                return ResourceBundle.getString("male");
        }
        return new String();
    }

    // Converts an Unicode string into CP1251 byte array
    public static byte[] stringToByteArray1251(String s) {
        byte abyte0[] = s.getBytes();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case 1025:
                    abyte0[i] = -88;
                    break;
                case 1105:
                    abyte0[i] = -72;
                    break;
                default:
                    char c1 = c;
                    if (c1 >= '\u0410' && c1 <= '\u044F') {
                        abyte0[i] = (byte) ((c1 - 1040) + 192);
                    }
                    break;
            }
        }
        return abyte0;
    }


    // Converts an CP1251 byte array into an Unicode string
    public static String byteArray1251ToString(byte abyte0[], int i, int j) {
        String s = new String(abyte0, i, j);
        StringBuffer stringbuffer = new StringBuffer(j);
        for (int k = 0; k < j; k++) {
            int l = abyte0[k + i] & 0xff;
            switch (l) {
                case 168:
                    stringbuffer.append('\u0401');
                    break;
                case 184:
                    stringbuffer.append('\u0451');
                    break;
                default:
                    if (l >= 192 && l <= 255) {
                        stringbuffer.append((char) ((1040 + l) - 192));
                    } else {
                        stringbuffer.append(s.charAt(k));
                    }
                    break;
            }
        }
        return stringbuffer.toString();
    }

}

