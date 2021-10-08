package jimm.util;


import java.util.Hashtable;


public class ResourceBundle {


    // Common prefix of all resource packages
    public static final String PREFIX = "jimm.res.Text";


    // List of available language packs
    public static String[] LANG_AVAILABLE =
            {
                    // #sijapp cond.if lang_EN is "true" #
                    "EN",
                    // #sijapp cond.end #
                    // #sijapp cond.if lang_BR is "true" #
                    "BR",
                    // #sijapp cond.end #
                    // #sijapp cond.if lang_BG is "true" #
                    "BG",
                    // #sijapp cond.end #
                    // #sijapp cond.if lang_CZ is "true" #
                    "CZ",
                    // #sijapp cond.end #
                    // #sijapp cond.if lang_DE is "true" #
                    "DE",
                    // #sijapp cond.end #
                    // #sijapp cond.if lang_ES is "true" #
                    "ES",
                    // #sijapp cond.end #
                    // #sijapp cond.if lang_HE is "true" #
                    "HE",
                    // #sijapp cond.end #
                    // #sijapp cond.if lang_IT is "true" #
                    "IT",
                    // #sijapp cond.end #
                    // #sijapp cond.if lang_LT is "true" #
                    "LT",
                    // #sijapp cond.end #
                    // #sijapp cond.if lang_PL is "true" #
                    "PL",
                    // #sijapp cond.end #
                    // #sijapp cond.if lang_RU is "true" #
                    "RU",
                    // #sijapp cond.end #
                    // #sijapp cond.if lang_SE is "true" #
                    "SE",
                    // #sijapp cond.end #
                    // #sijapp cond.if lang_SR is "true" #
                    "SR",
                    // #sijapp cond.end #
                    // #sijapp cond.if lang_UA is "true" #
                    "UA",
                    // #sijapp cond.end #
            };


    // Current language
    private static String currUiLanguage = ResourceBundle.LANG_AVAILABLE[0];


    // ResourceBundles
    private static Hashtable groups = new Hashtable();


    // Get user interface language/localization for current session
    public static String getCurrUiLanguage() {
        return (new String(ResourceBundle.currUiLanguage));
    }


    // Set user interface language/localization for current session
    public static void setCurrUiLanguage(String currUiLanguage) {
        for (int i = 0; i < ResourceBundle.LANG_AVAILABLE.length; i++) {
            if (ResourceBundle.LANG_AVAILABLE[i].equals(currUiLanguage)) {
                ResourceBundle.currUiLanguage = new String(currUiLanguage);
                return;
            }
        }
    }


    // Get string from active language pack
    public static synchronized String getString(String key) {

        // Get/load resource bundle
        ResourceBundle bundle;
        synchronized (groups) {
            bundle = (ResourceBundle) groups.get(ResourceBundle.currUiLanguage);
            if (bundle == null) {
                bundle = ResourceBundle.loadBundle(ResourceBundle.currUiLanguage);
            }
        }

        // Return value from resource bundle
        return bundle._getString(key);

    }


    // Initialize ResourceBundle
    public static ResourceBundle loadBundle(String name) {

        // Load
        ResourceBundle bundle = null;
        try {
            bundle = (ResourceBundle) Class.forName(ResourceBundle.PREFIX + "_" + name).newInstance();
        } catch (Exception e) {
            // Do nothing
        }

        // Put into hashtable
        ResourceBundle.groups.put(name, bundle);

        // Return ResourceBundle
        return (bundle);

    }


    /****************************************************************************/


    // Resource hashtable
    protected Hashtable resources = new Hashtable();


    // Return value form hashtable
    protected String _getString(String key) {
        String value = (String) resources.get(key);
        if (value != null) {
            return (value);
        } else {
            return (key);
        }
    }


}

