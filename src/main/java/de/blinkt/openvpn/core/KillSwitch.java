package de.blinkt.openvpn.core;

import android.content.Context;
import android.util.Log;

import com.privateinternetaccess.android.PIAOpenVPNTunnelLibrary;
import com.privateinternetaccess.android.PIAKillSwitchStatus;

import java.io.FileDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by arne on 30.11.2014.
 */
public class KillSwitch {

    private static int mOpenVPNFd;

    static {
        mOpenVPNFd = -1;
    }

    public static void closeOpenVPNFdIfOpen(Context c) {
        if(mOpenVPNFd!=-1) {
            NativeUtils.jniclose(mOpenVPNFd);
            mOpenVPNFd=-1;
        }
        PIAKillSwitchStatus.triggerUpdateKillState(false);
        PIAOpenVPNTunnelLibrary.mNotifications.stopKillSwitchNotification(c);
    }

    private static void closeOpenVpnTun(Context c, int fdint) {
        mOpenVPNFd = fdint;

        if (!PIAOpenVPNTunnelLibrary.mCallbacks.isKillSwitchEnabled(c))
            closeOpenVPNFdIfOpen(c);
        else {
            PIAOpenVPNTunnelLibrary.mNotifications.showKillSwitchNotification(c);
            PIAKillSwitchStatus.triggerUpdateKillState(true);
        }
    }

    /*
     * OpenVPNManagement thread:
     *  else if (needed.equals("CLOSETUN")) {
     * FileDescriptor fdtoclose = mFDList.pollFirst();
     * openVPNrequestClose(fdtoclose);
	 */
    static void openVPNrequestClose (FileDescriptor fd, Context c) {
        try {
            Method getInt = FileDescriptor.class.getDeclaredMethod("getInt$");
            int fdint = (Integer) getInt.invoke(fd);

            // You can even get more evil by parsing toString() and extract the int from that :)

            closeOpenVpnTun(c, fdint);

            //ParcelFileDescriptor pfd = ParcelFileDescriptor.fromFd(fdint);
            //pfd.close();

            return;
        } catch (NoSuchMethodException | NullPointerException | InvocationTargetException | IllegalAccessException | IllegalArgumentException e) {
            e.printStackTrace();
            Log.e("Openvpn", "Failed to retrieve fd from socket: " + fd);
            VpnStatus.logInfo("Failed to retrieve fd from socket: " + e.getLocalizedMessage());
        }

    }
}
