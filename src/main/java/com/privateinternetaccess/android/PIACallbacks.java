package com.privateinternetaccess.android;

import android.app.Activity;
import android.content.Context;

import de.blinkt.openvpn.VpnProfile;

/**
 * Created by arne on 06.10.17.
 */

public interface PIACallbacks {
    boolean isKillSwitchEnabled(Context c);

    VpnProfile getAlwaysOnProfile();

    Class<? extends Activity> getMainClass();
}
