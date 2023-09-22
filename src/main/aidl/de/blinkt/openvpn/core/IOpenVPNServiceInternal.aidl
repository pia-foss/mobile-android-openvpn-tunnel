/*
 * Copyright (c) 2012-2016 Arne Schwabe
 * Distributed under the GNU GPL v2 with additional terms. For full terms see the file doc/LICENSE.txt
 */

package de.blinkt.openvpn.core;

import android.os.ParcelFileDescriptor;


/**
 * Created by arne on 15.11.16.
 */

interface IOpenVPNServiceInternal {

    boolean protect(in ParcelFileDescriptor fd);

    void userPause(boolean b);

    /**
     * @param replaceConnection True if the VPN is connected by a new connection.
     * @return true if there was a process that has been send a stop signal
     */
    boolean stopVPN(boolean replaceConnection);
}
