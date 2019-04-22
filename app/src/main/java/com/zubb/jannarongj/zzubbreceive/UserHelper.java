package com.zubb.jannarongj.zzubbreceive;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by jannarong.j on 13/6/2560.
 */
public class UserHelper {
    Version vers;
    Context context;
    SharedPreferences sharedPerfs;
    SharedPreferences.Editor editor;

    // Prefs Keys
    static String perfsName = "UserHelper";
    static int perfsMode = 0;

    public UserHelper(Context context) {
        this.context = context;
        this.sharedPerfs = this.context.getSharedPreferences(perfsName, perfsMode);
        this.editor = sharedPerfs.edit();
    }

    public void createSession(String sUserName, String sPassword, String sPlant) {
        vers = new Version();
        editor.putBoolean("LoginStatus", true);
        editor.putString("Username", sUserName);
        editor.putString("Password", sPassword);
        editor.putString("Version", vers.Version.trim());
        editor.putString("Plant", sPlant);
        editor.commit();
    }

    public void deleteSession() {
        editor.clear();
        editor.commit();
    }

    public boolean getLoginStatus() {
        return sharedPerfs.getBoolean("LoginStatus", false);
    }
    public String getUserName() {
        return sharedPerfs.getString("Username", null);
    }
    public String getPassword() {
        return sharedPerfs.getString("Password", null);
    }
    public String getVer() {
        return sharedPerfs.getString("Version", null);
    }
    public String getPlant() {
        return sharedPerfs.getString("Plant", null);
    }
}