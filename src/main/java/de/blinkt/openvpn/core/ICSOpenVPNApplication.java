/*
 * Copyright (c) 2012-2016 Arne Schwabe
 * Distributed under the GNU GPL v2 with additional terms. For full terms see the file doc/LICENSE.txt
 */

package de.blinkt.openvpn.core;

import android.annotation.TargetApi;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;

import de.blinkt.openvpn.R;

/*
import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;
*/

public class ICSOpenVPNApplication extends Application {
    private static StatusListener mStatus;

    @Override
    public void onCreate() {
        super.onCreate();


        init(getApplicationContext());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            createNotificationChannels(getApplicationContext());
    }

    public static void init(Context c)
    {
        PRNGFixes.apply();
        mStatus = new StatusListener();
        mStatus.init(c);
    }

    @TargetApi(Build.VERSION_CODES.O)
    public static void createNotificationChannels(Context c) {
        NotificationManager mNotificationManager =
                (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);

        // Background message
        CharSequence name = c.getString(R.string.channel_name_background);
        NotificationChannel mChannel = new NotificationChannel(OpenVPNService.NOTIFICATION_CHANNEL_BG_ID,
                name, NotificationManager.IMPORTANCE_MIN);

        mChannel.setDescription(c.getString(R.string.channel_description_background));
        mChannel.enableLights(false);
        mChannel.setSound(null, null);

        mChannel.setLightColor(Color.DKGRAY);
        mNotificationManager.createNotificationChannel(mChannel);

        // Connection status change messages

        name = c.getString(R.string.channel_name_status);
        mChannel = new NotificationChannel(OpenVPNService.NOTIFICATION_CHANNEL_NEWSTATUS_ID,
                name, NotificationManager.IMPORTANCE_LOW);

        mChannel.setDescription(c.getString(R.string.channel_description_status));
        mChannel.enableLights(true);

        mChannel.setLightColor(Color.BLUE);
        mNotificationManager.createNotificationChannel(mChannel);
    }
}
