package com.privateinternetaccess.android;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import de.blinkt.openvpn.core.VpnStatus;


public class PIAStatusListener {
    private ServiceConnection mConnection = new ServiceConnection() {


        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            IPIAServiceStatus serviceStatus = IPIAServiceStatus.Stub.asInterface(service);
            try {
                /* Check if this a local service ... */
                if (service.queryLocalInterface("com.privateinternetaccess.android.IPIAServiceStatus") == null) {
                    // Not a local service
                    serviceStatus.registerStatusCallback(mCallback);
                }

            } catch (RemoteException e) {
                e.printStackTrace();
                VpnStatus.logException(e);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {

        }

    };

    public void init(Context c) {
        Intent intent = new Intent(c, PIAStatusService.class);
        c.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }


    private IPIAStatusCallbacks.Stub mCallback = new IPIAStatusCallbacks.Stub()
    {
        @Override
        public void triggerUpdateKillState(boolean inKillSwitch) throws RemoteException {
            PIAKillSwitchStatus.triggerUpdateKillState(inKillSwitch);
        }
    };

}
