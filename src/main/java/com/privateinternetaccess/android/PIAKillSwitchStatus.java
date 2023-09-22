package com.privateinternetaccess.android;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import java.util.LinkedList;

import de.blinkt.openvpn.core.VpnStatus;

/**
 * Created by arne on 30.11.2014.
 */
public class PIAKillSwitchStatus {




    private static boolean inKillSwitch = false;





    public static boolean isKillSwitchActive() {
        return inKillSwitch;
    }

    public interface KillSwitchStateListener {
        void killSwitchUpdate(boolean isInKillSwitch);
    }

    static LinkedList<KillSwitchStateListener> killSwitchStateListeners = new LinkedList<>();

    public static void addKillSwitchListener(KillSwitchStateListener ksl) {
        killSwitchStateListeners.add(ksl);
    }

    public static void removeKillSwitchListener(KillSwitchStateListener ksl) {
        killSwitchStateListeners.remove(ksl);
    }

    public static void triggerUpdateKillState(boolean inKillSwitch) {
        PIAKillSwitchStatus.inKillSwitch = inKillSwitch;
        for (KillSwitchStateListener ksl : killSwitchStateListeners)
            ksl.killSwitchUpdate(inKillSwitch);
    }

    //! Call with an application context to avoid context problems
    public static void stopKillSwitch(final Context c) {
        ServiceConnection mConnection = new ServiceConnection() {


            @Override
            public void onServiceConnected(ComponentName className,
                                           IBinder service) {
                // We've bound to LocalService, cast the IBinder and get LocalService instance
                IPIAServiceStatus serviceStatus = IPIAServiceStatus.Stub.asInterface(service);
                try {
                    serviceStatus.stopKillSwitchIfActive();
                    c.unbindService(this);
                } catch (RemoteException e) {
                    e.printStackTrace();
                    VpnStatus.logException(e);
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName arg0) {

            }

        };
        Intent intent = new Intent(c, PIAStatusService.class);
        c.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }
}
