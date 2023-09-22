package com.privateinternetaccess.android;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteCallbackList;
import android.os.RemoteException;

import java.lang.ref.WeakReference;

import de.blinkt.openvpn.core.ConnectionStatus;
import de.blinkt.openvpn.core.KillSwitch;

public class PIAStatusService extends Service implements PIAKillSwitchStatus.KillSwitchStateListener {


    private static final PIAStatusHandler mHandler = new PIAStatusHandler();

    private static final int SEND_KILLSWITCH_UPDATE = 100;

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }


    static final RemoteCallbackList<IPIAStatusCallbacks> mCallbacks =
            new RemoteCallbackList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        PIAKillSwitchStatus.addKillSwitchListener(this);
        mHandler.setService(this);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        PIAKillSwitchStatus.removeKillSwitchListener(this);
        mCallbacks.kill();

    }

    private  final IPIAServiceStatus.Stub mBinder = new IPIAServiceStatus.Stub() {

        @Override
        public void registerStatusCallback(IPIAStatusCallbacks cb) throws RemoteException {
            mCallbacks.register(cb);
            cb.triggerUpdateKillState(PIAKillSwitchStatus.isKillSwitchActive());
        }

        @Override
        public void unregisterStatusCallback(IPIAStatusCallbacks cb) throws RemoteException {
            mCallbacks.unregister(cb);
        }

        @Override
        public void stopKillSwitchIfActive() throws RemoteException {
            KillSwitch.closeOpenVPNFdIfOpen(PIAStatusService.this);
        }

    };


    @Override
    public void killSwitchUpdate(boolean inKillSwitch) {
        Message msg = mHandler.obtainMessage(SEND_KILLSWITCH_UPDATE, inKillSwitch);
        msg.sendToTarget();
    }

    static class UpdateMessage {
        public String state;
        public String logmessage;
        public ConnectionStatus level;
        int resId;

        UpdateMessage(String state, String logmessage, int resId, ConnectionStatus level) {
            this.state = state;
            this.resId = resId;
            this.logmessage = logmessage;
            this.level = level;
        }
    }


    private static class PIAStatusHandler extends Handler {
        WeakReference<PIAStatusService> service = null;

        private void setService(PIAStatusService statusService) {
            service = new WeakReference<>(statusService);
        }

        @Override
        public void handleMessage(Message msg) {

            RemoteCallbackList<IPIAStatusCallbacks> callbacks;
            if (service == null || service.get() == null)
                return;
            callbacks = service.get().mCallbacks;
            // Broadcast to all clients the new value.
            final int N = callbacks.beginBroadcast();
            for (int i = 0; i < N; i++) {

                try {
                    IPIAStatusCallbacks broadcastItem = callbacks.getBroadcastItem(i);

                    switch (msg.what) {
                        case SEND_KILLSWITCH_UPDATE:
                            broadcastItem.triggerUpdateKillState((Boolean)msg.obj);
                            break;
                    }
                } catch (RemoteException e) {
                    // The RemoteCallbackList will take care of removing
                    // the dead object for us.
                }
            }
            callbacks.finishBroadcast();
        }
    }
}
