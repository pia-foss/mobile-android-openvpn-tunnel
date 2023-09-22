package de.blinkt.openvpn.core;

import android.content.Context;

import androidx.core.app.NotificationCompat;

/**
 * Created by arne on 06.10.17.
 */

public interface VPNNotifications {

    void addPiaNotificationExtra(NotificationCompat.Builder nbuilder, Context context, DeviceStateReceiver receiver);

    void showKillSwitchNotification(Context context);

    void stopKillSwitchNotification(Context context);

    void showRevokeNotification(Context context);

    int getIconByConnectionStatus(ConnectionStatus level);

    int getColorByConnectionStatus(Context context, ConnectionStatus level);
}
