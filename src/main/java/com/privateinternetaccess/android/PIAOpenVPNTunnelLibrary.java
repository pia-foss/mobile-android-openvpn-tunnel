package com.privateinternetaccess.android;

import android.content.Context;
import android.os.Build;

import de.blinkt.openvpn.core.ICSOpenVPNApplication;
import de.blinkt.openvpn.core.OpenVPNService;
import de.blinkt.openvpn.core.VPNNotifications;

/**
 * Created by arne on 06.10.17.
 */

public class PIAOpenVPNTunnelLibrary {
    private static PIAStatusListener mPIAStatusListener;

    public static VPNNotifications mNotifications;
    public static PIACallbacks mCallbacks;

    /**
     * Configures and initialises the OpenVPN library.
     * @param c should be getApplicationContext()
     * @param notifications
     * @param callbacks
     */

    public static void init(Context c, VPNNotifications notifications, PIACallbacks callbacks){
        ICSOpenVPNApplication.init(c);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            ICSOpenVPNApplication.createNotificationChannels(c);


        mPIAStatusListener = new PIAStatusListener();
        mPIAStatusListener.init(c);



        mNotifications = notifications;

        mCallbacks = callbacks;
        OpenVPNService.setNotificationActivityClass(mCallbacks.getMainClass());

    }
}
