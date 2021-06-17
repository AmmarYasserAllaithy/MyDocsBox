/*
 * Author   : Ammar Yasser AllaiThy
 * Website  : https://ammaryasserallaithy.github.io
 * Portfolio: https://ammaryasser.netlify.app
 */

package com.example.ammaryasser.mydocsbox.util;

import android.content.Context;
import android.content.SharedPreferences;


public final class Prefs {

    private static Prefs instance;
    private static SharedPreferences preferences;

    public static Prefs getPrefs(Context context) {
        if (instance == null) {
            instance = new Prefs();
            preferences = context.getSharedPreferences(Keys.MY_PREFS_BOX, Context.MODE_PRIVATE);
        }
        return instance;
    }


    /**
     * Utils
     */
    public int switchLang() {
        int lang = getLang() == Lang.EN ? Lang.AR : Lang.EN;
        putLang(lang);
        return lang;
    }

    public int switchViewMode() {
        int viewMode = getViewMode() == ViewMode.COMPACT ? ViewMode.INFORMATIVE : ViewMode.COMPACT;
        putViewMode(viewMode);
        return viewMode;
    }


    /**
     * Read
     */
    public int getLang() {
        return get(Keys.LANG, Lang.EN);
    }

    public int getDateFormat() {
        return get(Keys.DATE_FORMAT, DateFormat.FULL);
    }

    public int getViewMode() {
        return get(Keys.VIEW_MODE, ViewMode.COMPACT);
    }

    public int getTheme() {
        return get(Keys.THEME, Theme.LIGHT);
    }

    public int getFont() {
        return get(Keys.FONT, Font.SYSTEM);
    }

    public int getLastPosition() {
        return get(Keys.LAST_POSITION, 0);
    }


    /**
     * Write
     */
    public void putLang(int val) {
        put(Keys.LANG, val);
    }

    public void putDateFormat(int val) {
        put(Keys.DATE_FORMAT, val);
    }

    public void putViewMode(int val) {
        put(Keys.VIEW_MODE, val);
    }

    public void putTheme(int val) {
        put(Keys.THEME, val);
    }

    public void putFont(int val) {
        put(Keys.FONT, val);
    }

    public void putLastPosition(int val) {
        put(Keys.LAST_POSITION, val);
    }


    /**
     * private get
     */
    private int get(String key, int defVal) {
        return preferences.getInt(key, defVal);
    }

    private String get(String key, String defVal) {
        return preferences.getString(key, defVal);
    }


    /**
     * private put
     */
    private void put(String key, int val) {
        preferences.edit().putInt(key, val).apply();
    }

    private void put(String key, String val) {
        preferences.edit().putString(key, val).apply();
    }


    /**
     * Prefs Keys
     */
    public static final class Keys {
        public static final String MY_PREFS_BOX = "MyPrefsBox";
        public static final String LAST_POSITION = "LAST_POSITION";
        public static final String LANG = "LANG";
        public static final String DATE_FORMAT = "DATE_FORMAT";
        public static final String VIEW_MODE = "VIEW_MODE";
        public static final String THEME = "THEME";
        public static final String FONT = "FONT";
    }


    /**
     * Prefs Values
     */
    public static final class Lang {
        public static final int EN = 0;
        public static final int AR = 1;
    }

    public static final class DateFormat {
        public static final int FULL = 0;
        // TODO: List formats constants
    }

    public static final class ViewMode {
        public static final int COMPACT = 0;
        public static final int INFORMATIVE = 1;
    }

    public static final class Theme {
        public static final int LIGHT = 0;
        public static final int DARK = 1;
        public static final int SEPIA = 2;
    }

    public static final class Font {
        public static final int SYSTEM = 0;
        public static final int TAJAWAL = 1;
        public static final int SEGOE = 2;
        public static final int LAILA = 3;
    }

}
